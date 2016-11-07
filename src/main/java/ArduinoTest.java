

public class ArduinoTest {

	public static void main(String[] args) {

		FlipperGame flipperGame = new FlipperGame();

		for (int i = 0; i < 250; i++) {
			if (i % 4 == 0 && i < 200) {
				flipperGame.generateNewFragments();

			}
			flipperGame.pressButtonLevel1();
			flipperGame.pressButtonHLT_L1();
			flipperGame.pressButtonHLT_L2();
			flipperGame.pressButtonHLT_L3();
			flipperGame.pressButtonHLT_R1();
			flipperGame.pressButtonHLT_R2();
			flipperGame.pressButtonHLT_R3();

			flipperGame.doStep();
		}

		System.out.println(flipperGame.getController().observer.toString());
		System.out.println("There should be x events in storage: " + flipperGame.getStorage().getQueue().size());

	}

}
