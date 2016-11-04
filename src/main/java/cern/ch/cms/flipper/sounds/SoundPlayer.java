package cern.ch.cms.flipper.sounds;

import java.util.ArrayList;
import java.util.List;

import cern.ch.cms.flipper.model.NamedObject;

public class SoundPlayer extends NamedObject {

	public SoundPlayer(String name) {
		super(name);
		sounds = new ArrayList<Integer>();
	}

	private final List<Integer> sounds;

	public void register(int soundId) {
		sounds.add(soundId);
	}

	public List<Integer> getSounds() {
		return sounds;

	}

	public void flush() {
		sounds.clear();
	}

	public void register(Sound sound) {
		register(sound.getId());
	}

}
