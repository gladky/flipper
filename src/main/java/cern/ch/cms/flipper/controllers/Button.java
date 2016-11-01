package cern.ch.cms.flipper.controllers;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.model.NamedObject;

public class Button extends NamedObject {

	private static final Logger logger = Logger.getLogger(Button.class);

	/** Only when enabled pressing will be accepted */
	private boolean enabled;

	/** pressed or not */
	private boolean pressed;

	private boolean disableRequest;

	public Button(String name) {
		super(name);
		this.enabled = false;
		this.pressed = false;
		this.disableRequest = false;
	}

	public void enable() {
		enabled = true;
		pressed = false;
	}

	public void press() {
		if (enabled) {
			pressed = true;
		} else {
			logger.debug("Ignoring button pressed");
		}
	}

	public boolean isPressed() {
		return this.pressed;
	}

	public void disable() {
		disableRequest = true;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void doStep() {
		if (disableRequest) {
			logger.debug("Button deactivated");
			enabled = false;
			pressed = false;
			disableRequest = false;
		}
	}

}
