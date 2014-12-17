package org.sempmessaging.libsemp.io;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageParserCacheTest {
	private MessageParserCache messageParserCache;

	@Before
	public void setup() {
		messageParserCache = new MessageParserCache();
	}

	@Test
	public void sizeAfterAddingOneArrayEqualsByteSizeFromArray() {
		byte[] array = "123456".getBytes();
		messageParserCache.add(array, 3);

		assertEquals(3, messageParserCache.size());
	}

	@Test
	public void getByteFromWithinFirstArrayReturnsTheByteFromTheArray() {
		byte[] array = "123456".getBytes();
		messageParserCache.add(array, 3);

		assertEquals('2', messageParserCache.getByte(1));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void getByteAfterSizeThrowsAnException() {
		byte[] array = "123456".getBytes();
		messageParserCache.add(array, 3);

		messageParserCache.getByte(3);
	}

	@Test
	public void sizeAfterAddingMultipleArraysIsTheSumOfAllSizes() {
		messageParserCache.add("12345".getBytes(), 3);
		messageParserCache.add("67890".getBytes(), 5);
		messageParserCache.add("abcde".getBytes(), 4);

		assertEquals(12, messageParserCache.size());
	}

	@Test
	public void getByteFromWithinThirdArrayReturnsTheByteFromTheArray() {
		messageParserCache.add("12345".getBytes(), 3);
		messageParserCache.add("67890".getBytes(), 5);
		messageParserCache.add("abcde".getBytes(), 4);

		assertEquals('a', messageParserCache.getByte(8));
	}

	@Test
	public void sizeAfterSkipBytesIsSizeMinusSkippedBytes() {
		messageParserCache.add("12345".getBytes(), 3);
		messageParserCache.add("67890".getBytes(), 5);
		messageParserCache.add("abcde".getBytes(), 4);

		messageParserCache.skipBytes(4);

		assertEquals(8, messageParserCache.size());
	}

	@Test
	public void sizeAfterSkippingAllBytesIsZero() {
		messageParserCache.add("12345".getBytes(), 3);
		messageParserCache.add("67890".getBytes(), 5);
		messageParserCache.add("abcde".getBytes(), 4);

		messageParserCache.skipBytes(messageParserCache.size());

		assertEquals(0, messageParserCache.size());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void throwsExceptionWhenTryingToSkipMoreBytesThanAvailable() {
		messageParserCache.add("12345".getBytes(), 3);
		messageParserCache.skipBytes(4);
	}

	@Test
	public void getByteAfterSkippingOneArrayCompletelyReturnsFirstByteOfNextArray() {
		messageParserCache.add("12345".getBytes(), 3);
		messageParserCache.add("67890".getBytes(), 5);
		messageParserCache.add("abcde".getBytes(), 4);

		messageParserCache.skipBytes(3);

		assertEquals('6', messageParserCache.getByte(0));
	}

	@Test
	public void getByteAfterSkipBytesGetsByteFromNewBuffer() {
		messageParserCache.add("12345".getBytes(), 3);
		messageParserCache.add("67890".getBytes(), 5);
		messageParserCache.add("abcde".getBytes(), 4);

		messageParserCache.skipBytes(4);

		assertEquals('7', messageParserCache.getByte(0));
	}

	@Test
	public void getByteFromLastArrayAfterSkipBytesGetsByteFromNewBuffer() {
		messageParserCache.add("12345".getBytes(), 3);
		messageParserCache.add("67890".getBytes(), 5);
		messageParserCache.add("abcde".getBytes(), 4);

		messageParserCache.skipBytes(4);

		assertEquals('a', messageParserCache.getByte(4));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void getDataThrowsExceptionWhenTryingToGetMoreDataThanAvailable() {
		messageParserCache.add("12345".getBytes(), 3);

		messageParserCache.getData(4);
	}

	@Test
	public void getDataReturnsDataAcrossMultipleArrays() {
		messageParserCache.add("12345".getBytes(), 3);
		messageParserCache.add("67890".getBytes(), 5);
		messageParserCache.add("abcde".getBytes(), 4);

		String data = messageParserCache.getData(5);

		assertEquals("12367", data);
	}
}
