package cern.ch.cms.flipper.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.log4j.Logger;

import cern.ch.cms.flipper.event.Data;

public abstract class FlipperObject extends NamedObject {

	/** Indicator of simulation progress in this object, values: 0-99 */
	protected int progress;

	/** Step of simulation progress in this object */
	private final int progressStep;

	/** Successors of this flipper object */
	private final List<FlipperObject> successors;

	/** Data currently in this object */
	protected final CircularFifoQueue<Data> queue;

	private final int capacity;

	private boolean awaiting;

	private static final Logger logger = Logger.getLogger(FlipperObject.class);

	public FlipperObject(String name, int capacity, int progressStep) {
		super(name);
		this.progressStep = progressStep;
		this.successors = new ArrayList<FlipperObject>();
		this.capacity = capacity;
		this.queue = new CircularFifoQueue<Data>(capacity);
		this.setBusy(false);
	}

	/** Can accept more data */
	public boolean canAccept() {
		if (queue.size() == capacity) {
			logger.info(name + " sorry, cannot accept");
			return false;
		} else {
			return true;
		}
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

	protected boolean canSend() {

		boolean allAccept = true;
		for (FlipperObject successor : successors) {
			boolean accept = successor.canAccept();
			if (!accept) {
				allAccept = false;
			}
		}
		if (!allAccept) {
			logger.info(name + " cannot pass the data to successor");
		}
		return allAccept;

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
