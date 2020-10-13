package lk.sparkx.ncms.controller;

import lk.sparkx.ncms.dao.UserDao;
import lk.sparkx.ncms.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserServlet")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        Boolean moh = Boolean.valueOf(request.getParameter("moh"));
        Boolean hospital = Boolean.valueOf(request.getParameter("hospital"));

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setMoh(moh);
        user.setHospital(hospital);

        UserDao userDao = new UserDao();
        String userRegistered = userDao.viewStatistics(user);

        if(userRegistered.equals("SUCCESS"))   //On success, you can display a message to user on Home page
        {
            System.out.println("Success");
        }
        else   //On Failure, display a meaningful message to the User.
        {
            System.out.println("Failed");
        }

        try {
            userDao.viewStatistics(user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        user.getModel();
        System.out.println("doGet user success");
    }
}
