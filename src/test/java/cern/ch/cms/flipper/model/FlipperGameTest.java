package cern.ch.cms.flipper.model;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import cern.ch.cms.flipper.FlipperGame;
import cern.ch.cms.flipper.FlipperObjectFactory;
import cern.ch.cms.flipper.FlowObserver;
import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Event;

public class FlipperGameTest {

	private static final Logger logger = Logger.getLogger(FlipperGameTest.class);

	private static final int stepsLXToStorage = 20;
	private static final int timeout = 100;

	// LZ
	private static final int stepsToL1Min = 17;
	private static final int stepsToL1Avg = 19;
	private static final int stepsToL1Max = 24;

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
		doFlow(flipperGame, stepsToL1Avg, stepsLZToLXAvg, stepsLXToStorage);
		Assert.assertEquals("Event in storage", 1, flipperGame.getStorage().queue.size());

	}

	@Test
	public void oneEventAcceptedLevelZeroTest() {

		FlipperGame flipperGame = new FlipperGame();

		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());

		flipperGame.generateNewFragments();
		flipperGame.doSteps(stepsToL1Avg);
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
		doFlow(flipperGame, stepsToL1Avg, stepsLZToLXMin, stepsLXToStorage);
		Assert.assertEquals("Event in storage", 1, flipperGame.getStorage().queue.size());

		FlipperGame flipperGame2 = new FlipperGame();
		Assert.assertEquals("Nothing in storage", 0, flipperGame2.getStorage().queue.size());
		doFlow(flipperGame2, stepsToL1Avg, stepsLZToLXMin - 1, stepsLXToStorage);
		Assert.assertEquals("Still nothing in storage", 0, flipperGame2.getStorage().queue.size());
	}

	@Test
	public void LXAcceptedLastMoment() {

		FlipperGame flipperGame = new FlipperGame();
		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());
		doFlow(flipperGame, stepsToL1Avg, stepsLZToLXMax, stepsLXToStorage);
		Assert.assertEquals("Event in storage", 1, flipperGame.getStorage().queue.size());

		FlipperGame flipperGame2 = new FlipperGame();
		Assert.assertEquals("Nothing in storage", 0, flipperGame2.getStorage().queue.size());
		doFlow(flipperGame2, stepsToL1Avg, stepsLZToLXMax + 1, stepsLXToStorage);
		Assert.assertEquals("Still nothing in storage", 0, flipperGame2.getStorage().queue.size());
	}

	@Test
	public void stressTest() {

		FlipperGame flipperGame = new FlipperGame();

		Logger.getRootLogger().setLevel(Level.OFF);
		Logger.getLogger(Switch.class).setLevel(Level.OFF);
		Logger.getLogger(Clickable.class).setLevel(Level.OFF);
		Logger.getLogger(Button.class).setLevel(Level.OFF);
		Logger.getLogger(Buffer.class).setLevel(Level.OFF);
		Logger.getLogger(BUFU.class).setLevel(Level.OFF);
		//Logger.getLogger(FlipperObject.class).setLevel(Level.TRACE);
		//Logger.getLogger(IndividualPogressObject.class).setLevel(Level.TRACE);
		Logger.getLogger(Event.class).setLevel(Level.OFF);
		//Logger.getLogger(FlipperGameTest.class).setLevel(Level.DEBUG);
		Logger.getLogger(Dispatcher.class).setLevel(Level.OFF);
		Logger.getLogger(FlowObserver.class).setLevel(Level.OFF);

		int update = 0;

		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());

		int progressStep = flipperGame.link11.progressStep;
		int cycles = (100 / progressStep);
		cycles = cycles+1;
		logger.info("Progress step of link level 1 is: " + progressStep + " will generate events every " + cycles);
		/* Generate new events while pressing the buttons */
		int generatedEvents = 0;
		for (int i = 0; i < 500; i++) {

			if (generatedEvents < 12) {

				if (i % cycles*2 == 0) {
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
				// observer.persist();
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
			// observer.persist();
			flipperGame.doStep();
			update++;
			logger.debug("Update" + update + " ------------------------------ update " + update);
		}

		logger.info("Accepted events: " + flipperGame.getStorage().queue.toString());

		System.out.println(flipperGame.getController().observer.toString());
		Assert.assertEquals("Event in storage", 12, flipperGame.getStorage().queue.size());

	}

	@Test
	public void backpressureTest() {

		FlipperObjectFactory slowBufusObjectFactory = new FlipperObjectFactory();
		slowBufusObjectFactory.setBufuProcessingStep(1);
		FlipperGame flipperGame = new FlipperGame(slowBufusObjectFactory);

		Logger.getRootLogger().setLevel(Level.OFF);
		Logger.getLogger(Clickable.class).setLevel(Level.OFF);
		Logger.getLogger(Button.class).setLevel(Level.OFF);
		Logger.getLogger(FlipperGameTest.class).setLevel(Level.OFF);
		Logger.getLogger(Buffer.class).setLevel(Level.OFF);
		Logger.getLogger(Dispatcher.class).setLevel(Level.OFF);

		int update = 0;
		Assert.assertEquals("Nothing in storage", 0, flipperGame.getStorage().queue.size());

		int progressStep = flipperGame.link11.progressStep;
		int cycles = (100 / progressStep);
		cycles = cycles*2;
		logger.info("Progress step of link level 1 is: " + progressStep + " will generate events every " + cycles);
		/* Generate new events while pressing the buttons */
		int generatedEvents = 0;
		for (int i = 0; i < 500; i++) {

			if (generatedEvents < 23) {

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
				// observer.persist();
				flipperGame.doStep();
				update++;
				logger.debug("Update" + update + " ------------------------------ update " + update);
			}
		}

		Assert.assertEquals("Make sure 23 events were generated", 23, generatedEvents);

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
			// observer.persist();
			flipperGame.doStep();
			update++;
			logger.debug("Update" + update + " ------------------------------ update " + update);
		}

		logger.info("Accepted events: " + flipperGame.getStorage().queue.toString());

		System.out.println(flipperGame.getController().observer.toString());
		Assert.assertEquals("Event in storage", 12, flipperGame.getStorage().queue.size());

	}
}
