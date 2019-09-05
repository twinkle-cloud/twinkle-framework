package com.twinkle.framework.core.asm.compiler;

import com.twinkle.framework.core.asm.bytecode.ByteCode;
import com.twinkle.framework.core.asm.bytecode.Code;
import com.twinkle.framework.core.asm.bytecode.SourceCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 10:41<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class JavaMemoryFileSystem {
    private static JavaMemoryFileSystem filesystem;
    private File defaultDumpDir = null;
    private Map<URI, JavaFileObject> byteCode_ = new ConcurrentHashMap();
    private Map<URI, JavaFileObject> sourceCode_ = new ConcurrentHashMap();

    private JavaMemoryFileSystem() {
    }

    public static synchronized JavaMemoryFileSystem instance() {
        if (filesystem == null) {
            filesystem = new JavaMemoryFileSystem();
        }

        return filesystem;
    }

    public synchronized JavaFileObject addBytecode(String _className, byte[] _classByteArray) throws IOException {
        ByteCode tempByteCode = new ByteCode(_className, _classByteArray);
        this.byteCode_.put(tempByteCode.toUri(), tempByteCode);
        if (log.isTraceEnabled()) {
            this.dump(_className, _classByteArray, ".class", this.getDefaultDumpDir());
        }

        return tempByteCode;
    }

    /**
     * Get the ByteCode of the given class.
     *
     * @param _className: full path classname.
     * @return
     */
    public synchronized byte[] getBytecode(String _className) {
        URI tempURI = Code.getUri(_className, JavaFileObject.Kind.CLASS);
        JavaFileObject tempObj = this.byteCode_.get(tempURI);
        if (tempObj != null) {
            ByteCode tempByteCode = (ByteCode) tempObj;
            return tempByteCode.getBytes();
        } else {
            return null;
        }
    }

    /**
     * Create Byte Code class.
     *
     * @param _className
     * @return
     */
    synchronized JavaFileObject newBytecode(String _className) {
        ByteCode tempByteCode = new ByteCode(_className);
        this.byteCode_.put(tempByteCode.toUri(), tempByteCode);
        return tempByteCode;
    }

    /**
     * Add Source Code.
     *
     * @param _className
     * @param _sourceCode: Java file's content.
     * @return
     */
    public synchronized JavaFileObject addSourceCode(String _className, String _sourceCode) {
        SourceCode tempSourceCode = new SourceCode(_className, _sourceCode);
        this.sourceCode_.put(tempSourceCode.toUri(), tempSourceCode);
        if (log.isTraceEnabled()) {
            this.dump(_className, _sourceCode.getBytes(), ".java", this.getDefaultDumpDir());
        }

        return tempSourceCode;
    }

    /**
     * Get files under the given path.
     *
     * @param _location:   ClASS_PATH or SOURCE_PATH.
     * @param _path
     * @param _kindSet
     * @param _scanSubFlag
     * @return
     */
    synchronized Set<JavaFileObject> getFiles(JavaFileManager.Location _location, String _path, Set<JavaFileObject.Kind> _kindSet, boolean _scanSubFlag) {
        Set<JavaFileObject> tempResultSet = new HashSet();
        Map<URI, JavaFileObject> tempFileObjMap;
        if (_location.equals(StandardLocation.CLASS_PATH)) {
            tempFileObjMap = this.byteCode_;
        } else {
            if (!_location.equals(StandardLocation.SOURCE_PATH)) {
                return tempResultSet;
            }
            tempFileObjMap = this.sourceCode_;
        }
        tempResultSet = tempFileObjMap.entrySet().stream().filter(item -> {
                    if (!_kindSet.contains(item.getValue().getKind())) {
                        return false;
                    }
                    String tempPackage = ((Code) item.getValue()).getPackage();
                    if (!_scanSubFlag) {
                        if (tempPackage.equals(_path)) {
                            //If file object's path is same with given path.
                            //Keep the item.
                            return true;
                        }
                    } else {
                        boolean _isParentPackage = tempPackage.startsWith(_path);
                        boolean _isHavingDot = tempPackage.charAt(_path.length()) == '.';
                        if (_isParentPackage && _isHavingDot) {
                            //If the file object belongs to sub package, then
                            //Keep the item.
                            return true;
                        }
                    }
                    //Dismiss the unmatched items.
                    return false;
                }
        ).map(item -> item.getValue()).collect(Collectors.toSet());
        return tempResultSet;
    }

    /**
     * Delete the file Object.
     *
     * @param _fileObj
     * @return
     */
    public synchronized boolean delete(JavaFileObject _fileObj) {
        URI tempUri = _fileObj.toUri();
        if (_fileObj.getKind().equals(JavaFileObject.Kind.CLASS)) {
            return this.byteCode_.remove(tempUri) != null;
        } else if (_fileObj.getKind().equals(JavaFileObject.Kind.SOURCE)) {
            return this.sourceCode_.remove(tempUri) != null;
        } else {
            return false;
        }
    }

    /**
     * Clean the current file system.
     */
    public synchronized void clear() {
        this.clear(this.byteCode_);
        this.clear(this.sourceCode_);
        this.byteCode_.clear();
        this.sourceCode_.clear();
    }

    /**
     * Clean the given file object map.
     *
     * @param _map
     */
    private void clear(Map<URI, JavaFileObject> _map) {
        if (MapUtils.isNotEmpty(_map)) {
            _map.forEach((k, v) -> {
                v.delete();
            });
        }
    }

    public synchronized void dump() {
        this.dump(this.getDefaultDumpDir());
    }

    /**
     * Dump the files.
     *
     * @param _directory: File Path, should be a directory.
     */
    public synchronized void dump(File _directory) {
        this.byteCode_.forEach((k, v) -> {
            if (v instanceof ByteCode) {
                ByteCode tempByteCode = (ByteCode) v;
                this.dump(tempByteCode.getClassName(), tempByteCode.getBytes(), ".class", _directory);
            }
        });
        this.sourceCode_.forEach((k, v) -> {
            if (v instanceof SourceCode) {
                SourceCode tempSourceCode = (SourceCode) v;
                this.dump(tempSourceCode.getClassName(), tempSourceCode.getSourceCode().getBytes(), ".java", _directory);
            }
        });
    }

    /**
     * Get ByteCodes List.
     *
     * @return LinkedList.
     */
    public Collection<ByteCode> getByteCodes() {
        List<ByteCode> tempResult = this.byteCode_.entrySet().stream().filter(item -> item.getValue() instanceof ByteCode).map(
                item -> (ByteCode) item.getValue()
        ).collect(Collectors.toList());

        return new LinkedList(tempResult);
    }

    /**
     * Dump file.
     *
     * @param _className
     * @param _byteArray
     * @param _suffix
     * @param _directory
     */
    public static void dump(String _className, byte[] _byteArray, String _suffix, File _directory) {
        try {
            OutputStream tempOutputSteam = createDumpFile(_className, _suffix, _directory);
            if (tempOutputSteam == null) {
                return;
            }

            tempOutputSteam.write(_byteArray);
            tempOutputSteam.close();
        } catch (IOException e) {
            log.warn("Could not dump " + _className, e);
        }

    }

    /**
     * Create dump file.
     *
     * @param _className: file name.
     * @param _suffix:    file suffix.
     * @param _directory: directory.
     * @return
     * @throws FileNotFoundException
     */
    private static OutputStream createDumpFile(String _className, String _suffix, File _directory) throws FileNotFoundException {
        File tempFile = new File(_directory, _className.replace('.', File.separatorChar) + _suffix);
        File tempDirectory = tempFile.getParentFile();
        if (!tempDirectory.exists()) {
            boolean var6 = tempDirectory.mkdirs();
            if (!var6) {
                log.warn("Could not create dump store dir " + tempDirectory);
                return null;
            }
        }

        return new FileOutputStream(tempFile);
    }

    private File getDefaultDumpDir() {
        if (this.defaultDumpDir == null) {
            this.defaultDumpDir = new File("compiler.dump");
            String tempDirectory = System.getProperty("VARROOT");
            if (tempDirectory != null) {
                File tempFile = new File(tempDirectory);
                if (tempFile.exists()) {
                    this.defaultDumpDir = new File(tempFile, "tmp" + File.separatorChar + "compiler.dump");
                }
            }
        }

        return this.defaultDumpDir;
    }
}
