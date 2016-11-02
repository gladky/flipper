package cern.ch.cms.flipper;

import java.util.Arrays;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Fragment;
import cern.ch.cms.flipper.model.Buffer;
import cern.ch.cms.flipper.model.Dispatcher;
import cern.ch.cms.flipper.model.FlipperObject;

public class FlipperGame {

	private static final Logger logger = Logger.getLogger(FlipperGame.class);

	private final GameController controller;
	private final FlipperObjectFactory factory;

	private final Button buttonL1;
	private final Button buttonHLT_L1;
	private final Button buttonHLT_L2;
	private final Button buttonHLT_L3;
	private final Button buttonHLT_R1;
	private final Button buttonHLT_R2;
	private final Button buttonHLT_R3;

	private final FlipperObject storage;

	private final FlipperObject link11;
	private final FlipperObject link12;
	private final FlipperObject link13;
	private final FlipperObject link14;

	private final Buffer buffer1;
	private final Buffer buffer2;
	private final Buffer buffer3;
	private final Buffer buffer4;

	private final FlipperObject link21;
	private final FlipperObject link22;
	private final FlipperObject link23;
	private final FlipperObject link24;

	private final FlipperObject link31;
	private final FlipperObject link32;
	private final FlipperObject link33;
	private final FlipperObject link34;
	private final FlipperObject link35;
	private final FlipperObject link36;

	private final FlipperObject bufuL1;
	private final FlipperObject bufuL2;
	private final FlipperObject bufuL3;
	private final FlipperObject bufuR1;
	private final FlipperObject bufuR2;
	private final FlipperObject bufuR3;

	private final FlipperObject link41;
	private final FlipperObject link42;
	private final FlipperObject link43;
	private final FlipperObject link44;
	private final FlipperObject link45;
	private final FlipperObject link46;
	private final FlipperObject link47;
	private final FlipperObject link48;

	private final FlipperObject switch_;

	private final Dispatcher dispatcher;

	public FlipperGame() {

		controller = new GameController();
		factory = new FlipperObjectFactory(controller);

		buttonL1 = factory.createButton("button Level0");
		buttonHLT_L1 = factory.createButton("button L1");
		buttonHLT_L2 = factory.createButton("button L2");
		buttonHLT_L3 = factory.createButton("button L3");
		buttonHLT_R1 = factory.createButton("button R1");
		buttonHLT_R2 = factory.createButton("button R2");
		buttonHLT_R3 = factory.createButton("button R3");

		/* links to buffer */
		link11 = factory.createLink("  left to buffer link");
		link12 = factory.createLink(" mleft to buffer link");
		link13 = factory.createLink("mright to buffer link");
		link14 = factory.createLink(" right to buffer link");

		buffer1 = factory.createBuffer("  left buffer", buttonL1);
		buffer2 = factory.createBuffer(" mleft buffer", buttonL1);
		buffer3 = factory.createBuffer("mright buffer", buttonL1);
		buffer4 = factory.createBuffer(" right buffer", buttonL1);

		/* links to switch */
		link21 = factory.createLink("  left to switch link");
		link22 = factory.createLink(" mleft to switch link");
		link23 = factory.createLink("mright to switch link");
		link24 = factory.createLink(" right to switch link");

		switch_ = factory.createSwitch("switch");

		/* links to bufus */
		link31 = factory.createLink("to L1 bufu link");
		link32 = factory.createLink("to L2 bufu link");
		link33 = factory.createLink("to L3 bufu link");
		link34 = factory.createLink("to R1 bufu link");
		link35 = factory.createLink("to R2 bufu link");
		link36 = factory.createLink("to R3 bufu link");

		bufuL1 = factory.createBUFU("bufu L1", buttonHLT_L1);
		bufuL2 = factory.createBUFU("bufu L2", buttonHLT_L2);
		bufuL3 = factory.createBUFU("bufu L3", buttonHLT_L3);
		bufuR1 = factory.createBUFU("bufu R1", buttonHLT_R1);
		bufuR2 = factory.createBUFU("bufu R2", buttonHLT_R2);
		bufuR3 = factory.createBUFU("bufu R3", buttonHLT_R3);

		link41 = factory.createLink("L1 to storage link");
		link42 = factory.createLink("L2 to storage link");
		link43 = factory.createLink("LS to storage link");
		link44 = factory.createLink("LF to storage link");
		link45 = factory.createLink("R storage link");
		link46 = factory.createLink("R2 to storage link");
		link47 = factory.createLink("RS to storage link");
		link48 = factory.createLink("RF to storage link");

		storage = factory.createStorage();

		dispatcher = factory.createDispatcher(Arrays.asList(bufuL1, bufuL2, bufuL3, bufuR1, bufuR2, bufuR3),
				Arrays.asList(link31, link32, link33, link34, link35, link36));
		buffer1.setDispatcher(dispatcher);
		buffer2.setDispatcher(dispatcher);
		buffer3.setDispatcher(dispatcher);
		buffer4.setDispatcher(dispatcher);

		link11.getSuccessors().add(buffer1);
		link12.getSuccessors().add(buffer2);
		link13.getSuccessors().add(buffer3);
		link14.getSuccessors().add(buffer4);

		buffer1.getSuccessors().add(link21);
		buffer2.getSuccessors().add(link22);
		buffer3.getSuccessors().add(link23);
		buffer4.getSuccessors().add(link24);

		link21.getSuccessors().add(switch_);
		link22.getSuccessors().add(switch_);
		link23.getSuccessors().add(switch_);
		link24.getSuccessors().add(switch_);

		switch_.getSuccessors().add(link31);
		switch_.getSuccessors().add(link32);
		switch_.getSuccessors().add(link33);
		switch_.getSuccessors().add(link34);
		switch_.getSuccessors().add(link35);
		switch_.getSuccessors().add(link36);

		link31.getSuccessors().add(bufuL1);
		link32.getSuccessors().add(bufuL2);
		link33.getSuccessors().add(bufuL3);
		link34.getSuccessors().add(bufuR1);
		link35.getSuccessors().add(bufuR2);
		link36.getSuccessors().add(bufuR3);

		bufuL1.getSuccessors().add(link41);
		link41.getSuccessors().add(link43);
		bufuL2.getSuccessors().add(link42);
		bufuL3.getSuccessors().add(link44);
		link42.getSuccessors().add(link43);
		link43.getSuccessors().add(link44);

		bufuR1.getSuccessors().add(link45);
		link45.getSuccessors().add(link47);
		bufuR2.getSuccessors().add(link46);
		bufuR3.getSuccessors().add(link48);
		link46.getSuccessors().add(link47);
		link47.getSuccessors().add(link48);

		link44.getSuccessors().add(storage);
		link48.getSuccessors().add(storage);
	}

