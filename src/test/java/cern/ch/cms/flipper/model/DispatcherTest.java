package cern.ch.cms.flipper.model;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class DispatcherTest {

	/**
	 * <pre>
	 * <code>
	 * .................
	 * ....|......|.....  <-link 1-2
	 * ...bufu...bufu...
	 * .................
	 * </code>
	 * </pre>
	 */
	@Test
	public void test() {

		FlipperObject link1 = new Link("[link-left]", 1, 25, new SoundPlayer("sp"));
		FlipperObject link2 = new Link("[link-left]", 1, 25, new SoundPlayer("sp"));

		FlipperObject bufu1 = new BUFU("[test-bufu-1]", 5, 25, new Button("b1"), new SoundPlayer("sp"));
		FlipperObject bufu2 = new BUFU("[test-bufu-2]", 5, 25, new Button("b1"), new SoundPlayer("sp"));

		Dispatcher dispatcher = new Dispatcher(Arrays.asList(bufu1, bufu2), Arrays.asList(link1, link2),
				new SoundPlayer("sp"));

		Assert.assertEquals("Should find first link", 0, dispatcher.findAvailableTarget());
		Assert.assertEquals("Should return last result", 0, dispatcher.findAvailableTarget());
		

		dispatcher.getTarget(dispatcher.findAvailableTarget()).setBusy(true);
		dispatcher.invalidate();

		Assert.assertEquals("Should find second link, first busy", 1, dispatcher.findAvailableTarget());
		Assert.assertEquals("Should return last result", 1, dispatcher.findAvailableTarget());

		bufu1.setBusy(false);
		dispatcher.invalidate();

		Assert.assertEquals("Should find first link again", 0, dispatcher.findAvailableTarget());
		Assert.assertEquals("Should return last result", 0, dispatcher.findAvailableTarget());

	}

	@Test
	public void backpressure() {

		FlipperObject link1 = new Link("[link-left]", 1, 25, new SoundPlayer("sp"));
		FlipperObject link2 = new Link("[link-left]", 1, 25, new SoundPlayer("sp"));

		FlipperObject bufu1 = new BUFU("[test-bufu-1]", 5, 25, new Button("b1"), new SoundPlayer("sp"));
		FlipperObject bufu2 = new BUFU("[test-bufu-2]", 5, 25, new Button("b1"), new SoundPlayer("sp"));

		Dispatcher dispatcher = new Dispatcher(Arrays.asList(bufu1, bufu2), Arrays.asList(link1, link2),
				new SoundPlayer("sp"));

		Assert.assertEquals("Should find first link", link1, dispatcher.getLink(dispatcher.findAvailableTarget()));
		Assert.assertEquals("Should return last result", link1, dispatcher.getLink(dispatcher.findAvailableTarget()));
		Assert.assertFalse("Should not be backpressure", dispatcher.isBackpressure());

		dispatcher.getTarget(dispatcher.findAvailableTarget()).setBusy(true);
		dispatcher.invalidate();

		Assert.assertEquals("Should find second link, first busy", link2,
				dispatcher.getLink(dispatcher.findAvailableTarget()));
		Assert.assertEquals("Should return last result", link2, dispatcher.getLink(dispatcher.findAvailableTarget()));
		Assert.assertFalse("Should not be backpressure", dispatcher.isBackpressure());
		dispatcher.getTarget(dispatcher.findAvailableTarget()).setBusy(true);
		dispatcher.invalidate();

		Assert.assertEquals("Should NOT find any path", -1, dispatcher.findAvailableTarget());
		Assert.assertTrue("Should be backpressure", dispatcher.isBackpressure());

	}

}
