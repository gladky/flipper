package cern.ch.cms.flipper.model;

import java.util.List;

import org.apache.log4j.Logger;

public class Dispatcher extends NamedObject{

	private List<FlipperObject> linksToTarget;
	private List<FlipperObject> targets;
	private FlipperObject result;
	private boolean valid;
	private boolean backpressure;

	private static Logger logger = Logger.getLogger(Dispatcher.class);

	public Dispatcher(List<FlipperObject> targets, List<FlipperObject> linksToTargets) {
		super("Dispatcher");
		this.targets = targets;
		this.linksToTarget = linksToTargets;
		this.valid = false;
		this.backpressure = false;
	}

	public FlipperObject findAvailableTarget() {
		backpressure = false;

		if (valid) {
			logger.debug("Returning valid result without recalculation " + result.name);
		} else {
			logger.debug("Recalculating path to avaialble target");

			for (int i = 0; i < targets.size(); i++) {
				FlipperObject bufu = targets.get(i);
				if (!bufu.isBusy() && bufu.canAccept()) {

					targets.get(i).setBusy(true);
					result = linksToTarget.get(i);
					valid = true;
					break;
				} else {
					logger.trace("bufu " + bufu.getName() + " is not available");
				}
			}

			if (valid == false) {
				backpressure = true;
				logger.info("Canot find available target, there is backpressure");
			} else {

				logger.info("Recalculated path to avaialble target, result " + result.getName());
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

}
