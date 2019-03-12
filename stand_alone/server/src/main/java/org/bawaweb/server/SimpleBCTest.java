package org.bawaweb.server;

/**
 * Created by medhoran on 12/5/13.
 * //TODO remove later
 * replace the existing main class in the jar to test
 * change in pom.xml (\server\pom.xml)
 * run mvn clean install (from bwwsecurityWebService\stand_alone)
 * java -jar customerCardService.jar
 * either BC is installed  // not installed
 */
import java.security.Security;

public class SimpleBCTest
{
    public static void main (String[] args)
    {
        String name= "BC";
        if (Security.getProvider(name) == null)
        {
            System.out.println("BouncyCastle not installed");
        }
        else
        {
            System.out.println("BouncyCastle IZ installed");
        }
    }
}