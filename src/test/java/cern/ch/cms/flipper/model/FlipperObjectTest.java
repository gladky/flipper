package cern.ch.cms.flipper.model;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Fragment;

public class FlipperObjectTest {

	@Test
	public void acceptTest() {
		FlipperObject link = new Link("test-link", 1, 25);
		Data f1 = new Fragment();
		link.insert(f1);
		Assert.assertEquals(false, link.canAccept());
		Assert.assertEquals(0, link.getProgress());

		link.doStep();
		Assert.assertEquals(25, link.getProgress());
		Assert.assertEquals(false, link.canAccept());

		link.doStep();
		Assert.assertEquals(50, link.getProgress());
		Assert.assertEquals(false, link.canAccept());

		link.doStep();
		Assert.assertEquals(75, link.getProgress());
		Assert.assertEquals(false, link.canAccept());

		link.doStep();
		Assert.assertEquals(0, link.getProgress());
		Assert.assertEquals(true, link.canAccept());

		link.doStep();
	}

	@Test
	public void forceInsertTest() {
		FlipperObject link = new Link("test-link", 1, 25);
		Data f1 = new Fragment();
		Data f2 = new Fragment();

		boolean r1 = link.insert(f1);
		Assert.assertEquals(true, r1);
		Assert.assertEquals(f1, link.queue.get(0));

		boolean r2 = link.insert(f2);
		Assert.assertEquals(false, r2);
		Assert.assertEquals(f1, link.queue.get(0));

	}

	
}
