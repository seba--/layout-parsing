package org.spoofax.jsglr.client.imploder;

/** 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class LineStartOffsetList {
	
	private static final double EXPECTED_NEWLINES_DIVIDER = 12; 
	
	private int[] lineStarts;
	
	private int lastLineStartOffset;
	
	public LineStartOffsetList(String input) {
		int size = (int) (input.length() / EXPECTED_NEWLINES_DIVIDER) + 1;
		lineStarts = new int[size];
		addLineStartOffset(0);
		for (int i = 0, max = input.length(); i < max; i++) {
			if (input.charAt(i) == '\n') addLineStartOffset(i);
		}
	}
 	
	public void addLineStartOffset(int lineStartOffset) {
		if (lastLineStartOffset == lineStarts.length) {
			int[] oldLineStarts = lineStarts;
			lineStarts = new int[lineStarts.length * 2];
			System.arraycopy(oldLineStarts, 0, lineStarts, 0, oldLineStarts.length);
		}
		lineStarts[lastLineStartOffset++] = lineStartOffset;
	}
	
	public int getLine(int index) {
		return index + 1;
	}
	
	public int getColumn(int index, int offset) {
		assert offset >= lineStarts[index];
		return offset - lineStarts[index];
	}
	
	public int getIndex(int offset) {
		// TODO: Optimize - binary search?
		for (int i = 0, max = lineStarts.length; i < max; i++) {
			if (offset > lineStarts[i])
				return i;
		}
		return 0;
	}

}
