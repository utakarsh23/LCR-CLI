package lcr.TestCases;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;


//use for future ref
public class TestCaseRunner {
    public static void runTestCases(String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<TestCase> cases = mapper.readValue(new File(path), new TypeReference<>() {});

        for (TestCase testCase : cases) {
            String input = TestCaseCryptoUtils.decrypt(testCase.getInput());
            String output = TestCaseCryptoUtils.decrypt(testCase.getOutput());

            System.out.println("Running test case " + testCase.getId());
            System.out.println("Decrypted Input: " + input);
            System.out.println("Expected Output: " + output);

            // You can now run user code with this input and compare with expected output, implement login when created
        }
    }
}