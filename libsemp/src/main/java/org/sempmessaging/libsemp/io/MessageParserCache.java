package org.sempmessaging.libsemp.io;

import org.sempmessaging.libsemp.arguments.Args;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

class MessageParserCache implements ByteData {
	private int size;
	private final List<byte[]> byteArrays = new LinkedList<>();

	public void add(final byte[] array, final int numBytesRead) {
		Args.notNull(array, "array");
		Args.is(array.length > 0, "array", "must not be empty");
		Args.is(numBytesRead > 0, "numBytesRead", "must be > 0");
		Args.is(numBytesRead <= array.length, "numBytesRead", "\""+numBytesRead+"\" must be <= array.length (\""+array.length+"\").");

		size += numBytesRead;
		byte[] newArray = new byte[numBytesRead];
		System.arraycopy(array, 0, newArray, 0, numBytesRead);
		byteArrays.add(newArray);
	}

	public int size() {
		return size;
	}

	public String getData(final int length) {
		if(length > size) {
			throw new IndexOutOfBoundsException("Cannot get more data than available. Size: "+size+", you tried to get "+length+" bytes.");
		}

		byte[] targetArray = copyBytesToNewArray(length);
		return safelyCreateNewStringFromData(targetArray);
	}

	private String safelyCreateNewStringFromData(final byte[] targetArray) {
		try {
			return new String(targetArray, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	private byte[] copyBytesToNewArray(final int length) {
		byte[] targetArray = new byte[length];

		copyBytesToTargetArray(length, targetArray);

		return targetArray;
	}

	private void copyBytesToTargetArray(final int length, final byte[] targetArray) {
		int bytesLeft = length;
		int targetArrayPosition = 0;
		Iterator<byte[]> iterator = byteArrays.iterator();

		while(bytesLeft > 0) {
			checkIteratorHasNext(iterator);
			byte[] sourceArray = iterator.next();

			System.arraycopy(sourceArray, 0, targetArray, targetArrayPosition, Math.min(bytesLeft, sourceArray.length));
			bytesLeft -= sourceArray.length;
			targetArrayPosition += sourceArray.length;
		}
	}

	public void skipBytes(final int numBytes) {
		if(numBytes > size) {
			throw new IndexOutOfBoundsException("Cannot skip more bytes than available. Size: "+size+", you tried to skip "+numBytes+" bytes.");
		}

		skipBytesInArrays(numBytes);

		size -= numBytes;
	}

	private void skipBytesInArrays(final int numBytes) {
		int bytesLeftToSkip = numBytes;

		ListIterator<byte[]> iterator = byteArrays.listIterator();
		checkIteratorHasNext(iterator);
		byte[] currentArray = iterator.next();

		while(bytesLeftToSkip >= currentArray.length) {
			bytesLeftToSkip -= currentArray.length;
			currentArray = skipCompleteArray(bytesLeftToSkip, iterator, currentArray);
		}

		if(bytesLeftToSkip > 0) {
			skipBytesOfLastArray(bytesLeftToSkip, iterator, currentArray);
		}
	}

	private void skipBytesOfLastArray(final int bytesLeftToSkip, final ListIterator<byte[]> iterator, final byte[] currentArray) {
		int newSize = currentArray.length - bytesLeftToSkip;
		byte[] newArray = new byte[newSize];
		System.arraycopy(currentArray, bytesLeftToSkip, newArray, 0, newSize);

		iterator.set(newArray);
	}

	private byte[] skipCompleteArray(final int bytesLeftToSkip, final ListIterator<byte[]> iterator, byte[] currentArray) {
		iterator.remove();

		if(bytesLeftToSkip > 0) {
			checkIteratorHasNext(iterator);
			currentArray = iterator.next();
		}
		return currentArray;
	}

	@Override
	public byte getByte(final int index) {
		if(index >= size) {
			throw new IndexOutOfBoundsException("Index \""+index+"\" was out of bounds. Size: "+size);
		}

		int arrayIndex = index;
		Iterator<byte[]> iterator = getAndCheckIterator();

		byte[] currentArray = iterator.next();
		while(arrayIndex >= currentArray.length) {
			checkIteratorHasNext(iterator);
			arrayIndex -= currentArray.length;
			currentArray = iterator.next();
		}

		return currentArray[arrayIndex];
	}

	private Iterator<byte[]> getAndCheckIterator() {
		Iterator<byte[]> iterator = byteArrays.iterator();
		checkIteratorHasNext(iterator);
		return iterator;
	}

	private void checkIteratorHasNext(final Iterator<byte[]> iterator) {
		if(!iterator.hasNext()) {
			throw new IllegalStateException("Iterator always must have next, otherwise the size check would have failed.");
		}
	}
}
