package cern.ch.cms.flipper.model;

public class Storage extends FlipperObject {

	public Storage(String name, int capacity) {
		super(name, capacity, 0);
	}

	@Override
	public int[] getProgress() {
		int size = queue.size();
		int result = size * 100 / capacity;
		return new int[] { result };

	}

	@Override
	public void doStep() {

	}
}
