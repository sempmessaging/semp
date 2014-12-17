package org.sempmessaging.libsemp.io;

import org.sempmessaging.libsemp.arguments.Args;

public class ByteSequenceFinder {
	public int find(final ByteData byteData, final byte[] seqToFind, final int searchFrom, final int searchTo) {
		checkArgumentsForFind(byteData, seqToFind, searchFrom, searchTo);
		return findSequence(byteData, seqToFind, searchFrom, searchTo);
	}

	private void checkArgumentsForFind(final ByteData byteData, final byte[] seqToFind, final int searchFrom, final int searchTo) {
		Args.notNull(byteData, "byteData");
		Args.notNull(seqToFind, "seqToFind");
		Args.is(seqToFind.length > 0, "seqToFind", "must not be empty");
		Args.is(searchFrom >= 0, "searchFrom", "must be >= 0");
		Args.is(searchFrom < byteData.size(), "searchFrom", "\""+searchFrom+"\" must be <= byteData.size() (\""+byteData.size()+"\")");
		Args.is(searchTo >= 0, "searchFrom", "must be >= 0");
		Args.is(searchTo < byteData.size(), "searchFrom", "\"\"+searchFrom+\"\" must be <= byteData.size() (\""+byteData.size()+"\")");
	}

	private int findSequence(final ByteData byteData, final byte[] seqToFind, final int searchFrom, final int searchTo) {
		for(int i=searchFrom; i<=searchTo; i++) {
			if(isSequenceAtPosition(byteData, seqToFind, i, searchTo)) {
				return i;
			}
		}
		return -1;
	}

	private boolean isSequenceAtPosition(final ByteData byteData, final byte[] seqToFind, final int searchIndex, final int searchTo) {
		for(int seqIndex=0; seqIndex<seqToFind.length; seqIndex++) {
			if (seqIndexOutOfBounds(searchIndex, searchTo, seqIndex)
					|| !nextSequencByteAt(byteData, searchIndex + seqIndex, seqToFind[seqIndex])) {
				return false;
			}
		}
		return true;
	}

	private boolean nextSequencByteAt(final ByteData byteData, final int index, final byte sequenceByte) {
		return (byteData.getByte(index) == sequenceByte);
	}

	private boolean seqIndexOutOfBounds(final int searchIndex, final int searchTo, final int seqIndex) {
		return (searchIndex+seqIndex) > searchTo;
	}
}
