package cern.ch.cms.flipper.model;

import cern.ch.cms.flipper.sounds.SoundPlayer;

public class Storage extends FlipperObject {

	public Storage(String name, int capacity, SoundPlayer soundPlayer) {
		super(name, capacity, 0, soundPlayer);
	}

	@Override
	public int[] getProgress() {
		int size = queue.size();
		int result = size * 100 / capacity;
		return new int[] { result };

	}

	@Override
	public void doStep() {

	}
}
