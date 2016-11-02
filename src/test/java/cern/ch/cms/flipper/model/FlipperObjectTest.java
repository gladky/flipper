package cern.ch.cms.flipper.model;

import org.junit.Assert;
import org.junit.Test;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Event;
import cern.ch.cms.flipper.event.Fragment;

public class FlipperObjectTest {

	@Test
	public void acceptTest() {
		FlipperObject link = new Link("test-link", 1, 25);
		FlipperObject storage = new Storage("[storage]", 10);
		link.getSuccessors().add(storage);

		Data f1 = new Fragment();
		link.insert(f1);
		Assert.assertEquals(false, link.canAccept());
		Assert.assertEquals(1, link.getProgress().length);
		Assert.assertEquals(0, link.getProgress()[0]);

		link.doStep();
		Assert.assertEquals(1, link.getProgress().length);
		Assert.assertEquals(25, link.getProgress()[0]);
		Assert.assertEquals(false, link.canAccept());

		link.doStep();
		Assert.assertEquals(1, link.getProgress().length);
		Assert.assertEquals(50, link.getProgress()[0]);
		Assert.assertEquals(false, link.canAccept());

		link.doStep();
		Assert.assertEquals(1, link.getProgress().length);
		Assert.assertEquals(75, link.getProgress()[0]);
		Assert.assertEquals(false, link.canAccept());

		link.doStep();
		Assert.assertEquals(1, link.getProgress().length);
		Assert.assertEquals(0, link.getProgress()[0]);
		Assert.assertEquals(true, link.canAccept());

		link.doStep();
	}

	@Test
	public void forceInsertTest() {
		FlipperObject link = new Link("test-link", 1, 25);
		FlipperObject storage = new Storage("[storage]", 10);
		link.getSuccessors().add(storage);
		Data f1 = new Fragment();
		Data f2 = new Fragment();

		boolean r1 = link.insert(f1);
		Assert.assertEquals(true, r1);
		Assert.assertEquals(f1, link.queue.get(0));

		boolean r2 = link.insert(f2);
		Assert.assertEquals(false, r2);
		Assert.assertEquals(f1, link.queue.get(0));

	}

	@Test
	public void linksDontWorkAsBuffers() {
		Button button = new Button("[test-button]");
		FlipperObject link = new Link("[test-link]", 1, 25);
		FlipperObject bufu = new BUFU("[test-bufu]", 20, 25, button);
		link.getSuccessors().add(bufu);

		FlipperObject[] objects = { bufu, link };

		Data data = new Event("e1");
		Assert.assertEquals("link is empty, will accept", true, link.canAccept());
		link.insert(data);
		Assert.assertEquals("link is progressing, will not accept", false, link.canAccept());

		doSteps(objects);
		Assert.assertEquals("link is progressing, will not accept", false, link.canAccept());

		doSteps(objects);
		Assert.assertEquals("link is progressing, will not accept", false, link.canAccept());

		doSteps(objects);
		Assert.assertEquals("link is progressing, will not accept", false, link.canAccept());
		Assert.assertEquals("last step of link processing", 75, link.getProgress()[0]);

		doSteps(objects);
		Assert.assertEquals("link is done here", 0, link.getProgress()[0]);
		Assert.assertEquals("but will not accept as buffer is still not done", false, link.canAccept());

		doSteps(objects);
		Assert.assertEquals("link is done already", 0, link.getProgress()[0]);
		Assert.assertEquals("bufu is progressing", 20, bufu.getProgress()[0]);
		Assert.assertEquals("link will not accept", false, link.canAccept());

		doSteps(objects);
		Assert.assertEquals("link is done already", 0, link.getProgress()[0]);
		Assert.assertEquals("bufu is progressing", 40, bufu.getProgress()[0]);
		Assert.assertEquals("link will not accept", false, link.canAccept());

		doSteps(objects);
		Assert.assertEquals("link is done already", 0, link.getProgress()[0]);
		Assert.assertEquals("bufu is progressing", 60, bufu.getProgress()[0]);
		Assert.assertEquals("link will not accept", false, link.canAccept());

		doSteps(objects);
		Assert.assertEquals("link is done already", 0, link.getProgress()[0]);
		Assert.assertEquals("bufu is progressing", 80, bufu.getProgress()[0]);
		Assert.assertEquals("link will not accept", false, link.canAccept());

		doSteps(objects);
		Assert.assertEquals("link is done already", 0, link.getProgress()[0]);
		Assert.assertEquals("bufu done here, but needs to release", 100, bufu.getProgress()[0]);
		Assert.assertEquals("link will still NOT accept, bufu needs to release", false, link.canAccept());

		button.press();
		doSteps(objects);
		Assert.assertEquals("link is done already", 0, link.getProgress()[0]);
		Assert.assertEquals("bufu released the event here", 0, bufu.getProgress()[0]);
		Assert.assertEquals("link will NOW accept", true, link.canAccept());

	}

	private void doSteps(FlipperObject... objects) {
		for (FlipperObject object : objects) {
			object.doStep();
		}
	}

}
