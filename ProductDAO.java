package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.ProductDTO;
import utils.DB;

public class ProductDAO {

    public List<ProductDTO> getProducts(String searchProductName) throws SQLException, ClassNotFoundException {
        List<ProductDTO> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        conn = DB.getConnection();

        String query;
        if (searchProductName != null && !searchProductName.isEmpty()) {
            query = "SELECT product_code, product_name, price, register_datetime, update_datetime, delete_datetime " +
                    "FROM m_product WHERE product_name LIKE ? AND delete_datetime IS NULL ORDER BY product_code ASC";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + searchProductName + "%");
        } else {
            query = "SELECT product_code, product_name, price, register_datetime, update_datetime, delete_datetime " +
                    "FROM m_product WHERE delete_datetime IS NULL ORDER BY product_code ASC";
            stmt = conn.prepareStatement(query);
        }

        rs = stmt.executeQuery();

        while (rs.next()) {
            ProductDTO product = new ProductDTO();
            product.setProductCode(rs.getInt("product_code"));
            product.setProductName(rs.getString("product_name"));
            product.setProductPrice(rs.getInt("price"));
            product.setDeleteDateTime(rs.getString("delete_datetime"));
            products.add(product);
        }

        return products;
    }
    
    
    public void addProduct(ProductDTO product) throws SQLException, ClassNotFoundException {
        Connection conn = DB.getConnection();

        String insertQuery = "INSERT INTO m_product (product_name, price, register_datetime, update_datetime) VALUES (?, ?, datetime('now'), datetime('now'))";
        PreparedStatement preparedStatement = conn.prepareStatement(insertQuery); 
        preparedStatement.setString(1, product.getProductName());
        preparedStatement.setInt(2, product.getProductPrice());
        preparedStatement.executeUpdate();

        conn.close();
    }
    
    
    public void updateProduct(ProductDTO product) throws SQLException, ClassNotFoundException {
        Connection conn = DB.getConnection();
        
        String updateQuery = "UPDATE m_product SET product_name = ?, price = ?, update_datetime = datetime('now') WHERE product_code = ? AND delete_datetime IS NULL AND product_code NOT IN (SELECT product_code FROM t_sales)";
        PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
        preparedStatement.setString(1, product.getProductName());
        preparedStatement.setInt(2, product.getProductPrice());
        preparedStatement.setInt(3, product.getProductCode());
        preparedStatement.executeUpdate();
    }
    
    
    public void updateProductName(ProductDTO product) throws SQLException, ClassNotFoundException {
        Connection conn = DB.getConnection();

        String updateQuery = "UPDATE m_product SET product_name = ?, update_datetime = datetime('now') WHERE product_code = ? AND delete_datetime IS NULL";
        PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
        preparedStatement.setString(1, product.getProductName());
        preparedStatement.setInt(2, product.getProductCode());
        preparedStatement.executeUpdate();
    }
    

    public void deleteProduct(int productCode) throws SQLException, ClassNotFoundException {
        Connection conn = DB.getConnection();

        String updateQuery = "UPDATE m_product SET delete_datetime = datetime('now') WHERE product_code = ? AND delete_datetime IS NULL AND product_code NOT IN (SELECT product_code FROM t_sales) ";
        PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
        preparedStatement.setInt(1, productCode);
        preparedStatement.executeUpdate();
		
    }

    public ProductDTO getProduct(int productId) throws SQLException, ClassNotFoundException {
        ProductDTO product = null;
        Connection conn = DB.getConnection();

        String query = "SELECT * FROM m_product WHERE product_code = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, productId);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            product = new ProductDTO();
            product.setProductCode(rs.getInt("product_code"));
            product.setProductName(rs.getString("product_name"));
            product.setProductPrice(rs.getInt("price"));
            product.setRegisterDateTime(rs.getString("register_datetime"));
            product.setUpdateDateTime(rs.getString("update_datetime"));
            // new change
            product.setDeleteDateTime(rs.getString("delete_datetime"));
        }

        conn.close();
        
        return product;
    }

    public boolean hasProductBeenSold(int productId) throws SQLException, ClassNotFoundException {
        Connection conn = DB.getConnection();

        String query = "SELECT sales_date FROM t_sales WHERE product_code=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, productId);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return true;
        }
        
        return false;
    }
    
    
}
