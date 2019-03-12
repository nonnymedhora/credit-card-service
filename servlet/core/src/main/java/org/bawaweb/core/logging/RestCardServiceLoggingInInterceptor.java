/**
 * 
 */
package org.bawaweb.core.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.cxf.message.Message;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.jaxrs.interceptor.JAXRSInInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import java.util.Calendar;

/**
 * @author medhoran
 *
 *ref 1 - http://stackoverflow.com/questions/4592422/logging-request-response-with-apache-cxf-as-xml
 *ref 2 - http://stackoverflow.com/questions/6438178/cxf-outgoing-interceptor-get-soap-response-body-that-is-always-null/10501704#10501704
 */
public class RestCardServiceLoggingInInterceptor extends LoggingInInterceptor { //JAXRSInInterceptor {
	
	PrintWriter writer = null;

	public void finalize() {
		try {
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RestCardServiceLoggingInInterceptor() {
		super(Phase.PRE_STREAM);
		addBefore(RestCardServiceLoggingOutInterceptor.class.getName());
		//super(Phase.RECEIVE);
		
		/*try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("request.Log", true)));	
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		*/
	}
	
	
	/*
	 * 
	
	try {
		writer = new PrintWriter(new BufferedWriter(new FileWriter("foo.out")));
	} catch (IOException ie) { ie.printStackTrace(); }
	 */
	/*FileWriter writer = new FileWriter("outFile.txt", true);

	private boolean append = true;
	PrintWriter pw  = new PrintWriter(new File("d:\\stam\\stam.txt"));//new PrintWriter(new FileWriter(new File("requestLog.txt")));
	
	
	boolean append = true;
	
	pw = new PrintWriter(new FileWriter(new File("filepath.txt"), append));

	File respF = new File("responseLog.txt");
	try {
		PrintWriter pw  = new PrintWriter(new FileWriter("d:\\stam\\stam.txt", true));//new PrintWriter(respF);
	} catch (FileNotFoundException ff) {}
	
	public RestCardServiceLoggingInInterceptor() {
		super(Phase.RECEIVE);
	}*/

	/* (non-Javadoc)
	 * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
	 */
	@Override
	public void handleMessage(Message message) throws Fault {
	
		
		System.out.println("\n*****************\nInvoked RestCardServiceLoggingInInterceptor.handleMessage()\n**********************\n");
		System.out.println("message addreess is " + message.getDestination().getAddress().getAddress().getValue());
		System.out.println("\n____ request message id is " + message.get(LoggingMessage.ID_KEY));
		
		
		boolean isInbound = false;
        isInbound = message == message.getExchange().getInMessage()
               || message == message.getExchange().getInFaultMessage();
        
        if ( isInbound ) {
     
			/*//get the remote address
		    HttpServletRequest httpRequest = (HttpServletRequest) message.get ( AbstractHTTPDestination.HTTP_REQUEST );
		    System.out.println ("Request From the address : " + httpRequest.getRemoteAddr ( ) );*/
			try {
				GregorianCalendar cal = new GregorianCalendar();	// @TODO add locale info later
				String dateSuff = String.valueOf(cal.get(Calendar.MONTH)) + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + String.valueOf(cal.get(Calendar.YEAR)) ;
				writer = new PrintWriter(new BufferedWriter(new FileWriter(dateSuff+"request.Log", true)));	
			} catch (IOException ie) {
				ie.printStackTrace();
			}
			
		//		Destination dest = message.getDestination();
			
		    try
		    {
		        // now get the request xml
		        InputStream is = message.getContent ( InputStream.class );
		        CachedOutputStream os = new CachedOutputStream ( );
		        IOUtils.copy ( is, os );
		        os.flush ( );
		        message.setContent (  InputStream.class, os.getInputStream ( ) );
		        is.close ( );
		
		        System.out.println ("\n_________________\nThe request is: " + IOUtils.toString ( os.getInputStream() )+"\n___________________\n");
		        os.close ( );
		        
		        writer.write( new Date().getTime() + "   " + IOUtils.toString(os.getInputStream()) + "\n");
		        writer.close();
		    }
		
		    catch ( Exception ex )
		    {
		        ex.printStackTrace ( );
		    }
		
		}
	}

}
