/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Cart.product;

import java.util.Objects;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.google.gson.Gson;

 public class Product {
    private String name;
    private double price;
    private int quantity;

    // Constructor, getters và setters
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Tính tổng giá của sản phẩm (giá * số lượng)
    public double getTotalPrice() {
        return price * quantity;
    }
}
