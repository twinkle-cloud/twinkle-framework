package com.twinkle.framework.core.asm.event.components;

import com.twinkle.framework.core.asm.event.Event;

import java.io.File;

/**
 * Event for requesting exporting.
 * 
 * @author Matt
 */
public class RequestExportEvent extends Event {
	private final File file;

	public RequestExportEvent(File file) {
		this.file = file;
	}

	/**
	 * @return Location to save file to.
	 */
	public File getFile() {
		return file;
	}
}