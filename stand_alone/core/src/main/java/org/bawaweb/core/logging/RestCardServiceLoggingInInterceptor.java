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

import org.bawaweb.core.model.SessionInfo;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;

/**
 * @author medhoran
 *
 *ref 1 - http://stackoverflow.com/questions/4592422/logging-request-response-with-apache-cxf-as-xml
 *ref 2 - http://stackoverflow.com/questions/6438178/cxf-outgoing-interceptor-get-soap-response-body-that-is-always-null/10501704#10501704
 * This class will capture the information from incoming requests and log it
 * Also sets the request_id which is used for logging the info in card_audit_log table
 */
public class RestCardServiceLoggingInInterceptor extends LoggingInInterceptor {//AbstractPhaseInterceptor
	
	PrintWriter writer = null;

	protected void finalize() {
		try {
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RestCardServiceLoggingInInterceptor() {
		super(Phase.PRE_STREAM);
		addBefore(RestCardServiceLoggingOutInterceptor.class.getName());
	}


	/* (non-Javadoc)
	 * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
	 * Processes the incoming request messages, sets the @RequestID.requestID
	 */
	@Override
	public void handleMessage(Message message) throws Fault {
		System.out.println("\n*****************\nInvoked RestCardServiceLoggingInInterceptor.handleMessage()\n**********************\n");
		System.out.println("message addreess is " + message.getDestination().getAddress().getAddress().getValue());
		System.out.println("\n____ request message id is " + message.get(LoggingMessage.ID_KEY));

        // capture the request id
//        RequestID.setRequestID(Integer.parseInt((String)(message.get(LoggingMessage.ID_KEY))));

//        org.eclipse.jetty.server.Request request = (org.eclipse.jetty.server.Request) message.get(AbstractHTTPDestination.HTTP_REQUEST);

        //get the remote address
        HttpServletRequest httpRequest = (HttpServletRequest) message.get ( AbstractHTTPDestination.HTTP_REQUEST );
        System.out.println ("Request From the address : " + httpRequest.getRemoteAddr ( ) );

        HttpSession session = httpRequest.getSession(true);
        System.out.println("SI SESSION NULL " + session==null);
        String id = session.getId();

        System.out.println("\n*****\nSESSION ID IS " + id + "\n*****\n");
        SessionInfo.setSessionId(id);
        SessionInfo.setServerRequestNumber(Integer.parseInt((String) (message.get(LoggingMessage.ID_KEY))));



//        MessageContext mc = context.getMessageContext();
//        HttpSession session = ((javax.servlet.http.HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST))
//                .getSession();
//        // Get a session property "counter" from context
//        if (session == null)
//        { throw new WebServiceException("No session in WebServiceContext"); }

//        HttpServletRequest req = (HttpServletRequest)message.get("HTTP.REQUEST");

        /*HttpSession session = httpRequest.getSession();//(true);
        System.out.println("Session id is " + session.getId());*/

//        HTTPSession cxfSession = new HTTPSession(httpRequest);
//
//        System.out.println("cxf session id is " + cxfSession.getSession().getId());










        boolean isInbound = false;
        isInbound = message == message.getExchange().getInMessage()
               || message == message.getExchange().getInFaultMessage();
        
        if ( isInbound ) {
            try {
				GregorianCalendar cal = new GregorianCalendar();	// @TODO add locale info later
				String dateSuff = String.valueOf(cal.get(Calendar.MONTH)) + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + String.valueOf(cal.get(Calendar.YEAR)) ;
				writer = new PrintWriter(new BufferedWriter(new FileWriter(dateSuff+"request.Log", true)));	
			} catch (IOException ie) {
				ie.printStackTrace();
			}

		    try {
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

            } catch ( Exception ex ) {
		        ex.printStackTrace ( );
		    }
		}
	}

}
