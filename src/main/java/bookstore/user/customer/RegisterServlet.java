package bookstore.user.customer;

import java.io.*;

import bookstore.subsystem.mysqlsubsystem.MySQLUserDAO;
import bookstore.subsystem.iface.IUserDAO;
import bookstore.subsystem.mysqlsubsystem.MySQLConnector;
import bookstore.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("fullName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String agreeTerms = request.getParameter("agreeTerms");

            HttpSession httpSession = request.getSession();

            IUserDAO userDAO = new MySQLUserDAO((new MySQLConnector().getConnection()));

            // Kiểm tra xem email đã tồn tại hay chưa
            if (userDAO.isEmailExist(email)) {
                httpSession.setAttribute("failedMsg", "Email already exists!");
                response.sendRedirect("register.jsp");
                return; // Kết thúc phương thức
            }

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            boolean res = userDAO.userRegister(user);
            if (res) {
                httpSession.setAttribute("successMsg", "User Registration Success");
                response.sendRedirect("register.jsp");
            } else {
                httpSession.setAttribute("failedMsg", "Something wrong on server");
                response.sendRedirect("register.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}