package com.twinkle.framework.core.utils;

import com.google.common.base.MoreObjects;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author chenxj
 */
@Slf4j
public class InputBuilder {
    private final Set<String> classes = new HashSet<>();
    private final Set<String> resources = new HashSet<>();
    private final Map<String, byte[]> content;

    public InputBuilder(File input) throws IOException {
        if (input.getName().endsWith(".class")) {
            content = readClass(input);
        } else {
            content = readArchive(input);
        }
    }

    public InputBuilder(Instrumentation instrumentation) throws IOException {
        content = readInstrumentation(instrumentation);
    }

    /**
     * Build Inputbuilder with class name, content.
     *
     * @param _className
     * @param _content
     * @throws IOException
     */
    public InputBuilder(String _className, byte[] _content) throws IOException {
        content = new HashMap<>();
        this.addClass(content, _className, _content);
    }

    /**
     * Populates class and resource maps.
     *
     * @param input Archive to read.
     * @throws IOException Thrown if the archive could not be read, or an internal file
     *                     could not be read.
     */
    private Map<String, byte[]> readArchive(File input) throws IOException {
        Map<String, byte[]> contents = new HashMap<>();
        try (ZipFile file = new ZipFile(input)) {
            // iterate zip entries
            Enumeration<? extends ZipEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                // skip directories
                if (entry.isDirectory())
                    continue;
                try (InputStream is = file.getInputStream(entry)) {
                    // add as class, or resource if not a class file.
                    String name = entry.getName();
                    if (name.endsWith(".class")) {
                        addClass(contents, name, is);
                    } else {
                        addResource(contents, name, is);
                    }
                }
            }
        }
        return contents;
    }

    /**
     * Populates class map.
     *
     * @param input Class to read.
     * @throws IOException Thrown if the file could not be read.
     */
    private Map<String, byte[]> readClass(File input) throws IOException {
        Map<String, byte[]> contents = new HashMap<>();
        try (InputStream is = new FileInputStream(input)) {
            addClass(contents, input.getName(), is);
        }
        return contents;
    }

    /**
     * Read bytecode of classes in the instrumented environment.
     *
     * @param instrumentation
     * @return Map of runtime classes's bytecode.
     * @throws IOException Thrown if a class could not be loaded.
     */
    private Map<String, byte[]> readInstrumentation(Instrumentation instrumentation) throws
            IOException {
        Map<String, byte[]> map = new HashMap<>();
        // add all existing classes
        for (Class<?> c : instrumentation.getAllLoadedClasses()) {
            String name = Type.getInternalName(c);
            String path = name.concat(".class");
            ClassLoader loader = MoreObjects.firstNonNull(c.getClassLoader(), Classpath.scl);
            try (InputStream in = loader.getResourceAsStream(path)) {
                if (in != null) {
                    map.put(name, Streams.from(in));
                    classes.add(name);
                }
            }
        }
        return map;
    }

    /**
     * Try to add the class contained in the given stream to the classes map.
     *
     * @param name Entry name.
     * @param is   Stream of entry.
     * @throws IOException Thrown if stream could not be read or if ClassNode could not
     *                     be derived from the streamed content.
     */
    private void addClass(Map<String, byte[]> contents, String name, InputStream is) throws
            IOException {
        byte[] value = Streams.from(is);
        this.addClass(contents, name, value);
    }


    /**
     * Add Class with byte array.
     *
     * @param contents
     * @param name
     * @param _value
     * @throws IOException
     */
    private void addClass(Map<String, byte[]> contents, String name, byte[] _value) {
        try {
            ClassReader cr = new ClassReader(_value);
            String className = cr.getClassName();
            // run some basic verification
            if (className.endsWith("/")) {
                log.warn(String.format("Invalid file-name, '%s', skipping this entry", name));
                return;
            }
            if (!verify(cr)) {
                log.warn(String.format("Invalid code, '%s', skipping this entry", name));
                return;
            }
            classes.add(className);
            contents.put(className, _value);
        } catch (Exception e) {
            log.warn(String.format("Could not read archive entry: '%s' as a class. Added as "
                    + "resource instead.", name));
            contents.put(name, _value);
        }
    }

    /**
     * Try to add the resource contained in the given stream to the resource
     * map.
     *
     * @param name Entry name.
     * @param is   Stream of entry.
     * @throws IOException Thrown if stream could not be read.
     */
    private void addResource(Map<String, byte[]> contents, String name, InputStream is) throws
            IOException {
        resources.add(name);
        contents.put(name, Streams.from(is));
    }


    /**
     * Verify that constant-pool values are not malformed.
     *
     * @param cr ClassReader.
     * @return Validity of code, {@code true} for valid.
     */
    private static boolean verify(ClassReader cr) {
        try {
            // The class reader will attempt to parse the raw bytecode stored in
            // the array "b" in ClassReader and save it back to a class file via
            // ClassWriter. If any errors occur in this process, the code is
            // invalid.
            //
            // This kinda sucks for speed/performance, but hey it means not
            // dealing with invalid code (which IMO are not within the scope of
            // this tool)
            cr.accept(new ClassWriter(0), 0);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // ========================================================================= //


    public Set<String> getClasses() {
        return classes;
    }

    public Set<String> getResources() {
        return resources;
    }

    public Map<String, byte[]> getContent() {
        return content;
    }

    public Map<String, byte[]> getResourceContent() {
        Map<String, byte[]> value = new HashMap<>();
        getResources().forEach(key -> value.put(key, content.get(key)));
        return value;
    }

    public Map<String, byte[]> getClassContent() {
        Map<String, byte[]> value = new HashMap<>();
        getClasses().forEach(key -> value.put(key, content.get(key)));
        return value;
    }

}
