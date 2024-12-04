package Cart.Addcart;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Cart.product.Product;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import Cart.product.Product;
import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import java.io.PrintWriter;

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        if ("add".equals(action)) {
            String name = request.getParameter("name");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Kiểm tra xem sản phẩm đã có trong giỏ chưa
            Product existingProduct = findProductInCart(cart, name);
            if (existingProduct != null) {
                existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
            } else {
                cart.add(new Product(name, price, quantity));
            }

        } else if ("update".equals(action)) {
            String name = request.getParameter("name");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            Product product = findProductInCart(cart, name);
            if (product != null) {
                product.setQuantity(quantity);
            }

        } else if ("remove".equals(action)) {
            String name = request.getParameter("name");
            cart.removeIf(product -> product.getName().equals(name));
        }

        // Trả về giỏ hàng dưới dạng JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(new Gson().toJson(cart));  // Sử dụng Gson để chuyển đối tượng thành JSON
        out.flush();
    }

    private Product findProductInCart(ArrayList<Product> cart, String name) {
        for (Product product : cart) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
}
