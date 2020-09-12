package json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * JSON Array representing by [] in JSON. Content a List of IJsonField
 * Implements Iterable for implicit for and foreach
 *
 */
public class JsonArray extends AJsonField implements Iterable<IJsonField> {

	private List<IJsonField> listObject = new ArrayList<>();

	public void addField(IJsonField aField) {
		listObject.add(aField);
	}
	
	/**
	 * Get Object at index
	 * 
	 * @param aIndex
	 * @return
	 * @throws TechnicalException
	 */
	public IJsonField get(int aIndex) throws IllegalArgumentException {
		if (aIndex >= listObject.size()) {
			throw new IllegalArgumentException("Index is out of range for JsonArray : " + getString());
		}
		return listObject.get(aIndex);
	}

	/**
	 * Return size of listObject
	 * 
	 * @return
	 */
	public int size() {
		return listObject.size();
	}

	@Override
	public Iterator<IJsonField> iterator() {
		return listObject.iterator();
	}

	@Override
	public Type getType() {
		return IJsonField.Type.ARRAY;
	}

	@Override
	public IJsonField getField(String aFieldName) throws IllegalArgumentException {
		throw new IllegalArgumentException("Json getField on JsonArray");
	}
}
