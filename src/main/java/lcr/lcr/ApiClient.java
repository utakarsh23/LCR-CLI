package lcr;

import Question.TemplateGenerator;
import TestCases.TestCase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class ApiClient {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static TemplateGenerator templateGenerator = new TemplateGenerator();

    public static void getDailyQuestion() {
        String sessionId = SessionManager.getSession();

        if (sessionId == null) {
            System.out.println("Please login first using: lcr -login");
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/user/cli/daily-question"))
                .header("Cookie", "JSESSIONID=" + sessionId)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            System.out.println("Status: " + status);

            if (status == 200) {
                Map<String, List<TestCase>> questionMap = MAPPER.readValue(response.body(), new TypeReference<>() {});
                questionMap.forEach((link, testCases) -> {
                    System.out.println("Question Link: " + link);
                    for (TestCase testCase : testCases) {
                        System.out.println("  Input: " + testCase.getInput());
                        System.out.println("  Output: " + testCase.getOutput());
                    }
                });
            } else {
                System.out.println("Failed to fetch daily question. Server returned status " + status);
                System.out.println("Raw response body: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching question: " + e.getMessage());
        }
    }

    public static void fetchAndSetupDailyQuestion(int index, String lang) {
        String sessionId = SessionManager.getSession();

        if (sessionId == null) {
            System.out.println("Please login first using: lcr -login");
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8082/user/cli/daily-question"))
                .header("Cookie", "JSESSIONID=" + sessionId)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Map<String, List<TestCase>> questionMap = MAPPER.readValue(response.body(), new TypeReference<>() {
                });

                List<String> keys = questionMap.keySet().stream().toList(); // Converts to indexable list

                if (index >= keys.size()) {
                    System.out.println("Invalid Question(Index): " + index);
                    return;
                }

                String selectedLink = keys.get(index);
                List<TestCase> testCases = questionMap.get(selectedLink);

                if (testCases == null || testCases.isEmpty()) {
                    System.out.println("No test cases found for: " + selectedLink);
                    return;
                }

                System.out.println("Generating template for " + (index == 0 ? "daily assigned Question 1" : "daily assigned Question") + " -> " + selectedLink + " in language: " + lang);

                TemplateGenerator.generate(selectedLink, lang, testCases);
            } else {
                System.out.println("Failed to fetch daily question. Server returned status " + response.statusCode());
                System.out.println("Raw response body: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching question: " + e.getMessage());
        }
    }
}