package cern.ch.cms.flipper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.model.FlipperObject;
import cern.ch.cms.flipper.model.Link;
import cern.ch.cms.flipper.model.Storage;
import cern.ch.cms.flipper.model.Switch;

public class FlowObserver {

	private static final int MIN = 2;
	private static final int WIDTH = 4;
	private static final int SWITCH_WIDTH = 11;
	private static final int STORAGE_WIDTH = 14;
	private static final String empty = "";

	private List<FlipperObject> observedObjects;

	private List<Map<String, String>> states;

	private static final Logger logger = Logger.getLogger(FlowObserver.class);

	private Map<Integer, Integer> lengths;

	public FlowObserver(FlipperGame flipperGame) {
		this.states = new ArrayList<Map<String, String>>();
		this.observedObjects = new ArrayList<FlipperObject>();

		// observedObjects.add(flipperGame.getBuffer1());
		// observedObjects.add(flipperGame.getBuffer2());
		// observedObjects.add(flipperGame.getBuffer3());
		// observedObjects.add(flipperGame.getBuffer4());

		observedObjects.add(flipperGame.link21);
		observedObjects.add(flipperGame.link22);
		observedObjects.add(flipperGame.link23);
		observedObjects.add(flipperGame.link24);

		observedObjects.add(flipperGame.getSwitch());

		observedObjects.add(flipperGame.link31);
		observedObjects.add(flipperGame.link32);
		observedObjects.add(flipperGame.link33);
		observedObjects.add(flipperGame.link34);
		observedObjects.add(flipperGame.link35);
		observedObjects.add(flipperGame.link36);

		observedObjects.add(flipperGame.getBufuL1());
		observedObjects.add(flipperGame.getBufuL2());
		observedObjects.add(flipperGame.getBufuL3());

		observedObjects.add(flipperGame.getBufuR1());
		observedObjects.add(flipperGame.getBufuR2());
		observedObjects.add(flipperGame.getBufuR3());

		observedObjects.add(flipperGame.link41);
		observedObjects.add(flipperGame.link42);
		observedObjects.add(flipperGame.link43);
		observedObjects.add(flipperGame.link44);
		observedObjects.add(flipperGame.link45);
		observedObjects.add(flipperGame.link46);
		observedObjects.add(flipperGame.link47);
		observedObjects.add(flipperGame.link48);

		observedObjects.add(flipperGame.getStorage());

		lengths = new HashMap<Integer, Integer>();

		for (int i = 0; i < observedObjects.size(); i++) {
			FlipperObject object = observedObjects.get(i);
			if (object instanceof Storage) {
				lengths.put(i, STORAGE_WIDTH);
			} else if (object instanceof Switch) {
				lengths.put(i, SWITCH_WIDTH);
			} else if (object instanceof Link) {
				lengths.put(i, MIN);
			} else {
				lengths.put(i, WIDTH);
			}
		}

	}

	public void persist() {
		Map<String, String> currentState = new LinkedHashMap<String, String>();

		for (FlipperObject observedObject : observedObjects) {
			logger.info("Persisting state of object, key: " + observedObject.getName());

			int elements = observedObject.getQueue().size();

			String data;

			if (elements == 0) {
				data = empty;
			} else if (elements == 1) {

				data = observedObject.getQueue().get(0).getName();
			} else {
				data = "";
				for (int e = 0; e < elements; e++) {
					if (e != 0 && !(observedObject instanceof Storage)) {
						data += ",";
					}
					data += observedObject.getQueue().get(e).getName();

				}
			}

			currentState.put(observedObject.getName(), data);

		}

		states.add(currentState);
		logger.info("Persisted " + currentState.size() + " objects");

	}

	public String toString() {

		StringBuilder sb = new StringBuilder();

		String stepHeading = fixedLengthString("step", WIDTH);

		sb.append("|");
		sb.append(stepHeading);
		sb.append("|");
		int h = 0;
		for (FlipperObject object : observedObjects) {
			String heading = fixedLengthString(object.getName(), lengths.get(h));

			sb.append(heading);
			sb.append("|");
			h++;

		}

		sb.append("\n");
		int i = 0;
		for (Map<String, String> state : states) {

			String step = fixedLengthString(i + "", WIDTH);

			sb.append("|");
			sb.append(step);
			// sb.append("|");

			sb.append(toString(state));
			sb.append("\n");
			i++;

		}

		return sb.toString();

	}

	private String toString(Map<String, String> row) {

		StringBuilder sb = new StringBuilder();
		sb.append("|");

		int i = 0;
		for (Entry<String, String> entry : row.entrySet()) {

			String curr = fixedLengthString(entry.getValue(), lengths.get(i));

			sb.append(curr);
			sb.append("|");
			i++;
		}

		return sb.toString();

	}

	public static String fixedLengthString(String string, int length) {

		if (string.length() > length) {
			string = string.substring(0, length - 1);
			string += "*";
		}
		return String.format("%1$" + length + "s", string);
	}

}