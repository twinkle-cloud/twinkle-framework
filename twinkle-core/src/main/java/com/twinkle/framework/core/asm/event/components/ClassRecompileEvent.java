package com.twinkle.framework.core.asm.event.components;

import com.twinkle.framework.core.asm.event.Event;
import org.objectweb.asm.tree.ClassNode;

/**
 * Event for when a class is recompiled.
 * 
 * @author Matt
 */
public class ClassRecompileEvent extends Event {
	private final ClassNode oldValue, newValue;

	public ClassRecompileEvent(ClassNode oldValue, ClassNode newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * @return Old instance of node.
	 */
	public ClassNode getOldNode() {
		return oldValue;
	}
	
	/**
	 * @return New instance of node.
	 */
	public ClassNode getNewNode() {
		return newValue;
	}
}