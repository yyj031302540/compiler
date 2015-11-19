package com.pan.lexer;

import java.io.IOException;

public class main {
	public static void main(String argv[]) throws IOException {
		Lexer lexer = new Lexer();
		boolean iskey = true;
		
		// 0xffff是EOF
		while ((int) lexer.peek != 0xffff) {
			Token token = lexer.scan();
			if (token != null) {
				if (token instanceof Word) {
					if (token.tag != Tag.ID) {
						iskey = true;
						System.out.println(((Word) token).toString(iskey));
					} else {
						iskey = false;
						System.out.println(((Word) token).toString(iskey));
					}
				} else if (token instanceof Num) {
					System.out.println(token.toString());
				} else if (token instanceof Real) {
					System.out.println(token.toString());
				} else {
					// 屏蔽换行和回车的ASCII码值，10和13
					if (token.tag != 13 && token.tag != 10) {
						System.out.println(token.toString());
					}
				}
			}
		}
		lexer.saveSymbolsTable();
	}
}
