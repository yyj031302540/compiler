package com.pan.lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.pan.symbols.Type;

public class Lexer {
	public static int line = 1;
	char peek = ' ';
	char lpeek = ' ';
	Hashtable<String, Word> words = new Hashtable<String, Word>();
	List<String> list_word = new LinkedList<String>();
	int flag = 1; // 判断正数负数
	BufferedReader reader = null;

	void reserve(Word w) {
		words.put(w.lexme, w);
		list_word.add(w.toString(true));
	}

	// 输出符号表
	public void saveSymbolsTable() throws IOException {
		FileWriter writer = new FileWriter(".\\bin\\符号表.txt");
		/* 写入文件 */
		for (int i = 0; i < list_word.size(); ++i) {
			String tok = (String) list_word.get(i);
			writer.write(tok + "\r\n");
		}

		writer.flush();
		writer.close();
	}

	// 构造函数
	public Lexer() {
		try {
			reader = new BufferedReader(new FileReader(".\\bin\\输入.txt"));
		} catch (IOException e) {
			System.out.print(e);
		}
		
		// 保留字
		this.reserve(new Word("and", Tag.AND));
		this.reserve(new Word("array", Tag.ARRAY));
		this.reserve(new Word("begin", Tag.BEGIN));
		this.reserve(new Word("case", Tag.CASE));
		this.reserve(new Word("const", Tag.CONST));
		this.reserve(new Word("div", Tag.DIV));
		this.reserve(new Word("do", Tag.DO));
		this.reserve(new Word("downto", Tag.DOWNTO));
		this.reserve(new Word("else", Tag.ELSE));
		this.reserve(new Word("end", Tag.END));		
		this.reserve(new Word("file", Tag.FILE));
		this.reserve(new Word("for", Tag.FOR));
		this.reserve(new Word("forward", Tag.FORWARD));		
		this.reserve(new Word("function", Tag.FUNCTION));
		this.reserve(new Word("goto", Tag.GOTO));
		this.reserve(new Word("if", Tag.IF));		
		this.reserve(new Word("in", Tag.IN));		
		this.reserve(new Word("label", Tag.LABEL));
		this.reserve(new Word("mod", Tag.MOD));		
		this.reserve(new Word("nil", Tag.NIL));
		this.reserve(new Word("not", Tag.NOT));		
		this.reserve(new Word("of", Tag.OF));
		this.reserve(new Word("or", Tag.OR));		
		this.reserve(new Word("packed", Tag.PACKED));		
		this.reserve(new Word("procedure", Tag.PROCEDURE));
		this.reserve(new Word("program", Tag.PROGRAM));
		this.reserve(new Word("record", Tag.RECORD));		
		this.reserve(new Word("repeat", Tag.REPEAT));
		this.reserve(new Word("set", Tag.SET));		
		this.reserve(new Word("then", Tag.THEN));
		this.reserve(new Word("to", Tag.TO));
		this.reserve(new Word("type", Tag.TYPE));
		this.reserve(new Word("until", Tag.UNTIL));
		this.reserve(new Word("var", Tag.VAR));
		this.reserve(new Word("while", Tag.WHILE));
		this.reserve(new Word("with", Tag.WITH));			
		
		// 类型
		this.reserve(Word.True);
		this.reserve(Word.False);
		this.reserve(Type.Shortint);
		this.reserve(Type.Integer);
		this.reserve(Type.Longint);
		this.reserve(Type.Real);
		this.reserve(Type.Double);
		this.reserve(Type.Extended);
		this.reserve(Type.Byte);
		this.reserve(Type.Word);
		this.reserve(Type.Dword);
		this.reserve(Type.Boolean);
		this.reserve(Type.String);
	}

	void readch() throws IOException {
		if (peek != ' ' && peek != '\t' && peek != '\n')
			lpeek = peek;
		peek = (char) reader.read();
		// 转换为小写
		if (peek >= 'A' && peek <= 'Z')
			peek = (char) (peek + 'a' - 'A');
	}

	boolean readch(char c) throws IOException {
		readch();
		if (peek != c)
			return false;
		peek = ' ';
		return true;
	}

