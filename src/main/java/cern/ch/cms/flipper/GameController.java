package cern.ch.cms.flipper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.model.Dispatcher;
import cern.ch.cms.flipper.model.FlipperObject;

public class GameController {

	private final List<FlipperObject> flipperObjects;
	private final Set<Button> buttons;
	private Dispatcher dispatcher;

	private static final Logger logger = Logger.getLogger(GameController.class);

	public GameController() {
		this.flipperObjects = new ArrayList<FlipperObject>();
		this.buttons = new LinkedHashSet<Button>();
	}

	public List<FlipperObject> getFlipperObjects() {
		return flipperObjects;
	}

	public void doStep() {

		logger.debug("Processing round");

		ListIterator<FlipperObject> li = flipperObjects.listIterator(flipperObjects.size());

		while (li.hasPrevious()) {
			FlipperObject flipperObject = li.previous();
			logger.trace("Processing: " + flipperObject.getName());
			flipperObject.doStep();
		}

		for (Button button : buttons) {
			button.doStep();
		}
		dispatcher.invalidate();
		
	}

	public Set<Button> getButtons() {
		return buttons;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
}
