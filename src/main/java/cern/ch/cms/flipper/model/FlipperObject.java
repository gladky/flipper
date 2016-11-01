package cern.ch.cms.flipper.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.SimpleFifoQueue;
import cern.ch.cms.flipper.event.Data;

public abstract class FlipperObject extends NamedObject {

	/** Indicator of simulation progress in this object, values: 0-99 */
	protected int progress;

	/** Step of simulation progress in this object */
	private final int progressStep;

	/** Successors of this flipper object */
	private final List<FlipperObject> successors;

	/** Data currently in this object */
	protected final SimpleFifoQueue queue;

	/** How many data objects can be hold at the same time */
	private final int capacity;

	/**
	 * Is awaiting data? true when some date is flowing to this objects (cannot
	 * accept more), false otherwise (optional use)
	 */
	private boolean awaiting;

	private static final Logger logger = Logger.getLogger(FlipperObject.class);

	public FlipperObject(String name, int capacity, int progressStep) {
		super(name);
		this.progressStep = progressStep;
		this.successors = new ArrayList<FlipperObject>();
		this.capacity = capacity;
		this.queue = new SimpleFifoQueue(capacity);
		this.setBusy(false);
	}

	/** Get the data */
	public boolean insert(Data data) {
		if (!canAccept()) {
			return false;
		} else {
			logger.debug(name + " received the data " + data.getName());
			queue.add(data);
			awaiting = false;
			return true;
		}
	}

	public int stepImplementation() {
		return progress += progressStep;
	}

	/** Do step of simulation, will increase the progress with step */
	public void doStep() {

		if (!queue.isEmpty()) {
			logger.trace(name + " will do step, progress: " + progress);
			progress = stepImplementation();
			if (progress > 99) {

				finished();

				if (canSend()) {
					sendData();
					progress = 0;
				}
			}
		}
	}

	protected void finished() {
		Data data = queue.peek();
		logger.debug(name + " finished with " + data);
		return;
	}

	/** Can accept more data */
	public boolean canAccept() {

		boolean iAmAbleToAccept;

		if (queue.size() == capacity) {
			logger.info(name + " sorry, I cannot accept");
			iAmAbleToAccept = false;
			return false;
		} else {
			iAmAbleToAccept = true;
		}

		boolean existsNonLinkSuccessorsCanAccept = false;
		if (this instanceof Link) {
			logger.debug(name + " I am link so I have to ask others if they can accept");
			for (FlipperObject successor : successors) {
				boolean canAccept = successor.canAccept();
				if (canAccept == true) {
					logger.debug(name + " I found successor which will accept: " + successor.getName());
					existsNonLinkSuccessorsCanAccept = true;
				}
			}
		} else {
			logger.debug(name + " I am not link so I accept on my own");
			existsNonLinkSuccessorsCanAccept = true;
		}

		if (iAmAbleToAccept == false || existsNonLinkSuccessorsCanAccept == false) {
			logger.info(name + " cannot accept the data. 1. Can I accept: " + iAmAbleToAccept
					+ ". 2. Can my successors accept: " + existsNonLinkSuccessorsCanAccept);
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
		return allDirectAccept;

	}

	protected void sendData() {
		Data data = queue.poll();
		logger.trace(name + " removing data " + data.getName() + " from queue, now size is: " + queue.size());

		for (FlipperObject successor : successors) {
			successor.insert(data);
		}
	}

	public int getProgress() {
		return progress;
	}

	public List<FlipperObject> getSuccessors() {
		return successors;
	}

	public boolean isBusy() {
		return awaiting;
	}

	public void setBusy(boolean busy) {
		this.awaiting = busy;
	}

}
