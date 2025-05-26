package lcr.lcr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SessionManager {
    private static final String SESSION_FILE = System.getProperty("user.home") + "/.lcrsession";

    public static void saveSession(String sessionId) {
        try {
            Files.writeString(Paths.get(SESSION_FILE), sessionId);
            System.out.println("Saved session ID: " + sessionId);
        } catch (IOException e) {
            System.err.println("Failed to save session: " + e.getMessage());
        }
    }

    public static String getSession() {
        try {
            String sessionId = Files.readString(Paths.get(SESSION_FILE)).trim();
            System.out.println("Loaded session ID: " + sessionId);
            return sessionId;
        } catch (IOException e) {
            System.err.println("Failed to read session: " + e.getMessage());
            return null;
        }
    }
}