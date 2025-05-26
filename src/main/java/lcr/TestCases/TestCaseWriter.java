package lcr.TestCases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestCaseWriter {
    public static void writeEncryptedTestCases(List<TestCase> testCases, String path) throws Exception {
        List<TestCase> encryptedCases = new ArrayList<>();
        long id = 1;
        for (TestCase testCase : testCases) {
            TestCase encrypted = new TestCase();
            encrypted.setId(id++);
            encrypted.setInput(TestCaseCryptoUtils.encrypt(testCase.getInput()));
            encrypted.setOutput(TestCaseCryptoUtils.encrypt(testCase.getOutput()));
            encryptedCases.add(encrypted);
        }

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        writer.writeValue(new File(path), encryptedCases);
    }
}