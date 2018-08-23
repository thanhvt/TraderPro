/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.traderpro.thanhvt;

/**
 *
 * @author ThanhVT
 */

import java.util.ArrayList;

public class BittrexData {

	ArrayList<ArrayList<String>> object = null;

	public int size() {
		return object.size();
	}
	
	public String get(String keyword) {
		for (int a = 0; a < object.get(0).size(); a++) {
			if (new String(object.get(0).get(a)).equals(keyword)) {
				return object.get(0).get(a + 1);
			}
		}
		return keyword + " > NOT_FOUND";
	}

	public String get(int i, String keyword) {
		for (int a = 0; a < object.get(i).size(); a++) {
			if (new String(object.get(i).get(a)).equals(keyword)) {
				return object.get(i).get(a + 1);
			}
		}
		return keyword + " > NOT_FOUND";
	}

	public void printAllElements() {
		for (int b = 0; b < object.size(); b++) {
			System.out.println(object.get(b));
		}
	}

	public ArrayList<ArrayList<String>> getObject() {
		return object;
	}

	public ArrayList<String> Tokens(String Json) {
		ArrayList<String> temp = new ArrayList<String>();
		for (int a = 0; a < Json.length(); a++) {
			temp.add(Json.substring(a, a + 1));
		}
		return temp;
	}

	public void set(String data) {
		ArrayList<ArrayList<String>> MainData = new ArrayList<ArrayList<String>>();
		ArrayList<String> Tokens              = Tokens(data);
		ArrayList<String> Words               = new ArrayList<String>();
		String Word                           = "";
		boolean RecordWord                    = false;
		boolean Result                        = false;
		int Resultint                         = 0;

		for (int a = 0; a < Tokens.size(); a++) {

			if (new String(Word).equals("result")) {
				Words = new ArrayList<String>();
				Word = "";
				Result = true;
			}

			if (new String(Tokens.get(a)).equals("\"") & !RecordWord) {
				RecordWord = true;
			} else if (new String(Tokens.get(a)).equals("\"") & RecordWord) {
				RecordWord = false;
				if (!(Word.length() < 2))
					Words.add(Word);
				Word = "";
			}

			if (new String(Tokens.get(a)).equals(":") & !RecordWord & Result) {
				if (new String(Tokens.get(a + 1)).equals("\"")) {
				} else {
					if (Resultint == 1) {
						RecordWord = true;
						Tokens.set(a, "");
					} else {
						Resultint++;
					}
				}

			} else if (new String(Tokens.get(a)).equals(",") & RecordWord & Result) {
				RecordWord = false;
				if (!(Word.length() < 2)) {
					Words.add(Word);
				}
				Word = "";
			}

			if (RecordWord & !new String(Tokens.get(a)).equals("\"") & !new String(Tokens.get(a)).equals(",")
					& !new String(Tokens.get(a)).equals("}")) {
				Word += Tokens.get(a);
			}
			
			if (new String(Tokens.get(a)).equals("}")){
				RecordWord = false;
			}

			if (new String(Tokens.get(a)).equals("}") & !RecordWord) {
				if (!(Words.size() < 1)) {
					Words.add(Word);
					Word = "";
					MainData.add(Words);
				}
				Words = new ArrayList<String>();
			}

		}
		object = MainData;
	}

}