package cern.ch.cms.flipper;

import cern.ch.cms.flipper.model.BUFU;
import cern.ch.cms.flipper.model.Buffer;
import cern.ch.cms.flipper.model.Link;
import cern.ch.cms.flipper.model.Storage;

public class StateObserver extends FlowObserver {

	public StateObserver(FlipperGame flipperGame) {
		super(flipperGame, 3, 3, 3, 3, 3, 8);
	}

	@Override
	protected String getState(Link link) {
		return spaceSafePrintList(link.getProgress());
	}

	@Override
	protected String getState(BUFU bufu) {
		return spaceSafePrintList(bufu.getProgress());
	}

	@Override
	protected String getState(Buffer buffer) {

		return spaceSafePrintList(buffer.getProgress());
	}

	@Override
	protected String getState(Storage storage) {

		return spaceSafePrintList(storage.getProgress());
	}

	private String spaceSafePrintList(int[] list) {
		String result = "";
		for (int i = 0; i < list.length; i++) {
			if (i != 0) {
				result += ",";
			}
			result += list[i];
		}
		return result;
	}

}
