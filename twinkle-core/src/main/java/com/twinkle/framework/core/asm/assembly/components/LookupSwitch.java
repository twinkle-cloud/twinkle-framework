package com.twinkle.framework.core.asm.assembly.components;

import com.twinkle.framework.core.asm.assembly.AbstractAssembler;
import com.twinkle.framework.core.asm.bytecode.insn.NamedLookupSwitchInsnNode;
import com.twinkle.framework.core.utils.GroupMatcher;
import com.twinkle.framework.core.utils.InsnUtil;
import com.twinkle.framework.core.utils.OpcodeUtil;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * LookupSwitch assembler
 * <pre>
 *     mapping[&lt;MAPPING&gt;...] default[&lt;OFFSET/LABEL&gt;]
 * Examples:
 *     mapping[0=A, 1=B, 2=C] default[D]
 *     map[0=A, 1=B, 2=C] dflt[D]
 *     [0=A, 1=B, 2=C] [D]
 * </pre>
 *
 * @author Matt
 */
public class LookupSwitch extends AbstractAssembler<LookupSwitchInsnNode> {
	/**
	 * Matcher for the switch.
	 */
	private final static GroupMatcher matcher =
			new GroupMatcher("^(mapping|map)?\\[({MAPPING}(\\d+=\\w+[,\\s]*)+)+\\]\\s(default|dflt)" +
					"?\\[({DEFAULT}.+)\\]",
					new HashMap<String, Function<String, Object>>() {{
						put("MAPPING", (s -> s));
						put("DEFAULT", (s -> s));
					}});

	public LookupSwitch(int opcode) {super(opcode);}

	@Override
	public LookupSwitchInsnNode parse(String text) {
		if(matcher.run(text)) {
			String mapping = matcher.get("MAPPING");
			String dflt = matcher.get("DEFAULT");
			String[] mappingSplit = mapping.split("[,\\s]+");
			if(mappingSplit.length == 0)
				fail(text, "Failed to parse mappings");
			String[] labels = new String[mappingSplit.length];
			int[] keys = new int[mappingSplit.length];
			int i = 0;
			for(String map : mappingSplit) {
				String[] mapSplit = map.split("=");
				keys[i] = Integer.parseInt(mapSplit[0]);
				labels[i] = mapSplit[1];
				i++;
			}
			return new NamedLookupSwitchInsnNode(dflt, labels, keys);
		}
		return fail(text, "Expected: mapping[<MAPPING>...] default[<OFFSET/LABEL>]");
	}

	@Override
	public String generate(MethodNode method, LookupSwitchInsnNode insn) {
		List<String> keys = new ArrayList<>();
		for (int i = 0; i < insn.keys.size(); i++)
			keys.add(insn.keys.get(i) + "=" + InsnUtil.labelName(insn.labels.get(i)));
		String dflt = InsnUtil.labelName(insn.dflt);
		return OpcodeUtil.opcodeToName(opcode) + " mapping[" + String.join(",", keys) + "] default[" + dflt + "]";
	}
}