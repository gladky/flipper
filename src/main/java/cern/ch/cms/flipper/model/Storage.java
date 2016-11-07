package cern.ch.cms.flipper.model;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class Storage extends SinglePogressObject {

	private static final Logger logger = Logger.getLogger(Storage.class);

	public Storage(String name, int capacity, SoundPlayer soundPlayer) {
		super(name, capacity, 0, soundPlayer);
	}

	/** Can accept more data */
	@Override
	public boolean canAccept() {

		if (queue.size() == capacity) {
			logger.debug(name + " sorry, I cannot accept, I'm full");
			return false;
		} else {
			return true;
		}

	}

	@Override
	protected void finished() {
		logger.info("Storage is full");
	}

}
