package cern.ch.cms.flipper.model;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Data;

public abstract class Clickable extends FlipperObject {

	private final int timeoutStep;
	private int timeoutProgress;
	private final Button button;

	private static final Logger logger = Logger.getLogger(Clickable.class);

	public Clickable(String name, int capacity, int progressStep, int timeoutStep, Button button) {

		super(name, capacity, progressStep);
		this.timeoutStep = timeoutStep;
		this.button = button;
	}

	@Override
	protected boolean canSend() {

		/* when clickable wants to send data, button becomes enabled */
		if (!button.isEnabled()) {
			button.enable();
		}

		boolean pressed = button.isPressed();

		if (pressed) {
			logger.info(name + " accepted the data " + queue.peek().getName());
			this.timeoutProgress = 0;
			this.button.disable();
			return super.canSend();
		} else {
			this.timeoutProgress += timeoutStep;
			if (timeoutProgress > 99) {

				Data data = this.queue.poll();
				logger.info(name + " data " + data.getName() + " not accepted in given timespan, rejecting");
				this.progress = 0;
			}
			return false;
		}
	}
}
