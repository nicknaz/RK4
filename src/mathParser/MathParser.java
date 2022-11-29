package mathParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MathParser {
    private HashMap<String, Double> variables;
    private HashMap<String, LexemeBuffer> lexemesMemory;
    public MathParser(HashMap<String, Double> variables) {
        this.variables = variables;
        lexemesMemory = new HashMap<>();
    }

    public double parse(String expression){
        LexemeBuffer lexemeBuffer;
        if(lexemesMemory.get(expression) == null){
            ArrayList<Lexeme> lexemes = lexicalAnalyze(expression);
            lexemeBuffer = new LexemeBuffer(lexemes);
            lexemesMemory.put(expression, lexemeBuffer);
        } else {
            lexemeBuffer = lexemesMemory.get(expression);
        }

        double result = expr(lexemeBuffer);

        return result;
    }

    public double parse(String expression, HashMap<String, Double> variables){
        this.variables = variables;
        ArrayList<Lexeme> lexemes = lexicalAnalyze(expression);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);

        double result = expr(lexemeBuffer);

        return result;
    }



    private enum LexemeType {
        NUMBER, VARIABLE,
        RIGHT_BRACKET, LEFT_BRACKET,
        OP_PLUS, OP_MINUS, OP_MUL, OP_DIV,
        UN_MINUS,
        SIN, COS, LN, SQRT, PI,
        EOF;
    }

    private class Lexeme {
        private LexemeType type;
        private String value;

        public Lexeme(LexemeType type, String value) {
            this.type = type;
            this.value = value;
        }

        public Lexeme(LexemeType type, Character value) {
            this.type = type;
            this.value = value.toString();
        }

        @Override
        public String toString() {
            return "Lexeme{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public static class LexemeBuffer {
        private int iterator;

        public List<Lexeme> lexemes;

        public LexemeBuffer(List<Lexeme> lexemes) {
            this.lexemes = lexemes;
        }

        public Lexeme next() {
            return lexemes.get(iterator++);
        }

        public void back() {
            iterator--;
        }

        public int getPos() {
            return iterator;
        }
    }

    private ArrayList<Lexeme> lexicalAnalyze(String expression){
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int iterator = 0;

        while (iterator < expression.length()) {
            char character = expression.charAt(iterator);
            switch (character){
                case '(':
                    lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET, character));
                    iterator++;
                    continue;
                case ')':
                    lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET, character));
                    iterator++;
                    continue;
                case '+':
                    lexemes.add(new Lexeme(LexemeType.OP_PLUS, character));
                    iterator++;
                    continue;
                case '-':
                    boolean condition = lexemes.isEmpty()
                            || lexemes.get(lexemes.size()-1).type != LexemeType.RIGHT_BRACKET
                            && lexemes.get(lexemes.size()-1).type != LexemeType.NUMBER;
                    if (condition){
                        lexemes.add(new Lexeme(LexemeType.UN_MINUS, character));
                    } else {
                        lexemes.add(new Lexeme(LexemeType.OP_MINUS, character));
                    }

                    iterator++;
                    continue;
                case '*':
                    lexemes.add(new Lexeme(LexemeType.OP_MUL, character));
                    iterator++;
                    continue;
                case '/':
                    lexemes.add(new Lexeme(LexemeType.OP_DIV, character));
                    iterator++;
                    continue;
                default:
                    if(character <= '9' && character >= '0'){
                        StringBuilder sb = new StringBuilder();
                        do{
                            sb.append(character);
                            iterator++;
                            if(iterator >= expression.length()){
                                break;
                            }
                            character = expression.charAt(iterator);
                        }while(character <= '9' && character >= '0' || character == '.');
                        int countDot = (sb.length() - sb.toString().replace(".","").length());
                        if(countDot > 1){
                            throw new RuntimeException("Unexpected character: " + sb);
                        }
                        if(sb.charAt(sb.length()-1) == '.'){
                            sb.append("0");
                        }
                        lexemes.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
                    } else if(character <= 'Z' && character >= 'A' || character <= 'z' && character >= 'a'){
                        StringBuilder sb = new StringBuilder();
                        do{
                            sb.append(character);
                            iterator++;
                            if(iterator >= expression.length()){
                                break;
                            }
                            character = expression.charAt(iterator);
                        }while(character <= 'Z' && character >= 'A' || character <= 'z' && character >= 'a');
                        if(variables.containsKey(sb.toString())){
                            lexemes.add(new Lexeme(LexemeType.VARIABLE, sb.toString()));
                        } else {
                            switch(sb.toString().toLowerCase()){
                                case "sin":
                                    lexemes.add(new Lexeme(LexemeType.SIN, sb.toString()));
                                    break;
                                case "cos":
                                    lexemes.add(new Lexeme(LexemeType.COS, sb.toString()));
                                    break;
                                case "ln":
                                    lexemes.add(new Lexeme(LexemeType.LN, sb.toString()));
                                    break;
                                case "sqrt":
                                    lexemes.add(new Lexeme(LexemeType.SQRT, sb.toString()));
                                    break;
                                case "pi":
                                    lexemes.add(new Lexeme(LexemeType.PI, sb.toString()));
                                    break;
                                default:
                                    throw new RuntimeException("Unexpected character: " + sb.toString());
                            }
                        }
                        continue;
                    } else if (character != ' ') {
                        throw new RuntimeException("Unexpected character: " + character);
                    } else {
                        iterator++;
                    }
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
    }

    private double expr(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return 0;
        } else {
            lexemes.back();
            return plusMinus(lexemes);
        }
    }

    private double plusMinus(LexemeBuffer lexemes) {
        double value = multDiv(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case OP_PLUS:
                    value += multDiv(lexemes);
                    break;
                case OP_MINUS:
                    value -= multDiv(lexemes);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                    lexemes.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + lexemes.getPos());
            }
        }
    }

    private double multDiv(LexemeBuffer lexemes) {
        double value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case OP_MUL:
                    value *= factor(lexemes);
                    break;
                case OP_DIV:
                    value /= factor(lexemes);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                case OP_PLUS:
                case OP_MINUS:
                    lexemes.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + lexemes.getPos());
            }
        }
    }

    private double factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        double value;
        switch (lexeme.type) {
            case UN_MINUS:
                value = factor(lexemes);
                return -value;
            case NUMBER:
                return Double.parseDouble(lexeme.value);
            case VARIABLE:
                return variables.get(lexeme.value);
            case PI:
                return Math.PI;
            case SIN:
                return Math.sin(factor(lexemes));
            case COS:
                return Math.cos(factor(lexemes));
            case LN:
                return Math.log(factor(lexemes));
            case SQRT:
                return Math.sqrt(factor(lexemes));
            case LEFT_BRACKET:
                value = plusMinus(lexemes);
                lexeme = lexemes.next();
                if (lexeme.type != LexemeType.RIGHT_BRACKET) {
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + lexemes.getPos());
                }
                return value;
            default:
                throw new RuntimeException("Unexpected token: " + lexeme.value
                        + " at position: " + lexemes.getPos());
        }
    }
}
