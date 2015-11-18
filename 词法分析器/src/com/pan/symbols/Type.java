package com.pan.symbols;

import com.pan.lexer.Tag;
import com.pan.lexer.Word;

public class Type extends Word {
	public int width = 0;
	public Type(String s, int tag, int w){
		super(s,tag);
		width = w;
	}
	public static final Type
	Integer = new Type( "integer", Tag.BASIC, 4),
	Real = new Type("real", Tag.BASIC, 8),
	Char = new Type("char", Tag.BASIC, 1),
	Boolean = new Type("boolean", Tag.BASIC, 1),
	String = new Type("string", Tag.BASIC, 1);//?
}
