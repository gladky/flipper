package cern.ch.cms.flipper.model;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.sounds.Sound;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class Buffer extends Clickable {

	private boolean soundMasked;

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
			int choosenIndex = dispatcher.findAvailableTarget();

			if (!dispatcher.isBackpressure()) {
				queue.peek().setDispatched(true);
				queue.peek().setTargetIndex(choosenIndex);
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

	@Override
	protected void reserve() {
		Data data = queue.peek();
		int reservedIndex = data.getTargetIndex();
		FlipperObject target = dispatcher.getTarget(reservedIndex);
		FlipperObject link = dispatcher.getLink(reservedIndex);
		target.setBusy(true);
		data.setTarget(link);
	}

}
