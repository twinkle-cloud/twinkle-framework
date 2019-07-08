package com.twinkle.framework.core.asm.assembly.components;

import com.twinkle.framework.core.asm.assembly.AbstractAssembler;
import com.twinkle.framework.core.asm.bytecode.AccessFlag;
import com.twinkle.framework.core.asm.bytecode.insn.NamedVarInsnNode;
import com.twinkle.framework.core.asm.utils.InsnUtil;
import com.twinkle.framework.core.asm.assembly.NamedVariableGenerator;
import com.twinkle.framework.core.asm.utils.OpcodeUtil;
import com.twinkle.framework.core.asm.utils.UniMatcher;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * Local variable assembler
 * <pre>
 *     &lt;INDEX&gt;
 * </pre>
 *
 * @author Matt
 */
public class Var extends AbstractAssembler<VarInsnNode> implements NamedVariableGenerator {
	/**
	 * Matcher for the variable posiiton.
	 */
	private final static UniMatcher<String> matcher =
			new UniMatcher<>("^\\w+$", (s -> s));

	public Var(int opcode) {super(opcode);}

	@Override
	public VarInsnNode parse(String text) {
		if (matcher.run(text))
			return new NamedVarInsnNode(opcode, matcher.get());
		return fail(text, "Expected: <VARIABLE>");
	}

	@Override
	public String generate(MethodNode method, VarInsnNode insn) {
		int index = insn.var;
		LocalVariableNode lvn = InsnUtil.getLocal(method, index);
		String variable = null;
		if(lvn == null) {
			// Use "this" when possible
			if(index == 0 && !AccessFlag.isStatic(method.access)) {
				variable = "this";
			} else {
				variable = String.valueOf(index);
			}
		} else {
			variable = name(method, index, lvn.name);
		}
		return OpcodeUtil.opcodeToName(opcode) + " " + variable;
	}
}