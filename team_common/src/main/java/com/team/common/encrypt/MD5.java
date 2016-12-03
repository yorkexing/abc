package com.team.common.encrypt;

import java.security.MessageDigest;

// TODO: Auto-generated Javadoc
/**
 * The Class MD5.
 */
public class MD5 {

    /** The Constant hex. */
    private static final byte hex[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Encrypt.
     * 
     * @param s the s
     * @return the string
     * @version
     */
    public final static String encrypt(String s) {
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            byte str[] = new byte[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hex[byte0 >>> 4 & 0xf];
                str[k++] = hex[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Encrypt.
     * 
     * @param strTemp the str temp
     * @return the string
     * @version
     */
    public final static String encrypt(byte[] strTemp) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            byte str[] = new byte[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hex[byte0 >>> 4 & 0xf];
                str[k++] = hex[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        System.out.println(MD5.encrypt("admin"));
    }

}
