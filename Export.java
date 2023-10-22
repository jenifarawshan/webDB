package servlets;

import DAO.SalesDAO;
import DTO.SalesDTO;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DB;
import utils.ExportUtils;

public class Export extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/export.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            out.println("An error occurred: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Connection conn = DB.getConnection();
            SalesDAO salesDAO = new SalesDAO(conn);

            String action = request.getParameter("action");

            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/csv");

            List<SalesDTO> sales = new ArrayList<SalesDTO>();

            if (action.equals("sales_summary")) {
                response.setHeader("Content-Disposition",
                        "attachment; filename=sales_summary.csv");
                sales = salesDAO.getSalesSummary();
            } else if (action.equals("total_sales_by_year_month")) {
                String yearMonth = request.getParameter("yearMonth");
                int year = Integer.parseInt(yearMonth.split("-")[0]);
                int month = Integer.parseInt(yearMonth.split("-")[1]);
                LocalDate localDate = LocalDate.now();
                int currentYear = localDate.getYear();
                int currentMonth = localDate.getMonthValue();

                if (year < 2020) {
                    throw new IOException("Year cannot be less than 2020");
                } else if (year > currentYear) {
                    throw new IOException("Year cannot be greater than current year");
                } else if (month > currentMonth) {
                    throw new IOException("Month cannot be greater than current month");
                }
                response.setHeader("Content-Disposition", "attachment; filename=\"sales_" + yearMonth + ".csv\"");

                sales = salesDAO.getTotalSalesByYearMonth(yearMonth);
            }

            if (sales.size() == 0) {
                throw new IOException("No sale found for the given year and month");
            }
            
            OutputStream outputStream = response.getOutputStream();

            ExportUtils.salesSummary(sales, outputStream);


        } catch (ClassNotFoundException | IOException | NullPointerException | SQLException e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.setHeader("Content-Disposition", "");
            request.setAttribute("errorMessage",
                    "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/export.jsp")
                    .forward(request, response);
        }
    }
}
