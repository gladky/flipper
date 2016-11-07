package cern.ch.cms.flipper.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import cern.ch.cms.flipper.FlipperObjectTestBase;
import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Fragment;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class BufferTest extends FlipperObjectTestBase {

	private static final Logger logger = Logger.getLogger(BufferTest.class);

	/**
	 * Check that buffer will not accept more events than capacity
	 */
	@Test
	public void capacityTest() {

		int capacity = 12;
		Buffer buffer = new Buffer("test-buffer", capacity, 10, 10, new Button("test-button"), new SoundPlayer("sp"),
				true);
		buffer.setDispatcher(new DispatcherStub());

		for (int i = 0; i < capacity; i++) {
			Assert.assertTrue(buffer.canAccept());
			Assert.assertTrue(buffer.insert(new Fragment()));
			buffer.doStep();
		}
		Assert.assertFalse(buffer.canAccept());
		Assert.assertFalse(buffer.insert(new Fragment()));
	}

	/**
	 * Full buffer will not accept. When released, will again accept
	 * 
	 * <pre>
	 * <code>
	 * ............
	 * ...buffer...
	 * .....|...... <- no link here
	 * ...storage..
	 * ............
	 * </code>
	 * </pre>
	 */
	@Test
	public void acceptTest() {

		Logger.getLogger(FlipperObject.class).setLevel(Level.TRACE);

		FlipperObject[] objects;
		Button[] buttons;
		int capacity = 10;
		Button button = new Button("test-button");
		buttons = new Button[] { button };
		Buffer buffer = new Buffer("test-buffer", capacity, 10, 10, button, new SoundPlayer("sp"), true);
		FlipperObject storage = new Storage("storage", 10, new SoundPlayer("sp"));
		Dispatcher dispatcher = new Dispatcher(Arrays.asList(storage), Arrays.asList(storage), new SoundPlayer("sp"));
		objects = new FlipperObject[] { storage, buffer };

		buffer.setDispatcher(dispatcher);
		buffer.getSuccessors().add(storage);

		for (int i = 0; i < capacity; i++) {
			Assert.assertTrue("Non full buffer can accept", buffer.canAccept());
			Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));
			logger.info(Arrays.toString(buffer.getProgress()));
			doSteps(objects, buttons, dispatcher);
		}
		logger.info("Now buffer should be full");

		Assert.assertFalse("Full buffer cannot accept", buffer.canAccept());
		Assert.assertFalse("Full buffer refuse to accept", buffer.insert(new Fragment()));
		Assert.assertEquals("Full buffer size equals capacity", capacity, buffer.queue.size());

		logger.info(Arrays.toString(buffer.getProgress()));

		button.press();
		doSteps(objects, buttons, dispatcher);
		logger.info(Arrays.toString(buffer.getProgress()));

		Logger.getLogger(this.getClass()).info("Testing the buffer");
		Assert.assertEquals("Storage received one event", 1, storage.queue.size());
		Assert.assertEquals("One data less in buffer after passing to storage", capacity - 1, buffer.queue.size());

		Assert.assertTrue("Buffer can accept again", buffer.canAccept());
		Assert.assertTrue("Buffer accepts sucessfully", buffer.insert(new Fragment()));
		Assert.assertEquals("Buffer data size is again equal to capacity", capacity, buffer.queue.size());
	}

	@Test
	public void localProgressLimitTest() {

		Logger.getLogger(FlipperObject.class).setLevel(Level.TRACE);

		FlipperObject[] objects;
		Button[] buttons;
		int capacity = 10;
		Button button = new Button("test-button");
		buttons = new Button[] { button };
		Buffer buffer = new Buffer("test-buffer", capacity, 10, 30, button, new SoundPlayer("sp"), true);
		FlipperObject storage = new Storage("storage", 10, new SoundPlayer("sp"));
		Dispatcher dispatcher = new Dispatcher(Arrays.asList(storage), Arrays.asList(storage), new SoundPlayer("sp"));
		objects = new FlipperObject[] { storage, buffer };

		buffer.setDispatcher(dispatcher);
		buffer.getSuccessors().add(storage);

		Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));
		Assert.assertArrayEquals(new int[] { 0 }, buffer.getProgress());
		doSteps(objects, buttons, dispatcher);

		Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));
		Assert.assertArrayEquals(new int[] { 10, 0 }, buffer.getProgress());
		doSteps(objects, buttons, dispatcher);

		Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));
		Assert.assertArrayEquals(new int[] { 20, 10, 0 }, buffer.getProgress());
		doSteps(objects, buttons, dispatcher);

		Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));
		Assert.assertArrayEquals(new int[] { 30, 20, 10, 0 }, buffer.getProgress());
		doSteps(objects, buttons, dispatcher);

		Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));
		Assert.assertArrayEquals(new int[] { 40, 30, 20, 10, 0 }, buffer.getProgress());
		doSteps(objects, buttons, dispatcher);

		Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));
		Assert.assertArrayEquals(new int[] { 50, 40, 30, 20, 10, 0 }, buffer.getProgress());
		doSteps(objects, buttons, dispatcher);

		Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));
		Assert.assertArrayEquals(new int[] { 60, 50, 40, 30, 20, 10, 0 }, buffer.getProgress());
		doSteps(objects, buttons, dispatcher);

		Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));
		Assert.assertArrayEquals(new int[] { 70, 60, 50, 40, 30, 20, 10, 0 }, buffer.getProgress());
		doSteps(objects, buttons, dispatcher);

		Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));
		Assert.assertArrayEquals(new int[] { 80, 70, 60, 50, 40, 30, 20, 10, 0 }, buffer.getProgress());
		doSteps(objects, buttons, dispatcher);

		Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));
		Assert.assertArrayEquals(new int[] { 90, 80, 70, 60, 50, 40, 30, 20, 10, 0 }, buffer.getProgress());
		doSteps(objects, buttons, dispatcher);

		logger.info("Now buffer should be full");
		Assert.assertArrayEquals(new int[] { 100, 90, 80, 70, 60, 50, 40, 30, 20, 10 }, buffer.getProgress());

		doSteps(objects, buttons, dispatcher);
		Assert.assertArrayEquals(new int[] { 110, 90, 80, 70, 60, 50, 40, 30, 20, 10 }, buffer.getProgress());

		doSteps(objects, buttons, dispatcher);
		Assert.assertArrayEquals(new int[] { 120, 90, 80, 70, 60, 50, 40, 30, 20, 10 }, buffer.getProgress());

		logger.info("Timeout is full here");
		doSteps(objects, buttons, dispatcher);
		Assert.assertArrayEquals(new int[] { 90, 80, 70, 60, 50, 40, 30, 20, 10 }, buffer.getProgress());

		doSteps(objects, buttons, dispatcher);
		Assert.assertArrayEquals(new int[] { 100, 90, 80, 70, 60, 50, 40, 30, 20 }, buffer.getProgress());

		doSteps(objects, buttons, dispatcher);
		Assert.assertArrayEquals(new int[] { 110, 90, 80, 70, 60, 50, 40, 30, 20 }, buffer.getProgress());

		doSteps(objects, buttons, dispatcher);
		Assert.assertArrayEquals(new int[] { 120, 90, 80, 70, 60, 50, 40, 30, 20 }, buffer.getProgress());
		
		
	}

}

class DispatcherStub extends Dispatcher {

	public DispatcherStub() {
		super(new ArrayList(), new ArrayList(), new SoundPlayer("sp"));
	}

}
