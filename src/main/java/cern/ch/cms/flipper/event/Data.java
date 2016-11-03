package cern.ch.cms.flipper.event;

import cern.ch.cms.flipper.model.FlipperObject;

public abstract class Data {
	
	private int progress;

	private boolean dispatched;

	private FlipperObject target;

	private final String name;

	public Data(String name, boolean isFragment) {
		this.name = name;
		this.isFragment = isFragment;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Data [name=" + name + "]";
	}

	public FlipperObject getTarget() {
		return target;
	}

	public void setTarget(FlipperObject target) {
		this.target = target;
	}

	public boolean isDispatched() {
		return dispatched;
	}

	public void setDispatched(boolean dispatched) {
		this.dispatched = dispatched;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	private final boolean isFragment;

}
