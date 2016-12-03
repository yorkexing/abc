package com.team.common.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team.common.util.ByteUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class TripleDes.
 */
public class TripleDes {

    /** The log. */
    private static Logger log = LoggerFactory.getLogger(TripleDes.class);
    // 默认加密算法
    /** The Constant DefaultAlgorithm. */
    private static final String DefaultAlgorithm = "DESede";
    // 默认Block模式
    /** The Constant DefaultBlockPattern. */
    private static final String DefaultBlockPattern = "ECB";
    // 默认Padding模式
    /** The Constant DefaultPaddingPattern. */
    private static final String DefaultPaddingPattern = "PKCS5Padding";

    // 加密算法,可用DES,DESede,Blowfish
    /** The algorithm. */
    private String algorithm;
    // Block模式
    /** The block pattern. */
    private String blockPattern;
    // Padding模式
    /** The padding pattern. */
    private String paddingPattern;

    /**
     * Instantiates a new triple des.
     */
    public TripleDes() {
        this.algorithm = DefaultAlgorithm;
        this.blockPattern = DefaultBlockPattern;
        this.paddingPattern = DefaultPaddingPattern;
    }

    /**
     * Instantiates a new triple des.
     * 
     * @param algorithm the algorithm
     * @param blockPattern the block pattern
     * @param paddingPattern the padding pattern
     */
    public TripleDes(String algorithm, String blockPattern, String paddingPattern) {
        this.algorithm = algorithm;
        this.blockPattern = blockPattern;
        this.paddingPattern = paddingPattern;
    }

    /**
     * 用key加密字符串，key的字节长度必须是16或24或32位。字符串的编解码默认采用utf-8，若有汉字务必注意一个汉字占用3个字节.
     * 
     * @param src the src
     * @param key the key
     * @return the string
     * @author David
     * @since: 2012-4-6
     */
    public String encrypt(String src, String key) {
        byte[] srcbyte = ByteUtil.stringToBytes(src);
        byte[] keybyte = ByteUtil.stringToBytes(key);
        byte[] destByte = encrypt(srcbyte, keybyte);
        return Base64.encrypt(destByte);
    }

    /**
     * Description:.
     * 
     * @param src the src
     * @param key the key
     * @return the byte[]
     * @author David
     * @since: 2012-4-6
     */
    public byte[] encrypt(byte[] src, byte[] key) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(key, algorithm);
            // 加密
            Cipher c = Cipher.getInstance(algorithm + "/" + blockPattern + "/" + paddingPattern);
            c.init(Cipher.ENCRYPT_MODE, deskey);
            return c.doFinal(src);
        } catch (Exception e) {
            log.error("encrypt error", e);
        }
        return null;
    }

    /**
     * Description:.
     * 
     * @param src the src
     * @param key the key
     * @return the string
     * @author David
     * @since: 2012-4-6
     */
    public String decrypt(String src, String key) {
        byte[] srcbyte = Base64.decrypt(src);
        byte[] keybyte = ByteUtil.stringToBytes(key);
        byte[] destByte = decrypt(srcbyte, keybyte);
        return ByteUtil.byteToString(destByte);
    }

    /**
     * Description:.
     * 
     * @param src the src
     * @param key the key
     * @return the byte[]
     * @author David
     * @since: 2012-4-6
     */
    public byte[] decrypt(byte[] src, byte[] key) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(key, algorithm);
            // 解密
            Cipher c1 = Cipher.getInstance(algorithm + "/" + blockPattern + "/" + paddingPattern);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.lang.Exception e) {
            log.error("decrypt error", e);
        }
        return null;
    }

}
