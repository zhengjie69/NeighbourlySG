package com.nusiss.neighbourlysg.util;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

    private static final String PRIVATE_KEY =
                    "MIIEugIBADANBgkqhkiG9w0BAQEFAASCBKQwggSgAgEAAoIBAQCrQQ7YTCB+algR" +
                    "4EpxLAlfPZIWuKdVFbWEHmVyGnPuUhPiuKMPxmUHvBMmrqkN4OVDo5HQUh+Gu8dE" +
                    "Wxk9BOJBPfw1aw2QlHYH49CcNc7UX/9jeggMz/pzqg/EWWQBFhKhtir557tyUwjZ" +
                    "AANAgW8+m7bzQ1+AIlNfOIVot2y46CXXoNBFG5Hq1x96bjlFBwBSM9oFDr2LL9wD" +
                    "unuQSYtm+Oc1FO6D3wxqx95BmFjlJJBAbxIPxgrVu0Vlq+mybe8Ca7dn/AxcALtk" +
                    "J3hgfkFOs/0zFEc+I+uH9TifKizoAvItM5ZwLGkkOjMdg6UIxqvdWI3MqBRz4dfQ" +
                    "Az1UdoOhAgMBAAECgf9TiQiUuY2gHH+ZOK8XRUPNEVuRecEl39ELAUi6v+yM87x/" +
                    "e1EPANeHTlaJJ+mOu4UZ2U/nLifysOYikc1d1mQHLxU3eWDEhaaNcrLvf8/ECI+r" +
                    "FnuWxFyGzw2h2XjOjtXHy/Rqsn7c5WQLddYgQQLip1I2l0aeWiAQ8qEg01hVWRnj" +
                    "Z0TMrtVzEJZl8I3eMF4UYKhlvpeYGOrv29d93JPKyiV/kAk4vjg3C4gRaej4IMG2" +
                    "HQH2jKOiOJFmQP5y9XibfqeiZLalfzVafHG1ZWg7PdyzPj63sDvWe7OBP5Ma8wcW" +
                    "k4hNPNhEtk57hVs5c7aqyv3TuAZ2M27Zxi2xL+ECgYEAtl3wkHun+Rf7Uy/Ckqej" +
                    "KsX9lQlZ/oL9/kIipw2f9bC74clOiVz238f0aS7YrOqxCq5m2bfhauvjL6I6W53U" +
                    "Foll8Jjg+ipENpzwEWfEQRND1RJmOH9cgmk4nJjp2GnqrV47ZmN2vpjiXDHU3/cu" +
                    "rErBcZax6VlDTwaQBrdrahECgYEA8GZ1wc4pxzDbNbBZO+TmTGmgpEJgdY+fQ2tv" +
                    "y5eDxhXWpP2rKV+8eZlQhHkbdN3tzioAmIoAmmm/Ei9vuTpeC5aP36hP6mSObwwB" +
                    "vzy0o3ZUCHidxQYnm0rhbi3Rdt/vgfoQSOZ8ehly2VcmxZc9aSmDDZ1QasxA9dio" +
                    "Mta4cJECgYBSG5uLQdGQCmcivhcptoZ1ZK1Wo948BDhdeONnOXAyy37cGqncmNmi" +
                    "ScHWdbwipoxNldZC4lKAtHIWaUFDLJ1oBDa8i+iHWe5adHB2NbUb45Of5GfXuigp" +
                    "ozxU7VgkExQROKMf51p4WBdakSkaKY1/Dy1negTiXkk61GaWT5DyMQKBgGPPhpDC" +
                    "0iG7rSfrYgE2FlAfguzwCjCFQvCxCKeU7scthgLWt1vkPdKWzxFRXw70nal9UuYC" +
                    "ViHGSeh4/YAANc/F4VJRBW0h9ukwKQGHaNB9boJd4dNNrOATBOnD6DXc4AotEtnR" +
                    "qFjhfAe04YokMwEYlSrv59WPnGD2ZU9GSymhAoGAHiPjdHG2huWZUFv5h6PrEw8y" +
                    "uitABDKFO3bjNlRNAZxhPr2FDXJiZ66XpU61+D8hrO7OZ7A2FIMWztcCph6nc+am" +
                    "i6aUB3bMC4QM8SkpGikbuWoJFuqcty6sAlqts4WcvyUqxvRCdqdT9mrv73p1HLqy" +
                    "EAyoNVK9HWe79ifkbOI=";


    public static String decrypt(String encryptedData) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }
}
