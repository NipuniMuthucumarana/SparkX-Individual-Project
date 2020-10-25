package lk.sparkx.ncms.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.sparkx.ncms.dao.DBConnectionPool;
import lk.sparkx.ncms.dao.DoctorDao;
import lk.sparkx.ncms.models.Doctor;

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

@WebServlet(name = "DoctorServlet")
public class DoctorServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String hospitalId = request.getParameter("hospitalId");
        Boolean isDirector = Boolean.valueOf(request.getParameter("isDirector"));

        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setName(name);
        doctor.setHospitalId(hospitalId);
        doctor.setDirector(isDirector);

        DoctorDao doctorDao = new DoctorDao();
        String doctorRegistered = doctorDao.registerDoctor(doctor);

        if(doctorRegistered.equals("SUCCESS")) { //On success
            System.out.println("Success");
        } else {  //On Failure
            System.out.println("Failed");
        }

        try {
            doctorDao.registerDoctor(doctor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonArray doctorArray = new JsonArray();
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            ResultSet resultSet2;

            statement = connection.prepareStatement("SELECT * FROM doctor");
            //statement.setString(1, id);
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String hospitalId = resultSet.getString("hospital_id");
                boolean isDirector = resultSet.getBoolean("is_director");

                PrintWriter printWriter = response.getWriter();

                JsonObject doctorDetails = new JsonObject();
                doctorDetails.addProperty("Id", id);
                doctorDetails.addProperty("name", name);
                doctorDetails.addProperty("hospitalId", hospitalId);
                doctorDetails.addProperty("isDirector", isDirector);
                doctorArray.add(doctorDetails);

            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(doctorArray.toString());
            System.out.println(doctorArray.toString());
            connection.close();

        } catch (Exception exception) {

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String doctorId = request.getParameter("doctor_id");
        String patientId = request.getParameter("patient_id");
        String severityLevel = request.getParameter("severity_level");

        DoctorDao doctorDao = new DoctorDao();
        doctorDao.admitPatients(patientId,doctorId, severityLevel);
    }

}
