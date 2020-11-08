package lk.sparkx.ncms.controller;

import com.google.gson.JsonArray;
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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

@WebServlet(name = "HospitalServlet")
public class HospitalServlet extends HttpServlet {
    //Register a hospital
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

    //view hospital details
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HospitalDao hospitalDao = new HospitalDao();
        JsonArray hospitalArray = hospitalDao.viewHospital();
        response.getWriter().write(hospitalArray.toString());
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
