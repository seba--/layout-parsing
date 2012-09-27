package org.spoofax.jsglr.client;

import java.io.Serializable;
import java.util.Arrays;

/**
 * A series of character ranges.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class RangeList implements Serializable {
    
    private static final long serialVersionUID = 16593569;
    
    public static final int NONE = -1;
    
    private final int[] ranges;
    
    private final int singularRange;
    
    public RangeList(int[] ranges) {
        if (ranges.length == 1) {
            this.ranges = null;
            singularRange = ranges[0];
        } else {
            this.ranges = ranges;
            singularRange = NONE;
        }
    }
    
    public final boolean within(int c) {
        if (singularRange != NONE) return c == singularRange;
        for (int i = 0; i < ranges.length; i += 2) {
            int low = ranges[i];
            if (low <= c) {
                int high = ranges[i + 1];
                if (c <= high) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Gets the character of a single-character range.
     * 
     * @return  The single range character, or {@link NONE} if not applicable.
     */
    public int getSingularRange() {
        return singularRange;
    }
    
    /*
     * Returns a char value that can be used for "brute-force" recovery
     */
    public int getFirstRangeElement() {
        return singularRange == NONE ? ranges[0] : singularRange;
    }
    
    public int getLastRangeElement() {
        return singularRange == NONE ? ranges[ranges.length - 1] : singularRange;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RangeList))
            return false;
        if (singularRange == NONE) {
            return Arrays.equals(((RangeList) obj).ranges, ranges);
        } else {
            return singularRange == ((RangeList) obj).singularRange;
        }
    }
    
    @Override
    public int hashCode() {
        return singularRange == NONE ? Arrays.hashCode(ranges) : singularRange;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (singularRange != NONE) {
            sb.append(singularRange);
        } else {
            sb.append('[');
            for (int i = 0, end = ranges.length - 1; i < end; i++) {
                int low = ranges[i];
                int high = ranges[i + 1];
                sb.append(low);
                if (low != high) {
                    sb.append('-');
                    sb.append(high);
                }
                sb.append(',');
            }
            sb.replace(sb.length() - 1, sb.length(), "]");
        }
        return sb.toString();
    }
}
