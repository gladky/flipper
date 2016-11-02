package cern.ch.cms.flipper.model;

import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Fragment;

public class BufferTest {

	/**
	 * Check that buffer will not accept more events than capacity
	 */
	@Test
	public void capacityTest() {

		int capacity = 12;
		FlipperObject buffer = new Buffer("test-buffer", capacity, 10, 10, new Button("test-button"));

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

		Logger.getLogger(FlipperObject.class).setLevel(Level.DEBUG);

		int capacity = 12;
		Button button = new Button("test-button");
		Buffer buffer = new Buffer("test-buffer", capacity, 10, 10, button);
		

		FlipperObject storage = new Storage("storage");
		Dispatcher dispatcher = new Dispatcher(Arrays.asList(storage), Arrays.asList(storage));

		buffer.setDispatcher(dispatcher);
		buffer.getSuccessors().add(storage);

		for (int i = 0; i < capacity; i++) {
			buffer.doStep();
			Assert.assertTrue(buffer.canAccept());
			Assert.assertTrue(buffer.insert(new Fragment()));
		}
		Assert.assertFalse(buffer.canAccept());
		Assert.assertFalse(buffer.insert(new Fragment()));
		Assert.assertEquals(capacity, buffer.queue.size());

		button.press();
		buffer.doStep();
		button.doStep();

		Logger.getLogger(this.getClass()).info("Testing the buffer");
		Assert.assertEquals(1, storage.queue.size());
		Assert.assertEquals(capacity - 1, buffer.queue.size());

		Assert.assertTrue(buffer.canAccept());
		Assert.assertTrue(buffer.insert(new Fragment()));
		Assert.assertEquals(capacity, buffer.queue.size());
	}

}
