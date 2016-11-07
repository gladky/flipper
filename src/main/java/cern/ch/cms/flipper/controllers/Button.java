package cern.ch.cms.flipper.controllers;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.model.NamedObject;
import cern.ch.cms.flipper.sounds.Sound;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class Button extends NamedObject {

	private static final Logger logger = Logger.getLogger(Button.class);

	/** Only when enabled pressing will be accepted */
	private boolean enabled;

	/** pressed or not */
	private boolean pressed;

	private boolean disableRequest;

	private final SoundPlayer soundPlayer;
	
	public Button(String name, SoundPlayer soundPlayer) {
		super(name);
		this.enabled = false;
		this.pressed = false;
		this.disableRequest = false;
		this.soundPlayer = soundPlayer;
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
			soundPlayer.register(Sound.ButtonPressedWhenEnabled);
			return true;
		} else {
			logger.debug(name + " Ignoring button pressed");
			soundPlayer.register(Sound.ButtonPressedWhenDisabled);
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
