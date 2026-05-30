package com.example.views;

import com.example.model.Client;
import com.example.dao.ClientDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddClientWindow extends JFrame {

    private JTextField txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtBalance;
    private ClientDao clientDao;

    public AddClientWindow() {
        clientDao = new ClientDao();

        setTitle("Nouveau Client");
        setSize(400, 350);
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(7, 2, 10, 10));

        add(new JLabel("Prénom :"));
        txtFirstName = new JTextField();
        add(txtFirstName);

        add(new JLabel("Nom :"));
        txtLastName = new JTextField();
        add(txtLastName);

        add(new JLabel("Email :"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Téléphone :"));
        txtPhone = new JTextField();
        add(txtPhone);

        add(new JLabel("Adresse :"));
        txtAddress = new JTextField();
        add(txtAddress);

        add(new JLabel("Solde de départ :"));
        txtBalance = new JTextField("0.00");
        add(txtBalance);

        JButton btnSave = new JButton("Enregistrer");
        add(new JLabel(""));
        add(btnSave);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveClient();
            }
        });
    }

    private void saveClient() {
        try {
            Client newClient = new Client();
            
            newClient.setFirstName(txtFirstName.getText());
            newClient.setLastName(txtLastName.getText());
            newClient.setEmail(txtEmail.getText());
            newClient.setPhoneNumber(txtPhone.getText());
            newClient.setAddress(txtAddress.getText());
            newClient.setBalance(Double.parseDouble(txtBalance.getText().replace(",", ".")));

            boolean success = clientDao.add(newClient);

            if (success) {
                JOptionPane.showMessageDialog(this, "client enregistré !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "erreur à la création", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "le solde doit être un nombre valide.", "erreur de saisie", JOptionPane.WARNING_MESSAGE);
        }
    }
}