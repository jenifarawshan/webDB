<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="DTO.ProductDTO" %>

<%
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Update Product</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        h1 {
            font-size: 24px;
        }
        form {
            max-width: 500px;
            margin-top: 20px;
        }
        .input-container {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }
        .input-container label {
            margin-right: 10px;
            
        }
        input[type='text'], input[type='number'] {
            flex-grow: 1;
            padding: 10px;
            margin: 10px;
        }
        input[type='submit'] {
            padding: 10px 20px;
            margin: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
        }
        .error-message {
            color: red;
            font-weight: bold;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <h1>商品変更・削除</h1>
    
    
    <form action="${pageContext.request.contextPath}/products/update/${product.productCode}" method="POST">
        <input type="hidden" name="action" value="update">
        <div class="form-group">
            <label for="productCode">商品一下</label>
            <span id="productCode">${product.productCode}</span>
        </div><br>
        
        
        <div class="form-group">
            <label for="productName">商品名</label>
            <input type="text" name="productName" value="${product.productName}" maxlength="50" required>
        </div>
        <div class="form-group">
            <label for="unitPrice">単価</label>
            <input type="number" step="1" name="unitPrice" value="${product.productPrice}" min="1" max="10000" required>
        </div>
        
        <input type="submit" value="変更">
    </form>

    <form action="${pageContext.request.contextPath}/products/delete/${product.productCode}" method="POST">
        <input type="hidden" name="action" value="delete">
        <input type="submit" value="削除" class="delete-button">
    </form>

    <% if (errorMessage != null) { %>
        <p class="error-message"><%= errorMessage %></p>
    <% } %>
</body>
</html>
