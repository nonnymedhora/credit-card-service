package org.bawaweb.server.util;

import java.util.ResourceBundle;

/**
 * Created by medhoran on 12/12/13.
 * WIll be the store house for all properties files of the application (except the config db stuff ofcourse)
 * note that the location of the properties file within the jar is in the top level 'META-INF' directory
 * jar >>>> META-INF >>>>>> messages_en_US.properties, etc
 */


public class ResourceBundleUtil {

//    public ResourceBundleUtil(){}

    private final static ResourceBundle rb;

    static {
        rb = ResourceBundle.getBundle("META-INF.messages", java.util.Locale.getDefault());
    }

    public static String getValue(String key) {
        return rb.getString(key);
    }


}
