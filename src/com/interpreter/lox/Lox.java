package com.interpreter.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lox {
	static boolean hasError = false;
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("jlox [usage]");
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPromt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if(hasError) System.exit(65);
    }

    private static void runPromt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            run(buffer.readLine());
            hasError = false;
        }
    }

    private static void run(String source) {
    	
    }

    static void error(int line, String message) {
        report(line ,"", message);
    }

    static private void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] where: " + where + ": " + message
        );
        hasError = true;
    }


}
