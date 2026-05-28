package com.example;

import com.example.views.ClientWindow;
import com.example.views.MenuWindow;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // ClientWindow window = new ClientWindow();
                // window.setVisible(true);
                
                MenuWindow window = new MenuWindow();
                window.setVisible(true);
            }
        });
    }
}