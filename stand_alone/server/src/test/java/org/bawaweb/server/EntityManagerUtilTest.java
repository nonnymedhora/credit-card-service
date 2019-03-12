package org.bawaweb.server;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by medhoran on 12/17/13.
 */
public class EntityManagerUtilTest {


    @Before
    public void setUp() throws Exception {
        // note that we are using the same properties file
        // but is on another location in the server's file system
        // TODO: write utility class to find proper properties file?
        final String propFilePath = System.getProperty("user.home") + System.getProperty("file.separator") + "app.properties";
        EntityManagerUtil.initialize(propFilePath);
    }

    @Test
    public void testGetEntityManager() throws Exception {
        assertNotNull(EntityManagerUtil.getEntityManager());
    }

    @Test
    public void testCloseEntityManager() throws Exception {
        EntityManagerUtil.closeEntityManager();
//        assertNull(EntityManagerUtil.getEntityManager());
    }

    @Test
    public void testInitialize() throws Exception {
        assertNotNull(EntityManagerUtil.getEntityManager());
    }

}
