package com.pan.symbols;

import com.pan.lexer.Tag;
import com.pan.lexer.Word;

/**
 * ��������
 * @author Schaepher
 *
 */
public class Type extends Word {
	public int width = 0;
	public Type(String s, int tag, int w){
		super(s,tag);
		width = w;
	}
	public static final Type
	// ����
	Shortint = new Type("shortint",Tag.BASIC,1),
	Integer = new Type( "integer", Tag.BASIC, 2),
	Longint = new Type( "longint",Tag.BASIC,4),
	// ������
	Real = new Type("real", Tag.BASIC, 4),
	Double = new Type("double", Tag.BASIC, 8),
	Extended = new Type("extended", Tag.BASIC, 10),
	// �ֽ���
	Byte = new Type("byte", Tag.BASIC, 1),
	Word = new Type("word", Tag.BASIC, 2),
	Dword = new Type("dword", Tag.BASIC, 4),
	// ������
	Boolean = new Type("boolean", Tag.BASIC, 1),
	String = new Type("string", Tag.BASIC, 1); // ����֧��string
}
