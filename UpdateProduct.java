package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.ProductDAO;
import DTO.ProductDTO;

public class UpdateProduct extends HttpServlet {

    private ProductDAO productDAO = new ProductDAO();
    private ProductDTO product;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");
    	response.setCharacterEncoding("UTF-8");
    	
        PrintWriter out = response.getWriter();
        int productId = getProductIdFromPath(request);

        if (productId == -1) {
            response.sendRedirect(request.getContextPath() + "/app/products");
            return;
        }

        try {
            product = productDAO.getProduct(productId);

            if (product != null) {
                request.setAttribute("product", product);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/update_product.jsp");
                dispatcher.forward(request, response);
            } else {
                response.getWriter().println("Product not found.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            out.println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        int productId = getProductIdFromPath(request);
        

        if (productId == -1) {
            response.sendRedirect(request.getContextPath() + "/app/products");
            return;
        }

        String action = request.getParameter("action");

        if (action != null) {
            try {
                if (action.equals("update")) {
                    if (product == null) {
                        product = productDAO.getProduct(productId);
                    }
                    String productName = request.getParameter("productName");
                    String unitPriceStr = request.getParameter("unitPrice");

                    if (productName != null && unitPriceStr != null && !productName.isEmpty() && !unitPriceStr.isEmpty()) {
                        ProductDTO tempProduct = productDAO.getProduct(productId);
                        String existingUpdateDateTime = product.getUpdateDateTime();
                        String newUpdateDateTime = tempProduct.getUpdateDateTime();

                        if (!existingUpdateDateTime.equals(newUpdateDateTime)) {
                            request.setAttribute("product", product);
                            request.setAttribute("errorMessage", "Product can't be updated");
                            request.getRequestDispatcher("/WEB-INF/update_product.jsp").forward(request, response);
                            response.sendRedirect(request.getContextPath() + "/products/update/" + productId);
                            return;
                        }

                        int unitPrice = Integer.parseInt(unitPriceStr);
                        int existingUnitPrice = product.getProductPrice();
                        product.setProductName(productName); // Update product name
                        
                        if (!productDAO.hasProductBeenSold(productId)) {
                            product.setProductPrice(unitPrice); // Update product price
                            productDAO.updateProduct(product); // Update the product
                        } else {
                            productDAO.updateProductName(product); // Update product name
                            if (existingUnitPrice != unitPrice) {
                                request.setAttribute("product", product);
                                product = null;
                                request.setAttribute("errorMessage", "Price can't be updated");
                                request.getRequestDispatcher("/WEB-INF/update_product.jsp").forward(request, response);
                                response.sendRedirect(request.getContextPath() + "/products/update/" + productId);
                                return;
                            }
                        }


                    }
                } 
                if (action.equals("delete")) {
                   if (!productDAO.hasProductBeenSold(productId)) {
                        productDAO.deleteProduct(productId);
                   }
                }
                
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            } catch (SQLException | ClassNotFoundException e) {
                request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/update_product.jsp").forward(request, response);
            }
        }

        response.sendRedirect(request.getContextPath() + "/products/update/" + productId);
    }

    private int getProductIdFromPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            return -1;
        }

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length < 2) {
            return -1;
        }

        try {
//            return Integer.parseInt(pathParts[1]);
        	
        	
        	String productCodeString = pathParts[1].replaceFirst("^0+(?!$)", "");
        	return Integer.parseInt(productCodeString);
        	
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
