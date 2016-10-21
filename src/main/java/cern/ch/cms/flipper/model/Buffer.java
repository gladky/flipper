package cern.ch.cms.flipper.model;

import cern.ch.cms.flipper.controllers.Button;

public class Buffer extends Clickable {

	public Buffer(String name, int capacity, int progressStep, int timeoutStep, Button button) {
		super(name, capacity, progressStep, timeoutStep, button);
	}

}
