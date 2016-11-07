package cern.ch.cms.flipper.model;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public abstract class IndividualPogressObject extends FlipperObject {

	private final static Logger logger = Logger.getLogger(IndividualPogressObject.class);

	/** One event per cycle can be accepted */
	private boolean acceptedThisCycle;

	private final int fakeInf = 100000;

	public IndividualPogressObject(String name, int capacity, int progressStep, SoundPlayer soundPlayer) {
		super(name, capacity, progressStep, soundPlayer);
	}

	protected void performInsert(Data data) {
		data.setProgress(0);
		data.setTimeOutProgress(0);
		queue.add(data);
		acceptedThisCycle = true;
	}

	@Override
	public boolean canAccept() {

		if (acceptedThisCycle) {
			logger.info(name + " Cannot accept. Only one event per cycle can be accepted");
			return false;
		}
		return super.canAccept();
	}

	/** Indicator of simulation progress in this object, values: 0-99 */
	public int[] getProgress() {
		return queue.getProgress();
	}

	@Override
	public int stepImplementation(Data current) {
		int newProgress = current.getProgress() + progressStep;
		logger.trace("Doing step: " + current.getProgress() + " -> " + newProgress);
		current.setProgress(newProgress);
		return newProgress;
	}

	/** Do step of simulation, will increase the progress with step */
	@Override
	public void doStep() {

		if (!queue.isEmpty()) {

			int localProgressLimit = fakeInf;

			int initialSize = queue.size();
			int recentSize = initialSize;

			for (int i = 0; i < recentSize; i++) {

				Data current = queue.get(i);
				logger.trace("Processing data " + current.getName());

				if (current.getProgress() + progressStep < localProgressLimit) {
					logger.trace(name + " will do step, progress before step: " + current.getProgress()
							+ " current local progress limit: " + localProgressLimit);
					int progress = stepImplementation(current);
					if (progress < localProgressLimit) {
						localProgressLimit = progress;
					}
					if (progress > 99) {

						finished();

						if (canSend()) {
							sendData();
							// localProgressLimit = fakeInf;
						}
					}

				} else {

					logger.debug("Local data backpressure, " + current.getName() + " cannot progress from "
							+ current.getProgress() + " waiting for predecessor data with progress" + localProgressLimit
							+ " to increase");
					if (current.getProgress() < localProgressLimit) {
						localProgressLimit = current.getProgress();
					}
				}

				if (localProgressLimit > 100) {
					localProgressLimit = 100;
				}

				if (recentSize != queue.size()) {
					recentSize = queue.size();
					i--;
				}
			}
		}
		acceptedThisCycle = false;
	}

	protected void finished() {
		Data data = queue.peek();
		logger.debug(name + " finished with " + data + " my progress is now " + data.getProgress());
		return;
	}
}
