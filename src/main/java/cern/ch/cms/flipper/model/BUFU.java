package cern.ch.cms.flipper.model;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.sounds.Sound;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class BUFU extends Clickable {

	private Logger logger = Logger.getLogger(BUFU.class);

	public BUFU(String name, int progressStep, int timeoutStep, Button button, SoundPlayer soundPlayer) {
		// capacity is always 1 as bufu may process one event
		super(name, 1, progressStep, timeoutStep, button,soundPlayer);
	}

	@Override
	protected boolean canSend() {

		boolean basicCheck = super.canSend();

		boolean allLinksFree = true;
		for (FlipperObject successor : getSuccessors()) {
			boolean linksReserved = checkLinksReserved(successor);
			if (linksReserved) {
				allLinksFree = false;
			}
		}

		if (basicCheck && allLinksFree) {
			return true;
		} else {
			logger.info("Cannot send, all links free? " + allLinksFree);
			return false;
		}

	}

	@Override
	protected void sendData() {
		logger.info(name + " reserving links");

		for (FlipperObject successor : getSuccessors()) {
			reserveLinks(successor);
		}
		super.sendData();
	}

	private boolean checkLinksReserved(FlipperObject successor) {
		if (successor instanceof Link) {
			boolean reserved = successor.isBusy();
			if (reserved) {
				return true;
			} else {
				for (FlipperObject next : successor.getSuccessors()) {
					return checkLinksReserved(next);
				}
			}
		}
		return false;
	}

	private void reserveLinks(FlipperObject successor) {
		if (successor instanceof Link) {
			successor.setBusy(true);
			for (FlipperObject next : successor.getSuccessors()) {
				reserveLinks(next);
			}
		}
	}

	@Override
	protected void registerAcceptedSound(boolean interesting) {
		if (interesting) {
			soundPlayer.register(Sound.AcceptedInterestingEvent);
		} else {
			soundPlayer.register(Sound.AcceptedNotInteresingEvent);
		}

	}

	@Override
	protected void registerMissedSound(boolean interesting) {
		if (interesting) {
			soundPlayer.register(Sound.MissedInterestedEvent);
		} else {
			soundPlayer.register(Sound.MissedNotInterestingEvent);
		}
	}

}
