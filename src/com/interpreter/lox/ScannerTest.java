package com.interpreter.lox;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScannerTest {
	
	List<String> sourceList = new ArrayList<>();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testScanTokens() throws IOException {
		readSource();
		
		for (String line : sourceList) {
			System.out.println(line);
			
			Scanner scanner = new Scanner(line);
			printTokens(scanner.scanTokens());
		}
	}
	
	private void readSource() throws IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream("code.txt")));
		
		String line;
		while ((line = buffer.readLine()) != null) {
			sourceList.add(line);
		}
		
		buffer.close();
	}
	
	private void printTokens(List<Token> tokenList) {
		for (Token token : tokenList) {
			System.out.println(token);
		}
	}

}
