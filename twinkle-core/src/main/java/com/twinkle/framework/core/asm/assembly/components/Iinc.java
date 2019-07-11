package com.twinkle.framework.core.asm.assembly.components;

import com.twinkle.framework.core.asm.assembly.AbstractAssembler;
import com.twinkle.framework.core.asm.bytecode.insn.NamedIincInsnNode;
import com.twinkle.framework.core.utils.GroupMatcher;
import com.twinkle.framework.core.utils.InsnUtil;
import com.twinkle.framework.core.asm.assembly.NamedVariableGenerator;
import com.twinkle.framework.core.utils.OpcodeUtil;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Iinc assembler
 * <pre>
 *     &lt;VARIABLE&gt; &lt;OPERATION&gt; &lt;VALUE&gt;
 * </pre>
 *
 * @author Matt
 */
public class Iinc extends AbstractAssembler<IincInsnNode> implements NamedVariableGenerator {
	/**
	 * Matcher for the increment values.
	 */
	private final static GroupMatcher matcher =
			new GroupMatcher("({VARIABLE}\\w+)\\s+({OPERAND}[-+])?\\s*({INCREMENT}\\d+)",
					new HashMap<String, Function<String, Object>>() {{
						put("VARIABLE", (s -> s));
						put("OPERAND", (s -> s));
						put("INCREMENT", (s -> Integer.parseInt(s)));
					}});

	public Iinc(int opcode) {super(opcode);}

	@Override
	public String generate(MethodNode method, IincInsnNode insn) {
		int index = insn.var;
		LocalVariableNode lvn = InsnUtil.getLocal(method, index);
		String variable = lvn == null ? String.valueOf(index) : name(method, index, lvn.name);
		String operation = insn.incr >= 0 ? "+" : "-";
		int value = Math.abs(insn.incr);
		return OpcodeUtil.opcodeToName(opcode) + " " + variable + " " + operation + " " + value;
	}

	@Override
	public IincInsnNode parse(String text) {
		if(matcher.run(text)) {
			String index = matcher.get("VARIABLE");
			int increment = matcher.get("INCREMENT");
			// Optional operand to specify negative numbers
			String op = matcher.get("OPERAND");
			if(op != null && op.equalsIgnoreCase("-")) {
				increment = -increment;
			}
			return new NamedIincInsnNode(increment, index);
		}
		return fail(text, "Expected: <VARIABLE> <OPERATION> <VALUE>");
	}
}