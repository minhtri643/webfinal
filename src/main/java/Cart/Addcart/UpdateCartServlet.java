/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Cart.Addcart;

import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import Cart.product.Product;
import jakarta.servlet.http.Cookie;

@WebServlet("/updateCart")
public class UpdateCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy giỏ hàng từ session
        HttpSession session = request.getSession();
        ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");
        
         // Thêm cookie để lưu trạng thái giỏ hàng
        Cookie cartCookie = new Cookie("cart_status", "updated");
        cartCookie.setMaxAge(60 * 60); // Cookie tồn tại trong 1 giờ
        response.addCookie(cartCookie);
        
        if (cart != null) {
            // Kiểm tra nếu yêu cầu là cập nhật số lượng
            for (Product product : cart) {
                // Cập nhật số lượng sản phẩm
                String newQuantityStr = request.getParameter("quantity_" + product.getName());
                if (newQuantityStr != null) {
                    try {
                        int newQuantity = Integer.parseInt(newQuantityStr);
                        if (newQuantity > 0) {
                            product.setQuantity(newQuantity);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Kiểm tra nếu yêu cầu là xóa sản phẩm
            String productToRemove = request.getParameter("remove");
            if (productToRemove != null) {
                // Duyệt qua giỏ hàng và xóa sản phẩm theo tên
                cart.removeIf(product -> product.getName().equals(productToRemove));
            }
        }

        // Cập nhật lại giỏ hàng trong session
        session.setAttribute("cart", cart);
        // Chuyển hướng về lại trang giỏ hàng
        response.sendRedirect("cart.jsp");
    }
}