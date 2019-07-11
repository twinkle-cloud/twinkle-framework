package com.twinkle.framework.core.asm.assembly.components;

import com.twinkle.framework.core.asm.assembly.AbstractAssembler;
import com.twinkle.framework.core.asm.bytecode.insn.NamedLabelNode;
import com.twinkle.framework.core.utils.InsnUtil;
import com.twinkle.framework.core.utils.UniMatcher;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Label assembler
 * <pre>
 *     &lt;LABEL_TITLE&gt;
 * </pre>
 * @author chenxj
 */
public class Label extends AbstractAssembler<LabelNode> {
	/**
	 * Matcher for the label name.
	 */
	private final static UniMatcher<String> matcher = new UniMatcher<>("^[\\w-]+$", (s -> s));

	public Label(int opcode) {super(opcode);}

	@Override
	public LabelNode parse(String text) {
		if(matcher.run(text)) {
			return new NamedLabelNode(matcher.get());
		}
		return fail(text, "Expected: <LABEL_TITLE>");
	}

	@Override
	public String generate(MethodNode method, LabelNode insn) {
		return "LABEL " + InsnUtil.labelName(insn);
	}
}