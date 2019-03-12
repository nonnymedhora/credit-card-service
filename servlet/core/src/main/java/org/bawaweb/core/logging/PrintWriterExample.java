package org.bawaweb.core.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
 
public class PrintWriterExample {
 
    public static void main(String[] args) {
 
        String filename = "JustExample.txt";
        File f = new File(filename);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(f);
            String strContent = "Just Example";
            int intContent = 1;
            double doubleContent = Math.random();
            //convinient way to add new line while print content
            pw.println(strContent);
            //using printf to format content. SInce java 1.5
            pw.printf("Hello this is %s. I am %d years old. My lucky number is  %f", strContent, intContent, doubleContent);
            pw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            //no matter what happen. close the output stream always.
            //note that closing a printer will not throw IOException
            if(pw!=null){
                pw.close();
            }
        }
 
    }
 
}
