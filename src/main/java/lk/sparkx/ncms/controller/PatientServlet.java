package lk.sparkx.ncms.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

import lk.sparkx.ncms.models.Patient;
import lk.sparkx.ncms.dao.PatientDao;

@WebServlet(name = "PatientServlet")
public class PatientServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String district = request.getParameter("district");
        int locationX = Integer.parseInt(request.getParameter("locationX"));
        int locationY = Integer.parseInt(request.getParameter("locationY"));
        String severityLevel = request.getParameter("severityLevel");
        String gender = request.getParameter("gender");
        String contact = request.getParameter("contact");
        String email = request.getParameter("email");
        int age = Integer.parseInt(request.getParameter("age"));

        java.util.Date dateAdmit = new java.util.Date();
        Date admitDate = new Date(dateAdmit.getTime());

        String admittedBy = request.getParameter("admittedBy");

        java.util.Date dateDischarged = new java.util.Date();
        Date dischargeDate = new Date(dateDischarged.getTime());

        /*String paramDischarge = request.getParameter("admitDate");
        Date dischargeDate = null;
        try {
            dischargeDate = (Date) date.parse(paramDischarge);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        String dischargedBy = request.getParameter("dischargedBy");

        
        Patient patient = new Patient();
        patient.setId(id);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setDistrict(district);
        patient.setLocationX(locationX);
        patient.setLocationY(locationY);
        patient.setSeverityLevel(severityLevel);
        patient.setGender(gender);
        patient.setContact(contact);
        patient.setEmail(email);
        patient.setAge(age);
        patient.setAdmitDate(admitDate);
        patient.setAdmittedBy(admittedBy);
        patient.setDischargeDate(dischargeDate);
        patient.setDischargedBy(dischargedBy);

        PatientDao patientDao = new PatientDao();
        String patientRegistered = patientDao.registerPatient(patient);

        if(patientRegistered.equals("SUCCESS"))   //On success, you can display a message to user on Home page
        {
            System.out.println("Success");
            //request.getRequestDispatcher("/PatientDetails.jsp").forward(request, response);
        }
        else   //On Failure, display a meaningful message to the User.
        {
            System.out.println("Failed");
            //request.setAttribute("errMessage", patientRegistered);
            //request.getRequestDispatcher("/PatientRegister.jsp").forward(request, response);
        }

        try {
            patientDao.registerPatient(patient);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Patient patient = new Patient();
        patient.getModel();
        System.out.println("doGet success");
        //response.getWriter().append("Served at: ").append(request.getContextPath());
        //RequestDispatcher dispatcher = request.getRequestDispatcher("../web/PatientRegister.jsp");
        //dispatcher.forward(request, response);
    }

}
