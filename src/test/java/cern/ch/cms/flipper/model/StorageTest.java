package cern.ch.cms.flipper.model;

import org.junit.Assert;
import org.junit.Test;

import cern.ch.cms.flipper.event.Event;
import cern.ch.cms.flipper.sounds.SoundPlayer;

public class StorageTest {

	@Test
	public void test() {
		Storage storage = new Storage("Storage", 10,new SoundPlayer("sp"));

		for (int i = 0; i < 10; i++) {
			int progress = storage.getProgress()[0];
			Assert.assertEquals(i * 10, progress);
			storage.insert(new Event("e" + i));
		}

	}

}