	public void generateNewFragments() {

		Data f1 = new Fragment();
		Data f2 = new Fragment();
		Data f3 = new Fragment();
		Data f4 = new Fragment();

		link11.insert(f1);
		link12.insert(f2);
		link13.insert(f3);
		link14.insert(f4);

	}

	public void pressButtonLevel1() {
		logger.debug("Level 1 button pressed");
		buttonL1.press();
	}

	public void pressButtonHLT_L1() {
		logger.debug("Button L1 pressed");
		buttonHLT_L1.press();
	}

	public void pressButtonHLT_L2() {
		logger.debug("Button L2 pressed");
		buttonHLT_L2.press();
	}

	public void pressButtonHLT_L3() {
		logger.debug("Button L3 pressed");
		buttonHLT_L3.press();
	}

	public void pressButtonHLT_R1() {
		logger.debug("Button R1 pressed");
		buttonHLT_R1.press();
	}

	public void pressButtonHLT_R2() {
		logger.debug("Button R2 pressed");
		buttonHLT_R2.press();
	}

	public void pressButtonHLT_R3() {
		logger.debug("Button R3 pressed");
		buttonHLT_R3.press();
	}

	public GameController getController() {
		return controller;
	}

	public void doStep() {
		controller.doStep();
	}

	public void doSteps(int steps) {
		for (int i = 0; i < steps; i++) {
			controller.doStep();
		}
	}

	public FlipperObject getStorage() {
		return storage;
	}

	public FlipperObject getSwitch() {
		return switch_;
	}

	public FlipperObject getBufuL1() {
		return bufuL1;
	}

	public FlipperObject getBufuL2() {
		return bufuL2;
	}

	public FlipperObject getBufuL3() {
		return bufuL3;
	}

	public FlipperObject getBufuR1() {
		return bufuR1;
	}

	public FlipperObject getBufuR2() {
		return bufuR2;
	}

	public FlipperObject getBufuR3() {
		return bufuR3;
	}
}
