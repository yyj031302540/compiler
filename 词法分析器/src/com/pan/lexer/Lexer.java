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
	char leftPeek = ' ';
	int sign = 1; // ���ţ��ж���������
	BufferedReader reader = null;

	// �洢������
	Hashtable<String, Word> words = new Hashtable<String, Word>();
	List<String> listWord = new LinkedList<String>();

	void reserve(Word w) {
		words.put(w.lexme, w);
		listWord.add(w.toString(true));
	}

	// ���캯��
	public Lexer() {
		try {
			reader = new BufferedReader(new FileReader(".\\bin\\����.txt"));
		} catch (IOException e) {
			System.out.print(e);
		}

		// ������
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

		// ����
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

	// ��ȡһ���ַ�
	void readch() throws IOException {
		// �ų��հ׷������������⣡" -   3 "���ȡ��ʲô��
		if (peek != ' ' && peek != '\t' && peek != '\n') {
			leftPeek = peek;
		}
		// ��ȡһ���ַ�
		peek = (char) reader.read();
		// ת��ΪСд
		if (peek >= 'A' && peek <= 'Z') {
			peek = (char) (peek + 'a' - 'A');
		}
	}

	// ��ȡһ���ַ����ж��Ƿ��������ַ����
	boolean readch(char c) throws IOException {
		readch();
		if (peek != c) {
			return false;
		} else {
			peek = ' ';
			return true;
		}
	}

	// ���������������ֵ����
	public Token stringToDigit() throws IOException {
		int intResult = 0;
		// Ĭ�Ͻ���ȡ������ת��Ϊ10����
		int intScale = 10;
		do {
			intResult = intScale * intResult + Character.digit(peek, intScale);
			readch();
			if (peek == 'x') {
				if (intResult == 0) {
					intScale = 36;
					readch();
				} else {
					System.out.println("error���Ƿ���ʶ�����кţ�" + line);
				}

			}
		} while (Character.isLetterOrDigit(peek));

		// �����һλ����С���㣬�������ֲ���С��
		if (peek != '.') {
			intResult = intResult * sign;
			// �����Ƿ�Խ��
			boolean isOutflow = false;
			if (sign > 0) {
				if (intResult < 0) {
					isOutflow = true;
				} else {
					int count = 0, temp = intResult;

					// ͨ�������Ķ��������Ƶ�0Ϊֹ�����ж��м�λ
					for (; temp > 0; count++)
						temp = temp >> 1;
					if (count > 24)
						isOutflow = true;
				}
			}
			if (sign > 0 && isOutflow) {
				System.out.println("error:integer outflow! �кţ�" + line);
				return null;
			}
			return new Num(intResult);
		} else {
			// �����һλ��С���㣬��������Ϊ������
			float floatResult = intResult;
			float floatScale = (float) intScale;
			for (;;) {
				readch();
				if (!Character.isDigit(peek))
					break;
				floatResult = floatResult + (float) Character.digit(peek, intScale) / floatScale;
				floatScale = floatScale * intScale;
			}
			floatResult = floatResult * sign;
			return new Real(floatResult);
		}
	}

	// ��ȡ��Ч�ַ�
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
			if (leftPeek == '+' || leftPeek == '/' || leftPeek == '*' || leftPeek == '-' || leftPeek == '=') {
				readch();
				return null;
			} else {
				readch();
				return new Token('+');
			}
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
			} else if (peek == '>') {
				readch();
				return Word.ne;
			} else {
				return new Token('<');
			}
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
				StringBuffer stringBuffer = new StringBuffer();
				for (;; readch()) {
					if (Character.isLetterOrDigit(peek)) {
						stringBuffer.append(peek);
						continue;
					}

					if (peek == '\n') {
						System.out.println("error���ַ���Խ�У��кţ�" + line);
						return null;
					}

					// ����о���bug! <- ��ȷ��bug��
					if (peek == '\'') {
						String string = stringBuffer.toString();
						Word word = new Word(string, Tag.STRING);
						words.put(string, word);
						readch();
						return word;
					}
				}
			} else
				return new Token('\'');
		case '-':
			if (leftPeek == '+' || leftPeek == '/' || leftPeek == '*' || leftPeek == '-' || leftPeek == '=') {
				sign = -1;
				for (;; readch()) {
					if (peek != ' ') {
						break;
					}
				}
			} else {
				sign = 1;
			}

			// �ж��Ƿ���ע��
			if (readch('-')) {
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

			// ��ȡ���֣���ȡ��ʱ��ֻ�����ַ���Ҫת�������֣�
			if (Character.isDigit(peek)) {
				stringToDigit();
			} else {
				// �����������
				if (sign == -1)
					return new Token('-');
				else {
					System.out.println("error:illegal \'-\' �кţ�" + line);
					return null;
				}
			}
		}
		// ��������switch����-------------------------------------------------------

		if (Character.isDigit(peek)) {
			stringToDigit();
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
				System.out.println("error:��ʶ��̫�����к�:" + line);
				return null;
			} else if (s.length() > 8) {
				System.out.println("warnning:��ʶ��������Ч���ȣ�" + line);
			}
			w = new Word(s, Tag.ID);
			words.put(s, w);
			listWord.add(w.toString(false));
			return w;
		}
		Token tok = new Token(peek);
		peek = ' ';
		return tok;

	}

	// ������ű�
	public void saveSymbolsTable() throws IOException {
		FileWriter writer = new FileWriter(".\\bin\\���ű�.txt");
		/* д���ļ� */
		for (int i = 0; i < listWord.size(); ++i) {
			String tok = (String) listWord.get(i);
			writer.write(tok + "\r\n");
		}

		writer.flush();
		writer.close();
	}
}
