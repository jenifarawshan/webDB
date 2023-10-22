package DAO;

import DTO.SalesDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {
    private Connection connection;

    public SalesDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addSales(String date, int productCode, int quantity)
            throws SQLException {
        // Check if the product already exists for the given date
        String checkQuery = "SELECT COUNT(*) FROM t_sales WHERE sales_date=? AND product_code=?";
        PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
        checkStatement.setString(1, date);
        checkStatement.setInt(2, productCode);
        ResultSet checkResult = checkStatement.executeQuery();

        if (checkResult.next() && checkResult.getInt(1) > 0) {
            // Product already exists for the given date, update the quantity
            String updateQuery = "UPDATE t_sales SET quantity = quantity + ? WHERE sales_date=? AND product_code=?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, quantity);
            updateStatement.setString(2, date);
            updateStatement.setInt(3, productCode);
            updateStatement.executeUpdate();
            return true;
        } else {
            // Product doesn't exist for the given date, add a new entry
            String insertQuery = "INSERT INTO t_sales(sales_date, product_code, quantity, register_datetime, update_datetime) values(?, ?, ?, datetime('now'), datetime('now'))";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, date);
            insertStatement.setInt(2, productCode);
            insertStatement.setInt(3, quantity);
            insertStatement.executeUpdate();
            return true;
        }
    }

    public void updateSales(String date, int productCode, int quantity)
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE t_sales SET quantity=?, update_datetime=datetime('now') WHERE sales_date=? AND product_code=?");
        preparedStatement.setInt(1, quantity);
        preparedStatement.setString(2, date);
        preparedStatement.setInt(3, productCode);
        preparedStatement.executeUpdate();
    }

    public List<SalesDTO> listSales(String date) throws SQLException {
        List<SalesDTO> salesList = new ArrayList<SalesDTO>();
        PreparedStatement preparedStatement = null;

        preparedStatement = connection.prepareStatement(
                "SELECT p.product_name, s.quantity FROM m_product p INNER JOIN t_sales s ON p.product_code = s.product_code WHERE s.sales_date=?");
        preparedStatement.setString(1, date);

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            SalesDTO sales = new SalesDTO();
            sales.setProductName(rs.getString("product_name"));
            sales.setQuantity(rs.getInt("quantity"));
            salesList.add(sales);
        }
        return salesList;
    }

    public List<SalesDTO> getSalesSummary() throws SQLException {
        List<SalesDTO> salesList = new ArrayList<SalesDTO>();
        PreparedStatement preparedStatement = null;

        preparedStatement = connection.prepareStatement("""
                    SELECT
                            p.product_code,
                            p.product_name,
                            p.price,
                            s.quantity,
                            (p.price * s.quantity) AS amount
                        from m_product p
                    LEFT JOIN t_sales s ON
                        p.product_code = s.product_code
                    ORDER BY p.product_code ASC;
                """);

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            SalesDTO sales = new SalesDTO();
            sales.setProductCode(rs.getInt("product_code"));
            sales.setProductName(rs.getString("product_name"));
            sales.setUnitPrice(rs.getInt("price"));
            sales.setQuantity(rs.getInt("quantity"));
            sales.setAmount(rs.getInt("amount"));
            salesList.add(sales);
        }
        return salesList;
    }

    public List<SalesDTO> getTotalSalesByYearMonth(String yearMonth)
            throws SQLException {
        List<SalesDTO> salesList = new ArrayList<SalesDTO>();
        PreparedStatement preparedStatement = null;

        preparedStatement = connection.prepareStatement("""
                    SELECT
                            p.product_code,
                            p.product_name,
                            p.price, s.quantity,
                            (p.price * s.quantity) AS amount
                        from m_product p
                    INNER JOIN  t_sales s ON
                        p.product_code = s.product_code
                    WHERE s.sales_date LIKE ?
                    ORDER BY p.product_code ASC;
                """);
        preparedStatement.setString(1, "" + yearMonth + "%");

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            SalesDTO sales = new SalesDTO();
            sales.setProductCode(rs.getInt("product_code"));
            sales.setProductName(rs.getString("product_name"));
            sales.setUnitPrice(rs.getInt("price"));
            sales.setQuantity(rs.getInt("quantity"));
            sales.setAmount(rs.getInt("amount"));
            salesList.add(sales);
        }
        return salesList;
    }
}
