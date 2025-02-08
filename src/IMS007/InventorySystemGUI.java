package IMS007;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class InventorySystemGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField, categoryField, quantityField, priceField;
    private InventoryDAO inventoryDAO;
    private JPanel mainPanel, homePanel, aboutPanel, addItemPanel, deleteItemPanel, reportPanel;
    private CardLayout cardLayout;

    public InventorySystemGUI() {
        inventoryDAO = new InventoryDAO();
        frame = new JFrame("Inventory Management System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        

        // Sidebar Navigation Panel
        JPanel sidePanel = new JPanel(new GridLayout(6, 1, 5, 5));
        sidePanel.setBackground(new Color(50, 50, 50));
        sidePanel.setPreferredSize(new Dimension(200, 600));

        JButton homeButton = new JButton("Home");
        JButton addButton = new JButton("Add Item");
        JButton deleteButton = new JButton("View And Delete");
        JButton reportButton = new JButton("Generate Report");
        JButton aboutButton = new JButton("About");
        JButton exitButton = new JButton("Exit");

        styleButton(homeButton);
        styleButton(addButton);
        styleButton(deleteButton);
        styleButton(reportButton);
        styleButton(aboutButton);
        styleButton(exitButton);

        sidePanel.add(homeButton);
        sidePanel.add(addButton);
        sidePanel.add(deleteButton);
        sidePanel.add(reportButton);
        sidePanel.add(aboutButton);
        sidePanel.add(exitButton);

        frame.add(sidePanel, BorderLayout.WEST);

        // Main Content Area with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Home Panel
        homePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Inventory System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        homePanel.add(welcomeLabel, BorderLayout.NORTH);
        JLabel totalItemsLabel = new JLabel("Total Items: " + inventoryDAO.getTotalItemCount(), SwingConstants.CENTER);
        homePanel.add(totalItemsLabel, BorderLayout.CENTER);
        homePanel.add(createChartPanel(), BorderLayout.SOUTH);

        mainPanel.add(homePanel, "Home");
        
        

        // Add Item Panel
        addItemPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        addItemPanel.add(new JLabel("Name:")); nameField = new JTextField(); addItemPanel.add(nameField);
        addItemPanel.add(new JLabel("Category:")); categoryField = new JTextField(); addItemPanel.add(categoryField);
        addItemPanel.add(new JLabel("Quantity:")); quantityField = new JTextField(); addItemPanel.add(quantityField);
        addItemPanel.add(new JLabel("Price:")); priceField = new JTextField(); addItemPanel.add(priceField);
        JButton addItemConfirmButton = new JButton("Add Item");
        addItemConfirmButton.addActionListener(e -> addItem());
        addItemPanel.add(addItemConfirmButton);
        mainPanel.add(addItemPanel, "AddItem");

        // Delete Item Panel
        deleteItemPanel = new JPanel(new BorderLayout());
        model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Quantity", "Price"}, 0);
        table = new JTable(model);
        deleteItemPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton deleteItemConfirmButton = new JButton("Delete Selected Item");
        deleteItemConfirmButton.addActionListener(e -> deleteItem());
        deleteItemPanel.add(deleteItemConfirmButton, BorderLayout.SOUTH);
        loadTableData();
        mainPanel.add(deleteItemPanel, "DeleteItem");

        // Report Panel
        reportPanel = new JPanel(new BorderLayout());
        JButton generateReportButton = new JButton("Generate PDF Report");
        generateReportButton.addActionListener(e -> InventoryDAO.generateReport());
        reportPanel.add(generateReportButton, BorderLayout.CENTER);
        mainPanel.add(reportPanel, "Report");

        // About Panel
        aboutPanel = new JPanel(new BorderLayout());
        JLabel aboutLabel = new JLabel("Version 1.0 - Developed by Softcrowd Technologies", SwingConstants.CENTER);
        aboutPanel.add(aboutLabel, BorderLayout.CENTER);
        mainPanel.add(aboutPanel, "About");

        frame.add(mainPanel, BorderLayout.CENTER);
        
        

        // Button Actions
        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        addButton.addActionListener(e -> cardLayout.show(mainPanel, "AddItem"));
        deleteButton.addActionListener(e -> cardLayout.show(mainPanel, "DeleteItem"));
        reportButton.addActionListener(e -> cardLayout.show(mainPanel, "Report"));
        aboutButton.addActionListener(e -> cardLayout.show(mainPanel, "About"));
        exitButton.addActionListener(e -> cardLayout.show(mainPanel, "Exit"));

        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void loadTableData() {
        model.setRowCount(0);
        for (InventoryItem item : inventoryDAO.getAllItems()) {
            model.addRow(new Object[]{item.getId(), item.getName(), item.getCategory(), item.getQuantity(), item.getPrice()});
        }
    }

    private void addItem() {
        InventoryItem item = new InventoryItem(
            nameField.getText(),
            categoryField.getText(),
            Integer.parseInt(quantityField.getText()),
            Double.parseDouble(priceField.getText())
        );
        inventoryDAO.addItem(item);
        loadTableData();
    }

    private void deleteItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) table.getValueAt(selectedRow, 0);
            inventoryDAO.deleteItem(id);
            loadTableData();
        }
    }
    
    private void exit()
    {
      System.out.println("Good Bye..");
    }

	private JPanel createChartPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (InventoryItem item : inventoryDAO.getAllItems()) {
            dataset.addValue(item.getQuantity(), item.getCategory(), item.getName());
        }
        JFreeChart barChart = ChartFactory.createBarChart(
                "Inventory Overview", "Category", "Quantity", dataset
        );
        return new ChartPanel(barChart);
    }

    public static void main(String[] args) {
        new InventorySystemGUI();
    }
}
