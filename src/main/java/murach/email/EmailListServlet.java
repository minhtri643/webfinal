package murach.email;

import Mail.MailUtilLocal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "join"; // Default action
        }

        String url = "/index.html";

        if (action.equals("sendOtp")) {
            String email = request.getParameter("email");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");

            // Generate OTP
            String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
            request.getSession().setAttribute("otp", otp);
            request.getSession().setAttribute("email", email);

            // Send OTP
            String subject = "Your OTP Code";
            String body = "Your OTP code is: " + otp;
            try {
                MailUtilLocal.sendMail(email, "laptrinhwebcodientu@gmail.com", subject, body, false);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": true, \"message\": \"OTP đã được gửi đến email của bạn.\"}");
            } catch (Exception e) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": false, \"message\": \"Không thể gửi OTP, vui lòng thử lại.\"}");
            }

        } else if (action.equals("verifyOtp")) {
            String inputOtp = request.getParameter("otp");
            String sessionOtp = (String) request.getSession().getAttribute("otp");

            if (inputOtp != null && inputOtp.equals(sessionOtp)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": true, \"message\": \"OTP hợp lệ.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": false, \"message\": \"OTP không chính xác.\"}");
            }

        } else if (action.equals("setPassword")) {
            String password = request.getParameter("password");
            String repassword = request.getParameter("repassword");

            if (password.equals(repassword)) {
                // Save password to database securely (hash it)
                // Add email to session after success
                String email = (String) request.getSession().getAttribute("email");
                request.getSession().setAttribute("loggedInEmail", email);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": true, \"message\": \"Đặt mật khẩu thành công.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": false, \"message\": \"Mật khẩu không khớp.\"}");
            }

        } else if (action.equals("completeOrder")) {
            // Lấy thông tin đơn hàng từ request (ví dụ: giỏ hàng, tên khách hàng, địa chỉ, v.v.)
            String email = request.getParameter("email"); // Email người nhận
            String orderDetails = request.getParameter("orderDetails"); // Thông tin đơn hàng (có thể là chi tiết giỏ hàng)

            // Cấu trúc tiêu đề và nội dung email
            String subject = "Thông tin đơn hàng hoàn tất";
            String body = "Cảm ơn bạn đã đặt hàng! Dưới đây là thông tin đơn hàng của bạn:\n\n" + orderDetails;

            try {
                // Gửi email thông tin đơn hàng
                MailUtilLocal.sendMail(email, "laptrinhwebcodientu@gmail.com", subject, body, false);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": true, \"message\": \"Đơn hàng đã được gửi qua email của bạn!\"}");
            } catch (Exception e) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": false, \"message\": \"Không thể gửi thông tin đơn hàng qua email!\"}");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String loggedInEmail = (String) request.getSession().getAttribute("loggedInEmail");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (loggedInEmail != null) {
            // Trả về email nếu người dùng đã đăng nhập thành công
            response.getWriter().write("{\"success\": true, \"email\": \"" + loggedInEmail + "\"}");
        } else {
            // Nếu không có email trong session, trả về thông báo lỗi
            response.getWriter().write("{\"success\": false, \"message\": \"Chưa đăng nhập\"}");
        }
    }
}
