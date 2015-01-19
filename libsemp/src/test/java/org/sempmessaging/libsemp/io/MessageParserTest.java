package org.sempmessaging.libsemp.io;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageParserTest {
	private MessageParser messageParser;
	private MessageParserCache messageParserCache;
	private ByteSequenceFinder byteSequenceFinder;

	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	@Before
	public void setup() {
		messageParser = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(MessageParser.class);

		messageParserCache = mock(MessageParserCache.class);
		messageParser.setMessageParserCache(messageParserCache);

		byteSequenceFinder = mock(ByteSequenceFinder.class);
		messageParser.setByteSequenceFinder(byteSequenceFinder);
	}

	@Test
	public void sendsEventWhenNewJsonDataIsAvailable() {
		eventTestRule.subscribeMandatory(messageParser, messageParser.newMessageDataEvent(), (data) -> {
			assertEquals("{}", data);
		});

		when(byteSequenceFinder.find(any(), any(), anyInt(), anyInt())).thenReturn(2);
		when(messageParserCache.getData(2)).thenReturn("{}");

		byte[] message = bytes("{}"+MessageParser.MESSAGE_END);
		messageParser.addMessageData(message, message.length);
	}

	@Test
	public void skipsMessageParserCacheToNewPositionWhenNewJsonDataIsAvailable() {
		when(byteSequenceFinder.find(any(), any(), anyInt(), anyInt())).thenReturn(2);

		byte[] message = bytes("{}"+MessageParser.MESSAGE_END+"{");
		messageParser.addMessageData(message, message.length);

		verify(messageParserCache).skipBytes(MessageParser.MESSAGE_END.length()+2);
	}

	private byte[] bytes(final String message) {
		try {
			return message.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
}
