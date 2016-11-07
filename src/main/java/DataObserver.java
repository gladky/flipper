public class DataObserver extends FlowObserver {

	public DataObserver(FlipperGame flipperGame) {
		super(flipperGame,2,4,5,14,7,4);
	}

	@Override
	protected String getState(Link link) {
		if (link.getQueue().queue.size() == 0) {
			return "";
		} else {
			return link.getQueue().queue.get(0).getName();
		}
	}

	@Override
	protected String getState(BUFU bufu) {

		if (bufu.getQueue().queue.size() == 0) {
			return "";
		} else {
			return bufu.getQueue().queue.get(0).getName();
		}
	}

	@Override
	protected String getState(Buffer buffer) {

		String data = "";
		int elements = buffer.getQueue().size();
		if (elements == 0) {
			data = empty;
		} else if (elements == 1) {

			data = buffer.getQueue().get(0).getName();
		} else {

			data = "[" + elements + "]";
		}

		return data;
	}

	@Override
	protected String getState(Storage storage) {
		String result = "";

		for (Data data : storage.getQueue().queue) {
			result += data.getName();

		}
		return result;
	}

}
