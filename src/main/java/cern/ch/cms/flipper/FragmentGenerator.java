package cern.ch.cms.flipper;

import cern.ch.cms.flipper.event.Data;
import cern.ch.cms.flipper.event.Fragment;
import cern.ch.cms.flipper.model.FlipperObject;

public class FragmentGenerator {

	private final FlipperObject link1;
	private final FlipperObject link2;
	private final FlipperObject link3;
	private final FlipperObject link4;

	private boolean val;

	public FragmentGenerator(FlipperObject link1, FlipperObject link2, FlipperObject link3, FlipperObject link4) {
		this.link1 = link1;
		this.link2 = link2;
		this.link3 = link3;
		this.link4 = link4;
		this.val = false;
	}

	public void generateAndInsertFragments() {
		/*
		 * fragment 1-3 are invisible, flag important is not relevant before
		 * switch component
		 */
		Data f1 = new Fragment(true);
		Data f2 = new Fragment(true);
		Data f3 = new Fragment(true);
		Data f4 = new Fragment(val);
		val = !val;

		link1.insert(f1);
		link2.insert(f2);
		link3.insert(f3);
		link4.insert(f4);
	}

}
