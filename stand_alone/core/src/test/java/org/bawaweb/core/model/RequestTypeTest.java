package org.bawaweb.core.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by medhoran on 12/9/13.
 */
public class RequestTypeTest {
    private static final Character G = 'G';
    private static final Character A = 'A';

    @Test
    public void testGet() throws Exception {
        RequestType rt = RequestType.GET;
        assertEquals(G, rt.getRequestType());
    }

    @Test
    public void testAdd() throws Exception {
        RequestType rt = RequestType.ADD;
        assertEquals(A, rt.getRequestType());
    }

}
