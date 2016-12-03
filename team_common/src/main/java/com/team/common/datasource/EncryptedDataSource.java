package com.team.common.datasource;

import com.team.common.encrypt.TripleDes;

// TODO: Auto-generated Javadoc
/**
 * md5加密 数据库密码.
 * 
 * @author gogogo119
 */
public class EncryptedDataSource {

    /** The key. */
    private final String key = "QE!@^&0(J6H#$%DRN*$v7rnt";

    /** The des. */
    private final TripleDes des = new TripleDes();

    /**
     * Decrpt password.
     * 
     * @param password the password
     * @return the string
     * @version
     */
    private String decrptPassword(String password) {
        String dePassword = null;
        try {
            dePassword = des.decrypt(password, key);
        } catch (Exception e) {
        }
        return dePassword;
    }

    /**
     * Encrypt password.
     * 
     * @param password the password
     * @return the string
     * @version
     */
    private String encryptPassword(String password) {
        String dePassword = null;
        try {
            dePassword = des.encrypt(password, key);
        } catch (Exception e) {
        }
        return dePassword;
    }

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {

        EncryptedDataSource de = new EncryptedDataSource();

        System.out.println(de.encryptPassword(""));
        System.out.println(de.decrptPassword("sePYRULoROV63bh3i8h+ap=="));
    }
}
