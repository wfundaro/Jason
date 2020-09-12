package json;

public interface IJsonField {
	
	public enum Type {
		STRING, OBJECT, ARRAY;
	}

	void setParent(IJsonField aParent);

	IJsonField getParent();

	IJsonField.Type getType();

	default String getString() throws IllegalAccessError{
		String result = null;
		if (this.getParent().getType() == IJsonField.Type.OBJECT) {
			JsonObject parent = (JsonObject) this.getParent();
			String res = parent.getStringKeyFromValue(this);
			if (res != null) {
				result = res + " is not a StringField this is an : " + this.getType();
			}
		} else {
			result = this.getParent().getString();
		}
		throw new IllegalAccessError(result);
	}

	IJsonField getField(String aFieldName);
}
