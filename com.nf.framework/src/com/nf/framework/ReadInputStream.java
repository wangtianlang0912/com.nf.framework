package com.nf.framework;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
/*
 * niufei
 * 
 * 2011.12.5
 * 
 * 读取数据流数据，返回string
 */
public class ReadInputStream {

	public  ReadInputStream(){
		
	}
	public static String Read(InputStream instr)throws Exception{
		/**
		 * 读取数据
		 * @param instr
		 * @return
		 * @throws Exception
		 */
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			byte[] buffer=new byte[1024];
			int len=-1;
			while((len=instr.read(buffer))!=-1){
				out.write(buffer,0,len);
				
			}
			byte[] data=out.toByteArray();
			out.close();
			instr.close();
			return new String(data);
	
	
	}
}
