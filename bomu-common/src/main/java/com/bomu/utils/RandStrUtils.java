package com.bomu.utils;

import java.util.Random;

public class RandStrUtils {

	private static Random rand = new Random();
	/**
	 * 产生随机数字组合
	 * @param len
	 * @return
	 */
	public static String randNumeric(int len) {
		char[] degit = new char[]{'1','2','3','4','5','6','7','8','9','0'};
		char[] retValue = new char[len];
		int length =  degit.length;
		for (int i = 0; i < len; i ++) {
			retValue[i] = degit[rand.nextInt(length)];
		}
		return new String(retValue);
	}

	/**
	 * 产生随机数字组合
	 * @param len
	 * @return
	 */
	public static String randNumeric0(int len) {
		char[] degit = new char[]{'1','2','3','4','5','6','7','8','9'};
		char[] retValue = new char[len];
		int length =  degit.length;
		for (int i = 0; i < len; i ++) {
			retValue[i] = degit[rand.nextInt(length)];
		}
		return new String(retValue);
	}


	/**
	 * 产生随机字母组合
	 * @param len
	 * @return
	 */
	public static String randAlpha(int len) {
		char[] alpha = new char[]{'q','w','e','r','t','y','u','i','o','p','a','s','d',
				                  'f','g','h','j','k','l','z','x','c','v','b','n','m',
				                  'Q','W','E','R','T','Y','U','I','O','P','A','S','D',
				                  'F','G','H','J','K','L','Z','X','C','V','B','N','M'};
		char[] retValue = new char[len];
		int length =  alpha.length;
		for (int i = 0; i < len; i ++) {
			retValue[i] = alpha[rand.nextInt(length)];
		}
		return new String(retValue);
	}

	/**
	 * 产生随机字母数字组合
	 * @param len
	 * @return
	 */
	public static String randAlphanumberic(int len) {
		char[] alpha = new char[]{'q','w','e','r','t','y','u','i','o','p','a','s','d',
				                  'f','g','h','j','k','l','z','x','c','v','b','n','m',
				                  'Q','W','E','R','T','Y','U','I','O','P','A','S','D',
				                  'F','G','H','J','K','L','Z','X','C','V','B','N','M',
				                  '1','2','3','4','5','6','7','8','9','0'};
		char[] retValue = new char[len];
		int length =  alpha.length;
		for (int i = 0; i < len; i ++) {
			retValue[i] = alpha[rand.nextInt(length)];
		}
		return new String(retValue);
	}

}
