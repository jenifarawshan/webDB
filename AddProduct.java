package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.ProductDAO;
import DTO.ProductDTO;

public class AddProduct extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Display the form to add a product
        request.getRequestDispatcher("/WEB-INF/add_product.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productName = request.getParameter("productName");
        String unitPriceStr = request.getParameter("unitPrice");

        if (productName != null && unitPriceStr != null && !productName.isEmpty() && !unitPriceStr.isEmpty()) {
            try {
               int unitPrice = Integer.parseInt(unitPriceStr);

                // Create a ProductDTO
                ProductDTO product = new ProductDTO();
                product.setProductName(productName);
                product.setProductPrice(unitPrice);

                // Insert the product into the database
                try {
                    ProductDAO productDAO = new ProductDAO();
                    productDAO.addProduct(product);

                    // Redirect to /products after adding the product
                    response.sendRedirect(request.getContextPath() + "/products");
                } catch (ClassNotFoundException | SQLException e) {
                    request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
                    request.getRequestDispatcher("/WEB-INF/add_product.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid unit price. Please enter a valid number.");
                request.getRequestDispatcher("/WEB-INF/add_product.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Please provide product name and unit price.");
            request.getRequestDispatcher("/WEB-INF/add_product.jsp").forward(request, response);
        }
    }
}
