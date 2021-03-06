package lk.sparkx.ncms.controller;

import com.google.gson.JsonArray;
import lk.sparkx.ncms.dao.MohDao;
import lk.sparkx.ncms.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MohServlet")
public class MohServlet extends HttpServlet {
    //Moh login
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        PrintWriter printWriter = response.getWriter();

        if(MohDao.validate(username, password)) {
            response.getWriter().write("True");
        }else{
            printWriter.print("Sorry username or password error");
        }
        printWriter.close();
    }

    //queue controlling
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MohDao mohDao = new MohDao();
        JsonArray queueArray = mohDao.viewQueue();
        response.getWriter().write(queueArray.toString());
    }
}
