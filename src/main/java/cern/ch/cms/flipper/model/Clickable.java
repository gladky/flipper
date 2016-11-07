package cern.ch.cms.flipper.model;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public abstract class Clickable extends FlipperObject {

	private final int timeoutStep;
	private final Button button;

	private boolean accepted;

	private static final Logger logger = Logger.getLogger(Clickable.class);

	public Clickable(String name, int capacity, int progressStep, int timeoutStep, Button button,
			SoundPlayer soundPlayer) {

		super(name, capacity, progressStep, soundPlayer);
		this.timeoutStep = timeoutStep;
		this.button = button;
		this.accepted = false;
	}

	@Override
	protected boolean canSend() {
		

		dispatch();

		/* when clickable wants to send data, button becomes enabled */
		if (!button.isEnabled() && !backpressure() && !accepted) {
			button.enable();
		}

		boolean pressed = button.isPressed();

		if (pressed || accepted) {
			
			Data data = this.queue.peek();
			if (!accepted) {
				accepted = true;
				registerAcceptedSound(data.isInteresting());
				this.button.disable();
			}
			
			logger.info(name + " accepted the data " + queue.peek().getName() + " in time");
			//data.setTimeOutProgress(0);
			boolean canSend = super.canSend();
			if (canSend) {
				
			}
			return canSend;
		} else {
			Data data = this.queue.peek();
			int timeoutProgress = data.getTimeOutProgress() + timeoutStep;
			
			data.setTimeOutProgress(timeoutProgress);
			if (timeoutProgress > 99) {

				Data dataToReject = this.queue.poll();
				logger.info(name + " data " + dataToReject.getName() + " not accepted in given timespan, rejecting");
				button.disable();
				registerMissedSound(data.isInteresting());
			}
			return false;
		}
	}

	protected abstract void registerAcceptedSound(boolean interesting);

	protected abstract void registerMissedSound(boolean interesting);

	protected boolean backpressure() {
		return false;
	}

	protected void dispatch() {

	}

	@Override
	protected void sendData() {
		super.sendData();
		this.accepted = false;
	}
}
