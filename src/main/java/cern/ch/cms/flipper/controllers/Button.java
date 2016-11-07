package cern.ch.cms.flipper.controllers;

import cern.ch.cms.flipper.model.NamedObject;
import cern.ch.cms.flipper.sounds.Sound;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class Button extends NamedObject {


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
		enabled = true;
		pressed = false;
		disableRequest = false;
	}

	public boolean press() {
		if (enabled) {
			pressed = true;
			soundPlayer.register(Sound.ButtonPressedWhenEnabled);
			return true;
		} else {
			soundPlayer.register(Sound.ButtonPressedWhenDisabled);
			return false;
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
			enabled = false;
			pressed = false;
			disableRequest = false;
		}
	}

}
