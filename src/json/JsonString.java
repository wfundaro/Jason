package json;

/**
 * a JSON Object representing by just a "key" : "string" in JSON
 *
 */
public class JsonString extends AJsonField {

	private String value;

	public JsonString(String aString) {
		value = aString;
	}

	@Override
	public String getString() {
		return value;
	}

	@Override
	public Type getType() {
		return IJsonField.Type.STRING;
	}

	@Override
	public IJsonField getField(String aFieldName) {
		throw new IllegalArgumentException("Json getField on JsonString");
	}

}
