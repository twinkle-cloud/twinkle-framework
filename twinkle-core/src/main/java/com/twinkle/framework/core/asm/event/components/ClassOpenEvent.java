package com.twinkle.framework.core.asm.event.components;

import com.twinkle.framework.core.asm.event.Event;
import org.objectweb.asm.tree.ClassNode;

/**
 * Event for when a class is selected.
 * 
 * @author Matt
 */
public class ClassOpenEvent extends Event {
	private final ClassNode node;

	public ClassOpenEvent(ClassNode node) {
		this.node = node;
	}

	/**
	 * @return Node selected.
	 */
	public ClassNode getNode() {
		return node;
	}
}