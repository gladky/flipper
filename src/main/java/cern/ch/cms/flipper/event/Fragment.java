package cern.ch.cms.flipper.event;

public class Fragment extends Data {

	private static int id = 0;

	public Fragment(boolean interesting) {
		super("" + id, true, interesting);
		id++;
	}

	public Fragment() {
		this(false);
	}

}
