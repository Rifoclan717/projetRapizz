package com.example.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.example.dao.ClientDao;
import com.example.dao.DeliveryGuyDao;
import com.example.dao.OrderDao;
import com.example.dao.OrderProductDao;
import com.example.dao.ProductDao;
import com.example.dao.VehicleDao;
import com.example.model.Client;
import com.example.model.DeliveryGuy;
import com.example.model.Product;
import com.example.model.Vehicle;
import com.example.repositories.DatabaseConnection;

public class OrderWindow extends JFrame {

    private final ClientDao clientDao = new ClientDao();
    private final ProductDao productDao = new ProductDao();

    private JComboBox<Client> cbClients;
    private JComboBox<Product> cbProducts;

    private JComboBox<Size> cbSize; 
    private JSpinner spQuantity;

    private JLabel lblUnitPrice; 
    private JLabel lblTotal;     

    private JButton btnRefresh;
    private JButton btnSubmit;
    private JButton btnClose;

    private final DecimalFormat money = new DecimalFormat("0.00"); 
    private final OrderDao orderDao = new OrderDao();
    private final OrderProductDao orderProductDao = new OrderProductDao();
    private final DeliveryGuyDao deliveryGuyDao = new DeliveryGuyDao();
    private final VehicleDao vehicleDao = new VehicleDao();

    private enum Size {
        NAINE("Naine", 2.0 / 3.0),
        HUMAINE("Humaine", 1.0),
        OGRESSE("Ogresse", 4.0 / 3.0);

        final String label;
        final double multiplier;

        Size(String label, double multiplier) {
            this.label = label;
            this.multiplier = multiplier;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public OrderWindow() {
        initUI();
        loadData();
        updatePrices(); 
    }

    private void initUI() {
        setTitle("Rapizz - Passer une commande");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(720, 380)); // a bit taller
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        JLabel header = new JLabel("Nouvelle commande");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        root.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        root.add(form, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        cbClients = new JComboBox<>();
        cbProducts = new JComboBox<>();

        cbSize = new JComboBox<>(Size.values()); 
        cbSize.setSelectedItem(Size.HUMAINE);

        spQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));

