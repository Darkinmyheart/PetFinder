package vn.finder.pet.common;

import java.security.SecureRandom;

/**
 * Chứa các phương thức random
 */
public class RandomCommon {
    static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Tạo chuỗi random
     * @param soluongChar số lượng kí tự trong chuỗi
     * @return StringBuilder
     */
    public static StringBuilder randomString(int soluongChar){
        StringBuilder string = new StringBuilder(soluongChar);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < soluongChar; i++) {
            int randomIndex = random.nextInt(characters.length());
            string.append(characters.charAt(randomIndex));
        }
        return string;
    }
}
