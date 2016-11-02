package cern.ch.cms.flipper;

import java.util.HashMap;
import java.util.Map;

public class NameGenerator {

	private static Map<String, String> nameMapper = new HashMap<String, String>();

	static {
		nameMapper.put("0", "A");
		nameMapper.put("4", "B");
		nameMapper.put("8", "C");
		nameMapper.put("12", "D");
		nameMapper.put("16", "E");
		nameMapper.put("20", "F");
		nameMapper.put("24", "G");
		nameMapper.put("28", "H");
		nameMapper.put("32", "I");
		nameMapper.put("36", "J");
		nameMapper.put("40", "K");
		nameMapper.put("44", "L");
		nameMapper.put("48", "M");
		nameMapper.put("52", "O");
		nameMapper.put("56", "P");
	}

	public static String generateSimpleName(String fragmentName) {
		if (nameMapper.containsKey(fragmentName)) {
			return nameMapper.get(fragmentName);
		} else {
			return "X";
		}
	}

}
