package com.example;

import com.example.views.AddClientWindow;
import com.example.views.ClientWindow;
import com.example.views.MainWindow;
import com.example.views.MenuWindow;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
                
                ;
            }
        });
    }
}