package json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * JSON Object representing by {} in JSON. Content a Map of IJsonField
 * Implements Iterable for implicit for and foreach
 */
public class JsonObject extends AJsonField implements Iterable<Entry<String, IJsonField>> {

	private Map<String, IJsonField> fields = new HashMap<>();

	public JsonObject() {
		setParent(this);
	}

	public void addField(String aKey, IJsonField aField) {
		fields.put(aKey, aField);
	}

	/**
	 * Return number of fields in this
	 * 
	 * @return
	 */
	public int size() {
		return fields.size();
	}

	/**
	 * Retrieve key name with this value in Map
	 * 
	 * @param aElem
	 * @return
	 */
	public String getStringKeyFromValue(IJsonField aElem) {
		for (Map.Entry<String, IJsonField> item : fields.entrySet()) {
			if (item.getValue() == aElem) {
				return item.getKey();
			}
		}
		return null;
	}

	/**
	 * Get JSON Field by this key name
	 */
	@Override
	public IJsonField getField(String aKey) {
		return fields.get(aKey);
	}

	/**
	 * Return Type Object representing by IJsonField.type
	 */
	@Override
	public Type getType() {
		return IJsonField.Type.OBJECT;
	}

	/**
	 * Iterable Override method
	 */
	@Override
	public Iterator<Entry<String, IJsonField>> iterator() {
		return fields.entrySet().iterator();
	}

}
