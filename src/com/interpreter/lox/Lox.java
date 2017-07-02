package com.interpreter.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
		if (hasError)
			System.exit(65);
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
		Scanner scanner = new Scanner(source);
		List<Token> tokens = scanner.scanTokens();
		System.out.println(tokens);

		Parser parser = new Parser(tokens);
		Expr expr = parser.parse();

		if (hasError)
			return;

		System.out.println(new AstPrinter().print(expr));
	}

	static void error(int line, String message) {
		report(line, "", message);
	}

	static void error(Token token, String message) {
		if (token.type == TokenType.EOF) {
			report(token.line, " at end", message);
		} else {
			report(token.line, " at '" + token.lexeme + "'", message);
		}
	}

	static private void report(int line, String where, String message) {
		System.err.println("[line " + line + "] where: " + where + ": " + message);
		hasError = true;
	}
}
