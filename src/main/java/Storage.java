public class Storage extends SinglePogressObject {


	public Storage(String name, int capacity, SoundPlayer soundPlayer) {
		super(name, capacity, 0, soundPlayer);
	}

	/** Can accept more data */
	@Override
	public boolean canAccept() {

		if (queue.size() == capacity) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	protected void finished() {
	}

	@Override
	protected void performInsert(Data data) {
		super.performInsert(data);
		if (data.isInteresting()) {
			soundPlayer.register(Sound.ArrivedInterestingToStorage);
		} else {
			soundPlayer.register(Sound.ArrivedNotInterestingToStorage);
		}
	}

}
