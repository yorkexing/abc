package com.team.common.util;

import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

// TODO: Auto-generated Javadoc
/**
 * 字节流工具类.
 * 
 * @author david
 * @version 1.0
 */
public class ByteUtil {
    
    /** The Constant hex. */
    private static final char[] hex = "0123456789ABCDEF".toCharArray();
    
    /** The byte buff. */
    private ByteBuffer byteBuff = null;
    
    /** The encode. */
    private static String encode = "GBK";

    /**
     * 用指定长度初始化byteBuff.
     * 
     * @param length int
     * @version
     */
    public void initialByteBuffer(int length) {
        byteBuff = ByteBuffer.allocate(length >= 0 ? length : 1);
    }

    /**
     * 向字节流中填充一个字节.
     * 
     * @param b byte
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @version
     */
    public ByteBuffer appendByte(byte b) throws BufferOverflowException {
        byteBuff.put(b);
        return byteBuff;
    }

    /**
     * 向字节流中填充一个字节.
     * 
     * @param b the b
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @version
     */
    public ByteBuffer appendByte(Byte b) throws BufferOverflowException {
        byteBuff.put(b == null ? 0 : b.byteValue());
        return byteBuff;
    }

    /**
     * 向字节流中填充字节数组.
     * 
     * @param b the b
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @version
     */
    public ByteBuffer appendBytes(byte[] b) throws BufferOverflowException {
        if (b == null || b.length == 0) {
            return byteBuff;
        }
        this.byteBuff.put(b);
        return byteBuff;
    }

    /**
     * 向字节流中填充字节数组.
     * 
     * @param B the b
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @version
     */
    public ByteBuffer appendBytes(Byte[] B) throws BufferOverflowException {
        if (B == null || B.length == 0) {
            return byteBuff;
        }
        int length = B.length;
        byte[] b = new byte[B.length];
        for (int i = 0; i < length; i++) {
            b[i] = B[i].byteValue();
        }
        this.byteBuff.put(b);
        return byteBuff;
    }

    /**
     * 向字节流中填充一个整数.
     * 
     * @param b the b
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @version
     */
    public ByteBuffer appendInt(int b) throws BufferOverflowException {
        byteBuff.putInt(b);
        return byteBuff;
    }

    /**
     * 向字节流中填充一个整数.
     * 
     * @param b the b
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @version
     */
    public ByteBuffer appendInt(Integer b) throws BufferOverflowException {
        byteBuff.putInt(b == null ? 0 : b.intValue());
        return byteBuff;
    }

    /**
     * 向字节流中填充一个长整数.
     * 
     * @param b the b
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @version
     */
    public ByteBuffer appendLong(long b) throws BufferOverflowException {
        byteBuff.putLong(b);
        return byteBuff;
    }

    /**
     * 向字节流中填充一个长整数.
     * 
     * @param b the b
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @version
     */
    public ByteBuffer appendLong(Long b) throws BufferOverflowException {
        byteBuff.putLong(b == null ? 0 : b.longValue());
        return byteBuff;
    }

    /**
     * 向字节流中填充一个短整数.
     * 
     * @param b the b
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @version
     */
    public ByteBuffer appendShort(short b) throws BufferOverflowException {
        byteBuff.putShort(b);
        return byteBuff;
    }

    /**
     * 向字节流中填充一个短整数.
     * 
     * @param b the b
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @version
     */
    public ByteBuffer appendShort(Short b) throws BufferOverflowException {
        byteBuff.putShort(b == null ? 0 : b.shortValue());
        return byteBuff;
    }

    /**
     * 向字节流中填充一个指定长度的字符串的GBK编码值.
     * 
     * @param str the str
     * @param byteLength the byte length
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @version
     */
    public ByteBuffer appendString(String str, int byteLength) throws BufferOverflowException, UnsupportedEncodingException {
        this.appendString(str, byteLength, this.encode);
        return byteBuff;
    }

