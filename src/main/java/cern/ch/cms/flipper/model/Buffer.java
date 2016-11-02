package cern.ch.cms.flipper.model;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.controllers.Button;

public class Buffer extends Clickable {

	private static Logger logger = Logger.getLogger(Buffer.class);

	private Dispatcher dispatcher;

	public Buffer(String name, int capacity, int progressStep, int timeoutStep, Button button) {
		super(name, capacity, progressStep, timeoutStep, button);
	}

	@Override
	protected boolean backpressure() {

		boolean backpressure = dispatcher.isBackpressure();
		return backpressure;
	}

	@Override
	protected void dispatch() {

		if (!queue.peek().isDispatched()) {
			logger.trace(name + " Dispatching");
			FlipperObject next = dispatcher.findAvailableTarget();

			if (!dispatcher.isBackpressure()) {
				logger.debug(name + " Found available BUFU via " + next.name);
				queue.peek().setDispatched(true);
				queue.peek().setTarget(next);
			}
		}
	}

	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

}
