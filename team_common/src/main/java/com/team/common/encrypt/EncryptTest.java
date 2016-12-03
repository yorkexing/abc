package com.team.common.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * 描述信息:.
 * 
 * @author David
 * @version 1.0
 */
public class EncryptTest {

    /** The log. */
    private static Logger log = LoggerFactory.getLogger(EncryptTest.class);

    /**
     * Triple des test.
     * 
     * @version
     */
    public static void tripleDesTest() {

        TripleDes des = new TripleDes();
        String key = "QE!@^&0(J6H#$%DRN*$v7rnt";
        String szSrc = "Zte0744~";

        log.info("加密前的字符串:" + szSrc);
        String encoded = des.encrypt(szSrc, key);
        log.info("加密后的字符串:" + encoded);

        String dest = des.decrypt(encoded, key);
        log.info("解密后的字符串:" + dest);
    }

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        tripleDesTest();
    }
}
