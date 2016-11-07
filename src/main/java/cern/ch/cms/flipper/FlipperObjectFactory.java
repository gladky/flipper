package cern.ch.cms.flipper;

import java.util.List;

import org.apache.log4j.Logger;

import cern.ch.cms.flipper.controllers.Button;
import cern.ch.cms.flipper.model.BUFU;
import cern.ch.cms.flipper.model.Buffer;
import cern.ch.cms.flipper.model.Dispatcher;
import cern.ch.cms.flipper.model.FlipperObject;
import cern.ch.cms.flipper.model.Link;
import cern.ch.cms.flipper.model.Storage;
import cern.ch.cms.flipper.model.Switch;
import cern.ch.cms.flipper.sounds.SoundPlayer;

/**
 * Factory of flipper components
 * 
 * @author Maciej Gladki (maciej.szymon.gladki@cern.ch)
 *
 */
public class FlipperObjectFactory {

	private static int counter = 0;

	private final GameController controller;

	private final int DEFAULT_BUFFER_PROCESSING_STEP = 10;
	private final int DEFAULT_BUFFER_TIMEOUT_STEP = 25;

	private final int DEFAULT_BUFU_PROCESSING_STEP = 10;
	private final int DEFAULT_BUFU_TIMEOUT_STEP = 25;

	private int bufferProcessingStep;
	private int bufferTimeoutStep;

	private int bufuProcessingStep;
	private int bufuTimeoutStep;

	private SoundPlayer soundPlayer;

	private static final Logger logger = Logger.getLogger(FlipperObjectFactory.class);

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

		logger.debug("Calculated step fore link " + name + " is " + calculatedStep);

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

	@Deprecated
	private String getUniqueName2(String name) {
		return "[" + String.format("%02d", counter++) + ": " + name + "]";
	}

	private String getShortName(String name) {
		counter++;
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
		Buffer buffer = new Buffer(uniqueName, 12, bufferProcessingStep, bufferTimeoutStep, button, soundPlayer, soundMasked);
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
		Button button = new Button(uniqueName);
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
