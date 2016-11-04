package cern.ch.cms.flipper.sounds;

public enum Sound {

	/* Game events */
	GameStart(12),
	GameOver(13),

	/* Resource events */
	EmptyProtonBottle(9),
	FullStorage(10),
	Backpressure(11),

	/* Accepted events */
	AcceptedInterestingFragments(0),
	AcceptedInterestingEvent(2),
	AcceptedNonInterestingFragments(1),
	AcceptedNotInteresingEvent(3),

	/* Missed events */
	MissedInterestedEvent(5),
	MissedInterestingFragments(7),
	MissedNotInterestingFragments(4),
	MissedNotInterestingEvent(6),
	
	/* Other */
	ButtonPressedWhenDisabled(8);
	
	
	
	

	private final int id;

	private Sound(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

}
