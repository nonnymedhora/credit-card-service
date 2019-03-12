/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.bawaweb.client;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import org.bawaweb.core.model.CustomerCard;



/**
 * @author medhoran
 * 
 *
 */
public final class Client {

    private static final String CLIENT_CONFIG_FILE = "resources/ClientConfig.xml";
    private static final String BASE_SERVICE_URL = "https://localhost:9000/customerCardService/customerCards";
    
    private static final Integer CUSTOMER_ID =  778899;//"739211";//"2392341";//"2398336";//"3341336";//"9038312936";//"713412936";//"9038312936";//"6738312936";//"4238312936";//"43254536";//"43254536";//"1234534567";//"43254536";//"43254536";// "1234534567";
    private static final Integer CUSTOMER_ID_Z =  123456789;//"739211";
    
    private static final String CARD_ID = "2dcf8eab-fe9a-4d84-9e9f-299f89141caf";//"10e805c2-8176-4514-ad2a-c3c5cd3a4dab";//"d31bd211-f6f9-4ef2-ac43-ff3142ade6f7";//"e02a2c28-5ca7-4aea-9043-335cb18ec1e8";//"d2b9656f-a8a2-4e38-90f0-4ee3bef5a5a4";//"5fbe3b48-7fdb-49bf-9adc-d93f1b323ed9";// "5fbe3b48-7fdb-49bf-9adc-d93f1b323ed9";//"b2b5f479-7aa1-484a-93b7-bf60bbed566f";
    private static final String CARD_ID_Z = "54632789-7aa1-484a-93b7-bf60bbed566f";
    
    private static final CustomerCard CARD = new CustomerCard();
    private static final CustomerCard CARD2 = new CustomerCard();
    	static {
//    		CARD.setCardNumber("5112345112345114");//("5405222222222226");//("5405222222222226");//("5111005111051128");//("5405222222222226");//("5111005111051821");//("5112345112345114");//("5111005111051128");			// see http://www.auricsystems.com/support-center/sample-credit-card-numbers/ for examples
//    		CARD.setCustomerId(778899);//(111199);		/// wont work --> 2231423736l === will work 43254536
//    		CARD.setCreatedBy("MEEEEEE");
//    		CARD.setModifiedBy("MEEEEEE");
    		
    		
    		CARD2.setCardNumber("5405222222222226");//("5405222222222226");//("5111005111051128");//("5405222222222226");//("5111005111051821");//("5112345112345114");//("5111005111051128");			// see http://www.auricsystems.com/support-center/sample-credit-card-numbers/ for examples
//    		CARD2.setCustomerId(778899);		/// wont work --> 2231423736l === will work 43254536
    		CARD2.setCreatedBy("TUUUMEEEEEE");
    		CARD2.setModifiedBy("TUUUMEEEEEE");
    	}

    private Client() {
    }
    
    public void doGetCardNumberRequest() throws Exception {
    	String keyStoreLoc = "config/clientKeystore.jks";

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStoreLoc), "cspass".toCharArray());

        /* 
         * Send HTTP GET request to query customer info using portable HttpClient
         * object from Apache HttpComponents
         */
        SSLSocketFactory sf = new SSLSocketFactory(keyStore, "ckpass", keyStore); 
        Scheme httpsScheme = new Scheme("https", 9000, sf);
        System.out.println("Sending HTTPS GET request to query customer info");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getConnectionManager().getSchemeRegistry().register(httpsScheme);
        System.out.println("URL is " + (BASE_SERVICE_URL + "/" + CARD_ID));//CUSTOMER_ID_Z));
        HttpGet httpget = new HttpGet(BASE_SERVICE_URL + "/" + CARD_ID);//CUSTOMER_ID_Z);//99123567");//"/254524");//"/231624");//"/22222222");//"/22222222");//"/123456");//"/7777");//"/123456");
        BasicHeader bh = new BasicHeader("Accept" , "text/xml");
        httpget.addHeader(bh);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        entity.writeTo(System.out);
        
        
       /* httpget = new HttpGet(BASE_SERVICE_URL + "/" + CUSTOMER_ID_Z);
        response = httpclient.execute(httpget);
        entity = response.getEntity();
        entity.writeTo(System.out);*/
        

//        httpclient.getConnectionManager().shutdown();
        
        System.out.println("\n");
    	
    }
    
    
    
    public void doAddCustomerCardPostRequest() {
    	/*
//       *  Send HTTP POST request to add customer, using JAXRSClientProxy
//       *  Note: if need to use basic authentication, use the JAXRSClientFactory.create(baseAddress,
//       *  username,password,configFile) variant, where configFile can be null if you're
//       *  not using certificates.
//       */
      
      /*remo for now*/
      System.out.println("\n\nSending HTTPS POST request to add customerCard");
      DefaultHttpClient httpclient = new DefaultHttpClient();////////////////
//      CustomerCardService proxy = JAXRSClientFactory.create(BASE_SERVICE_URL, CustomerCardService.class,
//            CLIENT_CONFIG_FILE);
      WebClient wc = WebClient.create(BASE_SERVICE_URL, CLIENT_CONFIG_FILE);
      
      // 123456 | YAHOOOOOOOOOOO	which exist
     /* CustomerCard customerCard = new CustomerCard();
      customerCard.setCardNumber("4298767868316");//9898787111111111
      customerCard.setCustomerXAID(new BigInteger("93243567"));//1111111
*/     
//      Response resp = wc.post(CARD);//(customerCard);
      /*ends rem for now*/
      /*
      entity = response.getEntity();
      entity.writeTo(System.out);*/	
		Response resp2 = wc.post(CARD2);
    }
    
    
    public static void main(String args[]) throws Exception {
    	Client cc = new Client();

    	cc.doAddCustomerCardPostRequest();
    	
    	//https://localhost:9000/customerCardService/customerCards/b2b5f479-7aa1-484a-93b7-bf60bbed566f
    	//https://localhost:9000/customerCardService/customerCards/5fbe3b48-7fdb-49bf-9adc-d93f1b323ed9
    	cc.doGetCardNumberRequest();
    }
    
    
}
