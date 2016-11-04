package cern.ch.cms.flipper.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Fragment;
import cern.ch.cms.flipper.sounds.SoundPlayer;

/**
 * <pre>
 * ..............
 * ...switch.....
 * .../....\.....   <-- link 1-2
 * .bufu1..bufu2.
 * ..............
 * </pre>
 * 
 * @author Maciej Gladki (maciej.szymon.gladki@cern.ch)
 *
 */
public class SwitchToBufusTest {

	FlipperObject switchSpy;

	FlipperObject link1;
	FlipperObject link2;

	FlipperObject bufu1;
	FlipperObject bufu2;
	FlipperObject[] objects;

	@Before
	public void initializeFlipperObjects() {
		switchSpy = Mockito.spy(new Switch("[test-switch]", new SoundPlayer("sp")));

		link1 = new Link("[test-link-1]", 1, 25, new SoundPlayer("sp"));
		link2 = new Link("[test-link-2]", 1, 25, new SoundPlayer("sp"));

		bufu1 = Mockito.spy(new BUFU("[test-bufu-1]", 10, 25, new Button("[test-button-1]"), new SoundPlayer("sp")));
		bufu2 = Mockito.spy(new BUFU("[test-bufu-2]", 10, 25, new Button("[test-button-2]"), new SoundPlayer("sp")));

		switchSpy.getSuccessors().add(link1);
		switchSpy.getSuccessors().add(link2);
		link1.getSuccessors().add(bufu1);
		link2.getSuccessors().add(bufu2);

		FlipperObject[] newobjects = { bufu1, bufu2, link1, link2, switchSpy };
		objects = newobjects;
	}

	@Test
	public void switchToBufuTest() {

		insertNewFragmentsToSwitch(bufu1);

		Mockito.verify(switchSpy, Mockito.times(0)).sendData();
		Mockito.verify(bufu1, Mockito.times(0)).canAccept();
		Mockito.verify(bufu1, Mockito.times(0)).insert(Mockito.any(Data.class));
		doSteps(objects);
		Mockito.verify(switchSpy, Mockito.times(1)).sendData();
		Mockito.verify(bufu1, Mockito.times(2)).canAccept();
		Mockito.verify(bufu1, Mockito.times(1)).insert(Mockito.any(Data.class));

		doSteps(objects);
		Mockito.verify(bufu1, Mockito.times(1)).insert(Mockito.any(Data.class));
		doSteps(objects);
		Mockito.verify(bufu1, Mockito.times(1)).insert(Mockito.any(Data.class));
		doSteps(objects);
		Mockito.verify(bufu1, Mockito.times(1)).insert(Mockito.any(Data.class));
		doSteps(objects);
		Mockito.verify(bufu1, Mockito.times(1)).insert(Mockito.any(Data.class));
	}

	@Test
	public void twoEventsTest() {
		insertNewFragmentsToSwitch(bufu1);

		Mockito.verify(switchSpy, Mockito.times(0)).sendData();
		Mockito.verify(bufu1, Mockito.times(0)).canAccept();
		Mockito.verify(bufu1, Mockito.times(0)).insert(Mockito.any(Data.class));
		doSteps(objects);
		Mockito.verify(switchSpy, Mockito.times(1)).sendData();
		Mockito.verify(bufu1, Mockito.times(2)).canAccept();
		Mockito.verify(bufu2, Mockito.times(0)).canAccept();
		Mockito.verify(bufu1, Mockito.times(1)).insert(Mockito.any(Data.class));

		insertNewFragmentsToSwitch(bufu2);
		doSteps(objects);

		Mockito.verify(bufu1, Mockito.times(2)).canAccept();
		Mockito.verify(bufu2, Mockito.times(2)).canAccept();
	}

	private void insertNewFragmentsToSwitch(FlipperObject target) {
		Data f1 = new Fragment();
		Data f2 = new Fragment();
		Data f3 = new Fragment();
		Data f4 = new Fragment();
		f1.setTarget(target);
		f2.setTarget(target);
		f3.setTarget(target);
		f4.setTarget(target);

		switchSpy.insert(f1);
		switchSpy.insert(f2);
		switchSpy.insert(f3);
		switchSpy.insert(f4);
	}

	private void doSteps(FlipperObject... objects) {
		for (FlipperObject object : objects) {
			object.doStep();
		}
	}
}
