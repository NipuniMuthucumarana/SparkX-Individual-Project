package lk.sparkx.ncms.controller;

import com.google.gson.JsonArray;
import lk.sparkx.ncms.dao.StatisticsDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "StatisticsServlet")
public class StatisticsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    //view statistics
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        StatisticsDao statisticsDao = new StatisticsDao();
        JsonArray jArray = statisticsDao.viewStatistics(method);
        response.getWriter().write(jArray.toString());
    }
}
