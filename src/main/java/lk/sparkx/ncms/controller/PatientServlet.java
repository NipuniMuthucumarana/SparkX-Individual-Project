package lk.sparkx.ncms.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.sparkx.ncms.util.DBConnectionPool;
import lk.sparkx.ncms.dao.PatientDao;
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
        String gender = request.getParameter("gender");
        String contact = request.getParameter("contact");
        String email = request.getParameter("email");
        int age = Integer.parseInt(request.getParameter("age"));

        Patient patient = new Patient();
        patient.setId(id);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setDistrict(district);
        patient.setLocationX(locationX);
        patient.setLocationY(locationY);
        patient.setGender(gender);
        patient.setContact(contact);
        patient.setEmail(email);
        patient.setAge(age);

        PatientDao patientDao = new PatientDao();
        String patientRegistered = patientDao.registerPatient(patient);

        if(patientRegistered.equals("SUCCESS")) { //On success
            System.out.println("Success");
        } else {  //On Failure
            System.out.println("Failed");
        }

        try {
            patientDao.registerPatient(patient);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //String id = request.getParameter("id");
        //JsonObject patientDetails = new JsonObject(); //create a JSON Object stats.
        JsonArray patientArray = new JsonArray();

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            ResultSet resultSet2;

            statement = connection.prepareStatement("SELECT * FROM patient");
            //statement.setString(1, id);
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
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

                JsonObject patientDetails = new JsonObject();
                patientDetails.addProperty("Id", id);
                patientDetails.addProperty("Firstname", firstName);
                patientDetails.addProperty("Lastname", lastName);
                patientDetails.addProperty("District", district);
                patientDetails.addProperty("Location_X", locationX);
                patientDetails.addProperty("Location_Y", locationY);
                patientDetails.addProperty("SeverityLevel", severityLevel);
                patientDetails.addProperty("Gender", gender);
                patientDetails.addProperty("Contact", contact);
                patientDetails.addProperty("Email", email);
                patientDetails.addProperty("Age", age);
                patientDetails.addProperty("AdmitDate", String.valueOf(admitDate));
                patientDetails.addProperty("AdmittedBy", admittedBy);
                patientDetails.addProperty("DischargeDate", String.valueOf(dischargeDate));
                patientDetails.addProperty("DischargedBy", dischargedBy);
                patientArray.add(patientDetails);

                /*printWriter.println("Id: " + id);
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
                System.out.println("doGet patient success");*/
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(patientArray.toString());
            System.out.println(patientArray.toString());
            connection.close();

        } catch (Exception exception) {

        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        JsonArray sendToPatientArray = new JsonArray();
        JsonArray sendToQueueArray = new JsonArray();
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            ResultSet resultSet2;

            statement = connection.prepareStatement("SELECT patient.serial_no, hospital_bed.id AS bed_id, hospital.name, hospital.district FROM patient INNER  JOIN hospital_bed ON patient.id=hospital_bed.patient_id INNER JOIN hospital ON hospital_bed.hospital_id=hospital.id where patient.id ='"+id+"'");
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String serialNo = resultSet.getString("serial_no");
                String bedId = resultSet.getString("bed_id");
                String name = resultSet.getString("name");
                String district = resultSet.getString("district");

                PrintWriter printWriter = response.getWriter();

                JsonObject sendToPatient = new JsonObject();
                sendToPatient.addProperty("Id", id);
                sendToPatient.addProperty("serialNo", serialNo);
                sendToPatient.addProperty("bedId", bedId);
                sendToPatient.addProperty("Hospital Name", name);
                sendToPatient.addProperty("District", district);
                sendToPatientArray.add(sendToPatient);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            //response.getWriter().write(sendToPatientArray.toString());
            //System.out.println(sendToPatientArray.toString());

            if(sendToPatientArray.size()!=0) {
                response.getWriter().write(sendToPatientArray.toString());
                System.out.println(sendToPatientArray.toString());
            } else {
                statement2 = connection.prepareStatement("SELECT patient_queue.id as queueId FROM patient_queue INNER  JOIN patient ON patient.id=patient_queue.patient_id where patient.id ='"+id+"'");
                System.out.println(statement2);
                resultSet2 = statement2.executeQuery();
                while (resultSet2.next()) {
                    int queueId = resultSet2.getInt("queueId");
                    PrintWriter printWriter = response.getWriter();

                    JsonObject sendToPatientQueue = new JsonObject();
                    sendToPatientQueue.addProperty("Id", id);
                    sendToPatientQueue.addProperty("queueId", queueId);
                    sendToQueueArray.add(sendToPatientQueue);
                }
                response.getWriter().write(sendToQueueArray.toString());
                System.out.println(sendToQueueArray.toString());
            }
            connection.close();

        } catch (Exception exception) {

        }

    }
}
