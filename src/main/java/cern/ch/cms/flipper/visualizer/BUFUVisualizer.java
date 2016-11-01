package cern.ch.cms.flipper.visualizer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BUFUVisualizer extends FlipperObjectVisualizer {

	/* animation steps */
	private int steps = 28;

	private Map<Integer, List<Boolean>> states;

	private Map<Integer, Integer> mapping;

	public BUFUVisualizer() {
		this.states = new HashMap<Integer, List<Boolean>>();
		this.mapping = new HashMap<Integer, Integer>();

		states.put(0, Arrays.asList(false, false, false, false, false, false, false, false, false));
		states.put(1, Arrays.asList(true, false, false, false, false, false, false, false, false));
		states.put(2, Arrays.asList(true, true, false, false, false, false, false, false, false));
		states.put(3, Arrays.asList(true, true, true, false, false, false, false, false, false));
		states.put(4, Arrays.asList(true, true, true, true, false, false, false, false, false));
		states.put(5, Arrays.asList(false, true, true, true, true, false, false, false, false));
		states.put(6, Arrays.asList(false, false, true, true, true, true, false, false, false));
		states.put(7, Arrays.asList(false, false, false, true, true, true, true, false, false));
		states.put(8, Arrays.asList(false, false, false, false, true, true, true, true, false));
		states.put(9, Arrays.asList(true, false, false, false, false, true, true, true, false));
		states.put(10, Arrays.asList(true, true, false, false, false, false, true, true, false));
		states.put(11, Arrays.asList(true, true, true, false, false, false, false, true, false));
		states.put(28, Arrays.asList(true, true, true, true, true, true, true, true, true));

		/* repeat animation loop 2 */
		mapping.put(12, 4);
		mapping.put(13, 5);
		mapping.put(14, 6);
		mapping.put(15, 7);
		mapping.put(16, 8);
		mapping.put(17, 9);
		mapping.put(18, 10);
		mapping.put(19, 11);

		/* repeat animation loop 3 */
		mapping.put(20, 4);
		mapping.put(21, 5);
		mapping.put(22, 6);
		mapping.put(23, 7);
		mapping.put(24, 8);
		mapping.put(25, 9);
		mapping.put(26, 10);
		mapping.put(27, 11);

	}

	public List<Boolean> visualize(int progress) {
		int state = (int) (progress * ((steps + 1) / 100f));
		// System.out.println("From progress: " + progress + " -> " + state);

		if (mapping.containsKey(state)) {
			state = mapping.get(state);
			// System.out.println("Getting mapping: " + state);
		}

		if (state > 28) {
			state = 28;
			// System.out.println("Limiting to max: " + state);
		}

		return states.get(state);
	}
}
