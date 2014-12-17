package org.sempmessaging.libsemp.io;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ByteSequenceFinderTest {
	private final String DATA_TO_SEARCH = "Here is a test string. It is nice ;-)";
	private final int SEARCH_TO_INDEX = DATA_TO_SEARCH.length()-1;

	private ByteSequenceFinder finder;
	private ByteData byteData;

	@Before
	public void setup() {
		finder = new ByteSequenceFinder();
		byteData = new ByteData() {
			private byte[] bytes = DATA_TO_SEARCH.getBytes();

			@Override
			public byte getByte(final int index) {
				return bytes[index];
			}

			@Override
			public int size() {
				return bytes.length;
			}
		};
	}

	@Test
	public void returnsMinusOneWhenSequenceIsNotFound() {
		int position = finder.find(byteData, "foo".getBytes(), 0, SEARCH_TO_INDEX);

		assertEquals(-1, position);
	}

	@Test
	public void findsSingleByteAtTheEndOfTheString() {
		int position = finder.find(byteData, ")".getBytes(), 0, SEARCH_TO_INDEX);

		assertEquals(DATA_TO_SEARCH.length()-1, position);
	}

	@Test
	public void findsSingleByteAtTheBeginningOfTheString() {
		int position = finder.find(byteData, "H".getBytes(), 0, SEARCH_TO_INDEX);

		assertEquals(0, position);
	}

	@Test
	public void findsFirstInstanceOfMultipleBytesInTheMiddleOfTheString() {
		int position = finder.find(byteData, "is".getBytes(), 0, SEARCH_TO_INDEX);

		assertEquals(DATA_TO_SEARCH.indexOf("is"), position);
	}

	@Test
	public void findsSecondInstanceOfMultipleBytesWhenGivenSearchBounds() {
		int position = finder.find(byteData, "is".getBytes(), 10, SEARCH_TO_INDEX-5);

		assertEquals(DATA_TO_SEARCH.indexOf("is", 10), position);
	}
}
