

public class Pair {

	private final String left;
	private final String right;

	public Pair(String left, String right) {
		this.left = left;
		this.right = right;
	}

	public static Pair of(String left, String right) {
		return new Pair(left, right);
	}

	public String getLeft() {
		return left;
	}

	public String getRight() {
		return right;
	}

}
