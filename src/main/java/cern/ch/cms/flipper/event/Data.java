package cern.ch.cms.flipper.event;

public abstract class Data {

	private final String name;

	public Data(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
