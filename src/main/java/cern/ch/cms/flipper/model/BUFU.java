package cern.ch.cms.flipper.model;

import cern.ch.cms.flipper.controllers.Button;

public class BUFU extends Clickable {

	public BUFU(String name, int progressStep, int timeoutStep, Button button) {
		// capacity is always 1 as bufu may process one event
		super(name, 1, progressStep, timeoutStep, button);
	}

}
