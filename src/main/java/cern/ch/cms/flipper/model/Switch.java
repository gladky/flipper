package cern.ch.cms.flipper.model;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.SimpleFifoQueue;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Event;

public class Switch extends FlipperObject {

	/** Data currently in this object */
	protected final SimpleFifoQueue outputQueue;

	private static final Logger logger = Logger.getLogger(Switch.class);

	public Switch(String name) {

		// arg 2nd: switch has always capacity of processing 4 frangents
		// arg 3nd: switch is very fast so step is big
		super(name, 4, 50);
		this.outputQueue = new SimpleFifoQueue(1);
	}

	@Override
	public boolean canAccept() {

		boolean basicAccept = super.canAccept();

		//boolean backpressure = !canSend();

		//if (basicAccept && !backpressure) {
			if (basicAccept) {
			return true;
		} else {
			logger.info(name + " cannot accept new event. Me: " + basicAccept);// + ", backpressure: " + backpressure);
			return false;
		}

	}

	@Override
	public int stepImplementation() {

		if (queue.size() == 4) {

			Data fragment1 = queue.get(0);
			Data fragment2 = queue.get(1);
			Data fragment3 = queue.get(2);
			Data fragment4 = queue.get(3);
			Event event = new Event(fragment1, fragment2, fragment3, fragment4);
			outputQueue.add(event);
			queue.clear();
			logger.info(name + " Build new event: " + event.getName());

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
		boolean atLeastOneAccept = false;

		FlipperObject successorToAvailableBufu = outputQueue.peek().getTarget();
		logger.debug(name + " Dispatched to Bufu successor " + successorToAvailableBufu.name
				+ ", will now verify if not busy");
		if (!successorToAvailableBufu.isBusy()) {
			atLeastOneAccept = true;
			logger.debug(name + " can send event, available bufu successor: " + successorToAvailableBufu.name);
		}

		if (!atLeastOneAccept) {
			logger.info("There is no BUFU avaialble right now");
		}
		return atLeastOneAccept;

	}

	@Override
	protected void sendData() {
		Data data = outputQueue.poll();

		for (FlipperObject successor : getSuccessors()) {

			FlipperObject bufuSuccessor = data.getTarget();
			if (!bufuSuccessor.isBusy() && successor.canAccept()) {
				logger.info(name + " sending event " + data.getName() + " to " + successor.name);
				bufuSuccessor.setBusy(true);
				successor.insert(data);
				return;
			}
		}
	}

	@Override
	protected void finished() {
		logger.debug(name + " finished assemblying event");
		return;
	}

}
