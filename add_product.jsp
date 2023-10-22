<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Add Product</title>
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
        }
        input[type='submit'] {
            padding: 10px 20px;
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
    <h1>商品登録</h1>
    <form action="${pageContext.request.contextPath}/products/add" method="post">
        <div class="input-container">
            <label for="productName">商品名</label>
            <input type="text" id="productName" name="productName" maxlength="50" required>
        </div>
        <div class="input-container">
            <label for="unitPrice">単価</label>
            <input type="number" id="unitPrice" name="unitPrice" step="1" min="1" max = "10000" required>
        </div>
        <input type="submit" value="登録">
        <% if (errorMessage != null) { %>
            <p class="error-message"><%= errorMessage %></p>
        <% } %>
    </form>
</body>
</html>
