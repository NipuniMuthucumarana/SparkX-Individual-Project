package lk.sparkx.ncms.controller;

import lk.sparkx.ncms.dao.DBConnectionPool;
import org.json.JSONArray;
import org.json.simple.JSONObject;

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
        String action = request.getParameter("action");
        JSONObject stats=new JSONObject(); //create a JSON Object stats.
        JSONArray jArray = new JSONArray(); //create a JSON Array jArray.

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        PreparedStatement statement4 = null;
        PreparedStatement statement5 = null;
        PreparedStatement statement6 = null;
        PreparedStatement statement7 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            ResultSet resultSet2;
            ResultSet resultSet3;
            ResultSet resultSet4;
            ResultSet resultSet5;
            ResultSet resultSet6;
            ResultSet resultSet7;

            statement = connection.prepareStatement("SELECT name FROM hospital");
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String  hospital = resultSet.getString("name");
                statement6 = connection.prepareStatement("SELECT COUNT(hospital_bed.id) AS hospitalLevel FROM hospital_bed INNER JOIN hospital ON hospital_bed.hospital_id = hospital.id WHERE hospital.name ='"+hospital+"'");
                System.out.println(statement6);
                resultSet6 = statement6.executeQuery();
                while (resultSet6.next()) {
                    int patientCount = resultSet6.getInt("hospitalLevel");
                    System.out.println(patientCount);
                    PrintWriter printWriter = response.getWriter();
                    printWriter.println("Hospital name: " + hospital);
                    printWriter.println("Hospital Level Statistics: " + patientCount);
                    request.setAttribute("hosPatientCount", patientCount);
                    stats.put(hospital, patientCount);
                    printWriter.println("\n");
                }
            }

            statement7 = connection.prepareStatement("SELECT district FROM hospital");
            System.out.println(statement7);
            resultSet7 = statement7.executeQuery();
            while (resultSet7.next()) {
                String  district = resultSet7.getString("district");
                statement2 = connection.prepareStatement("SELECT COUNT(hospital_bed.id) AS districtLevel FROM hospital_bed INNER JOIN hospital ON hospital_bed.hospital_id = hospital.id WHERE hospital.district ='" + district + "'");
                statement5 = connection.prepareStatement("SELECT COUNT(patient_queue.id) AS queueDistrictLevel FROM patient_queue INNER JOIN patient ON patient.id = patient_queue.patient_id WHERE patient.district ='" + district + "'");
                System.out.println(statement2);
                System.out.println(statement5);
                resultSet2 = statement2.executeQuery();
                resultSet5 = statement5.executeQuery();

                while (resultSet2.next()) {
                    int disPatientCount = resultSet2.getInt("districtLevel");
                    while (resultSet5.next()) {
                        int queueDisPatient = resultSet5.getInt("queueDistrictLevel");
                        int districtPatientCount = disPatientCount + queueDisPatient;
                        System.out.println(districtPatientCount);
                        PrintWriter printWriter = response.getWriter();

                        printWriter.println("District: " + district);
                        printWriter.println("District Level Statistics: " + districtPatientCount);
                        stats.put(district, districtPatientCount);
                        request.setAttribute("disPatientCount", districtPatientCount);
                        printWriter.println("\n");
                    }
                }
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
                    stats.put("country", countryPatientCount);
                    request.setAttribute("couPatientCount", "50");
                }
            }

            jArray.put(stats);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jArray.toString());
            //response.sendRedirect("file:///F:/SPARK/Project/Front-end/NCMS/index.html?name=&district=#section-counter");
            System.out.println(jArray.toString());
            connection.close();
            stats.clear();

        } catch (Exception exception) {

        }
    }
}
