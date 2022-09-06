package com.etz.gh.amard.utilities;

import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.Configuration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author seth.sebeh
 */
public class Config {

    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Config.class);
    public static final List<Configuration> CONFIG_DATA;
    private final static Properties props = new Properties();

    public static final String PATHS;
    public static final String FG_TERMINAL_ID;
    public static final String FG_PIN;

    private Config() {
    }

    static {
        try {
            PropertyConfigurator.configure("cfg\\log4j.config");
            props.load(new FileReader(new File("cfg\\amard.properties")));
        } catch (IOException ex) {
            logger.error("Sorry something went bad ooo. Unable to load config data from file|database. ", ex);
        }
        CONFIG_DATA = AmardDAO.getConfigData();
        PATHS = props.getProperty("PATHS");
        FG_TERMINAL_ID = props.getProperty("FG_TERMINAL_ID");
        FG_PIN = props.getProperty("FG_PIN");
    }

    public static String getKey(String key) {
        Configuration configuration = CONFIG_DATA.stream().filter(c -> c.getK().equals(key)).findFirst().orElse(null);
        String value = configuration.getV();
        logger.info(Thread.currentThread().getName() + " " + key + " => " + value);
        return value;
    }
    
    public static String getProperty(String key) {
        return props.getProperty(key);
    }
    
    public static String getPropertyEager(String key) {
        try {
            props.load(new FileReader(new File("cfg\\amard.properties")));
        } catch (Exception ex) {
            logger.error("sorry something wrong", ex);
        }
        return props.getProperty(key);
    }

    public static void printKV(List<Configuration> configs) {
        configs.forEach(c -> {
            logger.info(Thread.currentThread().getName() + " " + c.getK() + " - " + c.getV());
        });
    }

}
