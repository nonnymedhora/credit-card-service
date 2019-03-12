package org.bawaweb.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.FileInputStream;
import java.util.Properties;


/**
 * @author medhoran
 * The main class which is used for storing the persistence context
 */
//@TODO - move to a more sensible package
public class EntityManagerUtil {

    private static EntityManagerFactory entityManagerFactory;


	  public static EntityManager getEntityManager() {
	    return entityManagerFactory.createEntityManager();

	  }
	  
	  public static void closeEntityManager() {
		  entityManagerFactory.close();
	  }

    /**
     * Initializes the Resource Bundle based on the application.properties file specified
     * @param propFilePath
     */
    public static void initialize(String propFilePath) {
        try {
            //http://stackoverflow.com/questions/8775303/read-properties-file-outside-jar-file
            //http://stackoverflow.com/questions/3935394/how-to-externalize-properties-from-jpas-persistence-xml
            //to load application's properties
            Properties props = new Properties();

            FileInputStream file;

            //load the file handle for the property file
            file = new FileInputStream(propFilePath);

            //load all the properties from this file
            props.load(file);

            //we have loaded the properties, so close the file handle
            file.close();

            entityManagerFactory = Persistence.createEntityManagerFactory( "customer_card", props );

        } catch (Throwable ex) {
            System.err.println("EntityManagerFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }


    /**
     * Initializes the Resource Bundle based on the application.properties file from host.properties file within the
     * jar
     */
    public static void initialize() {
        try {
            //http://stackoverflow.com/questions/8775303/read-properties-file-outside-jar-file
            //http://stackoverflow.com/questions/3935394/how-to-externalize-properties-from-jpas-persistence-xml
            //to load application's properties
            Properties props = new Properties();

            FileInputStream file;

            //load the file handle for the property file
            file = new FileInputStream("/app.properties");

            //load all the properties from this file
            props.load(file);

            //we have loaded the properties, so close the file handle
            file.close();

            entityManagerFactory = Persistence.createEntityManagerFactory( "customer_card", props );

        } catch (Throwable ex) {
            System.err.println("EntityManagerFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
