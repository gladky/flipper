package cern.ch.cms.flipper.model;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import cern.ch.cms.flipper.FlipperGame;

public class FlipperObjectTest {

	private static final Logger logger = Logger.getLogger(FlipperObjectTest.class);

	@Test
	public void oneInterestingEventInStorageTest() {

		FlipperGame flipperGame = new FlipperGame();

		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());

		flipperGame.generateNewFragments();
		flipperGame.doSteps(15);
		flipperGame.pressButtonLZ();
		flipperGame.doSteps(20);
		flipperGame.pressButtonL1();
		flipperGame.doSteps(10);

		Assert.assertEquals("Event in storage", 1, flipperGame.getStorage().queue.size());

	}

	@Test
	public void oneEventAcceptedLevelZeroTest() {

		Logger.getLogger(FlipperObject.class).setLevel(Level.INFO);
		FlipperGame flipperGame = new FlipperGame();

		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());

		flipperGame.generateNewFragments();
		flipperGame.doSteps(15);
		flipperGame.pressButtonLZ();
		flipperGame.doSteps(10);

		Assert.assertEquals(1, flipperGame.getBufuL1().queue.size());

		flipperGame.doSteps(10);
		Assert.assertEquals("Still nothing in storage", 0, flipperGame.getStorage().queue.size());
	}

}
