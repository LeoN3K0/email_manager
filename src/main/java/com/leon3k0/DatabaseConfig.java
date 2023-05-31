package com.leon3k0;

public class DatabaseConfig {
    
    static String hostname;
    static String port;
    static String username;
    static String password;
    static String databasename;
    static String url;  
    static boolean demoMode = false;
    
    public static void updateConfig(String hostname, String port, String username, String password, String databasename) {
        if(!isDemoMode()){
            DatabaseConfig.hostname = hostname;
            DatabaseConfig.port = port;
            DatabaseConfig.username = username;
            DatabaseConfig.password = password;
            DatabaseConfig.databasename = databasename;
            DatabaseConfig.url = "jdbc:postgresql://" + hostname + ":" + port + "/" + databasename;
        }
        else{
            DatabaseConfig.hostname = hostname;
            DatabaseConfig.port = port;
            DatabaseConfig.username = username;
            DatabaseConfig.password = password;
            DatabaseConfig.databasename = databasename;
            DatabaseConfig.url = "jdbc:postgresql://ep-frosty-mouse-963060-pooler.us-west-2.aws.neon.tech/smartimg?user=LeoN3K0&password="+password;
        }
        
        
    }

    public static void setDemoMode(boolean value) {
        demoMode = value;
    }

    public static boolean isDemoMode() {
        return demoMode;
    }
   
}
