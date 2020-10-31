package lk.sparkx.ncms.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.sparkx.ncms.util.DBConnectionPool;
import lk.sparkx.ncms.dao.DoctorDao;
import lk.sparkx.ncms.dao.HospitalDao;
import lk.sparkx.ncms.models.Bed;
import lk.sparkx.ncms.models.Hospital;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "HospitalServlet")
public class HospitalServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String district = request.getParameter("district");
        int locationX = Integer.parseInt(request.getParameter("locationX"));
        int locationY = Integer.parseInt(request.getParameter("locationY"));
        java.util.Date dateBuild = new java.util.Date();
        Date buildDate = new Date(dateBuild.getTime());

        Hospital hospital = new Hospital();
        hospital.setId(id);
        hospital.setName(name);
        hospital.setDistrict(district);
        hospital.setLocationX(locationX);
        hospital.setLocationY(locationY);
        hospital.setBuildDate(buildDate);

        HospitalDao hospitalDao = new HospitalDao();
        String hospitalRegistered = hospitalDao.registerHospital(hospital);

        if(hospitalRegistered.equals("SUCCESS")) {  //On success, can display a message to user
            System.out.println("Success");
        } else {  //On Failure, display a message to the User.
            System.out.println("Failed");
        }

        try {
            hospitalDao.registerHospital(hospital);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonArray hospitalArray = new JsonArray();

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM hospital");
            //statement.setString(1, district);
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String district = resultSet.getString("district");
                int locationX = resultSet.getInt("location_x");
                int locationY = resultSet.getInt("location_y");
                Date buildDate = resultSet.getDate("build_date");

                PrintWriter printWriter = response.getWriter();

                JsonObject hospitalDetails = new JsonObject();
                hospitalDetails.addProperty("Id", id);
                hospitalDetails.addProperty("name", name);
                hospitalDetails.addProperty("district", district);
                hospitalDetails.addProperty("locationX", locationX);
                hospitalDetails.addProperty("locationY", locationY);
                hospitalDetails.addProperty("buildDate", String.valueOf(buildDate));
                hospitalArray.add(hospitalDetails);

                /*printWriter.println("Id: " + id);
                printWriter.println("Name: " + name);
                printWriter.println("District: " + district);
                printWriter.println("Location_X: " + locationX);
                printWriter.println("Location_Y: " + locationY);
                printWriter.println("Build Date: " + buildDate);
                System.out.println("doGet doctor success");*/
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(hospitalArray.toString());
            System.out.println(hospitalArray.toString());
            connection.close();

            connection.close();

        } catch (Exception exception) {

        }
    }

    /* Discharge patient by director and make the bed available for other patients */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        PreparedStatement statement = null;

        String patientId = request.getParameter("id");
        String hospitalId = request.getParameter("hospital_id");

        DoctorDao doctor = new DoctorDao();
        doctor.dischargePatients(patientId, hospitalId);

        Bed bed = new Bed();
        bed.makeAvailable(patientId, hospitalId);
    }



}
