

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dispatcher extends NamedObject {

	private List<FlipperObject> linksToTarget;
	private List<FlipperObject> targets;
	private int result;
	private boolean valid;
	private boolean backpressure;

	private final SoundPlayer soundPlayer;

	public Dispatcher(List<FlipperObject> targets, List<FlipperObject> linksToTargets, SoundPlayer soundPlayer) {
		super("Dispatcher");
		this.targets = targets;
		this.linksToTarget = linksToTargets;

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
		} else {
			result = -1;
			List<Integer> ready = new ArrayList<Integer>();

			for (int i = 0; i < targets.size(); i++) {
				FlipperObject bufu = targets.get(i);

				if (!bufu.isBusy() && bufu.canAccept()) {
					ready.add(i);
				}
			}

			if (ready.size() > 0) {
				if (backpressure == true) {
					soundPlayer.register(Sound.BackpressureOver);
				}
				backpressure = false;

				// TODO: randomly
				int choosenRandom = ready.get((new Random()).nextInt(ready.size()));
				int choosen = ready.get(0);

				result = choosenRandom;
				result = choosen;

				valid = true;
			} else {

				if (backpressure == false) {
					soundPlayer.register(Sound.Backpressure);
				}
				backpressure = true;
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
