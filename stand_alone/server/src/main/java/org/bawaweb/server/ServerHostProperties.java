package org.bawaweb.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by medhoran on 2/13/14.
 * This class will store the information of the hosting web service IP and related info
 *
 */
public final class ServerHostProperties {

    private static /*final*/ Properties serverProperties;

    public static final void initialize() {
        serverProperties  = new Properties();
        try {
            serverProperties.load(ClassLoader.class.getResourceAsStream("/host.properties"));//new FileInputStream("/host.properties"));

            if(_logger.isDebugEnabled()) {
                _logger.info("Acquired Properties: ");

                Enumeration enumeration = serverProperties.elements();
                while (enumeration.hasMoreElements()) {
                    String key = enumeration.nextElement().toString();_logger.info("key is >>" + key + "<<");
                    String value = serverProperties.getProperty(key);_logger.info("val is >>"+value+"<<");

                    String ppValue = (String) serverProperties.get(key);_logger.info("ppval is >>"+ppValue+"<<");

                    _logger.info("using getProperty " + key + " ==> " + value);
                    _logger.info("using get " + key + " ==> " + ppValue);

                }


            }
        } catch (IOException e) {
            System.out.println("Cannot do it");
            e.printStackTrace();
        }
    }

    final static Logger _logger = LoggerFactory.getLogger(ServerHostProperties.class);

   /* static {
       serverProperties  = new Properties();
        try {
            serverProperties.load(ServerHostProperties.class.getResourceAsStream("host.properties"));//new FileInputStream("/host.properties"));

            if(_logger.isDebugEnabled()) {
                _logger.info("Acquired Properties: ");

                Enumeration enumeration = serverProperties.elements();
                while (enumeration.hasMoreElements()) {
                    String key = enumeration.nextElement().toString();
                    String value = (serverProperties.getProperty(key)).toString();

                    _logger.info(key + " ==> " + value);
                }


            }
        } catch (IOException e) {
            System.out.println("Cannot do it");
            e.printStackTrace();
        }
    }*/


    public static Properties getServerProperties() {
        return serverProperties;
    }
}