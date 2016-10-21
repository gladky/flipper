package cern.ch.cms.flipper;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Fragment;
import cern.ch.cms.flipper.model.FlipperObject;

public class FlipperGame {

	private static final Logger logger = Logger.getLogger(FlipperGame.class);

	private final GameController controller = new GameController();
	private final FlipperObjectFactory factory = new FlipperObjectFactory(controller);

	private final Button buttonLZ = factory.createButton("button Level0");
	private final Button buttonLeft1 = factory.createButton("button L1");
	private final Button buttonLeft2 = factory.createButton("button L2");
	private final Button buttonLeft3 = factory.createButton("button L3");
	private final Button buttonRight1 = factory.createButton("button R1");
	private final Button buttonRight2 = factory.createButton("button R2");
	private final Button buttonRight3 = factory.createButton("button R3");

	private final FlipperObject storage = factory.createStorage();

	private final FlipperObject link1 = factory.createLink("  left to buffer link");
	private final FlipperObject link2 = factory.createLink(" mleft to buffer link");
	private final FlipperObject link3 = factory.createLink("mright to buffer link");
	private final FlipperObject link4 = factory.createLink(" right to buffer link");

	private final FlipperObject buffer1 = factory.createBuffer("  left buffer", buttonLZ);
	private final FlipperObject buffer2 = factory.createBuffer(" mleft buffer", buttonLZ);
	private final FlipperObject buffer3 = factory.createBuffer("mright buffer", buttonLZ);
	private final FlipperObject buffer4 = factory.createBuffer(" right buffer", buttonLZ);

	private final FlipperObject link05 = factory.createLink("  left to switch link");
	private final FlipperObject link06 = factory.createLink(" mleft to switch link");
	private final FlipperObject link07 = factory.createLink("mright to switch link");
	private final FlipperObject link08 = factory.createLink(" right to switch link");

	private final FlipperObject link09 = factory.createLink(" left to bufu link");
	private final FlipperObject link10 = factory.createLink("right to bufu link");

	private final FlipperObject bufuL1 = factory.createBUFU("bufu L1", buttonLeft1);
	private final FlipperObject bufuL2 = factory.createBUFU("bufu L2", buttonLeft2);
	private final FlipperObject bufuL3 = factory.createBUFU("bufu L3", buttonLeft3);
	private final FlipperObject bufuR1 = factory.createBUFU("bufu R1", buttonRight1);
	private final FlipperObject bufuR2 = factory.createBUFU("bufu R2", buttonRight2);
	private final FlipperObject bufuR3 = factory.createBUFU("bufu R3", buttonRight3);

	private final FlipperObject link11 = factory.createLink(" left to storage link");
	private final FlipperObject link12 = factory.createLink("right to storage link");

	private final FlipperObject switch_ = factory.createSwitch("switch");

	public FlipperGame() {

		link1.getSuccessors().add(buffer1);
		link2.getSuccessors().add(buffer2);
		link3.getSuccessors().add(buffer3);
		link4.getSuccessors().add(buffer4);

		buffer1.getSuccessors().add(link05);
		buffer2.getSuccessors().add(link06);
		buffer3.getSuccessors().add(link07);
		buffer4.getSuccessors().add(link08);

		link05.getSuccessors().add(switch_);
		link06.getSuccessors().add(switch_);
		link07.getSuccessors().add(switch_);
		link08.getSuccessors().add(switch_);

		switch_.getSuccessors().add(link09);
		switch_.getSuccessors().add(link10);

		link09.getSuccessors().add(bufuL1);
		link10.getSuccessors().add(bufuL2);

		bufuL1.getSuccessors().add(link11);
		bufuL2.getSuccessors().add(link12);

		link11.getSuccessors().add(storage);
		link12.getSuccessors().add(storage);
	}

	public void generateNewFragments() {

		Data f1 = new Fragment();
		Data f2 = new Fragment();
		Data f3 = new Fragment();
		Data f4 = new Fragment();

		link1.insert(f1);
		link2.insert(f2);
		link3.insert(f3);
		link4.insert(f4);

	}

	public void pressButtonLZ() {
		logger.info("LevelZero button pressed");
		buttonLZ.press();
	}

	public void pressButtonL1() {
		logger.info("Button L1 pressed");
		buttonLeft1.press();
	}

	public void pressButtonL2() {
		logger.info("Button L2 pressed");
		buttonLeft2.press();
	}

	public void pressButtonL3() {
		logger.info("Button L3 pressed");
		buttonLeft3.press();
	}

	public void pressButtonR1() {
		logger.info("Button R1 pressed");
		buttonRight1.press();
	}

	public void pressButtonR2() {
		logger.info("Button R2 pressed");
		buttonRight2.press();
	}

	public void pressButtonR3() {
		logger.info("Button R3 pressed");
		buttonRight3.press();
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
