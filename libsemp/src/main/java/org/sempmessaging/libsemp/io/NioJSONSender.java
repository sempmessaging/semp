package org.sempmessaging.libsemp.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.request.JSONSender;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Map;

public class NioJSONSender implements JSONSender {
	private static final int DEFAULT_BYTE_BUFFER_CAPACITY = 4096;

	private WritableByteChannel channel;
	private final ByteBuffer buffer;

	private final Gson jsonConverter = new Gson();
	private final int byteBufferCapacity;

	public NioJSONSender(final WritableByteChannel channel) {
		this(channel, DEFAULT_BYTE_BUFFER_CAPACITY);
	}

	NioJSONSender(final WritableByteChannel channel, final int byteBufferCapacity) {
		this.channel = channel;

		this.byteBufferCapacity = byteBufferCapacity;
		this.buffer = ByteBuffer.allocateDirect(byteBufferCapacity);
	}

	public void disconnect() {
	}

	@Override
	public void send(final Map<String, Object> jsonData) {
		Args.notNull(jsonData, "jsonData");

		JsonAppender jsonAppender = new JsonAppender();
		Type typeOfHashMap = new TypeToken<Map<String, Object>>() { }.getType();
		jsonConverter.toJson(jsonData, typeOfHashMap, jsonAppender);

		try {
			buffer.put(MessageParser.MESSAGE_END.getBytes("UTF-8"));
			buffer.flip();
			channel.write(buffer);
			buffer.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class JsonAppender implements Appendable {
		@Override
		public Appendable append(final CharSequence csq) throws IOException {
			return append(csq, 0, csq.length());
		}

		@Override
		public Appendable append(final CharSequence csq, final int start, final int end) throws IOException {
			Args.notNull(csq, "csq");
			Args.is(start >= 0, "start", ">= 0");
			Args.is(start < csq.length(), "start", "< csq.length()");
			Args.is(end >= 0, "end", ">= 0");
			Args.is(end < csq.length(), "end", "< csq.length()");

			String currentString = csq.subSequence(start, end).toString();
			byte[] currentBytes = currentString.getBytes("UTF-8");

			int offset = 0;
			while(offset < currentBytes.length) {
				int numBytesToWrite = Math.min(currentBytes.length - offset, byteBufferCapacity);
				buffer.put(currentBytes, offset, numBytesToWrite);
				offset += byteBufferCapacity;

				buffer.flip();

				channel.write(buffer);
				buffer.clear();
			}
			return this;
		}

		@Override
		public Appendable append(final char c) throws IOException {
			buffer.put(String.valueOf(c).getBytes("UTF-8"));

			return this;
		}
	}
}
