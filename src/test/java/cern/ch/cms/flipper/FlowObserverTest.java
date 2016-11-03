package cern.ch.cms.flipper;

import org.apache.log4j.Logger;
import org.junit.Test;

public class FlowObserverTest {

	private static final Logger logger = Logger.getLogger(FlowObserver.class);

	@Test
	public void test() {

		FlipperGame flipperGame = new FlipperGame();

		FlowObserver observer = new FlowObserver(flipperGame);

		logger.info(observer.fixedLengthString("abc", 3));
		logger.info(observer.fixedLengthString("a", 3));
		logger.info(observer.fixedLengthString("abcd", 3));
	}

}
