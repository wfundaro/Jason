package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import json.IJsonField;
import json.JsonArray;
import json.JsonObject;
import json.JsonParser;

class JsonTest {
	private static final String JSON_DATA = "{\r\n" + "    \"project\": \"PLM\",\r\n"
			+ "    \"creator\": \"Sylvain Garde\",\r\n" + "    \"day\": 28,\r\n" + "    \"description\": \"\",\r\n"
			+ "    \"links\": [\r\n" + "        \"http://site1\",\r\n" + "        \"http://site2\",\r\n"
			+ "        \"http://site3\",\r\n" + "        25,\r\n" + "        {\r\n"
			+ "            \"linker\": \"Iznogoud\"\r\n" + "        },\r\n" + "        {\r\n"
			+ "            \"url\": \"\"\r\n" + "        }\r\n" + "    ],\r\n" + "    \"modules\": [\r\n"
			+ "        {\r\n" + "            \"name\": \"openXml\"\r\n" + "        },\r\n" + "        {\r\n"
			+ "            \"name\": \"openXmlDriver\"\r\n" + "        },\r\n" + "        {\r\n"
			+ "            \"name\": \"Toolbox\"\r\n" + "        }\r\n" + "    ],\r\n" + "    \"city\": {\r\n"
			+ "        \"main\": \"Toulouse\",\r\n" + "        \"client\": \"Bordeaux\"\r\n" + "    },\r\n"
			+ "    \"issues\": [\r\n" + "        [\r\n" + "            \"issue 1 tab 1\",\r\n"
			+ "            \"issue 2 tab 1\"\r\n" + "        ],\r\n" + "        [\r\n"
			+ "            \"issue 1 tab 2\",\r\n" + "            \"issue 2 tab 2\"\r\n" + "        ]\r\n" + "    ]\r\n"
			+ "}";
	
	private static JsonObject mainJsonObject;

	@BeforeAll
	public static void init() {
		mainJsonObject = (JsonObject) JsonParser.parse(JSON_DATA);
	}
	
	@Test
	public void JsonMainObjectTest() throws IllegalArgumentException, IllegalAccessError {
		assertEquals(8, mainJsonObject.size());
		assertEquals("PLM", mainJsonObject.getField("project").getString());
		assertEquals("Sylvain Garde", mainJsonObject.getField("creator").getString());
		assertEquals("28", mainJsonObject.getField("day").getString());
		assertEquals("", mainJsonObject.getField("description").getString());
		// assert exception because the next fields is not of string field
		assertThrows(IllegalArgumentException.class, () -> mainJsonObject.getField("creator").getField("test"));
		assertThrows(IllegalAccessError.class, () -> mainJsonObject.getField("links").getString());
		assertThrows(IllegalAccessError.class, () -> mainJsonObject.getField("modules").getString());
		assertThrows(IllegalAccessError.class, () -> mainJsonObject.getField("city").getString());
		assertThrows(IllegalAccessError.class, () -> mainJsonObject.getField("issues").getString());
	}
	
	@Test
	public void JsonArrayTest() throws IllegalArgumentException, IllegalAccessError {
		assertEquals(IJsonField.Type.ARRAY, mainJsonObject.getField("links").getType());
		JsonArray jarray = (JsonArray) mainJsonObject.getField("links");
		assertEquals(6, jarray.size());
		assertThrows(IllegalAccessError.class, () -> jarray.get(50));
		assertThrows(IllegalAccessError.class, () -> jarray.getString());
		assertThrows(IllegalArgumentException.class, () -> jarray.getField("test"));
		assertEquals("http://site1", jarray.get(0).getString());
		assertNotEquals("http://site1", jarray.get(1).getString());
		assertEquals(IJsonField.Type.STRING, jarray.get(0).getType());
		assertEquals("http://site2", jarray.get(1).getString());
		assertNotEquals("http://site1", jarray.get(1).getString());
		assertEquals("http://site3", jarray.get(2).getString());
		assertEquals("25", jarray.get(3).getString());
		assertEquals(JsonObject.class, jarray.get(4).getClass());
		assertEquals(IJsonField.Type.OBJECT, jarray.get(4).getType());
		JsonObject job = (JsonObject) jarray.get(4);
		job.forEach(item -> assertEquals("linker", item.getKey()));
		assertEquals("linker", job.getStringKeyFromValue(job.getField("linker")));
		assertEquals("linker", ((JsonObject) jarray.get(4)).getStringKeyFromValue(job.getField("linker")));
		assertEquals("Iznogoud", job.getField("linker").getString());
		job = (JsonObject) jarray.get(5);
		assertEquals("", job.getField("url").getString());
		assertNotEquals("a", job.getField("url").getString());
	}
	
	@Test
	public void JsonArrayOfObjectTest() throws IllegalArgumentException, IllegalAccessError {
		JsonArray jar = (JsonArray) mainJsonObject.getField("modules");
		assertEquals(3, jar.size());
		assertEquals("openXml", jar.get(0).getField("name").getString());
		assertThrows(IllegalAccessError.class, () -> jar.get(0).getString());
		assertEquals("openXmlDriver", jar.get(1).getField("name").getString());
		assertEquals("Toolbox", jar.get(2).getField("name").getString());
		jar.forEach(item -> assertEquals(IJsonField.Type.OBJECT, item.getType()));
	}

	@Test
	public void JsonObjectOfStringObjectTest() throws IllegalArgumentException {
		assertEquals("Toulouse", mainJsonObject.getField("city").getField("main").getString());
		assertEquals("Bordeaux", mainJsonObject.getField("city").getField("client").getString());
	}

	@Test
	public void JsonArrayOfArrayTest() throws IllegalArgumentException, IllegalAccessError {
		assertEquals("issue 1 tab 1",
				((JsonArray) ((JsonArray) mainJsonObject.getField("issues")).get(0)).get(0).getString());
		assertEquals("issue 2 tab 1",
				((JsonArray) ((JsonArray) mainJsonObject.getField("issues")).get(0)).get(1).getString());
		JsonArray jar = (JsonArray) ((JsonArray) mainJsonObject.getField("issues")).get(1);
		assertEquals(mainJsonObject.getField("issues"), jar.getParent());
		assertEquals("issue 1 tab 2", jar.get(0).getString());
		assertNotEquals("issue 2 tab 2", jar.get(0).getString());
		assertEquals("issue 2 tab 2", jar.get(1).getString());
		assertNotEquals("issue 1 tab 2", jar.get(1).getString());
	}

}
