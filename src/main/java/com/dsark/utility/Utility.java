package com.dsark.utility;

import com.logging.MyLogger;
import org.bson.Document;
import security.SecurityUtility;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

/**
 * This is confidential, please don't share this information
 */
public class Utility {

    final static String workbenchFileName = "workbench.properties";
    private static final String COMPANY_NAME = "dsark";
    private static final byte[] SALT = {127, 10, 19, -128, -5, 90, 16, 23, 12, 27, 0, -70, 8, 9, 10, -94};

    public static boolean isStringEmptyOrNull(String st) {
        return st == null || st.isEmpty();
    }

    public static boolean isStringNonEmpty(String st) {
        return st != null && !st.isEmpty();
    }

    public static boolean docIsNonEmpty(Document document) {
        return document != null && !document.isEmpty();
    }

    public static void setCustomLogFile(String xmlFile) {
        if (isStringEmptyOrNull(xmlFile))
            xmlFile = "log4j-test.xml";
        System.setProperty("log4j.configurationFile", xmlFile);
    }

    private static byte[] getSalt() {
        return new byte[]{127, 10, 19, -128, -5, 90, 16, 23, 12, 27, 0, -70, 8, 9, 10, -94};
    }

    public static String getEncryptString(String st) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        return SecurityUtility.encrypt(st, COMPANY_NAME, SALT);
    }
    public static String getDecryptString(String encrptyString) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        return SecurityUtility.decrypt(encrptyString, COMPANY_NAME, SALT);
    }

    public static String getWorkbenchProperty(String key, String defaultValue) {
        try (FileReader reader = new FileReader(workbenchFileName)) {
            Properties p = new Properties();
            p.load(reader);
            return (p.getProperty(key, String.valueOf(defaultValue)));
        } catch (Exception e) {
            MyLogger.error(e);
        }
        return defaultValue;
    }
    public static boolean getWorkbenchProperty(String key, boolean defaultValue){
        String workbenchValue = getWorkbenchProperty(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(workbenchValue);
    }

    private static void writeOnProperties(String keyValue, String value, String fileName) {
        try {
            MyLogger.info("Writing on file: "+fileName+" ,Key: "+keyValue+" ,Value: "+value);
            FileInputStream in = new FileInputStream(fileName);
            Properties props = new Properties();
            props.load(in);
            in.close();
            FileOutputStream out = new FileOutputStream(fileName);
            props.setProperty(keyValue, value);
            props.store(out, null);
            out.close();
        } catch (UnknownHostException e) {
            MyLogger.error(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setWorkbenchProperty(String key, String val){
        writeOnProperties(key, val, workbenchFileName);
    }

    public static void main(String[] args) {
        //setWorkbenchProperty("abcd","12");
        setCustomLogFile(null);
        MyLogger.info("abc");
        MyLogger.error("abc");
    }

}
