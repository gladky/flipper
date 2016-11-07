package cern.ch.cms.flipper.model;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.SimpleFifoQueue;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Event;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class Switch extends SinglePogressObject {

	/** Data currently in this object */
	protected final SimpleFifoQueue outputQueue;

	private static final Logger logger = Logger.getLogger(Switch.class);

	public Switch(String name, SoundPlayer soundPlayer) {

		// arg 2nd: switch has always capacity of processing 4 frangents
		// arg 3nd: switch is very fast so step is big
		super(name, 4, 50, soundPlayer);
		this.outputQueue = new SimpleFifoQueue(1);
	}

	@Override
	public boolean canAccept() {

		boolean basicAccept = super.canAccept();

		if (basicAccept) {
			return true;
		} else {
			logger.debug(name + " cannot accept new event. Me: " + basicAccept);
			return false;
		}

	}

	@Override
	public int stepImplementation(Data data) {

		if (queue.size() == 4) {

			Data fragment1 = queue.get(0);
			Data fragment2 = queue.get(1);
			Data fragment3 = queue.get(2);
			Data fragment4 = queue.get(3);
			Event event = new Event(fragment1, fragment2, fragment3, fragment4);
			outputQueue.add(event);
			queue.clear();
			logger.debug(
					name + " Built new event: " + event.getName() + " with the target " + event.getTarget());

			return 100;
		} else if (queue.size() == 3) {
			return 75;
		} else if (queue.size() == 2) {
			return 50;
		} else if (queue.size() == 1) {
			return 25;
		} else {
			return 0;
		}

	}

	@Override
	protected boolean canSend() {

		logger.debug(name + " looking for bufu successor");
		return true;

	}

	@Override
	protected void sendData() {
		Data data = outputQueue.poll();

		FlipperObject bufuSuccessor = data.getTarget();
		if ( bufuSuccessor.canAccept()) {
			logger.info(name + " sending event " + data.getName() + " to " + bufuSuccessor.name);
			bufuSuccessor.setBusy(true);
			bufuSuccessor.insert(data);
			return;
		}
	}

	@Override
	protected void finished() {
		logger.debug(name + " finished assemblying event");
		return;
	}

	public SimpleFifoQueue getOutputQueue() {
		return outputQueue;
	}

}
