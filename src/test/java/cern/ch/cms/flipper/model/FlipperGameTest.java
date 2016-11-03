package cern.ch.cms.flipper.model;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import cern.ch.cms.flipper.FlipperGame;
import cern.ch.cms.flipper.FlowObserver;
import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Event;

public class FlipperGameTest {

	private static final Logger logger = Logger.getLogger(FlipperGameTest.class);

	private static final int stepsLXToStorage = 20;
	private static final int timeout = 100;

	// LZ
	private static final int stepsToLZMin = 13;
	private static final int stepsToLZAvg = 15;
	private static final int stepsToLZMax = 21;

	// LX
	private static final int stepsLZToLXMin = 21;
	private static final int stepsLZToLXAvg = 22;
	private static final int stepsLZToLXMax = 23;

	private void doFlow(FlipperGame flipperGame, int stepsToLZ, int stepsToLX, int stepsToStorage) {

		flipperGame.generateNewFragments();
		flipperGame.doSteps(stepsToLZ);
		flipperGame.pressButtonLevel1();
		flipperGame.doSteps(stepsToLX);
		flipperGame.pressButtonHLT_L1();
		flipperGame.doSteps(stepsToStorage);

	}

	@Test
	public void oneEventAvgTimingTest() {

		FlipperGame flipperGame = new FlipperGame();
		Logger.getLogger(FlipperObject.class).setLevel(Level.DEBUG);

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
		flipperGame.pressButtonLevel1();
		flipperGame.doSteps(stepsLZToLXAvg);

		Assert.assertEquals(1, flipperGame.getBufuL1().queue.size());

		flipperGame.doSteps(timeout);
		Assert.assertEquals("Still nothing in storage", 0, flipperGame.getStorage().queue.size());
	}

	@Test
	public void LXAcceptedFirstMoment() {

		Logger.getLogger(FlipperObject.class).setLevel(Level.DEBUG);
		Logger.getLogger(Clickable.class).setLevel(Level.DEBUG);
		Logger.getLogger(Button.class).setLevel(Level.DEBUG);
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
		FlowObserver observer = new FlowObserver(flipperGame);

		Logger.getRootLogger().setLevel(Level.OFF);
		Logger.getLogger(Switch.class).setLevel(Level.INFO);
		Logger.getLogger(Clickable.class).setLevel(Level.INFO);
		Logger.getLogger(Button.class).setLevel(Level.OFF);
		Logger.getLogger(FlipperObject.class).setLevel(Level.INFO);
		Logger.getLogger(Event.class).setLevel(Level.OFF);
		Logger.getLogger(FlipperGameTest.class).setLevel(Level.OFF);
		Logger.getLogger(Dispatcher.class).setLevel(Level.INFO);
		Logger.getLogger(FlowObserver.class).setLevel(Level.OFF);

		int update = 0;

		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());

		int progressStep = flipperGame.link11.progressStep;
		int cycles = (100 / progressStep);
		logger.info("Progress step of link level 1 is: " +progressStep + " will generate events every " + cycles );
		/* Generate new events while pressing the buttons */
		int generatedEvents = 0;
		for (int i = 0; i < 500; i++) {

			if (generatedEvents < 12) {

				if (i % cycles == 0) {
					logger.debug("Generating new event ");
					generatedEvents++;
					// 4 steps for link to process data
					flipperGame.generateNewFragments();

				}
				flipperGame.pressButtonLevel1();
				flipperGame.pressButtonHLT_L1();
				flipperGame.pressButtonHLT_L2();
				flipperGame.pressButtonHLT_L3();
				flipperGame.pressButtonHLT_R1();
				flipperGame.pressButtonHLT_R2();
				flipperGame.pressButtonHLT_R3();
				observer.persist();
				flipperGame.doStep();
				update++;
				logger.debug("Update" + update + " ------------------------------ update " + update);
			}
		}

		Assert.assertEquals("Make sure 12 events were generated", 12, generatedEvents);

		/* Only press the buttons */
		for (int i = 0; i < 500; i++) {
			logger.debug("Storage has: " + flipperGame.getStorage().queue.size());
			flipperGame.pressButtonLevel1();
			flipperGame.pressButtonHLT_L1();
			flipperGame.pressButtonHLT_L2();
			flipperGame.pressButtonHLT_L3();
			flipperGame.pressButtonHLT_R1();
			flipperGame.pressButtonHLT_R2();
			flipperGame.pressButtonHLT_R3();
			observer.persist();
			flipperGame.doStep();
			update++;
			logger.debug("Update" + update + " ------------------------------ update " + update);
		}

		logger.info("Accepted events: " + flipperGame.getStorage().queue.toString());

		System.out.println(observer.toString());
		Assert.assertEquals("Event in storage", 12, flipperGame.getStorage().queue.size());

	}
}
