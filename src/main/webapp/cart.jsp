<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Giỏ hàng</title>
</head>
<body>
    <h1>Giỏ hàng của bạn</h1>

    <c:if test="${empty cart}">
        <p>Giỏ hàng của bạn hiện tại trống!</p>
    </c:if>

    <c:if test="${not empty cart}">
        <table>
            <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Giá</th>
                    <th>Số lượng</th>
                    <th>Tổng tiền</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="product" items="${cart}">
                    <tr>
                        <td>${product.name}</td>
                        <td>${product.price}₫</td>
                        <td>
                            <form action="cart" method="post">
                                <input type="number" name="quantity" value="${product.quantity}" min="1"/>
                                <input type="hidden" name="action" value="update"/>
                                <input type="hidden" name="name" value="${product.name}"/>
                                <button type="submit">Cập nhật</button>
                            </form>
                        </td>
                        <td>${product.totalPrice}₫</td>
                        <td>
                            <form action="cart" method="post">
                                <input type="hidden" name="action" value="remove"/>
                                <input type="hidden" name="name" value="${product.name}"/>
                                <button type="submit">Xóa</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <h3>Tổng tiền: ${cart.stream().mapToDouble(product -> product.totalPrice()).sum()}₫</h3>
    </c:if>

    <form action="cart" method="post">
        <input type="text" name="name" placeholder="Tên sản phẩm" required />
        <input type="number" name="price" placeholder="Giá" required />
        <input type="number" name="quantity" placeholder="Số lượng" required min="1" />
        <input type="hidden" name="action" value="add"/>
        <button type="submit">Thêm vào giỏ hàng</button>
    </form>
</body>
</html>
