package cern.ch.cms.flipper.sounds;

public enum Sound {

	/* Game events */
	GameStart(1),
	GameOver(2),

	/* Resource events */
	EmptyProtonBottle(10),
	FullStorage(11),
	Backpressure(12),
	BackpressureOver(13),

	/* Accepted events */
	AcceptedInterestingFragments(20),
	AcceptedInterestingEvent(21),
	AcceptedNonInterestingFragments(22),
	AcceptedNotInteresingEvent(23),

	/* Missed events */
	MissedInterestedEvent(31),
	MissedInterestingFragments(32),
	MissedNotInterestingFragments(33),
	MissedNotInterestingEvent(34),

	/* Storage */
	ArrivedInterestingToStorage(40),
	ArrivedNotInterestingToStorage(41),

	/* Button pressed */
	ButtonPressedWhenDisabled(50),
	ButtonPressedWhenEnabled(51);

	private final int id;

	private Sound(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	/**
	 * For better debugging only, can be ommitted in production
	 * 
	 * @param id
	 * @return
	 */
	public static Sound getById(int id) {
		for (Sound sound : Sound.values()) {
			if (sound.getId() == id) {
				return sound;
			}
		}
		return null;
	}

}