        lblUnitPrice = new JLabel("—"); 
        lblTotal = new JLabel("—");     
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD));

        // Affichage lisible sans dépendre de toString()
        cbClients.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Client c) {
                    String fullName = (safe(c.getFirstName()) + " " + safe(c.getLastName())).trim();
                    String phone = safe(c.getPhoneNumber()).trim();
                    String email = safe(c.getEmail()).trim();

                    String label = fullName.isEmpty() ? ("Client #" + c.getId()) : fullName;
                    if (!phone.isEmpty()) {
                        label += " — " + phone;
                    } else if (!email.isEmpty()) {
                        label += " — " + email;
                    }

                    setText(label);
                }
                return this;
            }
        });

        cbProducts.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Product p) {
                    String name = safe(p.getName()).trim();
                    setText((name.isEmpty() ? ("Product #" + p.getId()) : name) + " — " + p.getBasePrice() + " € (Humaine)");
                }
                return this;
            }
        });

        cbProducts.addActionListener(e -> updatePrices());
        cbSize.addActionListener(e -> updatePrices());
        spQuantity.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updatePrices();
            }
        });

        int row = 0;
        addLabeled(form, gbc, row++, "Client:", cbClients);
        addLabeled(form, gbc, row++, "Pizza (Product):", cbProducts);
        addLabeled(form, gbc, row++, "Taille:", cbSize);            
        addLabeled(form, gbc, row++, "Quantité:", spQuantity);
        addLabeled(form, gbc, row++, "Prix unitaire:", lblUnitPrice); 
        addLabeled(form, gbc, row++, "Total:", lblTotal);             

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRefresh = new JButton("Rafraîchir");
        btnSubmit = new JButton("Valider");
        btnClose = new JButton("Fermer");

        actions.add(btnRefresh);
        actions.add(btnSubmit);
        actions.add(btnClose);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        form.add(actions, gbc);

        btnRefresh.addActionListener(e -> loadData());
        btnClose.addActionListener(e -> dispose());

        btnSubmit.addActionListener(e -> submitOrder());
    }

    private void loadData() {
        List<Client> clients = clientDao.getClients();
        DefaultComboBoxModel<Client> clientModel = new DefaultComboBoxModel<>();
        for (Client c : clients) {
            clientModel.addElement(c);
        }
        cbClients.setModel(clientModel);

        List<Product> products = productDao.getMenu();
        DefaultComboBoxModel<Product> productModel = new DefaultComboBoxModel<>();
        for (Product p : products) {
            productModel.addElement(p);
        }
        cbProducts.setModel(productModel);

        if (clientModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this, "Aucun client trouvé dans la table Clients.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        if (productModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this, "Aucun produit trouvé dans la table Products.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        updatePrices(); 
    }

    
    private void updatePrices() {
        Product product = (Product) cbProducts.getSelectedItem();
        Size size = (Size) cbSize.getSelectedItem();
        int qty = (int) spQuantity.getValue();

        if (product == null || size == null) {
            lblUnitPrice.setText("—");
            lblTotal.setText("—");
            return;
        }

        double unit = product.getBasePrice() * size.multiplier;
        double total = unit * qty;

        lblUnitPrice.setText(money.format(unit) + " €");
        lblTotal.setText(money.format(total) + " €");
    }

    private void submitOrder() {
        Client client = (Client) cbClients.getSelectedItem();
        Product product = (Product) cbProducts.getSelectedItem();
        Size size = (Size) cbSize.getSelectedItem();
        int qty = (int) spQuantity.getValue();

        if (client == null) {
            JOptionPane.showMessageDialog(this, "Sélectionne un client.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Sélectionne une pizza (product).", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (size == null) {
            JOptionPane.showMessageDialog(this, "Sélectionne une taille.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double unit = product.getBasePrice() * size.multiplier;
        double total = unit * qty;
        
        if (client.getBalance() < total) {
            JOptionPane.showMessageDialog(this, "Solde insuffisant pour cette commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DeliveryGuy freeDriver = deliveryGuyDao.getFreeDriver();
        if (freeDriver == null) {
            JOptionPane.showMessageDialog(this, "Aucun livreur disponible pour le moment.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Vehicle freeVehicle = vehicleDao.getFreeVehicle();
        if (freeVehicle == null) {
            JOptionPane.showMessageDialog(this, "Aucun véhicule disponible pour le moment.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String recap = "Commande :\n\n"
                + "Client: " + displayClient(client) + "\n"
                + "Solde à déduire: " + money.format(total) + " €\n"
                + "Produit: " + displayProduct(product) + "\n"
                + "Taille: " + size.label + "\n"
                + "Prix unitaire: " + money.format(unit) + " €\n"
                + "Quantité: " + qty + "\n"
                + "Total: " + money.format(total) + " €\n\n"
                + "Livreur assigné: " + freeDriver.getFirstName() + " " + freeDriver.getLastName() + "\n"
                + "Véhicule assigné: " + freeVehicle.getNumberPlate() + " (" + freeVehicle.getVehicleType() + ")\n\n"
                + "Confirmer ?";

        int choice = JOptionPane.showConfirmDialog(this, recap, "Confirmation", JOptionPane.OK_CANCEL_OPTION);
        if (choice == JOptionPane.OK_OPTION) {

            int driverId = freeDriver.getId();
            int vehicleId = freeVehicle.getId();

            String sizeDb = switch (size) {
                case NAINE -> "naine";
                case HUMAINE -> "humaine";
                case OGRESSE -> "ogresse";
            };

            Connection conn = DatabaseConnection.getInstance();
            try {
                conn.setAutoCommit(false);

                // Déduire le solde du client
                clientDao.updateBalance(conn, client.getId(), client.getBalance() - total);

                int orderId = orderDao.insertOrder(conn, client.getId(), driverId, vehicleId);
                orderProductDao.insertOrderProduct(conn, orderId, product.getId(), qty, sizeDb);

                conn.commit();

                JOptionPane.showMessageDialog(this,
                        "Commande enregistrée. (orderId=" + orderId + ")",
                        "OK", JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } catch (Exception ex) {
                try { conn.rollback(); } catch (Exception ignore) {}
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'enregistrement: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } finally {
                try { conn.setAutoCommit(true); } catch (Exception ignore) {}
            }
        }
    }

    private static String displayClient(Client c) {
        String fullName = (safe(c.getFirstName()) + " " + safe(c.getLastName())).trim();
        if (fullName.isEmpty()) {
            fullName = "Client #" + c.getId();
        }
        return fullName + " (id=" + c.getId() + ")";
    }

    private static String displayProduct(Product p) {
        String name = safe(p.getName()).trim();
        if (name.isEmpty()) {
            name = "Product #" + p.getId();
        }
        return name + " (id=" + p.getId() + ", base(Humaine)=" + p.getBasePrice() + "€)";
    }

    private static void addLabeled(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.25;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.75;
        panel.add(field, gbc);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}