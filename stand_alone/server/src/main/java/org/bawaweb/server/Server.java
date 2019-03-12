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

package org.bawaweb.server;


/**
 * @author medhoran
 * Main Server class -- this is the Main-Class in the jar file and is the server
 * //TODO make this into a running thread
 */
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.PerRequestResourceProvider;

import org.bawaweb.server.services.CustomerCardServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Server {
    //@TODO figure a way of using a saner IP resolving mechanism

    private static String BASE_SERVER_URL;


//    static final String BASE_SERVER_URL = "http://10.0.103.112:8080";       //ci
//	static final String BASE_SERVER_URL = "http://10.0.103.115:8080";       //qa
//    static final String BASE_SERVER_URL = "http://localhost:8080";       //localhost

    /********************************
     private static final Properties serverProperties;// = ServerHostProperties.getServerProperties();

     private static final String HTTP_PROTOCOL;//  = serverProperties.getProperty("host.protocol");
     private static final String SERVER_IP;// = serverProperties.getProperty("host.ip");
     private static final String PORT;// = serverProperties.getProperty("host.port");
     private static final String BASE_SERVER_URL;// = HTTP_PROTOCOL + SERVER_IP + ":" + PORT;       // prod 10.0.103.205


     static {
     try {
     serverProperties = ServerHostProperties.getServerProperties();
     HTTP_PROTOCOL  = serverProperties.getProperty("host.protocol");
     SERVER_IP = serverProperties.getProperty("host.ip");
     PORT = serverProperties.getProperty("host.port");
     BASE_SERVER_URL = HTTP_PROTOCOL + SERVER_IP + ":" + PORT;

     } catch (Exception e) {
     e.printStackTrace();
     //            throw new Exception("Could not init class.", e);

     //            throw new InitializationFailedException("Could not init class.", e);

     }
     }
     ***********************/

//    private static final Properties serverProperties = ServerHostProperties.getServerProperties();

//    private static final String HTTP_PROTOCOL  = serverProperties.getProperty("host.protocol");
//    private static final String SERVER_IP = serverProperties.getProperty("host.ip");
//    private static final String PORT = serverProperties.getProperty("host.port");
//    private static final String BASE_SERVER_URL = HTTP_PROTOCOL + SERVER_IP + ":" + PORT;       // prod 10.0.103.205
//
//    static final String BASE_SERVER_URL = "http://10.0.104.90:8080";       //prod
//    static final String BASE_SERVER_URL =  "http://localhost:8080";// qa;//"http://10.0.103.115:8080";// qa
    static {
        // set the configuration file
        SpringBusFactory factory = new SpringBusFactory();
        Bus bus = factory.createBus("/ServerConfig.xml");//resources
        BusFactory.setDefaultBus(bus);
    }

    protected Server() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(CustomerCardServiceImpl.class);
        sf.setResourceProvider(CustomerCardServiceImpl.class,
                new PerRequestResourceProvider(CustomerCardServiceImpl.class));

        // set the JSON provider
        List<Object> providers = new ArrayList<Object>();
//        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        providers.add( jsonProvider );
        sf.setProviders( providers );

        Properties serverProperties = ServerHostProperties.getServerProperties();
        String HTTP_PROTOCOL  = serverProperties.getProperty("host.protocol");
        String SERVER_IP = serverProperties.getProperty("host.ip");
        String PORT = serverProperties.getProperty("host.port");

        BASE_SERVER_URL = HTTP_PROTOCOL + SERVER_IP + ":" + PORT;
        System.out.println("BASE SERVER URL is " + BASE_SERVER_URL);
        sf.setAddress(BASE_SERVER_URL);

        BindingFactoryManager manager = sf.getBus().getExtension(BindingFactoryManager.class);
        JAXRSBindingFactory factory = new JAXRSBindingFactory();
        factory.setBus(sf.getBus());
        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, factory);

        sf.create();
    }

    public static void main(String args[]) throws Exception {
        // initialize the property bundle
        ServerHostProperties.initialize();
        EntityManagerUtil.initialize();

        new Server();
        System.out.println("Server ready... @ " + BASE_SERVER_URL);

    }
}