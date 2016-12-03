package com.team.common.util;

// TODO: Auto-generated Javadoc
/**
 * 描述信息:.
 * 
 * @author David
 * @version 1.0
 */
public class UByte {
    
    /** The value. */
    private byte[] value = new byte[1];
    /**
     * A constant holding the minimum value a <code>byte</code> can have,0.
     */
    public static final int MIN_VALUE = 0;

    /**
     * A constant holding the maximum value a <code>byte</code> can have,
     * 2<sup>8</sup>-1.
     */
    public static final int MAX_VALUE = 255;

    /**
     * Instantiates a new u byte.
     * 
     * @param b the b
     */
    public UByte(String b) {
        int temp = Integer.parseInt(b);
        if (temp < MIN_VALUE || temp > MAX_VALUE) {
            throw new NumberFormatException("Value out of range. Value:\"" + b + "\"");
        }
        value[0] = (byte) (temp & 0xff);
    }

    /**
     * Instantiates a new u byte.
     * 
     * @param b the b
     */
    public UByte(byte b) {
        value[0] = b;
    }

    /**
     * Instantiates a new u byte.
     * 
     * @param b the b
     */
    public UByte(int b) {
        if (b < MIN_VALUE || b > MAX_VALUE) {
            throw new NumberFormatException("Value out of range. Value:\"" + b + "\"");
        }
        value[0] = (byte) (b & 0xff);
    }

    /**
     * To string.
     * 
     * @return the string
     * @version
     */
    @Override
    public String toString() {
        short temp = (short) (value[0] & 0xff);
        return String.valueOf(temp);
    }
}
