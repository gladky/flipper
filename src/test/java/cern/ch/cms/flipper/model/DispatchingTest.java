package cern.ch.cms.flipper.model;

import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Fragment;
import cern.ch.cms.flipper.sounds.SoundPlayer;

/**
 * <code><pre>
 * buffer buffer  <--4x buffers
 * ..|......|...
 * ...\..../....  <--link 1-4
 * ....\../.....
 * ...switch....
 * ..../..\.....  <--link 5-6
 * .../....\....
 * ..bufu.bufu..  <--2x bufus
 * </pre></code>
 * 
 * 
 * @author Maciej Gladki (maciej.szymon.gladki@cern.ch)
 *
 */
public class DispatchingTest {
	Dispatcher dispatcher;
	Button level1Button;
	Button leftHLTButton;
	Button rightHLTButton;
	Buffer buffer1;
	Buffer buffer2;
	Buffer buffer3;
	Buffer buffer4;

	FlipperObject link1;
	FlipperObject link2;
	FlipperObject link3;
	FlipperObject link4;

	FlipperObject switch_;

	FlipperObject link5;
	FlipperObject link6;

	FlipperObject bufu1;
	FlipperObject bufu2;

	FlipperObject[] objects;
	private Logger logger = Logger.getLogger(DispatchingTest.class);
	private static final SoundPlayer sp = new SoundPlayer("sp");

	@Before
	public void initializeFlipperObjects() {

		level1Button = new Button("[L1-button]", sp);
		leftHLTButton = new Button("test-button-bufu-1", sp);
		rightHLTButton = new Button("test-button-bufu-1", sp);

		buffer1 = new Buffer("[buffer1]", 12, 20, 10, level1Button, sp, true);
		buffer2 = new Buffer("[buffer2]", 12, 20, 10, level1Button, sp, true);
		buffer3 = new Buffer("[buffer3]", 12, 20, 10, level1Button, sp, true);
		buffer4 = new Buffer("[buffer4]", 12, 20, 10, level1Button, sp, true);

		switch_ = Mockito.spy(new Switch("[test-switch]", sp));

		link1 = new Link("[buffer-switch-link-1]", 1, 25, sp);
		link2 = new Link("[buffer-switch-link-2]", 1, 25, sp);
		link3 = new Link("[buffer-switch-link-3]", 1, 25, sp);
		link4 = new Link("[buffer-switch-link-4]", 1, 25, sp);

		link5 = new Link("[switch-bufu-link-left]", 1, 25, sp);
		link6 = new Link("[switch-bufu-link-right]", 1, 25, sp);

		bufu1 = Mockito.spy(new BUFU("[test-bufu-1]", 5, 25, leftHLTButton, sp));
		bufu2 = Mockito.spy(new BUFU("[test-bufu-2]", 5, 25, rightHLTButton, sp));

		buffer1.getSuccessors().add(link1);
		buffer2.getSuccessors().add(link2);
		buffer3.getSuccessors().add(link3);
		buffer4.getSuccessors().add(link4);

		link1.getSuccessors().add(switch_);
		link2.getSuccessors().add(switch_);
		link3.getSuccessors().add(switch_);
		link4.getSuccessors().add(switch_);

		switch_.getSuccessors().add(link5);
		switch_.getSuccessors().add(link6);

		link5.getSuccessors().add(bufu1);
		link6.getSuccessors().add(bufu2);

		Dispatcher dispatcher = new Dispatcher(Arrays.asList(bufu1, bufu2), Arrays.asList(link5, link6),
				new SoundPlayer("sp"));
		buffer1.setDispatcher(dispatcher);
		buffer2.setDispatcher(dispatcher);
		buffer3.setDispatcher(dispatcher);
		buffer4.setDispatcher(dispatcher);

		FlipperObject[] newobjects = { bufu1, bufu2, link5, link6, switch_, link1, link2, link3, link4, buffer1,
				buffer2, buffer3, buffer4 };
		objects = newobjects;
	}

	/**
	 * 
	 * Fill the bufus and verify that there is backpressure and level one
	 * trigger will not be accepted
	 */
	@Test
	public void sequentiallyCreateBackpressure() {

		// Logger.getLogger(FlipperObject.class).setLevel(Level.DEBUG);
		// Logger.getLogger(Button.class).setLevel(Level.DEBUG);

		/* do 5 steps so that buffers will finish */
		for (int i = 0; i < 2; i++) {
			insertNewFragmentsToBuffers();
			doSteps(objects);
			doSteps(objects);
			doSteps(objects);
			doSteps(objects);
			doSteps(objects);
			Assert.assertTrue("accept level 1 trigger", level1Button.press());
			doSteps(objects);
		}

		insertNewFragmentsToBuffers();
		for (int i = 0; i < 2; i++) {
			doSteps(objects);
			Assert.assertFalse("DO NOT accept level 1 trigger", level1Button.press());
			doSteps(objects);
		}
	}

	private void insertNewFragmentsToBuffers() {
		Data f1 = new Fragment();
		Data f2 = new Fragment();
		Data f3 = new Fragment();
		Data f4 = new Fragment();

		buffer1.insert(f1);
		buffer2.insert(f2);
		buffer3.insert(f3);
		buffer4.insert(f4);
	}

	private void doSteps(FlipperObject... objects) {
		for (FlipperObject object : objects) {
			object.doStep();
		}
		level1Button.doStep();
		leftHLTButton.doStep();
		rightHLTButton.doStep();
	}
}
