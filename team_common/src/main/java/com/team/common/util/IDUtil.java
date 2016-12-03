package com.team.common.util;

// TODO: Auto-generated Javadoc
/**
 * 描述信息:移动ID生成类.
 * 
 * @author David
 * @version 1.0
 */
public class IDUtil {
    
    /**
     * 描述信息:移动ID流水号部分生成算法，currentID为当前id的流水号部分，length指定流水号部分的长度.
     * 
     * @param currentID the current id
     * @param length the length
     * @return the next id
     * @throws Exception the exception
     * @author David
     */
    public static String getNextID(String currentID, int length) throws Exception {
        if (null == currentID) {
            currentID = "0";
            for (int i = 1; i < length; i++) {
                currentID += "0";
            }
        }
        String reg = "[0-9A-Z]{" + length + "}";
        if (!currentID.matches(reg)) {
            throw new Exception("不合法的id：" + currentID);
        }
        char[] asc = currentID.toCharArray();
        char step = (char) 1;
        char[] temp = null;
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                temp = asciiPlus(asc[length - i - 1], step);
                asc[length - i - 1] = temp[1];
            } else {
                temp = asciiPlus(asc[length - i - 1], temp[0]);
                asc[length - i - 1] = temp[1];
            }

        }
        return new String(asc);
    }

    /**
     * Ascii plus.
     * 
     * @param ch the ch
     * @param step the step
     * @return the char[]
     * @version
     */
    private static char[] asciiPlus(char ch, char step) {
        char start = '0';

        char temp = (char) (ch + step);
        if ('9' < temp && temp < 'A') {
            temp = 'A';
        }
        char t1 = (char) (temp / ('Z' + 1));// 进位
        char t2 = (char) (temp % ('Z' + 1));// 余数
        if (t2 < start) {
            t2 += start;
        }
        char[] t = {t1, t2};
        return t;
    }

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        String id = null;
        try {
            id = getNextID(null, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(id);
    }
}
