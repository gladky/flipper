package cern.ch.cms.flipper.controllers;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.model.NamedObject;

public class Button extends NamedObject {

	private static final Logger logger = Logger.getLogger(Button.class);

	private boolean activated;

	private boolean state;

	private boolean deactivateRequest;

	public Button(String name) {
		super(name);
		this.activated = false;
		this.state = false;
		this.deactivateRequest = false;
	}

	public void activate() {
		activated = true;
		state = false;
	}

	public void press() {
		if (activated) {
			state = true;
		} else {
			logger.info("Ignoring button pressed");
		}
	}

	public boolean getState() {
		return this.state;
	}

	public void deactivate() {
		deactivateRequest = true;
	}

	public boolean isActivated() {
		return activated;
	}

	public void doStep() {
		if (deactivateRequest) {
			logger.info("Button deactivated");
			activated = false;
			state = false;
			deactivateRequest = false;
		}
	}

}
