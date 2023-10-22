package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.ProductDAO;
import DTO.ProductDTO;

public class ListProducts extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String searchProductName = request.getParameter("productName");

        try {
            ProductDAO productDAO = new ProductDAO();
            List<ProductDTO> products;

            request.setAttribute("searchResultFound", true);

            if (searchProductName != null && !searchProductName.isEmpty()) {
                products = productDAO.getProducts(searchProductName);
                if (products.isEmpty()) {
                    products = productDAO.getProducts(null);
                    request.setAttribute("searchResultFound", false);
                }
            } else {
                products = productDAO.getProducts(null);
            }

            request.setAttribute("products", products);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/list_products.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            out.println("An error occurred: " + e.getMessage());
        }
    }
}