    /**
     * 以enc指定的编码格式向字节流中填充指定长度的字符串.
     * 
     * @param str the str
     * @param byteLength the byte length
     * @param enc the enc
     * @return the byte buffer
     * @throws BufferOverflowException the buffer overflow exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @version
     */
    public ByteBuffer appendString(String str, int byteLength, String enc) throws BufferOverflowException, UnsupportedEncodingException {
        if (byteLength <= 0) {
            return byteBuff;
        }
        byte[] arr = str == null ? new byte[0] : str.getBytes(enc);
        byte[] dest = new byte[byteLength];
        int len = arr.length;
        int copyLen = len > byteLength ? byteLength : len;
        System.arraycopy(arr, 0, dest, 0, copyLen);
        byteBuff.put(dest);
        return byteBuff;
    }

    /**
     * 填充用16进制字符串承载的字节流.
     * 
     * @param hex the hex
     * @param byteLength the byte length
     * @return the byte buffer
     * @throws Exception the exception
     * @version
     */
    public ByteBuffer appendHex(String hex, int byteLength) throws Exception {
        if (null == hex || "".equals(hex)) {
            for (int i = 0; i < byteLength; i++) {
                this.appendByte(new Byte("0"));
            }
        } else {
            if (hex.length() / 2 > byteLength) {
                throw new Exception("非法填充：待填充的字节流大于分配的字节空间！");
            }
            for (int i = 0; i < hex.length(); i += 2) {
                Short temp = Short.parseShort(hex.substring(i, i + 2), 16);
                this.appendByte(temp.byteValue());
            }
            for (int i = 0; i < byteLength - hex.length() / 2; i++) {
                this.appendByte(new Byte("0"));
            }
        }
        return byteBuff;
    }

    /**
     * 从ByteBuffer中获取所有的字节.
     * 
     * @return the bytes
     * @return
     */
    public byte[] getBytes() {
        return this.byteBuff == null ? new byte[0] : this.byteBuff.array();
    }

    /**
     * 从ByteBuffer中获取指定长度的字节数.
     * 
     * @param length the length
     * @return the bytes
     * @return
     */
    public byte[] getBytes(int length) {
        length = length < 0 ? 0 : length;
        byte[] b = new byte[length];
        if (length == 0) {
            return b;
        }
        this.byteBuff.get(b);
        return b;
    }

    /**
     * 从ByteBuffer中获取一个字节对象.
     * 
     * @return the byte
     * @throws BufferOverflowException the buffer overflow exception
     * @return
     */
    public Byte getByte() throws BufferOverflowException {
        return new Byte(this.byteBuff.get());
    }

    /**
     * 从ByteBuffer中获取一个短整型数对象.
     * 
     * @return the short
     * @throws BufferOverflowException the buffer overflow exception
     * @return
     */
    public Short getShort() throws BufferOverflowException {
        return new Short(this.byteBuff.getShort());
    }

    /**
     * 从ByteBuffer中获取一个整数对象.
     * 
     * @return the int
     * @throws BufferOverflowException the buffer overflow exception
     * @return
     */
    public Integer getInt() throws BufferOverflowException {
        return new Integer(this.byteBuff.getInt());
    }

    /**
     * 从ByteBuffer中获取一个长整数对象.
     * 
     * @return the long
     * @throws BufferOverflowException the buffer overflow exception
     * @return
     */
    public Long getLong() throws BufferOverflowException {
        return new Long(this.byteBuff.getLong());
    }

    /**
     * 从ByteBuffer中获取指定长度的字符串.
     * 
     * @param byteLength the byte length
     * @return the string
     * @throws BufferOverflowException the buffer overflow exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @return
     */
    public String getString(int byteLength) throws BufferOverflowException, UnsupportedEncodingException {
        byte[] b = new byte[byteLength];
        this.byteBuff.get(b);
        int i = -1;
        while (b[++i] != 0 && i < byteLength - 1) {
            ;
        }
        return new String(b, 0, i, this.encode);
    }

