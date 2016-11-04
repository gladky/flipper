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
		logger.debug(name + " Button enabled");
		enabled = true;
		pressed = false;
		disableRequest = false;
	}

	public boolean press() {
		logger.trace(name + " Button pressed");
		if (enabled) {
			logger.debug(name + " Button pressed when enabled");
			pressed = true;
			//isableRequest = false;
			return true;
		} else {
			logger.debug(name + " Ignoring button pressed");
			return false;
		}
	}

	public boolean isPressed() {
		return this.pressed;
	}

	public void disable() {
		logger.debug(name + " Button disable requested");
		disableRequest = true;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void doStep() {
		if (disableRequest) {
			logger.debug("Button disabled");
			enabled = false;
			pressed = false;
			disableRequest = false;
		}
	}

}
