package org.sempmessaging.libsemp.connection;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectionListenerTest {
	private ConnectionListener listener;
	private ReadableByteChannel channel;

	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	@Before
	public void setup() {
		listener = EventComponents.createComponent(ConnectionListener.class);

		channel = mock(ReadableByteChannel.class);
		listener.listenTo(new ReadableConnection() {
			@Override
			public ReadableByteChannel readableChannel() {
				return channel;
			}
		});
	}

	@Test
	public void sendsEventWhenNewDataIsAvailable() throws IOException {
		when(channel.read(any())).then(answerChannelRead("a"));

		eventTestRule.subscribeMandatory(listener, listener.newDataEvent(), (data, length) -> {});
		listener.run();
	}

	@Test
	public void sendsEventWithReceivedStringWhenDataIsAvailable() throws IOException {
		when(channel.read(any())).then(answerChannelRead("test"));

		eventTestRule.subscribeMandatory(listener, listener.newDataEvent(), (data, length) -> {
			assertEquals(4, length);
			assertEquals("test", new String(data, 0, length));
		});
		listener.run();
	}

	@Test
	public void sendsEventWhenAnExceptionWasThrown() throws IOException {
		when(channel.read(any())).thenThrow(IOException.class);

		eventTestRule.subscribeMandatory(listener, listener.ioExceptionEvent(), (e) -> {});
		listener.run();
	}

	@Test
	public void sendsEventWhenConnectionWasClosed() throws IOException {
		when(channel.read(any())).thenReturn(-1);

		eventTestRule.subscribeMandatory(listener, listener.connectionClosedEvent(), (c) -> {});
		listener.run();
	}

	private Answer<Object> answerChannelRead(String text) throws UnsupportedEncodingException {
		return invocation -> {
			ByteBuffer buffer = (ByteBuffer) invocation.getArguments()[0];

			byte[] bytes = text.getBytes("UTF-8");
			buffer.put(bytes);

			return bytes.length;
		};
	}

}
