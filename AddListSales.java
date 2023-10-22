package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.ProductDAO;
import DAO.SalesDAO;
import DTO.ProductDTO;
import DTO.SalesDTO;
import utils.DB;

public class AddListSales extends HttpServlet {
    private SalesDAO salesDAO;
    private ProductDAO productDAO = new ProductDAO();
    private List<SalesDTO> salesList = new ArrayList<SalesDTO>();
    private List<SalesDTO> tempSalesList = new ArrayList<SalesDTO>();
    private List<ProductDTO> productList = new ArrayList<ProductDTO>();
    private String currentDate = java.time.LocalDate.now().toString();

    // Constructor to initialize the SalesDAO
    public AddListSales() throws ClassNotFoundException, SQLException {
        Connection conn = DB.getConnection();
        salesDAO = new SalesDAO(conn);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        try {
            salesList = salesDAO.listSales(currentDate);

            request.setAttribute("salesList", salesList);
            request.setAttribute("tempSalesList", tempSalesList);
            request.setAttribute("currentDate", currentDate);

            // Get the list of products from the ProductDAO ProductDAO
            productList = productDAO.getProducts(null); // Pass null to get all products
            request.setAttribute("productList", productList);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage",
                    "An error occurred: " + e.getMessage());
        } finally {
            request.getRequestDispatcher("/WEB-INF/add_list_sales.jsp")
                    .forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        // Implement logic to add new sales data
        try {

            String action = request.getParameter("action");

            if (action.equals("add_update_temp")) {
                int productCode = Integer.parseInt(request.getParameter("productCode"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                ProductDTO product = productDAO.getProduct(productCode);
                
                // check for deleted product
                if (product.getDeleteDateTime() != null) {
                    productList = productDAO.getProducts(null); // Pass null to get all products
                    throw new IOException("Product has been deleted!");
                }
                
                String productName = product.getProductName();

                // Check if a sales entry with the same productCode already exists
                boolean salesExists = false;
                for (SalesDTO sales : tempSalesList) {
                    if (sales.getProductCode() == productCode) {
                        sales.setQuantity(sales.getQuantity() + quantity);
                        salesExists = true;
                        break;
                    }
                }

                if (!salesExists) {
                    // Create a new sales entry if not found
                    SalesDTO sales = new SalesDTO();
                    sales.setProductCode(productCode);
                    sales.setProductName(productName);
                    sales.setQuantity(quantity);
                    tempSalesList.add(sales);
                }

            } else if (action.equals("add_update_batch")) {
                for (SalesDTO sales : tempSalesList) {
                    salesDAO.addSales(currentDate, sales.getProductCode(), sales.getQuantity());
                }
                salesList = salesDAO.listSales(currentDate);
                tempSalesList = new ArrayList<SalesDTO>();
            }

        } catch (ClassNotFoundException | IOException | SQLException e) {
            // Handle any exceptions appropriately
            e.printStackTrace();
            request.setAttribute("errorMessage",
                    "An error occurred: " + e.getMessage());

        } finally {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            request.setAttribute("salesList", salesList);
            request.setAttribute("tempSalesList", tempSalesList);
            request.setAttribute("currentDate", currentDate);
            request.setAttribute("productList", productList);
            request.getRequestDispatcher("/WEB-INF/add_list_sales.jsp")
                    .forward(request, response);
        }
    }
}
