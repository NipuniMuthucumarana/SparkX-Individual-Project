package lk.sparkx.ncms.controller;

import lk.sparkx.ncms.dao.DoctorDao;
import lk.sparkx.ncms.models.Doctor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String doctorId = request.getParameter("doctor_id");
        String patientId = request.getParameter("patient_id");
        String severityLevel = request.getParameter("severity_level");

        DoctorDao doctorDao = new DoctorDao();
        doctorDao.admitPatients(patientId,doctorId, severityLevel);
    }

}
