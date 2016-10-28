package cern.ch.cms.flipper;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Fragment;
import cern.ch.cms.flipper.model.FlipperObject;

public class FlipperGame {

	private static final Logger logger = Logger.getLogger(FlipperGame.class);

	private final GameController controller;
	private final FlipperObjectFactory factory;

	private final Button buttonLZ;
	private final Button buttonLeft1;
	private final Button buttonLeft2;
	private final Button buttonLeft3;
	private final Button buttonRight1;
	private final Button buttonRight2;
	private final Button buttonRight3;

	private final FlipperObject storage;

	private final FlipperObject link1;
	private final FlipperObject link2;
	private final FlipperObject link3;
	private final FlipperObject link4;

	private final FlipperObject buffer1;
	private final FlipperObject buffer2;
	private final FlipperObject buffer3;
	private final FlipperObject buffer4;

	private final FlipperObject link05;
	private final FlipperObject link06;
	private final FlipperObject link07;
	private final FlipperObject link08;

	private final FlipperObject link09;
	private final FlipperObject link10;

	private final FlipperObject bufuL1;
	private final FlipperObject bufuL2;
	private final FlipperObject bufuL3;
	private final FlipperObject bufuR1;
	private final FlipperObject bufuR2;
	private final FlipperObject bufuR3;

	private final FlipperObject link11;
	private final FlipperObject link12;

	private final FlipperObject switch_;

	public FlipperGame() {

		controller = new GameController();
		factory = new FlipperObjectFactory(controller);

		buttonLZ = factory.createButton("button Level0");
		buttonLeft1 = factory.createButton("button L1");
		buttonLeft2 = factory.createButton("button L2");
		buttonLeft3 = factory.createButton("button L3");
		buttonRight1 = factory.createButton("button R1");
		buttonRight2 = factory.createButton("button R2");
		buttonRight3 = factory.createButton("button R3");


		link1 = factory.createLink("  left to buffer link");
		link2 = factory.createLink(" mleft to buffer link");
		link3 = factory.createLink("mright to buffer link");
		link4 = factory.createLink(" right to buffer link");

		buffer1 = factory.createBuffer("  left buffer", buttonLZ);
		buffer2 = factory.createBuffer(" mleft buffer", buttonLZ);
		buffer3 = factory.createBuffer("mright buffer", buttonLZ);
		buffer4 = factory.createBuffer(" right buffer", buttonLZ);

		link05 = factory.createLink("  left to switch link");
		link06 = factory.createLink(" mleft to switch link");
		link07 = factory.createLink("mright to switch link");
		link08 = factory.createLink(" right to switch link");
		
		switch_ = factory.createSwitch("switch");

		link09 = factory.createLink(" left to bufu link");
		link10 = factory.createLink("right to bufu link");

		bufuL1 = factory.createBUFU("bufu L1", buttonLeft1);
		bufuL2 = factory.createBUFU("bufu L2", buttonLeft2);
		bufuL3 = factory.createBUFU("bufu L3", buttonLeft3);
		bufuR1 = factory.createBUFU("bufu R1", buttonRight1);
		bufuR2 = factory.createBUFU("bufu R2", buttonRight2);
		bufuR3 = factory.createBUFU("bufu R3", buttonRight3);
		
		link11 = factory.createLink(" left to storage link");
		link12 = factory.createLink("right to storage link");

		storage = factory.createStorage();
		

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
		logger.debug("LevelZero button pressed");
		buttonLZ.press();
	}

	public void pressButtonL1() {
		logger.debug("Button L1 pressed");
		buttonLeft1.press();
	}

	public void pressButtonL2() {
		logger.debug("Button L2 pressed");
		buttonLeft2.press();
	}

	public void pressButtonL3() {
		logger.debug("Button L3 pressed");
		buttonLeft3.press();
	}

	public void pressButtonR1() {
		logger.debug("Button R1 pressed");
		buttonRight1.press();
	}

	public void pressButtonR2() {
		logger.debug("Button R2 pressed");
		buttonRight2.press();
	}

	public void pressButtonR3() {
		logger.debug("Button R3 pressed");
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
