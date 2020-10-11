package lk.sparkx.ncms.controller;

import lk.sparkx.ncms.dao.DBConnectionPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "StatisticsServlet")
public class StatisticsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hospitalName = request.getParameter("name");
        String district = request.getParameter("district");

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        PreparedStatement statement4 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            ResultSet resultSet2;
            ResultSet resultSet3;
            ResultSet resultSet4;

            statement = connection.prepareStatement("SELECT COUNT(hospital_bed.id) AS hospitalLevel FROM hospital_bed INNER JOIN hospital ON hospital_bed.hospital_id = hospital.id WHERE hospital.name ='"+hospitalName+"'");
            //statement.setString(1, hospitalId);
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int patientCount = resultSet.getInt("hospitalLevel");
                System.out.println(patientCount);
                PrintWriter printWriter = response.getWriter();

                printWriter.println("Hospital name: " + hospitalName);
                printWriter.println("Hospital Level Statistics: " + patientCount);
                printWriter.println("\n");
            }

            statement2 = connection.prepareStatement("SELECT COUNT(hospital_bed.id) AS districtLevel FROM hospital_bed INNER JOIN hospital ON hospital_bed.hospital_id = hospital.id WHERE hospital.district ='"+district+"'");
            //statement.setString(1, hospitalId);
            System.out.println(statement2);
            resultSet2 = statement2.executeQuery();

            while (resultSet2.next()) {
                int disPatientCount = resultSet2.getInt("districtLevel");
                System.out.println(disPatientCount);
                PrintWriter printWriter = response.getWriter();

                printWriter.println("District: " + district);
                printWriter.println("District Level Statistics: " + disPatientCount);
                printWriter.println("\n");
            }

            statement3 = connection.prepareStatement("SELECT COUNT(id) AS countryLevel FROM hospital_bed");
            statement4 = connection.prepareStatement("SELECT COUNT(id) AS countryLevelQueue FROM patient_queue");
            System.out.println(statement3);
            resultSet3 = statement3.executeQuery();
            resultSet4 = statement4.executeQuery();

            while (resultSet3.next()) {
                int hospitalPatientCount = resultSet3.getInt("countryLevel");
                while (resultSet4.next()) {
                    int queuePatientCount = resultSet4.getInt("countryLevelQueue");
                    int countryPatientCount = hospitalPatientCount + queuePatientCount;
                    System.out.println(countryPatientCount);

                    PrintWriter printWriter = response.getWriter();

                    printWriter.println("Country Level Statistics: " + countryPatientCount);
                }
            }
            connection.close();

        } catch (Exception exception) {

        }
    }
}
