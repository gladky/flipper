package cern.ch.cms.flipper.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.sounds.Sound;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class Dispatcher extends NamedObject {

	private List<FlipperObject> linksToTarget;
	private List<FlipperObject> targets;
	private FlipperObject result;
	private boolean valid;
	private boolean backpressure;
	
	private final SoundPlayer soundPlayer;

	private static Logger logger = Logger.getLogger(Dispatcher.class);

	public Dispatcher(List<FlipperObject> targets, List<FlipperObject> linksToTargets,SoundPlayer soundPlayer) {
		super("Dispatcher");
		this.targets = targets;
		this.linksToTarget = linksToTargets;

		logger.info("There is " + targets.size() + ", targets: " + targets);
		this.valid = false;
		this.backpressure = false;
		this.soundPlayer = soundPlayer;
	}

	public FlipperObject findAvailableTarget() {

		if (valid) {
			logger.debug("Returning valid result without recalculation " + result.name);
		} else {
			//backpressure = false;
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
				if(backpressure == true){
					soundPlayer.register(Sound.BackpressureOver);
				}
				backpressure = false;
				
				//TODO: randomly 
				int choosen = ready.get(0);

				targets.get(choosen).setBusy(true);
				result = linksToTarget.get(choosen);
				logger.info("Randomly choosing BUFU: " + choosen + " via link " + result.getName());
				

				valid = true;
				logger.info("Recalculated path to avaialble target, result " + result.getName());
			} else {
				
				if(backpressure == false){
					soundPlayer.register(Sound.Backpressure);
				}
				backpressure = true;
				logger.info("Canot find available target, there is backpressure");
			}

		}
		return result;
	}

	public void invalidate() {
		result = null;
		valid = false;
	}

	public boolean isBackpressure() {
		return backpressure;
	}

	public FlipperObject getResult() {
		return result;
	}

}
