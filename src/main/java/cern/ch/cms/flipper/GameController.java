package cern.ch.cms.flipper;

import java.util.HashSet;
import java.util.Set;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.model.FlipperObject;

public class GameController {

	private final Set<FlipperObject> flipperObjects;
	private final Set<Button> buttons;

	public GameController() {
		this.flipperObjects = new HashSet<FlipperObject>();
		this.buttons = new HashSet<Button>();
	}

	public Set<FlipperObject> getFlipperObjects() {
		return flipperObjects;
	}

	public void doStep() {
		for (FlipperObject flipperObject : flipperObjects) {
			flipperObject.doStep();
		}

		for (Button button : buttons) {
			button.doStep();
		}
	}

	public Set<Button> getButtons() {
		return buttons;
	}
}
