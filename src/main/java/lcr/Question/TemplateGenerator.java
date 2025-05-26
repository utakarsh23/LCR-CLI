package lcr.Question;



import lcr.TestCases.TestCase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TemplateGenerator {

    public static void generate(String questionUrl, String lang, List<TestCase> testCases) {
        try {
            File dir = new File("DailyQues");
            if (dir.exists()) {
                for (File file : dir.listFiles()) {
                    if (!file.delete()) {
                        System.err.println("Failed to delete existing file: " + file.getName());
                    }
                }
            } else {
                dir.mkdir();
            }

            // Determine filename by language
            String filename;
            switch (lang.toLowerCase()) {
                case "java" -> filename = "Submission.java";
                case "cpp" -> filename = "Submission.cpp";
                case "python", "py" -> filename = "submission.py";
                default -> {
                    System.out.println("Unsupported language: " + lang);
                    return;
                }
            }

            StringBuilder testCasesSb = new StringBuilder();
            if(testCases != null) {
                for(int i = 0; i < testCases.size() - (testCases.size()-3); i++) {
                    TestCase testCase = testCases.get(i);
                    testCasesSb.append("Input : ").append(testCase.getInput()).append(" \nOutput :").append(testCase.getOutput()).append("\n");
                }
            }
            // Generate code file with basic template
            File codeFile = new File(dir, filename);
            try (FileWriter writer = new FileWriter(codeFile)) {
                switch (lang.toLowerCase()) {
                    case "java" -> writer.write(
                            "/*"+questionUrl + "\n" + testCasesSb + " */\n"+ """
                        public class Submission {
                            public static void main(String[] args) {
                                // Your code here
                            }
                        }
                        """);
                    case "cpp" -> writer.write(
                            "/*"+questionUrl + "\n" + testCasesSb + " */\n"+ """
                        #include <iostream>
                        using namespace std;

                        int main() {
                            // Your code here
                            return 0;
                        }
                        """);
                    case "python", "py" -> writer.write(
                            "/*"+questionUrl + "\n" + testCasesSb + " */\n"+ """
                        def main():
                            # Your code here
                            pass

                        if __name__ == "__main__":
                            main()
                        """);
                }
            }

            // Write test cases to file
            File testFile = new File(dir, "testcases.txt");
            try (FileWriter testWriter = new FileWriter(testFile)) {
                for (TestCase testCase : testCases) {
                    testWriter.write("Input: " + testCase.getInput() + "\n");
                    testWriter.write("Output: " + testCase.getOutput() + "\n\n");
                }
            } catch (IOException e) {
                System.err.println("Failed to write testcases: " + e.getMessage());
            }

            // Generate simple config.json with placeholder data
            File configFile = new File(dir, "config.json");
            String configJson = """
                    {
                      "language": "%s",
                      "methodName": "solve",
                      "arguments": [
                        { "name": "arg1", "type": "int[]" }
                      ],
                      "returnType": "int"
                    }
                    """.formatted(lang.toLowerCase());

            try (FileWriter configWriter = new FileWriter(configFile)) {
                configWriter.write(configJson);
            } catch (IOException e) {
                System.err.println("Failed to write config.json: " + e.getMessage());
            }

            System.out.println("Generated files in: " + dir.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error generating template files: " + e.getMessage());
        }
    }
}