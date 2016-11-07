

public class NamedObject {

	protected final String name;

	public NamedObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "NamedObject [name=" + name + "]";
	}

}
