package org.sempmessaging.libsemp.io;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class MessageParserIntegratedTest {
	private MessageParser messageParser;
	private MessageParserCache messageParserCache;
	private ByteSequenceFinder byteSequenceFinder;

	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	@Before
	public void setup() {
		messageParser = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(MessageParser.class);

		messageParserCache = new MessageParserCache();
		messageParser.setMessageParserCache(messageParserCache);

		byteSequenceFinder = new ByteSequenceFinder();
		messageParser.setByteSequenceFinder(byteSequenceFinder);
	}

	@Test
	public void sendsEventWhenNewJsonDataIsAvailable() {
		eventTestRule.subscribeMandatory(messageParser, messageParser.newMessageDataEvent(), (data) -> assertEquals("{}", data));

		byte[] message = bytes("{}"+MessageParser.MESSAGE_END);
		messageParser.addMessageData(message, message.length);
	}

	@Test
	public void skipsMessageParserCacheToNewPositionWhenNewJsonDataIsAvailable() {
		byte[] message = bytes("{}"+MessageParser.MESSAGE_END+"{");
		messageParser.addMessageData(message, message.length);

		assertEquals(1, messageParserCache.size());
	}

	@Test
	public void findsJsonDataFromTwoSeparateArrays() {
		eventTestRule.subscribeMandatory(messageParser, messageParser.newMessageDataEvent(), (data) -> {
			assertEquals("{ \"SomeProperty\": \"A very long string property\" }", data);
		});

		byte[] message = bytes("{ \"SomeProperty\": \"A ve");
		messageParser.addMessageData(message, message.length);

		message = bytes("ry long string property\" }"+MessageParser.MESSAGE_END);
		messageParser.addMessageData(message, message.length);
	}

	@Test
	public void findsMessageEndFromTwoSeparateArrays() {
		eventTestRule.subscribeMandatory(messageParser, messageParser.newMessageDataEvent(), (data) -> {
			assertEquals("{ \"SomeProperty\": \"A very long string property\" }", data);
		});

		byte[] message = bytes("{ \"SomeProperty\": \"A very long string property\" }"+MessageParser.MESSAGE_END.substring(0, 4));
		messageParser.addMessageData(message, message.length);

		message = bytes(MessageParser.MESSAGE_END.substring(4));
		messageParser.addMessageData(message, message.length);
	}

	private byte[] bytes(final String message) {
		try {
			return message.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
}
