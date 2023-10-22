<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Currency" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.ProductDTO" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>商品検索</title>
    <style>
        body { font-family: Arial, sans-serif; }
        form { display: flex; align-items: center; margin-bottom: 10px; }
        label { margin-right: 10px; }
        input[type='text'] { flex: 1; padding: 10px; border: 1px solid #ccc; box-sizing: border-box; margin-right: 10px; }
        input[type='submit'] { padding: 10px 20px; background-color: #4CAF50; color: white; border: none; height: 100%; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        table, th, td { border: 1px solid black; padding: 10px; text-align: left; }
        th { background-color: #f2f2f2; }
        td .edit-button { width: 100%; height: 40px; padding: 0; margin: 0; border: none; background-color: #3498db; color: white; cursor: pointer; }
        td .edit-button:hover { text-decoration: underline; }
        .error-message { color: red; }
        


		.add-product-button {
        padding: 10px 20px;
        margin-top: 10px;
        background-color: #3498db;
        color: white;
        border: none;
        cursor: pointer;
        font-size: 16px;
    }

    .add-product-button:hover {
        background-color: #2980b9;
    }
    </style>
</head>
<body>

<h1>商品検索</h1>

<form action='/products' method='GET'>
    <label for='productName'>商品名</label>
    <input type='text' id='productName' name='productName' maxlength='50' value='${searchProductName != null ? searchProductName : ""}' />
    <input type='submit' value='検索'>
</form>

<%
    boolean searchResultFound = (boolean) request.getAttribute("searchResultFound");
    List<ProductDTO> products = (List<ProductDTO>) request.getAttribute("products");
%>

<%-- Display error message if no products are found --%>
<%
    if (!searchResultFound) {
%>
    <p class="error-message">検索結果に商品が見つかりません!</p>
<%
    }
%>

<table>
    <tr>
        <th>商品コード</th>
        <th>商品名</th>
        <th>単価</th>
        <th>操作</th>
    </tr>

    <%
	    Locale locale = new Locale("ja", "JP");
	    Currency currency = Currency.getInstance("JPY");
	    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
	    ((DecimalFormat)currencyFormatter).setCurrency(currency);
    
        for (ProductDTO product : products) {
            int productCode = product.getProductCode();
            String productName = product.getProductName();
            int productPrice = product.getProductPrice();
    %>
        <tr>
            <td><%= String.format("%03d", productCode) %></td>
            <td><%= productName %></td>
            <td><%= currencyFormatter.format(productPrice) %></td> <!-- Formatted price with commas -->
            <td><a href='${pageContext.request.contextPath}/products/update/<%= productCode %>'><button class='edit-button'>操作</button></a></td>
        </tr>
    <%
        }
    %>
</table>

<button class="add-product-button" onclick="location.href='${pageContext.request.contextPath}/products/add'">製品を追加する</button>

</body>
</html>
