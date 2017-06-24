package com.interpreter.lox;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.interpreter.lox.TokenType.*;


public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and",  AND);
        keywords.put("class",  CLASS);
        keywords.put("else",  ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for", FOR);
        keywords.put("fun",  FUN);
        keywords.put("if",  IF);
        keywords.put("nil",  NIL);
        keywords.put("or", OR);
        keywords.put("print",  PRINT);
        keywords.put("return",  RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",  THIS);
        keywords.put("true",  TRUE);
        keywords.put("var",  VAR);
        keywords.put("while",  WHILE);
    }


    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '!': addToken(match('=')?BANG_EQUAL : BANG); break;  // ! or !=
            case '=': addToken(match('=')?EQUAL_EQUAL : EQUAL); break;  // = or ==
            case '<': addToken(match('=')?LESS_EQUAL : LESS); break;  // < or <=
            case '>': addToken(match('=')?GREATER_EQUAL : GREATER); break;  // > or >=
            case '/':
                if (match('/')) {
                    // a comment goes util the end of line
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':case '\r':case '\t':break;
            case '\n': line++;break;
            case '"': string();break;

            default:
                if (isDigits(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                }
                else {
                    Lox.error(line, "unexpected character");
                }
                break;
        }
    }

    // 消耗所有找到的字面量整数数字, 然后寻找后面的分数部分(.后跟着至少一个数字)
    private void number() {
        while (isDigits(peek())) advance();

        if (peek() == '.' && isDigits(peekNext())) {
            advance();

            while (isDigits(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    // 识别标识符
    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        // 查看标识符是不是一个保留字
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private char peekNext() {
        if (current+1 >= source.length()) return '\0';
        return source.charAt(current+1);
    }

    private char advance() {
        current++;
        return source.charAt(current-1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    // conditional advance
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek() {
        if (current >= source.length()) return '\0';
        return source.charAt(current);
    }

    // consume the characters util the " that end the string
    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if(peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        advance(); // 消耗最后一个双引号

        //
        String value = source.substring(start+1, current-1);
        addToken(STRING, value);

    }

    //
    private boolean isDigits(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigits(c);
    }


}
