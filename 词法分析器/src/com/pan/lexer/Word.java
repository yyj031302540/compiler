package com.pan.lexer;

public class Word extends Token {
	public String lexme = "";
	public Word(String s,int t){
		super(t);
		this.lexme = s;
	}
	public String toString(boolean iskey) {
		if(!iskey) return "<id, "+this.lexme+">";
		else return "<"+this.lexme+">";
	}
	public static final Word   
//    and = new Word("&&", Tag.AND),  
//    or = new Word("||", Tag.OR),  
    eq = new Word (":=", Tag.EQ),  
    ne = new Word("<>", Tag.NE),  
    le = new Word("<=", Tag.LE),  
    ge = new Word(">=", Tag.GE),
    True = new Word("true", Tag.TRUE),  
    False = new Word("false", Tag.FALSE),  
    temp = new Word("t", Tag.TEMP);  
	
}
