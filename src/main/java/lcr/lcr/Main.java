package lcr;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("   Usage:");
            System.out.println("  -login <email> <password>");
            System.out.println("  -getDailyQues1 --lang <language>");
            System.out.println("  -getDailyQues2 --lang <language>");
            return;
        }

        System.out.println(Arrays.stream(args).toList());
        switch (args[0]) {
            case "-login" -> {
                if (args.length != 3) {
                    System.out.println("Usage: -login <email> <password>");
                    return;
                }
                LoginCommand.login(args[1], args[2]);
            }

            case "-getDailyQues1" -> {
                if (args.length != 3 || !args[1].equals("--lang")) {
                    System.out.println("Usage: -getDailyQues1 --lang <language>");
                    return;
                }
                ApiClient.fetchAndSetupDailyQuestion(0, args[2]);
            }

            case "-getDailyQues2" -> {
                if (args.length != 3 || !args[1].equals("--lang")) {
                    System.out.println("Usage: -getDailyQues2 --lang <language>");
                    return;
                }
                ApiClient.fetchAndSetupDailyQuestion(1, args[2]);
            }

            default -> System.out.println("Unknown command: " + args[0]);
        }
    }
}