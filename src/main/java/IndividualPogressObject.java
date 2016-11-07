public abstract class IndividualPogressObject extends FlipperObject {


	/** One event per cycle can be accepted */
	private boolean acceptedThisCycle;

	private final int fakeInf = 100000;

	public IndividualPogressObject(String name, int capacity, int progressStep, SoundPlayer soundPlayer) {
		super(name, capacity, progressStep, soundPlayer);
	}

	protected void performInsert(Data data) {
		data.setProgress(0);
		data.setTimeOutProgress(0);
		queue.add(data);
		acceptedThisCycle = true;
	}

	@Override
	public boolean canAccept() {

		if (acceptedThisCycle) {
			return false;
		}
		return super.canAccept();
	}

	/** Indicator of simulation progress in this object, values: 0-99 */
	public int[] getProgress() {
		return queue.getProgress();
	}

	@Override
	public int stepImplementation(Data current) {
		int newProgress = current.getProgress() + progressStep;
		current.setProgress(newProgress);
		return newProgress;
	}

	/** Do step of simulation, will increase the progress with step */
	@Override
	public void doStep() {

		if (!queue.isEmpty()) {

			int localProgressLimit = fakeInf;

			int initialSize = queue.size();
			int recentSize = initialSize;

			for (int i = 0; i < recentSize; i++) {

				Data current = queue.get(i);

				if (current.getProgress() + progressStep < localProgressLimit) {
					int progress = stepImplementation(current);
					if (progress < localProgressLimit) {
						localProgressLimit = progress;
					}
					if (progress > 99) {

						finished();

						if (canSend()) {
							sendData();
						}
					}

				} else {

					if (current.getProgress() < localProgressLimit) {
						localProgressLimit = current.getProgress();
					}
				}

				if (localProgressLimit > 100) {
					localProgressLimit = 100;
				}

				if (recentSize != queue.size()) {
					recentSize = queue.size();
					i--;
				}
			}
		}
		acceptedThisCycle = false;
	}

	protected void finished() {
		return;
	}
}
