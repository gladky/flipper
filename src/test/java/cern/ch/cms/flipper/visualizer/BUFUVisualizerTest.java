package cern.ch.cms.flipper.visualizer;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class BUFUVisualizerTest {

	@Test
	public void test() {

		BUFUVisualizer bufuVisualizer = new BUFUVisualizer();

		Assert.assertEquals(Arrays.asList(false, false, false, false, false, false, false, false, false),
				bufuVisualizer.visualize(0));

		for (int i = 0; i <= 100; i++) {
			printMe(bufuVisualizer.visualize(i));
		}
	}

	private void printMe(List<Boolean> results) {

		for (Boolean result : results) {
			System.out.print(result ? "#" : " ");
		}
		System.out.println();

	}

}
