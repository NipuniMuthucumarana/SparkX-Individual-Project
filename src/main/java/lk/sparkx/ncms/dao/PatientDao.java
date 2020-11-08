package lk.sparkx.ncms.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.sparkx.ncms.models.Bed;
import lk.sparkx.ncms.models.Hospital;
import lk.sparkx.ncms.models.Patient;
import lk.sparkx.ncms.util.DBConnectionPool;

import java.sql.*;

public class PatientDao {
    public String registerPatient(Patient patient) {
        String INSERT_USERS_SQL = "INSERT INTO patient (id, first_name, last_name, district, location_x, location_y, gender, contact, email, age) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement statement2 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet2;

             // Step 2:Create a statement using connection object
            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, patient.getId());
            preparedStatement.setString(2, patient.getFirstName());
            preparedStatement.setString(3, patient.getLastName());
            preparedStatement.setString(4, patient.getDistrict());
            preparedStatement.setInt(5, patient.getLocationX());
            preparedStatement.setInt(6, patient.getLocationY());
            preparedStatement.setString(7, patient.getGender());
            preparedStatement.setString(8, patient.getContact());
            preparedStatement.setString(9, patient.getEmail());
            preparedStatement.setInt(10, patient.getAge());

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            result = preparedStatement.executeUpdate();

            Hospital hospital = new Hospital();
            String nearestHospital = hospital.assignHospital(patient.getLocationX(), patient.getLocationY());
            System.out.println("Nearest hospital: " + nearestHospital);

            Bed bed = new Bed();
            int bedId = bed.allocateBed(nearestHospital, patient.getId());
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
                        bedId = bed.allocateBed(hosId, patient.getId());
                    }
                }
                /* If there is no available beds, add to queue */
                bedNo = bedId;
                if(bedNo == 0){
                    QueueDao queue = new QueueDao();
                    queueLength = queue.addToQueue(patient.getId());
                }
            }
            if (result!=0)  //Just to ensure data has been inserted into the database
                return "SUCCESS";

        } catch (SQLException e) {
            // process sql exception
            System.out.println(e);
        }
        return "Oops.. Something went wrong there..!"; // On failure, send a message from here.
    }

    //view patient details
    public JsonArray viewPatient() {
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

            }
            connection.close();

        } catch (Exception exception) {

        }
        return patientArray;
    }

    //view patient details
    public JsonArray alertPatient(String id) {
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

                JsonObject sendToPatient = new JsonObject();
                sendToPatient.addProperty("Id", id);
                sendToPatient.addProperty("serialNo", serialNo);
                sendToPatient.addProperty("bedId", bedId);
                sendToPatient.addProperty("Hospital Name", name);
                sendToPatient.addProperty("District", district);
                sendToPatientArray.add(sendToPatient);
            }

            if(sendToPatientArray.size()!=0) {
                System.out.println(sendToPatientArray.toString());
            } else {
                statement2 = connection.prepareStatement("SELECT patient_queue.id as queueId FROM patient_queue INNER  JOIN patient ON patient.id=patient_queue.patient_id where patient.id ='"+id+"'");
                System.out.println(statement2);
                resultSet2 = statement2.executeQuery();
                while (resultSet2.next()) {
                    int queueId = resultSet2.getInt("queueId");

                    JsonObject sendToPatientQueue = new JsonObject();
                    sendToPatientQueue.addProperty("Id", id);
                    sendToPatientQueue.addProperty("queueId", queueId);
                    sendToPatientArray.add(sendToPatientQueue);
                }
            }
            connection.close();

        } catch (Exception exception) {

        }
        return sendToPatientArray;
    }
}
