package com.etz.gh.amard.utilities;

import com.etz.gh.amard.entities.Configuration;
import java.util.List;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author seth.sebeh
 */
public class Config1 {
    
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Config1.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    public static String getKey(List<Configuration> l, String key) {
       logger.info("Searching for value with key: " + key);
       Configuration configuration = l.stream().filter( c -> c.getK().equals(key)).findFirst().orElse(null);
       logger.info("Key found " + configuration.getV()); 
       return configuration.getV();
    }
    
    public static void printKV(List<Configuration> configs){
        configs.forEach(c -> {
            logger.info(c.getK() + " - " + c.getV());
        });
        
     
    }
    
}
