

import java.util.List;

/**
 * Factory of flipper components
 * 
 * @author Maciej Gladki (maciej.szymon.gladki@cern.ch)
 *
 */
public class FlipperObjectFactory {

	private final GameController controller;

	private final int DEFAULT_BUFFER_PROCESSING_STEP = 8;
	private final int DEFAULT_BUFFER_TIMEOUT_STEP = 25;

	private final int DEFAULT_BUFU_PROCESSING_STEP = 10;
	private final int DEFAULT_BUFU_TIMEOUT_STEP = 25;

	private int bufferProcessingStep;
	private int bufferTimeoutStep;

	private int bufuProcessingStep;
	private int bufuTimeoutStep;

	private SoundPlayer soundPlayer;

	public FlipperObjectFactory() {

		this.controller = new GameController();

		bufferProcessingStep = DEFAULT_BUFFER_PROCESSING_STEP;
		bufferTimeoutStep = DEFAULT_BUFFER_TIMEOUT_STEP;

		bufuProcessingStep = DEFAULT_BUFU_PROCESSING_STEP;
		bufuTimeoutStep = DEFAULT_BUFU_TIMEOUT_STEP;

		soundPlayer = new SoundPlayer("SP");
		this.controller.setSoundPlayer(soundPlayer);
	}

	/**
	 * Creates link
	 * 
	 * @param name
	 *            name of the link
	 * @param logicalLength
	 *            logica length of the link - most of the time its proportional
	 *            to number of leds but note that (1) we want the constant time
	 *            for different physical length on BUFFER to SWITCH links (2) we
	 *            want the constant speed for BUFU to storage links
	 * @return
	 */
	public FlipperObject createLink(String name, int logicalLength) {
		int calculatedStep = 100 / logicalLength;
		if (calculatedStep < 1) {
			calculatedStep = 1;
		} else if (calculatedStep > 50) {
			calculatedStep = 50;
		}

		String uniqueName = getShortName(name);
		FlipperObject link = new Link(uniqueName, 1, calculatedStep, soundPlayer);
		controller.getFlipperObjects().add(link);
		return link;
	}

	public FlipperObject createBUFU(String name, Button button) {
		String uniqueName = getShortName(name);
		FlipperObject bufu = new BUFU(uniqueName, bufuProcessingStep, bufuTimeoutStep, button, soundPlayer);
		controller.getFlipperObjects().add(bufu);
		return bufu;
	}

	public FlipperObject createSwitch(String name) {
		String uniqueName = getShortName(name);
		FlipperObject switch_ = new Switch(uniqueName, soundPlayer);
		controller.getFlipperObjects().add(switch_);
		return switch_;
	}

	private String getShortName(String name) {
		return name;
	}

	public FlipperObject createStorage() {

		String uniqueName = getShortName("Storage");
		FlipperObject storage = new Storage(uniqueName, 40, soundPlayer);
		controller.getFlipperObjects().add(storage);
		return storage;
	}

	public Buffer createBuffer(String name, Button button, boolean soundMasked) {

		String uniqueName = getShortName(name);
		Buffer buffer = new Buffer(uniqueName, 12, bufferProcessingStep, bufferTimeoutStep, button, soundPlayer,
				soundMasked);
		controller.getFlipperObjects().add(buffer);
		return buffer;

	}

	public Dispatcher createDispatcher(List<FlipperObject> bufus, List<FlipperObject> links) {
		Dispatcher dispatcher = new Dispatcher(bufus, links, soundPlayer);
		controller.setDispatcher(dispatcher);
		return dispatcher;
	}

	public Button createButton(String name) {
		String uniqueName = getShortName(name);
		Button button = new Button(uniqueName, soundPlayer);
		controller.getButtons().add(button);
		return button;
	}

	public GameController getController() {
		return controller;
	}

	public void setBufferProcessingStep(int bufferProcessingStep) {
		this.bufferProcessingStep = bufferProcessingStep;
	}

	public void setBufferTimeoutStep(int bufferTimeoutStep) {
		this.bufferTimeoutStep = bufferTimeoutStep;
	}

	public void setBufuProcessingStep(int bufuProcessingStep) {
		this.bufuProcessingStep = bufuProcessingStep;
	}

	public void setBufuTimeoutStep(int bufuTimeoutStep) {
		this.bufuTimeoutStep = bufuTimeoutStep;
	}

	public SoundPlayer getSoundPlayer() {
		return soundPlayer;
	}
}