<%@ page contentType="text/html;charset=UTF-8" %>

<%
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Export</title>
        <style>
            body { font-family: Arial, sans-serif; }
            div.container { max-width: 650px; margin: auto; }
            form { display: flex; align-items: center; margin-bottom: 10px; }
            label { margin-right: 10px; }
            input[type='text'] { flex: 1; padding: 10px; border: 1px solid #ccc; box-sizing: border-box; margin-right: 10px; }
            input[type='submit'] { padding: 10px 20px; background-color: #4CAF50; color: white; border: none; height: 100%; }
            table { width: 100%; border-collapse: collapse; margin-top: 20px; }
            table, th, td { border: 1px solid black; padding: 10px; text-align: left; }
            th { background-color: #f2f2f2; }
            td .edit-button { width: 100%; height: 40px; padding: 0; margin: 0; border: none; background-color: #3498db; color: white; cursor: pointer; }
            td .edit-button:hover { text-decoration: underline; }
            form#sales-summary > input[type='submit'] { margin-left: auto; }
            .error-message { color: red; }
        </style>
    </head>
    <body>

    <div class="container">
        <h1>Export</h1>
        <form id="sales-summary" action="${pageContext.request.contextPath}/export" method="post">
            <input type="hidden" name="action" value="sales_summary" />
            <input type="submit" value="Sales Summary by Product" />
        </form>

        <form id="total-sales-by-year-month" action="${pageContext.request.contextPath}/export" method="post">
            <input type="hidden" name="action" value="total_sales_by_year_month" />
            <label for="yearMonth">Year Month</label>
            <input type="text" id="yearMonth" name="yearMonth" maxlength=50 required />
            <input type="submit" value="Total Sales by Product by Year and Month" />
        </form>

        <% if (errorMessage != null) { %>
            <p class="error-message"><%= errorMessage %></p>
        <% } %>
    </div>



    </body>
</html>
