package TestCases;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class VariableGenerator {
    private final Map<String, Map<String, String>> config;

    public VariableGenerator() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("conversion-config.json")) {
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(is, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load conversion-config.json", e);
        }
    }

    public String generateDeclaration(String name, String type, String rawValue) {
        if (!config.containsKey(type)) {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }

        Map<String, String> typeConfig = config.get(type);
        String declarationTemplate = typeConfig.get("javaDeclaration");
        String parsedValue = typeConfig.get("parseValue").replace("${value}", rawValue);

        return declarationTemplate
                .replace("${name}", name)
                .replace("${value}", parsedValue);
    }

    public static void main(String[] args) {
        VariableGenerator gen = new VariableGenerator();

        // Sample test case
        System.out.println(gen.generateDeclaration("nums", "int[]", "1, 2, 3"));
        System.out.println(gen.generateDeclaration("k", "int", "10"));
        System.out.println(gen.generateDeclaration("grid", "int[][]", "{ {1, 2}, {3, 4} }"));
        System.out.println(gen.generateDeclaration("words", "List<String>", "\"apple\", \"banana\""));
    }
}