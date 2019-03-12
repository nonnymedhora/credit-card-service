package org.bawaweb.core.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;

import org.apache.cxf.interceptor.LoggingInInterceptor;

import java.util.Calendar;

public class RCardServiceLoggingInInterceptor extends LoggingInInterceptor {

	private final GregorianCalendar cal = new GregorianCalendar();	// @TODO add locale info later
	private final String dateSuff = String.valueOf(cal.get(Calendar.MONTH)) + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + String.valueOf(cal.get(Calendar.YEAR)) ;
	
	
	private static PrintWriter writer;// = new PrintWriter(new BufferedWriter(new FileWriter(dateSuff+"__MY_RCS__request.Log", true)));	
	
	static {
		try {
			GregorianCalendar cal = new GregorianCalendar();	// @TODO add locale info later
			String dateSuff = String.valueOf(cal.get(Calendar.MONTH)) + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + String.valueOf(cal.get(Calendar.YEAR)) ;
			
			writer = new PrintWriter(new BufferedWriter(new FileWriter(dateSuff+"__MY_RCS__request.Log", true)));
			
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	public RCardServiceLoggingInInterceptor() {
		super(writer);
	}
	

//	public RCardServiceLoggingInInterceptor(String phase) {
//		super(phase);
//		// TODO Auto-generated constructor stub
//	}
//
//	public RCardServiceLoggingInInterceptor(int lim) {
//		super(lim);
//		// TODO Auto-generated constructor stub
//	}
//
//	public RCardServiceLoggingInInterceptor(PrintWriter w) {
//		super(w);
//		// TODO Auto-generated constructor stub
//	}
//
//	public RCardServiceLoggingInInterceptor(String id, String phase) {
//		super(id, phase);
//		// TODO Auto-generated constructor stub
//	}
//
//	public RCardServiceLoggingInInterceptor(String id, int lim) {
//		super(id, lim);
//		// TODO Auto-generated constructor stub
//	}
//
//	public RCardServiceLoggingInInterceptor(String id, PrintWriter w) {
//		super(id, w);
//		// TODO Auto-generated constructor stub
//	}

	public static void main(String[] args) {


	}

}
