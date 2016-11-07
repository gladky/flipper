package cern.ch.cms.flipper.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.sounds.Sound;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class Dispatcher extends NamedObject {

	private List<FlipperObject> linksToTarget;
	private List<FlipperObject> targets;
	private int result;
	private boolean valid;
	private boolean backpressure;

	private final SoundPlayer soundPlayer;

	private static Logger logger = Logger.getLogger(Dispatcher.class);

	public Dispatcher(List<FlipperObject> targets, List<FlipperObject> linksToTargets, SoundPlayer soundPlayer) {
		super("Dispatcher");
		this.targets = targets;
		this.linksToTarget = linksToTargets;

		logger.info("There is " + targets.size() + ", targets: " + targets);
		this.valid = false;
		this.backpressure = false;
		this.soundPlayer = soundPlayer;

	}

	public FlipperObject getTarget(int id) {
		return targets.get(id);
	}

	public FlipperObject getLink(int id) {
		return linksToTarget.get(id);
	}

	public int findAvailableTarget() {

		if (valid) {
			logger.debug("Returning valid result without recalculation " + targets.get(result));
		} else {
			result = -1;
			// backpressure = false;
			logger.debug("Recalculating path to avaialble target");
			List<Integer> ready = new ArrayList<Integer>();

			for (int i = 0; i < targets.size(); i++) {
				FlipperObject bufu = targets.get(i);

				boolean currentBufuReady = false;
				if (!bufu.isBusy() && bufu.canAccept()) {
					currentBufuReady = true;
					ready.add(i);

				} else {
					logger.trace("bufu " + bufu.getName() + " is not available");
				}
				logger.info("Checking bufu: " + bufu.getName() + ", result: " + currentBufuReady);
			}

			logger.info("BUFUs ready: " + ready);

			if (ready.size() > 0) {
				if (backpressure == true) {
					soundPlayer.register(Sound.BackpressureOver);
				}
				backpressure = false;

				// TODO: randomly
				int choosenRandom = ready.get((new Random()).nextInt(ready.size()));
				int choosen = ready.get(0);

				result = choosen;
				logger.info("Randomly choosing BUFU: " + choosen + " via link " + linksToTarget.get(choosen).getName());

				valid = true;
				logger.info("Recalculated path to avaialble target, result " + linksToTarget.get(choosen).getName());
			} else {

				if (backpressure == false) {
					soundPlayer.register(Sound.Backpressure);
				}
				backpressure = true;
				logger.info("Canot find available target, there is backpressure");
			}

		}
		return result;
	}

	public void invalidate() {
		result = -1;
		valid = false;
	}

	public boolean isBackpressure() {
		return backpressure;
	}

	public int getResult() {
		return result;
	}

}
