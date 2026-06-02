package com.example.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static Connection connection = null;
//on met private pour obliger le user à passer par getInstance 
    private DatabaseConnection() {}

    public static Connection getInstance() {
        try {
            //si la connection existe pas encore ou qu'elle est fermée, on la crée
            if (connection == null || connection.isClosed()) {
                Properties properties = loadProperties();
                
                String url = properties.getProperty("db.url");
                String user = properties.getProperty("db.user");
                String password = properties.getProperty("db.password");
                
                connection = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            System.err.println("échec" + e.getMessage());
            throw new RuntimeException(e);
        }
        return connection;
    }

    // passer par un fichier properties est bien meilleur car on a pas besoin de stocker les ids dans une fichier java et d'avoir à tout recompilé à chaque fois

    private static Properties loadProperties() {
        Properties properties = new Properties();
        
        InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("impossible de trouver le fichier: " + input, e);
        }
        
        return properties;
    }
}