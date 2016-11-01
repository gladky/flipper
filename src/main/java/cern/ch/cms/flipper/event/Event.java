package cern.ch.cms.flipper.event;

public class Event extends Data {

	public Event(Data fragment1, Data fragment2, Data fragment3, Data fragment4) {
		super(fragment1.getName() + "+" + fragment2.getName() + "+" + fragment3.getName() + "+" + fragment4.getName());
	}

	public Event(String name) {
		super(name);
	}

}
