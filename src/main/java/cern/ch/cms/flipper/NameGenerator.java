package cern.ch.cms.flipper;

import java.util.HashMap;
import java.util.Map;

public class NameGenerator {

	private static Map<String, String> nameMapper = new HashMap<String, String>();

	static {
		nameMapper.put("0", "a");
		nameMapper.put("4", "b");
		nameMapper.put("8", "c");
		nameMapper.put("12", "d");
		nameMapper.put("16", "e");
		nameMapper.put("20", "f");
		nameMapper.put("24", "g");
		nameMapper.put("28", "h");
		nameMapper.put("32", "i");
		nameMapper.put("36", "j");
		nameMapper.put("40", "k");
		nameMapper.put("44", "l");
		nameMapper.put("48", "m");
		nameMapper.put("52", "n");
		nameMapper.put("56", "o");
		nameMapper.put("60", "p");
		nameMapper.put("64", "q");
		nameMapper.put("68", "r");
		nameMapper.put("72", "s");
		nameMapper.put("76", "t");
		nameMapper.put("80", "u");
		nameMapper.put("84", "v");
		nameMapper.put("88", "w");
		nameMapper.put("92", "x");
		nameMapper.put("96", "y");
		nameMapper.put("100", "z");
	}

	public static String generateSimpleName(String fragmentName) {
		if (nameMapper.containsKey(fragmentName)) {
			return nameMapper.get(fragmentName);
		} else {
			return "+";
		}
	}

}
