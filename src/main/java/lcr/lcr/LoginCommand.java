package lcr.lcr;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginCommand {

    public static void login(String email, String password) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        String body = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/public/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String setCookie = response.headers().firstValue("Set-Cookie").orElse(null);

            if (setCookie != null && setCookie.contains("JSESSIONID")) {
                String sessionId = setCookie.split(";")[0].split("=")[1];
                SessionManager.saveSession(sessionId);
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed: No session ID returned");
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Login failed: " + e.getMessage());
        }
    }
}