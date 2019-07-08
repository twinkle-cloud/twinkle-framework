package com.twinkle.framework.core.asm.assembly.components;

import com.twinkle.framework.core.asm.assembly.AbstractAssembler;
import com.twinkle.framework.core.asm.utils.OpcodeUtil;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Insn assembler
 *
 * <pre>
 *     n/a - no args
 * </pre>
 *
 * @author Matt
 */
public class Insn extends AbstractAssembler<InsnNode> {
	public Insn(int opcode) {super(opcode);}

	@Override
	public InsnNode parse(String text) {
		return new InsnNode(opcode);
	}

	@Override
	public String generate(MethodNode method, InsnNode insn) {
		return OpcodeUtil.opcodeToName(opcode);
	}
}