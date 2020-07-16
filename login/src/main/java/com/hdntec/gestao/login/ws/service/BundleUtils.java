
package com.hdntec.gestao.login.ws.service;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
public class BundleUtils {

    private static ResourceBundle bundleMSG;
  

    /**
    * getBundleMessage
    * @author <a href="mailto:hdn.jesse@cflex.com.br">Jessé</a>
    * @since 13/02/2009
    * @see
    * @param 
    * @return ResourceBundle
    */
    public static ResourceBundle getBundleMessage() {
    
        if (bundleMSG == null) {
            try {

                bundleMSG = ResourceBundle.getBundle("messages", Locale.getDefault(), Thread.currentThread()
                                .getContextClassLoader());

            } catch (MissingResourceException e) {

                StringBuffer msg = new StringBuffer();
                msg.append("The 'messages.properties' file was not found in classpath.");
            	throw new RuntimeException(msg.toString());

            }

        }

        return bundleMSG;

    }

    /**
     * getMessage 
     * @author <a href="mailto:hdn.jesse@cflex.com.br">Jessé</a>
     * @since 13/02/2009
     * @see
     * @param 
     * @return String
     */
    public static String getMessage(String key) {
        return PropertiesUtil.getMessage(key);
    }

}
