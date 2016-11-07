package cern.ch.cms.flipper.model;

import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public abstract class SinglePogressObject extends FlipperObject {

	private int progress;

	public SinglePogressObject(String name, int capacity, int progressStep, SoundPlayer soundPlayer) {
		super(name, capacity, progressStep, soundPlayer);
	}

	protected void performInsert(Data data) {
		queue.add(data);
		int size = queue.size();
		int result = size * 100 / capacity;
		this.progress = result;

	}

	/** Do step of simulation, will increase the progress with step */
	@Override
	public void doStep() {
		int progress = stepImplementation(null);
		if (progress > 99) {

			if (canSend()) {
				sendData();
			}

		}
	}

	/** Can accept more data */
	@Override
	public boolean canAccept() {

		if (queue.size() == capacity) {
			return false;
		} else {
			return true;
		}

	}

	/** Indicator of simulation progress in this object, values: 0-99 */
	@Override
	public int[] getProgress() {

		return new int[] { progress };
	}

	@Override
	protected int stepImplementation(Data data) {
		return 0;
	}

}
