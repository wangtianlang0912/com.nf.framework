/**
 * PatternUtil.java
 * com.Apricotforest
 * 工程：medicalJournals_for_android
 * 功能：正则表达式处理 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-8-10       上午10:42:15
 * Copyright (c) 2012, TNT All Rights Reserved.
*/

package com.nf.framework;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {
	public static PatternUtil pu=null;
	public static PatternUtil getInstance(){
		
		if(pu==null){
			pu=new PatternUtil();
		}
		return pu;
	}
	/**
	 * 判断邮箱
	 * @param str
	 * @return
	 */
	 public   boolean checkEmail(String email) {
		 boolean flag=false;
		 String regex =null;
		 if(email!=null){
			 regex ="\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*.\\w+([-.]\\w+)*";
			 flag= checkStr(regex,email);
		 }
		 return flag;
	}
	/**
	 * 判断用户名
	 * @param str
	 * @return
	 */
//	 public   boolean checkUserName(String username) {
//		 boolean flag=false;
//		 String regex =null;
//		 int len=username.length();
//		 if(len<6||len>18){
//			 flag=false;
//		 }else{
//		  regex = "([a-z]|[A-Z]|[0-9])+";//只判定数字或者字母
//		  flag= checkStr(regex,username);
//		 }
//		 return flag;
//	}
	 /**
	 * 判断手机号码 可为null 并且满足号码格式
	 * @param str
	 * @return
	 */
	 public   boolean checkTelPhone(String mobile) {
		 boolean flag=false;
		 String regex =null;
		 if(mobile!=null&&!"".equals(mobile)){
			 regex = "^(1(([358][0-9])|(47)))\\d{8}$";//判定电话号码
			  flag= checkStr(regex,mobile);
		 }else{
			 flag=true;
		 }
		 return flag;
	}
	 /**
	 *判断手机号码非空并且限制为11位
	 * @param str
	 * @return
	 */
	 public   boolean checkTelPhone2(String mobile) {
		 boolean flag=false;
		 String regex =null;
		 if(mobile!=null&&!"".equals(mobile)){
			 regex = "^\\d{11}$";
			 flag= checkStr(regex,mobile);
		 }
		 return flag;
	}
	/**
	 * 判断密码    字符串 字母或者数字
	 * @param str
	 * @return
	 */
	 public  boolean checkPassword(String password) {
		 boolean flag=false;
		 String regex =null;
		 int len=password.length();
		 if(len<6||len>18){
			 flag=false;
		 }else{
		  regex = "([a-z]|[A-Z]|[0-9]|[_*])+";//判定数字或者字母或者下划线
		  flag= checkStr(regex,password);
		 }
		 return flag;
	}
 /**
	 * 判断执业医师证书编码 18位数字 可为null
	 * @param str
	 * @return
	 */
	 public   boolean checkDoctorLicenceStr(String doctorLicence) {
		 boolean flag=false;
		 String regex =null;
		 if(doctorLicence!=null&&!"".equals(doctorLicence)){
			 regex = "^\\d{15}$";//判定执业医师证书编码
			  flag= checkStr(regex,doctorLicence);
		 }else{
			 flag=true;
		 }
		 return flag;
	}
	 /**
	  * 正则检查字符串
	  * @param regex
	  * @param input
	  * @return
	  */
	 private boolean checkStr(String regex,String input){
		 
		 boolean flag=false;
			if(regex!=null){
			  Pattern p = Pattern.compile(regex);
			  Matcher m = p.matcher(input);
		  	  flag=m.matches();
			}
		 return flag;
	 }
	/**
	 * 判定数字或者字母或者中文
	 * @param str
	 * @return
	 */
	 public   boolean checkAll(String str) {
		 boolean flag=false;
		 String regex =null;
		  regex = "([a-z]|[A-Z]|[0-9]|[\u4e00-\u9fa5])+";//判定数字或者字母或者中文
		  flag= checkStr(regex,str);
		 return flag;
	}
	 /**
	 * 判断用户名
	 * @param str
	 * @return
	 */
	 public   boolean checkContainChinese(String str) {
		 boolean flag=false;
		 String regex =null;
		  regex = "([\u4e00-\u9fa5])+";//判定中文
		  flag= checkStr(regex,str);
		  return flag;
	}
	/**
	 * 判断真实姓名 姓名为汉字，并且至少两位
	 * @param str
	 * @return
	 */
	 public   boolean checkTrueName(String str) {
		 boolean flag=false;
		 String regex =null;
		  regex = "([\u4e00-\u9fa5])+";//判定中文
		  flag= checkStr(regex,str);
		  if(flag){
			 return  str.length()>1?true:false;
		  }else
		  return flag;
	}
	 
	  public String[] getFileByPattern(String content){
		  
		  String[] strs=null;
		  
		  
		return strs;
		  
		  
	  }
	  /**
	   * 过滤标点符号
	   * @param str
	   * @return
	   */
	  public String  filterPunctuation(String str,String replaceStr){
		  if(str!=null)
			  str=str.replaceAll("\\pP|\\pS|\\s",replaceStr);
		  return str;
	  }
	  /**
	   * 过滤单个重复字符
	   * @param str
	   * @return
	   */
	  public String filterRepitChar(String str){
		  if(str!=null){
			  str=str.replaceAll("(?s)(.)(?=.*\\1)", "");
			  }
		return str;
				 
	  }
	  
//	//查找以Java开头,任意结尾的字符串 
//	  Pattern pattern = Pattern.compile("^Java.*"); 
//	  Matcher matcher = pattern.matcher("Java不是人"); 
//	  boolean b= matcher.matches(); 
//	  //当条件满足时，将返回true，否则返回false 
//	  System.out.println(b); 
//
//
//	  ◆以多条件分割字符串时 
//	  Pattern pattern = Pattern.compile("[, |]+"); 
//	  String[] strs = pattern.split("Java Hello World Java,Hello,,World|Sun"); 
//	  for (int i=0;i<strs.length;i++) { 
//	      System.out.println(strs[i]); 
//	  } 
//
//	  ◆文字替换（首次出现字符） 
//	  Pattern pattern = Pattern.compile("正则表达式"); 
//	  Matcher matcher = pattern.matcher("正则表达式 Hello World,正则表达式 Hello World"); 
//	  //替换第一个符合正则的数据 
//	  System.out.println(matcher.replaceFirst("Java")); 
//
//	  ◆文字替换（全部） 
//	  Pattern pattern = Pattern.compile("正则表达式"); 
//	  Matcher matcher = pattern.matcher("正则表达式 Hello World,正则表达式 Hello World"); 
//	  //替换第一个符合正则的数据 
//	  System.out.println(matcher.replaceAll("Java")); 
//
//
//	  ◆文字替换（置换字符） 
//	  Pattern pattern = Pattern.compile("正则表达式"); 
//	  Matcher matcher = pattern.matcher("正则表达式 Hello World,正则表达式 Hello World "); 
//	  StringBuffer sbr = new StringBuffer(); 
//	  while (matcher.find()) { 
//	      matcher.appendReplacement(sbr, "Java"); 
//	  } 
//	  matcher.appendTail(sbr); 
//	  System.out.println(sbr.toString()); 
//
//	  ◆验证是否为邮箱地址 
//
//	  String str="ceponline@yahoo.com.cn"; 
//	  Pattern pattern = Pattern.compile("[//w//.//-]+@([//w//-]+//.)+[//w//-]+",Pattern.CASE_INSENSITIVE); 
//	  Matcher matcher = pattern.matcher(str); 
//	  System.out.println(matcher.matches()); 
//
//	  ◆去除html标记 
//	  Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL); 
//	  Matcher matcher = pattern.matcher("<a href=/"index.html/">主页</a>"); 
//	  String string = matcher.replaceAll(""); 
//	  System.out.println(string); 
//
//	  ◆查找html中对应条件字符串 
//	  Pattern pattern = Pattern.compile("href=/"(.+?)/""); 
//	  Matcher matcher = pattern.matcher("<a href=/"index.html/">主页</a>"); 
//	  if(matcher.find()) 
//	  System.out.println(matcher.group(1)); 
//	  } 
//
//	  /**
//	   * 截取http://地址 
//	   * 
//	   */
//	  //截取url 
//	  Pattern pattern = Pattern.compile("(http://|https://){1}[//w//.//-/:]+"); 
//	  Matcher matcher = pattern.matcher("dsdsds<http://dsds//gfgffdfd>fdf"); 
//	  StringBuffer buffer = new StringBuffer(); 
//	  while(matcher.find()){              
//	      buffer.append(matcher.group());        
//	      buffer.append("/r/n");              
//	  System.out.println(buffer.toString()); 
//	  } 
//	          
//	/**
//	 * 替换指定{}中文字 
//	 */
//	  String str = "Java目前的发展史是由{0}年-{1}年"; 
//	  String[][] object={new String[]{"//{0//}","1995"},new String[]{"//{1//}","2007"}}; 
//	  System.out.println(replace(str,object)); 
//
//	  public static String replace(final String sourceString,Object[] object) { 
//	              String temp=sourceString;    
//	              for(int i=0;i<object.length;i++){ 
//	                        String[] result=(String[])object[i]; 
//	                 Pattern    pattern = Pattern.compile(result[0]); 
//	                 Matcher matcher = pattern.matcher(temp); 
//	                 temp=matcher.replaceAll(result[1]); 
//	              } 
//	              return temp; 
//	  } 

}
