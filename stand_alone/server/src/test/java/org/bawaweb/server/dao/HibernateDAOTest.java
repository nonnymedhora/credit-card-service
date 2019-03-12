package org.bawaweb.server.dao;

import org.bawaweb.server.EntityManagerUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by medhoran on 12/10/13.
 */
public class HibernateDAOTest {



    @Before
    public void setUp() {
        // note that we are using the same properties file
        // but is on another location in the server's file system
        // TODO: write utility class to find proper properties file?
        final String propFilePath = System.getProperty("user.home") + System.getProperty("file.separator") + "app.properties";
        EntityManagerUtil.initialize(propFilePath);
    }

    @Test
    public void testBeginAndClear() {
        HibernateDAO dao = new HibernateDAO();
        dao.beginAndClear();
    }


    @Test
    public void testCommit() {
        HibernateDAO dao = new HibernateDAO();
        dao.beginAndClear();


    }


}
