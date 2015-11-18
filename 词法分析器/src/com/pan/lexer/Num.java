package com.pan.lexer;

public class Num extends Token {
	public final int value;
	public Num(int v){
		super(Tag.NUM);
		this.value = v;
	}
	@Override
	public String toString() {
		return "<num, "+ this.value +">";
	}
	
}
