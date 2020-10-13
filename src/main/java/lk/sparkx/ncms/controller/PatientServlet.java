package lk.sparkx.ncms.controller;

import lk.sparkx.ncms.dao.DBConnectionPool;
import lk.sparkx.ncms.dao.PatientDao;
import lk.sparkx.ncms.dao.QueueDao;
import lk.sparkx.ncms.models.Bed;
import lk.sparkx.ncms.models.Hospital;
import lk.sparkx.ncms.models.Patient;

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

@WebServlet(name = "PatientServlet")
public class PatientServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String district = request.getParameter("district");
        int locationX = Integer.parseInt(request.getParameter("locationX"));
        int locationY = Integer.parseInt(request.getParameter("locationY"));
        //String severityLevel = request.getParameter("severityLevel");
        String gender = request.getParameter("gender");
        String contact = request.getParameter("contact");
        String email = request.getParameter("email");
        int age = Integer.parseInt(request.getParameter("age"));

        /*java.util.Date dateAdmit = new java.util.Date();
        Date admitDate = new Date(dateAdmit.getTime());

        String admittedBy = request.getParameter("admittedBy");

        java.util.Date dateDischarged = new java.util.Date();
        Date dischargeDate = new Date(dateDischarged.getTime());

        String dischargedBy = request.getParameter("dischargedBy");*/

        
        Patient patient = new Patient();
        patient.setId(id);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setDistrict(district);
        patient.setLocationX(locationX);
        patient.setLocationY(locationY);
        //patient.setSeverityLevel(severityLevel);
        patient.setGender(gender);
        patient.setContact(contact);
        patient.setEmail(email);
        patient.setAge(age);
        /*patient.setAdmitDate(admitDate);
        patient.setAdmittedBy(admittedBy);
        patient.setDischargeDate(dischargeDate);
        patient.setDischargedBy(dischargedBy);*/

        PatientDao patientDao = new PatientDao();
        String patientRegistered = patientDao.registerPatient(patient);

        if(patientRegistered.equals("SUCCESS")) { //On success, you can display a message to user on Home page

            System.out.println("Success");
        } else {  //On Failure, display a meaningful message to the User.
            System.out.println("Failed");
        }

        try {
            patientDao.registerPatient(patient);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            ResultSet resultSet2;

            statement = connection.prepareStatement("SELECT * FROM patient WHERE id=?");
            statement.setString(1, id);
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                //String id = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String district = resultSet.getString("district");
                int locationX = resultSet.getInt("location_x");
                int locationY = resultSet.getInt("location_y");
                String severityLevel = resultSet.getString("severity_level");
                String gender = resultSet.getString("gender");
                String contact = resultSet.getString("contact");
                String email = resultSet.getString("email");
                int age = resultSet.getInt("age");
                Date admitDate = resultSet.getDate("admit_date");
                String admittedBy = resultSet.getString("admitted_by");
                Date dischargeDate = resultSet.getDate("discharge_date");
                String dischargedBy = resultSet.getString("discharged_by");

                PrintWriter printWriter = response.getWriter();

                printWriter.println("Id: " + id);
                printWriter.println("First name: " + firstName);
                printWriter.println("Last name: " + lastName);
                printWriter.println("District: " + district);
                printWriter.println("Location_X: " + locationX);
                printWriter.println("Location_Y: " + locationY);
                printWriter.println("Severity Level: " + severityLevel);
                printWriter.println("Gender: " + gender);
                printWriter.println("Contact: " + contact);
                printWriter.println("Email: " + email);
                printWriter.println("Age: " + age);
                printWriter.println("Admit Date: " + admitDate);
                printWriter.println("Admitted By: " + admittedBy);
                printWriter.println("Discharge Date: " + dischargeDate);
                printWriter.println("Discharged By: " + dischargedBy);
                System.out.println("doGet patient success");

                Hospital hospital = new Hospital();
                String nearestHospital = hospital.assignHospital(locationX, locationY);
                System.out.println("Nearest hospital: " + nearestHospital);

                Bed bed = new Bed();
                int bedId = bed.allocateBed(nearestHospital, id);
                System.out.println("Bed ID: " + bedId);
                int bedNo = 0;

                if(bedId == 0){
                    statement2 = connection.prepareStatement("SELECT distinct id FROM hospital where id !='" + nearestHospital + "'");
                    System.out.println(statement2);
                    resultSet2 = statement2.executeQuery();
                    String hosId ="";
                    int queueLength;

                    /* Allocate a bed */
                    while(resultSet2.next()) {
                        if(bedId==0) {
                            hosId = resultSet2.getString("id");
                            System.out.println(hosId);
                            bedId = bed.allocateBed(hosId, id);
                        }
                    }
                    /* If there is no available beds, add to queue */
                    bedNo = bedId;
                    if(bedNo == 0){
                        QueueDao queue = new QueueDao();
                        queueLength = queue.addToQueue(id);
                    }

                }
            }
            connection.close();

        } catch (Exception exception) {

        }
    }

}
