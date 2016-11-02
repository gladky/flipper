package cern.ch.cms.flipper;

import java.util.List;

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

	public FlipperObjectFactory(GameController model) {
		this.model = model;
	}

	public FlipperObject createLink(String name) {
		String uniqueName = getUniqueName(name);
		FlipperObject link = new Link(uniqueName, 1, 25);
		model.getFlipperObjects().add(link);
		return link;
	}

	public FlipperObject createBUFU(String name, Button button) {
		String uniqueName = getUniqueName(name);
		FlipperObject bufu = new BUFU(uniqueName, 10, 25, button);
		model.getFlipperObjects().add(bufu);
		return bufu;
	}

	public FlipperObject createSwitch(String name) {
		String uniqueName = getUniqueName(name);
		FlipperObject switch_ = new Switch(uniqueName);
		model.getFlipperObjects().add(switch_);
		return switch_;
	}

	private String getUniqueName(String name) {
		return "[" + String.format("%02d", counter++) + ": " + name + "]";
	}

	public FlipperObject createStorage() {

		String uniqueName = getUniqueName("Storage");
		FlipperObject storage = new Storage(uniqueName);
		model.getFlipperObjects().add(storage);
		return storage;
	}

	public Buffer createBuffer(String name, Button button) {

		String uniqueName = getUniqueName(name);
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
		String uniqueName = getUniqueName(name);
		Button button = new Button(uniqueName);
		model.getButtons().add(button);
		return button;
	}
}
