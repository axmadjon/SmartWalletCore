package org.ethereum.crypto;

import org.ethereum.crypto.jce.SpongyCastleProvider;

import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;

import static java.util.Arrays.copyOfRange;

public class HashUtil {

    private static final Provider CRYPTO_PROVIDER;

    static {
        Security.addProvider(SpongyCastleProvider.getInstance());
        CRYPTO_PROVIDER = Security.getProvider("SC");

    }

    public static byte[] sha3(byte[] input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("ETH-KECCAK-256", CRYPTO_PROVIDER);
            digest.update(input);
            return digest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static byte[] sha3omit12(byte[] input) {
        byte[] hash = sha3(input);
        return copyOfRange(hash, 12, hash.length);
    }

}
