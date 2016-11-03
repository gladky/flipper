package cern.ch.cms.flipper;

import java.util.List;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.model.BUFU;
import cern.ch.cms.flipper.model.Buffer;
import cern.ch.cms.flipper.model.Dispatcher;
import cern.ch.cms.flipper.model.FlipperObject;
import cern.ch.cms.flipper.model.Link;
import cern.ch.cms.flipper.model.Storage;
import cern.ch.cms.flipper.model.Switch;

public class FlipperObjectFactory {

	private static int counter = 0;

	private final GameController model;
	
	private static final Logger logger = Logger.getLogger(FlipperObjectFactory.class);

	public FlipperObjectFactory(GameController model) {
		this.model = model;
	}

	public FlipperObject createLink(String name, int leds) {
		int calculatedStep = 100 / leds;
		if (calculatedStep < 1) {
			calculatedStep = 1;
		} else if (calculatedStep > 50) {
			calculatedStep = 50;
		}
		
		logger.info("Calculated step fore link " + name + " is " + calculatedStep);
		

		String uniqueName = getShortName(name);
		FlipperObject link = new Link(uniqueName, 1, calculatedStep);
		model.getFlipperObjects().add(link);
		return link;
	}

	public FlipperObject createBUFU(String name, Button button) {
		String uniqueName = getShortName(name);
		FlipperObject bufu = new BUFU(uniqueName, 10, 25, button);
		model.getFlipperObjects().add(bufu);
		return bufu;
	}

	public FlipperObject createSwitch(String name) {
		String uniqueName = getShortName(name);
		FlipperObject switch_ = new Switch(uniqueName);
		model.getFlipperObjects().add(switch_);
		return switch_;
	}

	private String getUniqueName2(String name) {
		return "[" + String.format("%02d", counter++) + ": " + name + "]";
	}

	private String getShortName(String name) {
		counter++;
		return name;
	}

	public FlipperObject createStorage() {

		String uniqueName = getShortName("Storage");
		FlipperObject storage = new Storage(uniqueName, 40);
		model.getFlipperObjects().add(storage);
		return storage;
	}

	public Buffer createBuffer(String name, Button button) {

		String uniqueName = getShortName(name);
		Buffer buffer = new Buffer(uniqueName, 12, 10, 10, button);
		model.getFlipperObjects().add(buffer);
		return buffer;

	}

	public Dispatcher createDispatcher(List<FlipperObject> bufus, List<FlipperObject> links) {
		Dispatcher dispatcher = new Dispatcher(bufus, links);
		model.setDispatcher(dispatcher);
		return dispatcher;
	}

	public Button createButton(String name) {
		String uniqueName = getShortName(name);
		Button button = new Button(uniqueName);
		model.getButtons().add(button);
		return button;
	}
}
