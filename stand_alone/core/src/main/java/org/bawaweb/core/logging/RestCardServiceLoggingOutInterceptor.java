/**
 * 
 */
package org.bawaweb.core.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
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
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

/**
 * @author medhoran
 * http://stackoverflow.com/questions/6438178/cxf-outgoing-interceptor-get-soap-response-body-that-is-always-null/10501704#10501704
 *  This class will capture the information from outgoing responses and log it
 */
public class RestCardServiceLoggingOutInterceptor extends LoggingOutInterceptor { //AbstractLoggingInterceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
	private PrintWriter writer = null;
	
	protected void finalize() {
		try {
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public RestCardServiceLoggingOutInterceptor() {
		super(Phase.PRE_STREAM);
	}

	/* (non-Javadoc)
	 * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
	 */
	@Override
	public void handleMessage(Message message) throws Fault {
		
		System.out.println("\n*****************\nInvoked RestCardServiceLoggingOUTInterceptor.handleMessage()\n**********************\n");

        //get the remote address
        HttpServletResponse httpResponse = (HttpServletResponse) message.get ( AbstractHTTPDestination.HTTP_RESPONSE );
        System.out.println("Response status : " + httpResponse.getStatus());
        System.out.println ("Response contenttype : " + httpResponse.getContentType() );
        System.out.println ("Response locale : " + httpResponse.getLocale() );
//        System.out.println ("Response status : " + httpResponse.getStatus ( ) );
//        System.out.println ("Response status : " + httpResponse.getStatus ( ) );
//        System.out.println ("Response status : " + httpResponse.getStatus ( ) );
//             // from example -- sample\configuration_interceptor\....\StreamInterceptor
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
		// TODO need to figure this one out
		return null;
	}

	class LoggingOutCallBack implements CachedOutputStreamCallback {
	    @Override
	    public void onClose ( CachedOutputStream cos ) {
	        try {
                String responseStr = null;
	            if ( cos != null ) {
                    responseStr = IOUtils.toString ( cos.getInputStream ( ) );
	                /*if (!responseStr.startsWith("<?xml")) {
                        System.out.println("response NO NON NOOO NOOOO xml");
                        responseStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + responseStr;
                    }*/
	            	System.out.println ("Response XML in RestCardServiceLoggingOUTInterceptor LoggingOutCallBack : " + responseStr);// IOUtils.toString ( cos.getInputStream ( ) ));
	            	
	            	try {
	            		GregorianCalendar cal = new GregorianCalendar();	// @TODO add locale info later
	        			String dateSuff = String.valueOf(cal.get(Calendar.MONTH)) + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + String.valueOf(cal.get(Calendar.YEAR)) ;
	        			
	        			writer = new PrintWriter(new BufferedWriter(new FileWriter(dateSuff + "response.Log", true)));
	        		} catch (IOException ie) {
	        			ie.printStackTrace();
	        		}
	        		
	    			writer.write(new Date().getTime() + "    " + responseStr + "\n");
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




