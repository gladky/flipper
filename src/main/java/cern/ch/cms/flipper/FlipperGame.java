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

	public final Button buttonL1;
	public final Button buttonHLT_L1;
	public final Button buttonHLT_L2;
	public final Button buttonHLT_L3;
	public final Button buttonHLT_R1;
	public final Button buttonHLT_R2;
	public final Button buttonHLT_R3;

	private final FlipperObject storage;

	public final FlipperObject link11;
	public final FlipperObject link12;
	public final FlipperObject link13;
	public final FlipperObject link14;

	private final Buffer buffer1;
	private final Buffer buffer2;
	private final Buffer buffer3;
	private final Buffer buffer4;

	public final FlipperObject link21;
	public final FlipperObject link22;
	public final FlipperObject link23;
	public final FlipperObject link24;

	public final FlipperObject link31;
	public final FlipperObject link32;
	public final FlipperObject link33;
	public final FlipperObject link34;
	public final FlipperObject link35;
	public final FlipperObject link36;

	private final FlipperObject bufuL1;
	private final FlipperObject bufuL2;
	private final FlipperObject bufuL3;
	private final FlipperObject bufuR1;
	private final FlipperObject bufuR2;
	private final FlipperObject bufuR3;

	public final FlipperObject link41;
	public final FlipperObject link42;
	public final FlipperObject link43;
	public final FlipperObject link44;
	public final FlipperObject link45;
	public final FlipperObject link46;
	public final FlipperObject link47;
	public final FlipperObject link48;

	private final FlipperObject switch_;

	/* max 4 */
	public final int linkBoost = 4;

	protected final Dispatcher dispatcher;

	public FlipperGame() {

		controller = new GameController();

		factory = new FlipperObjectFactory(controller);

		buttonL1 = factory.createButton("Lv1 btn");
		buttonHLT_L1 = factory.createButton("L1 btn");
		buttonHLT_L2 = factory.createButton("L2 btn");
		buttonHLT_L3 = factory.createButton("L3 btn");
		buttonHLT_R1 = factory.createButton("R1 btn");
		buttonHLT_R2 = factory.createButton("R2 btn");
		buttonHLT_R3 = factory.createButton("R3 btn");

		/* links to buffer */
		link11 = factory.createLink("11", 20 / linkBoost);
		link12 = factory.createLink("12", 20 / linkBoost);
		link13 = factory.createLink("13", 20 / linkBoost);
		link14 = factory.createLink("14", 20 / linkBoost);

		buffer1 = factory.createBuffer("buf1", buttonL1);
		buffer2 = factory.createBuffer("buf2", buttonL1);
		buffer3 = factory.createBuffer("buf3", buttonL1);
		buffer4 = factory.createBuffer("buf4", buttonL1);

		/* links to switch */
		link21 = factory.createLink("21", 20 / linkBoost);
		link22 = factory.createLink("22", 20 / linkBoost);
		link23 = factory.createLink("23", 20 / linkBoost);
		link24 = factory.createLink("24", 20 / linkBoost);

		switch_ = factory.createSwitch("switch");

		/* links to bufus */
		link31 = factory.createLink("31", 13 / linkBoost);
		link32 = factory.createLink("32", 17 / linkBoost);
		link33 = factory.createLink("33", 34 / linkBoost);
		link34 = factory.createLink("34", 34 / linkBoost);
		link35 = factory.createLink("35", 17 / linkBoost);
		link36 = factory.createLink("36", 13 / linkBoost);

		bufuL1 = factory.createBUFU("L1BF", buttonHLT_L1);
		bufuL2 = factory.createBUFU("L2BF", buttonHLT_L2);
		bufuL3 = factory.createBUFU("L3BF", buttonHLT_L3);
		bufuR1 = factory.createBUFU("R1BF", buttonHLT_R1);
		bufuR2 = factory.createBUFU("R2BF", buttonHLT_R2);
		bufuR3 = factory.createBUFU("R3BF", buttonHLT_R3);

		link41 = factory.createLink("41", 10 / linkBoost);
		link42 = factory.createLink("42", 4 / linkBoost);
		link43 = factory.createLink("43", 13 / linkBoost);
		link44 = factory.createLink("44", 25 / linkBoost);
		link45 = factory.createLink("45", 10 / linkBoost);
		link46 = factory.createLink("46", 4 / linkBoost);
		link47 = factory.createLink("47", 13 / linkBoost);
		link48 = factory.createLink("48", 25 / linkBoost);

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

		/* FIXME: Use this line for debugging, In production remove it */
		controller.observer = new FlowObserver(this);

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

	public Buffer getBuffer1() {
		return buffer1;
	}

	public Buffer getBuffer2() {
		return buffer2;
	}

	public Buffer getBuffer3() {
		return buffer3;
	}

	public Buffer getBuffer4() {
		return buffer4;
	}
}
