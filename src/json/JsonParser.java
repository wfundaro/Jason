package json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * JSON String parser Create an Object Tree from a string in JSON format
 *
 */
public class JsonParser {

	private JsonParser() {
	}

	/**
	 * add a String Field in Object Tree (IJsonField -> JsonString class)
	 * aForceInsert is in the case where string are empty but valid
	 * 
	 * @param aKey
	 * @param aValue
	 * @param aParent
	 * @param aForceInsert
	 */
	private static void addStringField(String aKey, String aValue, IJsonField aParent, boolean aForceInsert) {
		aValue = aValue.trim();
		if (aValue.length() > 0 || aForceInsert) {
			aValue = sanitizeQuoteString(aValue);
			IJsonField stringField = new JsonString(aValue);
			stringField.setParent(aParent);
			castAddField(aKey, stringField, aParent);
		}
	}

	/**
	 * add an array Field in Object Tree (IJsonField -> JsonArray class)
	 * 
	 * @param aKey
	 * @param aParent
	 * @return
	 */
	private static IJsonField addArrayField(String aKey, IJsonField aParent) {
		IJsonField arrayField = new JsonArray();
		castAddField(aKey, arrayField, aParent);
		arrayField.setParent(aParent);
		return arrayField;
	}

	/**
	 * add a Object Field in Object Tree (IJsonField -> JsonObject class)
	 * 
	 * @param aKey
	 * @param aParent
	 * @return
	 */
	private static IJsonField addObjectField(String aKey, IJsonField aParent) {
		IJsonField objectField = new JsonObject();
		castAddField(aKey, objectField, aParent);
		objectField.setParent(aParent);
		return objectField;
	}

	/**
	 * Add IJsonField in the parent array or object
	 * 
	 * @param aKey
	 * @param aObjectField
	 * @param aParent
	 */
	private static void castAddField(String aKey, IJsonField aObjectField, IJsonField aParent) {
		if (aParent.getType() == IJsonField.Type.ARRAY) {
			((JsonArray) aParent).addField(aObjectField);
		} else if (aParent.getType() == IJsonField.Type.OBJECT) {
			((JsonObject) aParent).addField(aKey, aObjectField);
		}
	}

	/**
	 * Parse JSON String in a Java Object Tree
	 * 
	 * @param aString
	 * @return
	 */
	public static JsonObject parse(String aString) {
		// The default main item is an JsonObject
		JsonObject jsonMainObject = new JsonObject();

		aString = aString.substring(aString.indexOf('{') + 1, aString.lastIndexOf('}')).replaceAll("\\r\\n", "");
		String patternKey = "\"\\w*\"\\s*:";
		Pattern pattern = Pattern.compile(patternKey);
		Matcher mKey = pattern.matcher(aString);
		List<String> keys = new ArrayList<>();

		// fill list with matching key
		while (mKey.find()) {
			String skey = mKey.group().replace(" ", "").substring(1);
			keys.add(skey.substring(0, skey.length() - 2));
		}

		// fill list with value
		List<String> values = Arrays.stream(pattern.split(aString)).skip(1).map(elem -> {
			elem = elem.trim();
			if (elem.length() > 0 && elem.substring(elem.length() - 1, elem.length()).equals(",")) {
				elem = (elem.substring(0, elem.length() - 1)).trim();
			}
			return elem;
		}).collect(Collectors.toList());

		// create Object Tree
		IJsonField currentParent = jsonMainObject;
		for (int index = 0; index < keys.size(); index++) {
			boolean isString = true;
			String value = values.get(index);
			if (countChar(value, '{') > 0 || countChar(value, '[') > 0
					|| countChar(value, '}') > 0) {
				StringBuilder string = new StringBuilder();
				boolean firstQuote = false;
				for (int iChar = 0; iChar < value.length(); iChar++) {
					switch (value.charAt(iChar)) {
					case '[':
						currentParent = addArrayField(keys.get(index), currentParent);
						break;
					case '{':
						currentParent = addObjectField(keys.get(index), currentParent);
						break;
					case ']':
					case '}':
						addStringField(keys.get(index), string.toString(), currentParent, false);
						string.setLength(0);
						currentParent = currentParent.getParent();
						break;
					case ',':
						if (!firstQuote) {
							addStringField(keys.get(index), string.toString(), currentParent, false);
							string.setLength(0);
						}
						break;
					case '"':
						if (firstQuote && iChar > 0 && value.charAt(iChar - 1) != '\\') {
							firstQuote = false;
							// Force insert in the case where string are empty but valid
							addStringField(keys.get(index), string.toString(), currentParent, true);
							string.setLength(0);
						} else {
							firstQuote = true;
						}
						break;
					default:
						string.append(value.charAt(iChar));
						break;
					}
				}
				isString = false;
			}
			if (isString) {
				addStringField(keys.get(index), values.get(index), currentParent, false);
			}
		}
		return jsonMainObject;
	}

	/**
	 * Remove " character
	 * 
	 * @param aString
	 * @return
	 */
	private static String sanitizeQuoteString(String aString) {
		aString = aString.trim();
		if (aString.indexOf('\"') == 0) {
			aString = aString.substring(1);
		}
		if (aString.length() > 0 && aString.indexOf('\"') == aString.length() - 1) {
			aString = aString.substring(0, aString.length() - 1);
		}
		aString = aString.replace("\\\"", "\"").replace("\\\\", "\\");
		return aString;
	}
		
	private static long countChar(String aStr, char aChr) {
		return aStr.chars().filter(c -> c == aChr).count();
	}
}
