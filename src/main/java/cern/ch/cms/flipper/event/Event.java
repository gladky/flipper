package cern.ch.cms.flipper.event;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.NameGenerator;

public class Event extends Data {

	private static final Logger logger = Logger.getLogger(Event.class);

	public Event(Data fragment1, Data fragment2, Data fragment3, Data fragment4) {
		super(generateSimpleName(fragment4.getName()),false);
		this.setTarget(fragment4.getTarget());
		this.setDispatched(fragment4.isDispatched());
	}

	public Event(String name) {
		super(name,false);
	}

	private static String generateSimpleName(String i) {
		// for the production do NOT use NameGenerator (its for debugging only)
		// String name = "e" + i;
		String name = NameGenerator.generateSimpleName(i);
		logger.debug("Generated name is: " + name);

		return name;
	}

}
