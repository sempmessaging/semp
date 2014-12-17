package org.sempmessaging.sempd.protocol.io;

import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.libsemp.request.RequestId;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class ResponseDataTest {
	private TestResponseData responseData;

	@Before
	public void setup() throws Exception {
		responseData = new TestResponseData();
	}

	@Test
	public void setObjectSetsTheObjectInTheUnderlyingMap() {
		responseData.setObject("FooBar");

		assertEquals("FooBar", responseData.asMap().get("Object"));
	}

	@Test
	public void setListAddsAListToTheUnderlyingMap() {
		responseData.setList(Collections.emptyList());

		assertTrue(responseData.asMap().containsKey("List"));
		assertTrue(responseData.asMap().get("List") instanceof List);
	}

	@Test
	public void setListOfPrimitivesAddsThePrimitives() {
		responseData.setList(Arrays.asList("Foo"));
		assumeTrue(responseData.asMap().containsKey("List"));
		assumeTrue(responseData.asMap().get("List") instanceof List);

		assertEquals("Foo", ((List)responseData.asMap().get("List")).get(0));
	}

	@Test
	public void setWithResponseDataConvertsTheResponseDataToAMap() {
		ResponseData nestedData = new ResponseData();
		nestedData.setRequestId(new RequestId("id"));

		responseData.setList(Arrays.asList(nestedData));
		assumeTrue(responseData.asMap().containsKey("List"));
		assumeTrue(responseData.asMap().get("List") instanceof List);

		Object addedObject = ((List) responseData.asMap().get("List")).get(0);
		assertTrue(addedObject instanceof Map);
	}

	@Test
	public void setWithResponseDataConvertsTheResponseDataToAMapWithCorrectData() {
		ResponseData nestedData = new ResponseData();
		nestedData.setRequestId(new RequestId("id"));

		responseData.setList(Arrays.asList(nestedData));
		assumeTrue(responseData.asMap().containsKey("List"));
		assumeTrue(responseData.asMap().get("List") instanceof List);

		Object addedObject = ((List) responseData.asMap().get("List")).get(0);
		assumeTrue(addedObject instanceof Map);

		assertEquals("id", ((Map)addedObject).get("RequestId"));
	}

	private class TestResponseData extends ResponseData {
		public void setObject(final Object object) {
			set("Object", object);
		}

		public void setList(final List<?> list) {
			set("List", list);
		}
	}
}