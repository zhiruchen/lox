package com.interpreter.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage: generate_ast <output_dir>");
			System.exit(1);
		}

		String output = args[0];

		defineAst(output, "Expr", Arrays.asList("Binary : Expr left, Token operator, Expr right",
				"Grouping : Expr expression", "Literal : Object value", "Unary : Token operator, Expr right"));

	}

	private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
		String path = outputDir + "/" + baseName + ".java";

		PrintWriter printer = new PrintWriter(path, "UTF-8");
		printer.println("package com.interpreter.lox;");
		printer.println("");
		printer.println("import java.util.List;");
		printer.println("");
		printer.println("abstract class " + baseName + " {");
		defineVisitor(printer, baseName, types);
		printer.println("}");

		// the ast class
		for (String type : types) {
			String className = type.split(":")[0].trim();
			String fields = type.split(":")[1].trim();
			defineType(printer, baseName, className, fields);
		}
		
		// the base accept method
		printer.println("");
		printer.println("  abstract <R> R accept(Visitor<R> visitor);");
		
		printer.close();
	}

	private static void defineType(PrintWriter printer, String baseName, String className, String fieldList) {
		printer.println("");
		printer.println("  static class " + className + " extends " + baseName + " {");
		printer.println("        " + className + "(" + fieldList + ") {");

		String[] fields = fieldList.split(", ");
		for (String field : fields) {
			String name = field.split(" ")[1];
			printer.println("    this." + name + " = " + name + ";");
		}
		printer.println("    }");
		
		// Visitor pattern
		printer.println();
		printer.println("    <R> R accept(Visitor<R> visitor) {");
		printer.println("      return visitor.visit" + className + baseName + "(this);");
		printer.println("    }");
		for (String field : fields) {
			printer.println("    final " + field + ";");
		}

		printer.println("    }");
	}
	
	private static void defineVisitor(PrintWriter printer, String baseName, List<String> types) {
		printer.println("  interface Visitor<R> {");
		
		for (String type : types) {
			String typeName = type.split(":")[0].trim();
			printer.println("    R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
		}
		
		printer.println("  }");
	}

}
