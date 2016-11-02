package cern.ch.cms.flipper;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.model.Dispatcher;
import cern.ch.cms.flipper.model.FlipperObject;

public class FlipperObjectTestBase {

	public void doSteps(FlipperObject[] objects) {
		for (FlipperObject object : objects) {
			object.doStep();
		}
	}

	public void doSteps(Button[] buttons) {
		for (Button object : buttons) {
			object.doStep();
		}
	}

	public void doSteps(FlipperObject[] flipperObjects, Button[] buttons) {
		doSteps(flipperObjects);
		doSteps(buttons);
	}
	
	public void doSteps(FlipperObject[] flipperObjects, Button[] buttons, Dispatcher dispatche) {
		doSteps(flipperObjects);
		doSteps(buttons);
		dispatche.invalidate();
	} 

}
