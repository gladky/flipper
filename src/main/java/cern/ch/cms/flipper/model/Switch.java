package cern.ch.cms.flipper.model;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.log4j.Logger;

import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Event;

public class Switch extends FlipperObject {

	/** Data currently in this object */
	protected final CircularFifoQueue<Event> outputQueue;

	private static final Logger logger = Logger.getLogger(Switch.class);

	public Switch(String name) {

		// arg 2nd: switch has always capacity of processing 4 frangents
		// arg 3nd: switch is very fast so step is big
		super(name, 4, 50);
		this.outputQueue = new CircularFifoQueue<Event>(1);
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

		boolean atLeastOneAccept = false;
		for (FlipperObject successor : getSuccessors()) {
			boolean accept = successor.canAccept();
			if (accept) {
				atLeastOneAccept = true;
			}
		}
		return atLeastOneAccept;

	}

	private FlipperObject findBUFUSuccesor(FlipperObject successor) {

		if (successor instanceof BUFU) {
			return successor;
		} else {
			for (FlipperObject nextSuccessor : successor.getSuccessors()) {
				return findBUFUSuccesor(nextSuccessor);
			}
		}
		logger.error(name + " I have no indirect successor of type BUFU, check the model");
		return null;

	}

	@Override
	protected void sendData() {
		Data data = outputQueue.poll();

		for (FlipperObject successor : getSuccessors()) {

			FlipperObject bufuSuccessor = findBUFUSuccesor(successor);
			if (bufuSuccessor.canAccept()) {
				logger.info(name + " sending event " + data.getName() + " to " + successor.name);
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
