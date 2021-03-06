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
 *
 */
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

import org.bawaweb.server.services.CustomerCardServiceImpl;

public class Server {
	static final String BASE_SERVER_URL = "https://localhost:9000";

    static {
        // set the configuration file
        SpringBusFactory factory = new SpringBusFactory();
        Bus bus = factory.createBus("resources/ServerConfig.xml");
        BusFactory.setDefaultBus(bus);
    }
   
    protected Server() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(CustomerCardServiceImpl.class);
        sf.setResourceProvider(CustomerCardServiceImpl.class, 
            new SingletonResourceProvider(new CustomerCardServiceImpl()));
        sf.setAddress(BASE_SERVER_URL);
        
        BindingFactoryManager manager = sf.getBus().getExtension(BindingFactoryManager.class);
        JAXRSBindingFactory factory = new JAXRSBindingFactory();
        factory.setBus(sf.getBus());
        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, factory);

        
//        sf.setInInterceptors(new RestCardServiceLoggingInInterceptor());
//sf.setI
        
        sf.create();
    }

    public static void main(String args[]) throws Exception {
        new Server();
        System.out.println("Server ready...");

        Thread.sleep(1125 * 60 * 1000);
        System.out.println("Server exiting");
        System.exit(0);
    }
}
