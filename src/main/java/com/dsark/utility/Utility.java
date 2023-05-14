package com.dsark.utility;

import org.bson.Document;
import security.SecurityUtility;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * This is confidential, please don't share this information
 */
public class Utility {

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

}
