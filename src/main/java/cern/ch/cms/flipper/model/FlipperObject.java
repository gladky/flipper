package cern.ch.cms.flipper.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.SimpleFifoQueue;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.sounds.SoundPlayer;

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

	private static final Logger logger = Logger.getLogger(FlipperObject.class);

	public FlipperObject(String name, int capacity, int progressStep, SoundPlayer soundPlayer) {
		super(name);
		this.progressStep = progressStep;
		this.successors = new ArrayList<FlipperObject>();
		this.capacity = capacity;
		this.queue = new SimpleFifoQueue(capacity);
		this.setBusy(false);
		this.soundPlayer = soundPlayer;
	}

	/** Get the data */
	public boolean insert(Data data) {
		if (!canAccept()) {
			logger.info(name + " refused insert of data " + data.getName());
			return false;
		} else {
			logger.info(name + " received the data " + data.getName());
			data.setProgress(0);
			queue.add(data);
			reserved = false;
			return true;
		}
	}

	public int stepImplementation(Data current) {
		int newProgress = current.getProgress() + progressStep;
		current.setProgress(newProgress);
		return newProgress;
	}

	/** Do step of simulation, will increase the progress with step */
	public void doStep() {

		if (!queue.isEmpty()) {
			boolean localBackpressure = false;

			Data backpressureCause = null;

			for (int i = 0; i < queue.size(); i++) {

				if (!localBackpressure) {
					Data current = queue.get(i);
					logger.trace(name + " will do step, progress before step: " + current.getProgress());
					int progress = stepImplementation(current);
					if (progress > 99) {

						finished();

						if (canSend()) {
							sendData();
							progress = 0;
						} else {
							backpressureCause = current;
							localBackpressure = true;
						}
					}

				} else {
					logger.debug("Local backpressure, waiting for finished data " + backpressureCause.getName()
							+ " to be released");
				}
			}
		}
	}

	protected void finished() {
		Data data = queue.peek();
		logger.debug(name + " finished with " + data + " my progress is now " + data.getProgress());
		return;
	}

	/** Can accept more data */
	public boolean canAccept() {

		boolean iAmAbleToAccept;

		if (queue.size() == capacity) {
			logger.debug(name + " sorry, I cannot accept, I'm full");
			iAmAbleToAccept = false;
			return false;
		} else {
			iAmAbleToAccept = true;
		}

		boolean existsNonLinkSuccessorsCanAccept = false;
		if (this instanceof Link) {
			logger.trace(name + " I am link so I have to ask others if they can accept");
			for (FlipperObject successor : successors) {
				boolean canAccept = successor.canAccept();
				if (canAccept == true) {
					logger.debug(name + " I found successor which will accept: " + successor.getName());
					existsNonLinkSuccessorsCanAccept = true;
				}
			}
		} else {
			logger.trace(name + " I am not link so I accept on my own");
			existsNonLinkSuccessorsCanAccept = true;
		}

		if (iAmAbleToAccept == false || existsNonLinkSuccessorsCanAccept == false) {
			logger.info(name + " cannot accept any new data. 1. Can I accept: " + iAmAbleToAccept
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

		if (allDirectAccept) {
			return true;
		} else {
			logger.info("Cannot send, all direct accept? " + allDirectAccept);
			return false;
		}

	}

	protected void sendData() {
		Data data = queue.poll();
		if (data.isDispatched()) {
			logger.info("Sending dispatched data " + data.getName() + "  to target " + data.getTarget().getName());
		}
		logger.trace(name + " removing data " + data.getName() + " from queue, now size is: " + queue.size());

		for (FlipperObject successor : successors) {
			successor.insert(data);
		}
	}

	/** Indicator of simulation progress in this object, values: 0-99 */
	public int[] getProgress() {

		return queue.getProgress();
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
