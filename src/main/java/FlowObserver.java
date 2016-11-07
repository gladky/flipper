

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class FlowObserver {

	private final int MIN_WIDTH;
	private final int WIDTH;
	private final int SWITCH_WIDTH;
	private final int STORAGE_WIDTH;
	private final int SOUND_WIDTH;
	private final int BUFFER_WIDTH;
	protected static final String empty = "";

	private List<NamedObject> observedObjects;

	private List<Map<String, String>> states;

	private Map<Integer, Integer> lengths;

	public FlowObserver(FlipperGame flipperGame, int minWidth, int width, int switchWidth, int storageWidth,
			int soundWidth, int bufferWidth) {

		this.MIN_WIDTH = minWidth;
		this.WIDTH = width;
		this.SWITCH_WIDTH = switchWidth;
		this.STORAGE_WIDTH = storageWidth;
		this.SOUND_WIDTH = soundWidth;
		this.BUFFER_WIDTH = bufferWidth;
		this.states = new ArrayList<Map<String, String>>();
		this.observedObjects = new ArrayList<NamedObject>();

		observedObjects.add(flipperGame.link11);
		observedObjects.add(flipperGame.link12);
		observedObjects.add(flipperGame.link13);
		observedObjects.add(flipperGame.link14);
		observedObjects.add(flipperGame.getBuffer1());
		observedObjects.add(flipperGame.getBuffer2());
		observedObjects.add(flipperGame.getBuffer3());
		observedObjects.add(flipperGame.getBuffer4());
		observedObjects.add(flipperGame.dispatcher);
		observedObjects.add(flipperGame.buttonL1);

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
		observedObjects.add(flipperGame.buttonHLT_L1);
		observedObjects.add(flipperGame.getBufuL2());
		observedObjects.add(flipperGame.buttonHLT_L2);
		observedObjects.add(flipperGame.getBufuL3());
		observedObjects.add(flipperGame.buttonHLT_L3);

		observedObjects.add(flipperGame.getBufuR1());
		observedObjects.add(flipperGame.buttonHLT_R1);
		observedObjects.add(flipperGame.getBufuR2());
		observedObjects.add(flipperGame.buttonHLT_R2);
		observedObjects.add(flipperGame.getBufuR3());
		observedObjects.add(flipperGame.buttonHLT_R3);

		observedObjects.add(flipperGame.link41);
		observedObjects.add(flipperGame.link42);
		observedObjects.add(flipperGame.link43);
		observedObjects.add(flipperGame.link44);
		observedObjects.add(flipperGame.link45);
		observedObjects.add(flipperGame.link46);
		observedObjects.add(flipperGame.link47);
		observedObjects.add(flipperGame.link48);

		observedObjects.add(flipperGame.getStorage());
		observedObjects.add(flipperGame.getSoundPlayer());

		lengths = new HashMap<Integer, Integer>();

		for (int i = 0; i < observedObjects.size(); i++) {
			Object object = observedObjects.get(i);
			if (object instanceof FlipperObject) {
				if (object instanceof Storage) {
					lengths.put(i, STORAGE_WIDTH);
				} else if (object instanceof Switch) {
					lengths.put(i, SWITCH_WIDTH);
				} else if (object instanceof Link) {
					lengths.put(i, MIN_WIDTH);
				} else if (object instanceof Buffer) {
					lengths.put(i, BUFFER_WIDTH);
				} else {
					lengths.put(i, WIDTH);
				}
			} else {
				if (object instanceof Button) {
					lengths.put(i, MIN_WIDTH);
				} else if (object instanceof SoundPlayer) {
					lengths.put(i, SOUND_WIDTH);
				} else {

					lengths.put(i, WIDTH);
				}
			}
		}

	}

	protected abstract String getState(Link link);

	protected abstract String getState(BUFU bufu);

	protected abstract String getState(Buffer buffer);

	protected abstract String getState(Storage storage);

	protected String getState(Switch switch_) {
		if (switch_.getQueue().queue.size() == 0) {
			return "";
		} else {
			return switch_.getQueue().queue.get(3).getName() + "-" + switch_.getQueue().queue.get(0).getName();
		}

	}

	private Pair getState(FlipperObject observedObject) {

		String data;
		if (observedObject instanceof Link) {
			data = getState((Link) observedObject);
		} else if (observedObject instanceof Buffer) {
			data = getState((Buffer) observedObject);
		} else if (observedObject instanceof BUFU) {
			data = getState((BUFU) observedObject);
		} else if (observedObject instanceof Storage) {
			data = getState((Storage) observedObject);
		} else if (observedObject instanceof Switch) {
			data = getState((Switch) observedObject);
		} else {
			data = "???";
		}

		return Pair.of(observedObject.getName(), data);

	}

	private Pair getState(Button observedButtonObject) {

		String name = observedButtonObject.getName();
		String enabled = observedButtonObject.isEnabled() ? "E" : " ";
		String pressed = observedButtonObject.isPressed() ? "P" : " ";
		return Pair.of(name, enabled + pressed);
	}

	public void persist() {
		Map<String, String> currentState = new LinkedHashMap<String, String>();

		for (NamedObject observedObject : observedObjects) {

			Pair result = null;

			if (observedObject instanceof FlipperObject) {
				FlipperObject observedFlipperObject = (FlipperObject) observedObject;
				result = getState(observedFlipperObject);
			} else if (observedObject instanceof Button) {
				Button observedButtonObject = (Button) observedObject;
				result = getState(observedButtonObject);
			} else if (observedObject instanceof Dispatcher) {
				Dispatcher dispatcher = (Dispatcher) observedObject;
				int target = dispatcher.getResult();
				String data = "";
				if (target != -1) {
					data += target;
				}
				if (dispatcher.isBackpressure()) {
					data += "BP";
				}

				result = Pair.of(dispatcher.getName(), data);
			} else if (observedObject instanceof SoundPlayer) {
				SoundPlayer soundPlayer = (SoundPlayer) observedObject;
				String data = "";
				for (int i = 0; i < soundPlayer.getSounds().size(); i++) {
					if (i != 0) {
						data += ",";
					}
					data += Sound.getById(soundPlayer.getSounds().get(i)).getId();
				}

				result = Pair.of(soundPlayer.getName(), data);
			} else {
				result = Pair.of("X", "?");
			}

			currentState.put(result.getLeft(), result.getRight());

		}

		states.add(currentState);

	}

	public String toString() {

		StringBuilder sb = new StringBuilder();

		String stepHeading = fixedLengthString("step", WIDTH);

		sb.append("|");
		sb.append(stepHeading);
		sb.append("|");
		int h = 0;
		for (NamedObject object : observedObjects) {
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