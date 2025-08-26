package southwest.monsoon.module.common.utils;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.util.Scanner;

public class SecretEncryptorUtils {
    public static void consoleRun() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please input password:");
        String password = scanner.nextLine();
        System.out.print("Please input salt:");
        String salt = scanner.nextLine();
        TextEncryptor encryptor = Encryptors.text(password, salt);

        while (true) {
            System.out.println();
            System.out.print("Please input plain text:");
            String plainText = scanner.nextLine();
            System.out.println("Encrypted text: " + encryptor.encrypt(plainText));
        }
    }
}
