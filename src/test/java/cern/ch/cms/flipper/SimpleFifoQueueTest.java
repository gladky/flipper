package cern.ch.cms.flipper;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Fragment;

public class SimpleFifoQueueTest {

	private static final Logger logger = Logger.getLogger(SimpleFifoQueueTest.class);

	@Test
	public void test() {

		SimpleFifoQueue queue = new SimpleFifoQueue(3);

		Data f1 = new Fragment();
		Data f2 = new Fragment();
		Data f3 = new Fragment();
		Data f4 = new Fragment();

		Assert.assertEquals(0, queue.size());

		Assert.assertTrue(queue.add(f1));
		Assert.assertEquals(1, queue.size());
		logger.info("after 1 add " + queue.toString());

		Assert.assertTrue(queue.add(f2));
		Assert.assertEquals(2, queue.size());
		logger.info("after 2 add " + queue.toString());

		Assert.assertTrue(queue.add(f3));
		Assert.assertEquals(3, queue.size());
		logger.info("after 3 add " + queue.toString());

		Assert.assertFalse(queue.add(f4));
		Assert.assertEquals(3, queue.size());
		logger.info("after 4 add " + queue.toString());

		Assert.assertEquals(f1, queue.peek());
		Assert.assertEquals(f1, queue.peek());
		Assert.assertEquals(f1, queue.poll());
		logger.info("after 1 poll " + queue.toString());
		Assert.assertEquals(f2, queue.poll());
		logger.info("after 2 poll " + queue.toString());
		Assert.assertEquals(f3, queue.poll());
		logger.info("after 3 poll " + queue.toString());

	}

}
