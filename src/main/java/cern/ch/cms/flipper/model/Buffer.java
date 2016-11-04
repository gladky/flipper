package cern.ch.cms.flipper.model;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.sounds.Sound;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class Buffer extends Clickable {

	private boolean soundMasked;

	private static Logger logger = Logger.getLogger(Buffer.class);

	private Dispatcher dispatcher;

	public Buffer(String name, int capacity, int progressStep, int timeoutStep, Button button, SoundPlayer soundPlayer,
			boolean soundMasked) {
		super(name, capacity, progressStep, timeoutStep, button, soundPlayer);
		this.soundMasked = soundMasked;
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

	@Override
	protected void registerAcceptedSound(boolean interesting) {
		if (!soundMasked) {
			if (interesting) {
				soundPlayer.register(Sound.AcceptedInterestingFragments);
			} else {
				soundPlayer.register(Sound.AcceptedNonInterestingFragments);
			}
		}

	}

	@Override
	protected void registerMissedSound(boolean interesting) {
		if (!soundMasked) {
			if (interesting) {
				soundPlayer.register(Sound.MissedInterestingFragments);
			} else {
				soundPlayer.register(Sound.MissedNotInterestingFragments);
			}
		}
	}

}
