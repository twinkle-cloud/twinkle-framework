package com.twinkle.framework.core.asm.event.components;

import javafx.scene.Node;
import com.twinkle.framework.core.asm.event.Event;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Event for when a method is selected.
 * 
 * @author Matt
 */
public class MethodOpenEvent extends Event {
	private final ClassNode owner;
	private final MethodNode method;
	private final Node node;

	public MethodOpenEvent(ClassNode owner, MethodNode method, Node node) {
		this.owner = owner;
		this.method = method;
		this.node = node;
	}

	/**
	 * @return ClassNode that contains the {@link #getMethod() method}.
	 */
	public ClassNode getOwner() {
		return owner;
	}

	/**
	 * @return Method selected.
	 */
	public MethodNode getMethod() {
		return method;
	}

	/**
	 * @return Node method selected from.
	 */
	public Node getContainerNode() {
		return node;
	}
}