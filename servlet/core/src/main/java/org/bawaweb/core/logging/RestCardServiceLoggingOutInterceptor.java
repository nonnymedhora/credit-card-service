/**
 * 
 */
package org.bawaweb.core.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.AbstractLoggingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.util.Calendar;

/**
 * @author medhoran
 * http://stackoverflow.com/questions/6438178/cxf-outgoing-interceptor-get-soap-response-body-that-is-always-null/10501704#10501704
 */
public class RestCardServiceLoggingOutInterceptor extends LoggingOutInterceptor { //AbstractLoggingInterceptor {

	PrintWriter writer = null;
	
	public void finalize() {
		try {
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public RestCardServiceLoggingOutInterceptor() {
		super(Phase.PRE_STREAM);
		
/*		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("response.Log", true)));	
		} catch (IOException ie) {
			ie.printStackTrace();
		}
*/
	}

	/* (non-Javadoc)
	 * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
	 */
	@Override
	public void handleMessage(Message message) throws Fault {
		
		System.out.println("\n*****************\nInvoked RestCardServiceLoggingOUTInterceptor.handleMessage()\n**********************\n");
		
		// from example -- sample\configuration_interceptor\....\StreamInterceptor
		boolean isOutbound = false;
        isOutbound = message == message.getExchange().getOutMessage()
               || message == message.getExchange().getOutFaultMessage();

        if (isOutbound) {
			try {
				OutputStream os = message.getContent ( OutputStream.class );
				CacheAndWriteOutputStream cwos = new CacheAndWriteOutputStream ( os);
				message.setContent ( OutputStream.class, cwos );
		
				cwos.registerCallback ( new LoggingOutCallBack ( ) );
				
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	

	/* (non-Javadoc)
	 * @see org.apache.cxf.interceptor.AbstractLoggingInterceptor#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// from example -- sample\configuration_interceptor\....\StreamInterceptor
	 private class CachedStream extends CachedOutputStream {
        public CachedStream() {
            super();
        }
        
        protected void doFlush() throws IOException {
            currentStream.flush();
        }

        protected void doClose() throws IOException {
        }
        
        protected void onWrite() throws IOException {
        }
    }
	
	
	class LoggingOutCallBack implements CachedOutputStreamCallback {
	    @Override
	    public void onClose ( CachedOutputStream cos ) {
	        try {
	            if ( cos != null ) {
	             
	            	System.out.println ("Response XML in RestCardServiceLoggingOUTInterceptor LoggingOutCallBack : " + IOUtils.toString ( cos.getInputStream ( ) ));
	            	
	            	try {
	            		GregorianCalendar cal = new GregorianCalendar();	// @TODO add locale info later
	        			String dateSuff = String.valueOf(cal.get(Calendar.MONTH)) + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + String.valueOf(cal.get(Calendar.YEAR)) ;
	        			
	        			writer = new PrintWriter(new BufferedWriter(new FileWriter(dateSuff + "response.Log", true)));	
	        		} catch (IOException ie) {
	        			ie.printStackTrace();
	        		}
	        		
	    			writer.write(new Date().getTime() + "    " + IOUtils.toString ( cos.getInputStream () ) + "\n");
	    			writer.close();
	                
	            }

	        } catch ( Exception e ) {
	            e.printStackTrace();
	        }       
	    }

	    @Override
	    public void onFlush ( CachedOutputStream arg0 ) {}   
	}
	
}