    /**
     * 从ByteBuffer中获取指定长度的字符串.
     * 
     * @param byteLength the byte length
     * @return the hex string
     * @throws BufferOverflowException the buffer overflow exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @return
     */
    public String getHexString(int byteLength) throws BufferOverflowException, UnsupportedEncodingException {
        byte[] b = new byte[byteLength];
        this.byteBuff.get(b);
        // bytesToHexString(b);
        return bytesToHexString(b);
    }

    /**
     * 描述信息:.
     * 
     * @param asc the asc
     * @return the string
     * @author David
     */
    public static String asciiToString(String asc) {
        if (asc == null || asc.length() == 0 || asc.length() % 2 != 0) {
            return "";
        }
        ByteBuffer ascBuffer = ByteBuffer.allocate(asc.length() / 2);

        for (int i = 0; i < asc.length(); i = i + 2) {
            Integer asciiValue = Integer.parseInt(asc.substring(i, i + 2), 16);
            ascBuffer.put(asciiValue.byteValue());
        }
        String temp = "";
        try {
            temp = new String(ascBuffer.array(), encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * Gets the byte buff.
     * 
     * @return the byteBuff
     */
    public ByteBuffer getByteBuff() {
        return byteBuff;
    }

    /**
     * Gets the encode.
     * 
     * @return the encode
     */
    public String getEncode() {
        return encode;
    }

    /**
     * Sets the encode.
     * 
     * @param encode the encode to set
     */
    public static void setEncode(String encode) {
        ByteUtil.encode = encode;
    }

    /**
     * Gets the hex string.
     * 
     * @param value the value
     * @param datatype the datatype
     * @param length the length
     * @return the hex string
     */
    public static String getHexString(String value, String datatype, int length) {
        String hexStr = "";
        if ("str".equals(datatype)) {
            byte[] temp = stringToBytes(value);
            hexStr = bytesToHexString(temp);
        } else if ("hex".equals(datatype)) {
            hexStr = value;
        } else if ("int".equals(datatype)) {
            hexStr = Integer.toHexString(Integer.parseInt(value));
        }
        String zeroPrefix = "";
        if (hexStr.length() < length * 2) {
            for (int i = 0; i < length * 2 - hexStr.length(); i++) {
                zeroPrefix += "0";
            }
        }
        hexStr = zeroPrefix + hexStr;
        return hexStr.toUpperCase();
    }

    /**
     * *********************************v20401**********************************
     * ******************.
     * 
     * @param value the value
     * @return the short
     * @version
     */
    /**
     * 字符串转无符号短整数
     * 
     * @param value String
     * @return short
     */
    public static short strToUINT16(String value) {
        int temp = Integer.parseInt(value);
        return (short) (temp & 0xffff);
    }

    /**
     * 字符串转无符号整数.
     * 
     * @param value the value
     * @return the int
     * @return
     */
    public static int strToUINT32(String value) {
        long temp = Long.parseLong(value);
        return (int) (temp & 0xffffffff);
    }

    /**
     * String转16进制字符串.
     * 
     * @param value the value
     * @return the string
     * @return
     */
    public static String toHexString(String value) {
        return bytesToHexString(stringToBytes(value));

    }

    /**
     * short转16进制字符串.
     * 
     * @param value the value
     * @return the string
     * @return
     */
    public static String toHexString(short value) {
        return bytesToHexString(shortToBytes(value));

    }

    /**
     * int转16进制字符串.
     * 
     * @param value the value
     * @return the string
     * @return
     */
    public static String toHexString(int value) {
        return bytesToHexString(intToBytes(value));

    }

    /**
     * int转16进制字符串.
     * 
     * @param value the value
     * @param length the length
     * @return the string
     * @return
     */
    public static String toHexString(int value, short length) {
        String hexStr = bytesToHexString(intToBytes(value));
        hexStr = hexStr.substring(8 - length * 2);
        return hexStr;

    }

    /**
     * float转16进制字符串.
     * 
     * @param value the value
     * @return the string
     * @return
     */
    public static String toHexString(float value) {
        return bytesToHexString(floatToBytes(value));
    }

    /**
     * String转byte数组.
     * 
     * @param strValue the str value
     * @param charset the charset
     * @return the byte[]
     * @return
     */
    public static byte[] stringToBytes(String strValue, String charset) {
        try {
            return strValue.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * String to bytes.
     * 
     * @param strValue the str value
     * @return the byte[]
     * @version
     */
    public static byte[] stringToBytes(String strValue) {
        return strValue.getBytes();
    }

    /**
     * float转byte数组.
     * 
     * @param floatValue the float value
     * @return the byte[]
     * @return
     */
    public static byte[] floatToBytes(float floatValue) {
        int intValue = Float.floatToIntBits(floatValue);
        return intToBytes(intValue);
    }

    /**
     * int转byte数组.
     * 
     * @param intValue the int value
     * @return the byte[]
     * @return
     */
    public static byte[] intToBytes(int intValue) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
        }
        return b;
    }

    /**
     * int转byte数组.
     * 
     * @param shortValue the short value
     * @return the byte[]
     * @return
     */
    public static byte[] shortToBytes(short shortValue) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            b[i] = (byte) (shortValue >> 8 * (1 - i) & 0xFF);
        }
        return b;
    }

    /**
     * 将字节数组转成16进制字符串.
     * 
     * @param bs the bs
     * @return the string
     * @return
     */
    public static String bytesToHexString(byte[] bs) {
        if (bs == null || bs.length == 0) {
            return "";
        }
        StringBuffer str = new StringBuffer(bs.length * 4);
        for (int i = 0; i < bs.length; i++) {
            str.append(hex[(bs[i] >> 4) & 0x0f]);
            str.append(hex[bs[i] & 0x0f]);
        }
        return str.toString();
    }

    /**
     * 将字节数组转成字符串.
     * 
     * @param bytes the bytes
     * @return the string
     * @return
     */
    public static String byteToString(byte[] bytes) {
        return new String(bytes);
    }

    /**
     * 以16进制的方式按行打印字节数组.
     * 
     * @param bs ：要打印的字节数组，lineLength：每行字节数
     * @param lineLength the line length
     * @return the string
     * @return
     */
    public static String bytesToHexStringLine(byte[] bs, int lineLength) {
        if (bs == null || bs.length == 0) {
            return "";
        }
        StringBuffer str = new StringBuffer(bs.length * 4);
        for (int i = 0; i < bs.length; i++) {
            str.append(hex[(bs[i] >> 4) & 0x0f]);
            str.append(hex[bs[i] & 0x0f]);
            if (i > 0 && i % lineLength == lineLength - 1) {
                str.append("\r\n");
            } else {
                str.append(" ");
            }
        }
        return str.toString();
    }

    /**
     * 16进制字符串转可显示的字符串.
     * 
     * @param hexStr the hex str
     * @return the string
     * @return
     */
    public static String hexStrToStr(String hexStr) {
        String decStr = "";
        ByteBuffer bytes = ByteBuffer.allocate(hexStr.length() / 2);
        for (int i = 0; i < hexStr.length(); i += 2) {
            Byte b = (byte) (0xff & Integer.parseInt(hexStr.substring(i, i + 2), 16));
            bytes.put(b);
        }
        bytes.position();
        try {
            decStr = new String(bytes.array(), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decStr;
    }

    /**
     * Hex to bytes.
     * 
     * @param hexStr the hex str
     * @return the byte[]
     * @version
     */
    public static byte[] hexToBytes(String hexStr) {
        ByteBuffer bytes = ByteBuffer.allocate(hexStr.length() / 2);
        for (int i = 0; i < hexStr.length(); i += 2) {
            Byte b = (byte) (0xff & Integer.parseInt(hexStr.substring(i, i + 2), 16));
            bytes.put(b);
        }
        return bytes.array();
    }

    // *********************************end*********************************************************

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        Short temp = Short.parseShort("E0", 16);
        System.out.println(temp.byteValue());
        System.out.println(asciiToString("3031"));
        Integer t = 200;
        String bytes = t.toHexString(15);
        System.out.println(getHexString("0023", "9", 4));
    }
}
