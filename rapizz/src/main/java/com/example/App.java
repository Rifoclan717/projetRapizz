package com.example;

import java.sql.Connection;

import com.example.repositories.DatabaseConnection;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Connection conn = DatabaseConnection.getInstance();
        
        System.out.println( "Hello World!" );
    }
}
