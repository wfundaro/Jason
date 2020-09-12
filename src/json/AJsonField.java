package json;

/**
 * AbstractClass for JSON parent attribute
 *
 */
public abstract class AJsonField implements IJsonField{

	protected IJsonField parent;

	public IJsonField getParent() {
		return parent;
	}

	public void setParent(IJsonField parent) {
		this.parent = parent;
	}
	
}
