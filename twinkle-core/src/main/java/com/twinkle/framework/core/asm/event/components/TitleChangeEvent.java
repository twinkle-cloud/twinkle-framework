package com.twinkle.framework.core.asm.event.components;

import com.twinkle.framework.core.asm.event.Event;

/**
 * Event for changing the main window's title.
 * 
 * @author Matt
 */
public class TitleChangeEvent extends Event {
	private final String title;

	public TitleChangeEvent(String title) {
		this.title = title;
	}

	/**
	 * @return New window title.
	 */
	public String getTitle() {
		return title;
	}
}