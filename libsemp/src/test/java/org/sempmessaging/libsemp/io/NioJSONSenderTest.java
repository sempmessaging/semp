package org.sempmessaging.libsemp.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.sempmessaging.libsemp.io.MessageParser;
import org.sempmessaging.libsemp.io.NioJSONSender;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class NioJSONSenderTest {
	private NioJSONSender jsonSender;
	private WritableByteChannel channel;

	private StringBuilder sentData = new StringBuilder();

	@Before
	public void setup() {
		channel = mock(WritableByteChannel.class);

		jsonSender = new NioJSONSender(channel, 128);
	}

	@Test
	public void sendsSomethingToByteChannelWhenSendingJsonData() throws IOException {
		Map<String, Object> jsonData = createJsonData();

		jsonSender.send(jsonData);

		verify(channel, atLeastOnce()).write(any(ByteBuffer.class));
	}

	@Test
	public void writesToChannelTwiceWhenDataIsTooLongForTheBuffer() throws IOException {
		Map<String, Object> jsonData = createJsonData();
		jsonData.put("LongString", "This is a very long string. It should ensure that the resulting data has more than 128 bytes.");

		jsonSender.send(jsonData);

		verify(channel, atLeast(2)).write(any(ByteBuffer.class));
	}

	@Test
	public void writesEndMessageTokenToChannel() throws IOException {
		Map<String, Object> jsonData = createJsonData();

		doAnswer(this::collectBufferData).when(channel).write(any(ByteBuffer.class));
		jsonSender.send(jsonData);

		assertThat(sentData.toString(), containsString(MessageParser.MESSAGE_END));
	}

	@Test
	public void writesCorrectDataToChannel() throws IOException {
		Map<String, Object> jsonData = createJsonData();

		doAnswer(this::collectBufferData).when(channel).write(any(ByteBuffer.class));
		jsonSender.send(jsonData);

		String sentString = sentData.toString();
		assumeThat(sentString, containsString(MessageParser.MESSAGE_END));
		String sentJsonString = sentString.substring(0, sentString.length() - MessageParser.MESSAGE_END.length());
		Map<String, Object> sentMap = new Gson().fromJson(sentJsonString, new TypeToken<Map<String, Object>>() {}.getType());

		assertEquals("World", sentMap.get("Hello"));
		assertEquals(2.0, ((Map<String, Object>) sentMap.get("Map")).get("Integer"));
	}

	private Object collectBufferData(final InvocationOnMock invocationOnMock) throws UnsupportedEncodingException {
		ByteBuffer byteBuffer = (ByteBuffer) invocationOnMock.getArguments()[0];

		byte[] bytes = new byte[byteBuffer.limit()];
		byteBuffer.get(bytes);
		sentData.append(new String(bytes, "UTF-8"));

		return null;
	}

	private Map<String, Object> createJsonData() {
		HashMap<String, Object> innerHashMap = new HashMap<>();
		innerHashMap.put("Integer", 2);

		return new HashMap<String, Object>() {{
				put("Hello", "World");
			put("Map", innerHashMap);
			}};
	}
}
