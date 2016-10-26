package cern.ch.cms.flipper.model;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import cern.ch.cms.flipper.FlipperGame;

public class FlipperObjectTest {

	private static final Logger logger = Logger.getLogger(FlipperObjectTest.class);

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
}
