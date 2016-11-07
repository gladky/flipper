

import java.util.ArrayList;
import java.util.List;

public abstract class FlipperObject extends NamedObject {

	/** Step of simulation progress in this object */
	protected final int progressStep;

	/** Successors of this flipper object */
	private final List<FlipperObject> successors;

	/** Data currently in this object */
	protected final SimpleFifoQueue queue;

	/** How many data objects can be hold at the same time */
	protected final int capacity;

	/**
	 * Is reserved? true when some date is flowing to this objects (cannot
	 * accept more), false otherwise (optional use)
	 */
	private boolean reserved;

	protected final SoundPlayer soundPlayer;

	public FlipperObject(String name, int capacity, int progressStep, SoundPlayer soundPlayer) {
		super(name);
		this.progressStep = progressStep;
		this.successors = new ArrayList<FlipperObject>();
		this.capacity = capacity;
		this.queue = new SimpleFifoQueue(capacity);
		this.setBusy(false);
		this.soundPlayer = soundPlayer;
	}

	protected abstract void performInsert(Data data);

	protected abstract int stepImplementation(Data data);

	public abstract void doStep();

	public abstract int[] getProgress();

	protected abstract void finished();

	/** Get the data */
	public boolean insert(Data data) {
		if (!canAccept()) {
			return false;
		} else {
			performInsert(data);
			reserved = false;
			return true;
		}
	}

	/** Can accept more data */
	public boolean canAccept() {

		boolean iAmAbleToAccept;

		if (queue.size() == capacity) {
			iAmAbleToAccept = false;
			return false;
		} else {
			iAmAbleToAccept = true;
		}

		boolean existsNonLinkSuccessorsCanAccept = false;
		if (this instanceof Link) {
			for (FlipperObject successor : successors) {
				boolean canAccept = successor.canAccept();
				if (canAccept == true) {
					existsNonLinkSuccessorsCanAccept = true;
				}
			}
		} else {
			existsNonLinkSuccessorsCanAccept = true;
		}

		if (iAmAbleToAccept == false || existsNonLinkSuccessorsCanAccept == false) {
			return false;
		} else {
			return true;
		}

	}

	protected boolean canSend() {

		boolean allDirectAccept = true;
		for (FlipperObject successor : successors) {
			boolean accept = successor.canAccept();
			if (!accept) {
				allDirectAccept = false;
			}
		}

		if (allDirectAccept) {
			return true;
		} else {
			return false;
		}

	}

	protected void sendData() {
		Data data = queue.poll();

		for (FlipperObject successor : successors) {
			successor.insert(data);
		}
	}

	public List<FlipperObject> getSuccessors() {
		return successors;
	}

	public boolean isBusy() {
		return reserved;
	}

	public void setBusy(boolean busy) {
		this.reserved = busy;
	}

	public SimpleFifoQueue getQueue() {
		return queue;
	}

}
