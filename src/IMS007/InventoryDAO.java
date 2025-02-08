package IMS007;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    public void addItem(InventoryItem item) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO products (name, category, quantity, price) VALUES (?, ?, ?, ?)") ) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteItem(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM products WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public int getTotalItemCount() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM products";  // Adjust table name if necessary

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


    public List<InventoryItem> getAllItems() {
        List<InventoryItem> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {
            while (rs.next()) {
                items.add(new InventoryItem(rs.getInt("id"), rs.getString("name"), rs.getString("category"), rs.getInt("quantity"), rs.getDouble("price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static void generateReport() {
        for (InventoryItem item : new InventoryDAO().getAllItems()) {
            System.out.println("ID: " + item.getId() + ", Name: " + item.getName() + ", Category: " + item.getCategory() + ", Quantity: " + item.getQuantity() + ", Price: " + item.getPrice());
        }
    }
}
