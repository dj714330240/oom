package com.coe.oom.core.base.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class EeceptionUtil {

	public static String getMessage(Exception exception)  {
		StringWriter stringWriter=new StringWriter();
		PrintWriter printWriter=new PrintWriter(stringWriter);
		try {
			exception.printStackTrace(printWriter);			
			String message=stringWriter.toString();
			return message;
		} catch (Exception e) {
		}finally{
			try {
				stringWriter.close();
			} catch (Exception e2) {
			}
		}
		return "";
	}
}
