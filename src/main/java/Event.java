

public class Event extends Data {


	/**
	 * Main constructor of Event
	 */
	public Event(Data f1, Data f2, Data f3, Data f4) {
		super(generateSimpleName(f4), false, isInteresting(f1, f2, f3, f4));
		this.setTarget(f4.getTarget());
		this.setDispatched(f4.isDispatched());
	}

	public Event(String name, boolean interesting) {
		super(name, false, interesting);
	}

	public Event(String name) {
		this(name, false);
	}

	private static String generateSimpleName(Data d) {
		String name = "e" + d.getName();
		return name;
	}

	private static boolean isInteresting(Data fragment1, Data fragment2, Data fragment3, Data fragment4) {

		boolean interesting = false;
		if (fragment1.isInteresting() && fragment2.isInteresting() && fragment3.isInteresting()
				&& fragment4.isInteresting()) {
			interesting = true;
		}
		return interesting;
	}

}
