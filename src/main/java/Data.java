public abstract class Data {

	private int progress;
	
	private int timeOutProgress;

	private boolean dispatched;

	private FlipperObject target;
	
	private int targetIndex;

	private final String name;

	private final boolean isFragment;

	private final boolean isInteresting;

	public Data(String name, boolean isFragment, boolean isInteresting) {

		this.isFragment = isFragment;
		this.isInteresting = isInteresting;
		String modifiedName;

		if (this.isInteresting && !this.isFragment) {
			modifiedName = name.toUpperCase();
		} else {
			modifiedName = name;
		}
		this.name = modifiedName;
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

	public boolean isInteresting() {
		return isInteresting;
	}

	public boolean isFragment() {
		return isFragment;
	}

	public int getTimeOutProgress() {
		return timeOutProgress;
	}

	public void setTimeOutProgress(int timeOutProgress) {
		this.timeOutProgress = timeOutProgress;
	}

	public int getTargetIndex() {
		return targetIndex;
	}

	public void setTargetIndex(int targetIndex) {
		this.targetIndex = targetIndex;
	}

}
