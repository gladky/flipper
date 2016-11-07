package cern.ch.cms.flipper.sounds;

public enum Sound {

	/* Game events */
	GameStart(1, "GS"),
	GameOver(2, "GO"),

	/* Resource events */
	EmptyProtonBottle(10, "PBE"),
	FullStorage(11, "SF"),
	Backpressure(12, "BP"),
	BackpressureOver(13, "BPO"),

	/* Accepted events */
	AcceptedInterestingFragments(20, "FAI"),
	AcceptedInterestingEvent(21, "EAI"),
	AcceptedNonInterestingFragments(22, "FAN"),
	AcceptedNotInteresingEvent(23, "EAN"),

	/* Missed events */
	MissedInterestedEvent(31, "EMI"),
	MissedInterestingFragments(32, "FMI"),
	MissedNotInterestingFragments(33, "FMN"),
	MissedNotInterestingEvent(34, "EMN"),

	/* Other */
	ButtonPressedWhenDisabled(40, "BPD");

	private final int id;

	/**
	 * This is for better debuggin only, can be ommited in production
	 */
	private final String code;

	private Sound(int id, String code) {
		this.id = id;
		this.code = code;
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

	public String getCode() {
		return code;
	}

}
