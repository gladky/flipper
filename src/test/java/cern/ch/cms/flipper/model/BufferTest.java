package cern.ch.cms.flipper.model;

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

	/**
	 * Check that buffer will not accept more events than capacity
	 */
	@Test
	public void capacityTest() {

		int capacity = 12;
		FlipperObject buffer = new Buffer("test-buffer", capacity, 10, 10, new Button("test-button"),
				new SoundPlayer("sp"),true);

		for (int i = 0; i < capacity; i++) {
			Assert.assertTrue(buffer.canAccept());
			Assert.assertTrue(buffer.insert(new Fragment()));
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
		int capacity = 12;
		Button button = new Button("test-button");
		buttons = new Button[] { button };
		Buffer buffer = new Buffer("test-buffer", capacity, 10, 10, button, new SoundPlayer("sp"),true);
		FlipperObject storage = new Storage("storage", 10, new SoundPlayer("sp"));
		Dispatcher dispatcher = new Dispatcher(Arrays.asList(storage), Arrays.asList(storage));
		objects = new FlipperObject[] { storage, buffer };

		buffer.setDispatcher(dispatcher);
		buffer.getSuccessors().add(storage);

		for (int i = 0; i < capacity; i++) {
			Assert.assertTrue("Non full buffer can accept", buffer.canAccept());
			Assert.assertTrue("Non full buffer accepts successfully", buffer.insert(new Fragment()));

			doSteps(objects, buttons, dispatcher);
		}
		Assert.assertFalse("Full buffer cannot accept", buffer.canAccept());
		Assert.assertFalse("Full buffer refuse to accept", buffer.insert(new Fragment()));
		Assert.assertEquals("Full buffer size equals capacity", capacity, buffer.queue.size());

		button.press();
		doSteps(objects, buttons, dispatcher);

		Logger.getLogger(this.getClass()).info("Testing the buffer");
		Assert.assertEquals("Storage received one event", 1, storage.queue.size());
		Assert.assertEquals("One data less in buffer after passing to storage", capacity - 1, buffer.queue.size());

		Assert.assertTrue("Buffer can accept again", buffer.canAccept());
		Assert.assertTrue("Buffer accepts sucessfully", buffer.insert(new Fragment()));
		Assert.assertEquals("Buffer data size is again equal to capacity", capacity, buffer.queue.size());
	}

}
