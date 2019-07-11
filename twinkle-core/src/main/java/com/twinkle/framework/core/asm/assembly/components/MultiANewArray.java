package com.twinkle.framework.core.asm.assembly.components;

import com.twinkle.framework.core.asm.assembly.TokenAssembler;
import com.twinkle.framework.core.utils.*;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;

import java.util.Collections;

/**
 * MultiANewArray assembler
 * <pre>
 *     &lt;TYPE&gt; &lt;DIMENSION&gt;
 * </pre>
 *
 * @author Matt
 */
public class MultiANewArray extends TokenAssembler<MultiANewArrayInsnNode> {
	public MultiANewArray(int opcode) {super(opcode);}

	@Override
	public MultiANewArrayInsnNode parse(String text) {
		RegexToken matcher = token();
		MatchResult result = matcher.matches(text);
		if(result.isSuccess()) {
			String type = matcher.getMatch("TYPE");
			int dimensions = matcher.getMatch("DIMENSION");
			return new MultiANewArrayInsnNode(type, dimensions);
		}
		return fail(text, "Expected: <TYPE> <DIMENSION>", result.getFailedToken().getToken());
	}

	@Override
	public String generate(MethodNode method, MultiANewArrayInsnNode insn) {
		return OpcodeUtil.opcodeToName(opcode) + " " + insn.desc + " " + insn.dims;
	}

	@Override
	public RegexToken createToken() {
		return RegexToken
				.create("TYPE", new UniMatcher<>("[$\\w\\/]+", (s -> s)),
						((tok, part) -> AutoComplete.internalName(part)))
				.append("DIMENSION", new UniMatcher<>("(?!= )[\\d]+", (s->Integer.parseInt(s))),
						((tok, part) -> Collections.emptyList()))
				.root();
	}
}