	public Token scan() throws IOException {
		for (;; readch()) {
			if (peek == ' ' || peek == '\t')
				continue;
			else if (peek == '\n')
				line += 1;
			else
				break;
		}
		
		switch (peek) {
		case ';':
			readch();
			return new Token(';');
		case '{':
			readch();
			return new Token('{');
		case '}':
			readch();
			return new Token('}');
		case '(':
			readch();
			return new Token('(');
		case ')':
			readch();
			return new Token(')');
		case '+': {
			if (lpeek == '+' || lpeek == '/' || lpeek == '*' || lpeek == '-' || lpeek == '=') {
				readch();
				return null;
			} else {
				readch();
			}
			return new Token('+');
		}
		case '*':
			readch();
			return new Token('*');
		case '/':
			readch();
			return new Token('/');
		case '=':
			readch();
			return new Token('=');
		case '<':
			if (readch('=')) {
				return Word.le;
			}
			if (peek == '>') {
				readch();
				return Word.ne;
			}
			return new Token('<');
		case '>':
			if (readch('=')) {
				return Word.ge;
			} else {
				return new Token('>');
			}
		case ':':
			if (readch('=')) {
				return Word.eq;
			} else {
				return new Token(':');
			}
		case '\'':
			readch();
			if (Character.isLetter(peek)) {
				StringBuffer b = new StringBuffer();
				for (;; readch()) {
					if (Character.isLetterOrDigit(peek)) {
						b.append(peek);
						continue;
					}
					if (peek == '\n') {
						System.out.println("error：字符串越行，行号：" + line);
						return null;
					}
					
					// 這裡感覺有bug!
					if (peek == '\'') {
						String s = b.toString();
						Word w = new Word(s, Tag.STRING);
						words.put(s, w);
						readch();
						return w;
					}
				}
			} else
				return new Token('\'');
		case '-':
			if (lpeek == '+' || lpeek == '/' || lpeek == '*' || lpeek == '-' || lpeek == '=') {
				flag = -1;
				for (;; readch())
					if (peek != ' ')
						break;
			} else
				flag = 1;
			if (readch('-')) {// 注释
				readch();
				StringBuffer b = new StringBuffer();
				for (;; readch()) {
					if (peek != '\n' && peek != '\r') {
						b.append(peek);
						continue;
					} else
						break;
				}
				return null;
			}
			if (Character.isDigit(peek)) {
				int v = 0;
				int scale = 10;
				do {
					v = scale * v + Character.digit(peek, scale);
					readch();
					if (peek == 'x') {
						scale = 36;
						readch();
					}
				} while (Character.isLetterOrDigit(peek));
				if (peek != '.') {
					v = v * flag;
					// 整數越界
					int su = 0;
					if (flag > 0) {
						if (v < 0) {
							su = 1;
						} else {
							int cnt = 0, tmp = v;
							for (; tmp > 0; cnt++)
								tmp /= 2;
							if (cnt > 24)
								su = 1;
						}
					}
					if (flag > 0 && su == 1) {
						System.out.println("error:integer outflow! 行号：" + line);
						return null;
					}
					return new Num(v);
				}
				float x = v;
				float d = (float) scale;
				for (;;) {
					readch();
					if (!Character.isDigit(peek))
						break;
					x = x + (float) Character.digit(peek, scale) / d;
					d = d * scale;
				}
				x = x * flag;
				return new Real(x);
			} else {
				if (flag == -1)
					return new Token('-');
				else {
					System.out.println("error:illegal \'-\' 行号：" + line);
					return null;
				}
			}

		}
		if (Character.isDigit(peek)) {
			int v = 0;
			int scale = 10;
			flag = 1;
			do {
				v = scale * v + Character.digit(peek, scale);
				readch();
				if (v == 0 && peek == 'x') {
					scale = 36;
					readch();
				}
			} while (Character.isLetterOrDigit(peek));
			if (peek != '.') {
				v = v * flag;
				int su = 0;
				if (flag > 0) {
					if (v < 0) {
						su = 1;
					} else {
						int cnt = 0, tmp = v;
						for (; tmp > 0; cnt++)
							tmp /= 2;
						if (cnt > 24)
							su = 1;
					}
				}
				if (flag > 0 && su == 1) {
					System.out.println("error:integer outflow! 行号：" + line);
					return null;
				}
				return new Num(v);
			}
			float x = v;
			float d = (float) scale;
			for (;;) {
				readch();
				if (!Character.isDigit(peek))
					break;
				x = x + (float) Character.digit(peek, scale) / d;
				d = d * scale;
			}
			x = x * flag;
			return new Real(x);
		}

		if (Character.isLetter(peek)) {
			StringBuffer b = new StringBuffer();
			do {
				b.append(peek);
				readch();
			} while (Character.isLetterOrDigit(peek));
			String s = b.toString();
			Word w = (Word) words.get(s);
			if (w != null) {
				return w;
			}
			if (s.length() > 32) {
				System.out.println("error:标识符太长！行号:" + line);
				return null;
			} else if (s.length() > 8) {
				System.out.println("warnning:标识符不是有效长度！" + line);
			}
			w = new Word(s, Tag.ID);
			words.put(s, w);
			list_word.add(w.toString(false));
			return w;
		}
		Token tok = new Token(peek);
		peek = ' ';
		return tok;
	}

}
