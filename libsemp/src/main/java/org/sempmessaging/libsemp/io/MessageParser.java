package org.sempmessaging.libsemp.io;

import com.google.inject.Inject;
import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;

public abstract class MessageParser extends EventComponent {
	public static final String MESSAGE_END = "\n<<<END-MESSAGE<<<\n";
	private static final byte[] MESSAGE_END_IDENTIFIER = MESSAGE_END.getBytes();

	private MessageParserCache messageParserCache;
	private ByteSequenceFinder byteSequenceFinder;

	@Event
	public abstract NewMessageDataEvent newMessageDataEvent();

	public void addMessageData(final byte[] array, final int numBytesRead) {
		Args.notNull(array, "array");
		Args.is(array.length > 0, "array", "must not be empty.");
		Args.is(numBytesRead > 0, "numBytesRead", "must be > 0");
		Args.is(numBytesRead <= array.length, "numBytesRead", "\""+numBytesRead+"\" must be <= array.length (\""+array.length+"\")");

		assert messageParserCache != null : "Message parser cache cannot be null because it came from dependency injection.";

		int previousCacheSize = messageParserCache.size();
		messageParserCache.add(array, numBytesRead);

		sendEventIfNewJsonDataIsAvailable(previousCacheSize);
	}

	private void sendEventIfNewJsonDataIsAvailable(final int previousCacheSize) {
		int searchFromIndex = Math.max(0, previousCacheSize - MESSAGE_END_IDENTIFIER.length);
		int searchToIndex = messageParserCache.size() - 1;
		int jsonEndPosition = byteSequenceFinder.find(messageParserCache, MESSAGE_END_IDENTIFIER, searchFromIndex, searchToIndex);

		if(jsonEndPosition >= 0) {
			String jsonData = messageParserCache.getData(jsonEndPosition);
			messageParserCache.skipBytes(jsonEndPosition + MESSAGE_END_IDENTIFIER.length);
			send(newMessageDataEvent()).newMessageDataAvailable(jsonData);
		}
	}

	@Inject
	void setMessageParserCache(final MessageParserCache messageParserCache) {
		Args.notNull(messageParserCache, "messageParserCache");
		this.messageParserCache = messageParserCache;
	}

	@Inject
	public void setByteSequenceFinder(final ByteSequenceFinder byteSequenceFinder) {
		Args.notNull(byteSequenceFinder, "byteSequenceFinder");
		this.byteSequenceFinder = byteSequenceFinder;
	}
}
