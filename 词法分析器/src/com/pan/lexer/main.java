package com.pan.lexer;

import java.io.IOException;

public class main {
	public static void main(String argv[]) throws IOException{
		Lexer lex = new Lexer();
		boolean iskey = true;
		while((int)lex.peek != 0xffff)
		{
			Token t = lex.scan();
			if(t != null){
				if(t instanceof Word){
					if(t.tag != Tag.ID){
						iskey = true;
						System.out.println(((Word) t).toString(iskey));
					}
					else{
						iskey = false;
						System.out.println(((Word) t).toString(iskey));
					}
				}
				else if(t instanceof Num){
					System.out.println(t.toString());
				}
				else if(t instanceof Real){
					System.out.println(t.toString());
				}
				else{
					//屏蔽换行和回车的ASCII码值，10和13
					if(t.tag != 13 && t.tag != 10){
						System.out.println(t.toString());
					}
				}
			}
		}
		lex.saveSymbolsTable();
	}
}
