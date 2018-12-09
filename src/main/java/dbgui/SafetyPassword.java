package dbgui;

import org.mindrot.jbcrypt.BCrypt;

public class SafetyPassword {
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPass(String plainPassword, String hashedPassword) throws IllegalArgumentException {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
