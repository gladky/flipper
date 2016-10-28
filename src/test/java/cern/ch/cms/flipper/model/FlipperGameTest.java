package cern.ch.cms.flipper.model;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import cern.ch.cms.flipper.FlipperGame;

public class FlipperGameTest {

	private static final Logger logger = Logger.getLogger(FlipperGameTest.class);

	private static final int stepsLXToStorage = 10;
	private static final int timeout = 100;

	// LZ
	private static final int stepsToLZMin = 13;
	private static final int stepsToLZAvg = 15;
	private static final int stepsToLZMax = 21;

	// LX
	private static final int stepsLZToLXMin = 20;
	private static final int stepsLZToLXAvg = 21;
	private static final int stepsLZToLXMax = 22;

	private void doFlow(FlipperGame flipperGame, int stepsToLZ, int stepsToLX, int stepsToStorage) {

		flipperGame.generateNewFragments();
		flipperGame.doSteps(stepsToLZ);
		flipperGame.pressButtonLZ();
		flipperGame.doSteps(stepsToLX);
		flipperGame.pressButtonL1();
		flipperGame.doSteps(stepsToStorage);

	}

	@Test
	public void oneEventAvgTimingTest() {

		FlipperGame flipperGame = new FlipperGame();
		// Logger.getLogger(FlipperObject.class).setLevel(Level.TRACE);

		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());
		doFlow(flipperGame, stepsToLZMax, stepsLZToLXAvg, stepsLXToStorage);
		Assert.assertEquals("Event in storage", 1, flipperGame.getStorage().queue.size());

	}

	@Test
	public void oneEventAcceptedLevelZeroTest() {

		FlipperGame flipperGame = new FlipperGame();

		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());

		flipperGame.generateNewFragments();
		flipperGame.doSteps(stepsToLZAvg);
		flipperGame.pressButtonLZ();
		flipperGame.doSteps(stepsLZToLXAvg);

		Assert.assertEquals(1, flipperGame.getBufuL1().queue.size());

		flipperGame.doSteps(timeout);
		Assert.assertEquals("Still nothing in storage", 0, flipperGame.getStorage().queue.size());
	}

	@Test
	public void LXAcceptedFirstMoment() {

		Logger.getLogger(FlipperObject.class).setLevel(Level.INFO);
		FlipperGame flipperGame = new FlipperGame();
		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());
		doFlow(flipperGame, stepsToLZAvg, stepsLZToLXMin, stepsLXToStorage);
		Assert.assertEquals("Event in storage", 1, flipperGame.getStorage().queue.size());

		FlipperGame flipperGame2 = new FlipperGame();
		Assert.assertEquals("Nothing in storage", 0, flipperGame2.getStorage().queue.size());
		doFlow(flipperGame2, stepsToLZAvg, stepsLZToLXMin - 1, stepsLXToStorage);
		Assert.assertEquals("Still nothing in storage", 0, flipperGame2.getStorage().queue.size());
	}

	@Test
	public void LXAcceptedLastMoment() {

		FlipperGame flipperGame = new FlipperGame();
		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());
		doFlow(flipperGame, stepsToLZAvg, stepsLZToLXMax, stepsLXToStorage);
		Assert.assertEquals("Event in storage", 1, flipperGame.getStorage().queue.size());

		FlipperGame flipperGame2 = new FlipperGame();
		Assert.assertEquals("Nothing in storage", 0, flipperGame2.getStorage().queue.size());
		doFlow(flipperGame2, stepsToLZAvg, stepsLZToLXMax + 1, stepsLXToStorage);
		Assert.assertEquals("Still nothing in storage", 0, flipperGame2.getStorage().queue.size());
	}

	@Test
	public void stressTest() {

		FlipperGame flipperGame = new FlipperGame();
		Logger.getLogger(FlipperObject.class).setLevel(Level.DEBUG);

		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());

		for (int i = 0; i < 50; i++) {
			if (i % 4 == 0) {
				// 4 steps for link to process data
				flipperGame.generateNewFragments();
			}
			flipperGame.pressButtonLZ();
			flipperGame.pressButtonL1();
			flipperGame.pressButtonL2();
			flipperGame.pressButtonL3();
			flipperGame.pressButtonR1();
			flipperGame.pressButtonR2();
			flipperGame.pressButtonR3();
			flipperGame.doStep();
			logger.info("Step " + i + " ------------------------------ step " + i);
		}

		Assert.assertEquals("Event in storage", 1, flipperGame.getStorage().queue.size());

	}
}