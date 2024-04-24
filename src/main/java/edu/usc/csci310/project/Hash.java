package edu.usc.csci310.project;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public interface Hash {
    public static String hash(String input) {
        return DigestUtils.sha256Hex(input);
    }
}
