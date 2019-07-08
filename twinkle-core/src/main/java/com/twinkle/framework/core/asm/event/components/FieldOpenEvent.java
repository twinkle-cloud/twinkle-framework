package com.twinkle.framework.core.asm.event.components;

import javafx.scene.Node;
import com.twinkle.framework.core.asm.event.Event;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

/**
 * Event for when a field is selected.
 * 
 * @author Matt
 */
public class FieldOpenEvent extends Event {
	private final ClassNode owner;
	private final FieldNode field;
	private final Node node;

	public FieldOpenEvent(ClassNode owner, FieldNode field, Node node) {
		this.owner = owner;
		this.field = field;
		this.node = node;
	}

	/**
	 * @return ClassNode that contains the {@link #getNode() field}.
	 */
	public ClassNode getOwner() {
		return owner;
	}

	/**
	 * @return Field selected.
	 */
	public FieldNode getNode() {
		return field;
	}

	/**
	 * @return Node field selected from.
	 */
	public Node getContainerNode() {
		return node;
	}

}