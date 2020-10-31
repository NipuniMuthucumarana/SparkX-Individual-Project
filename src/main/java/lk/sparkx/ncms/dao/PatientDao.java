package lk.sparkx.ncms.dao;

import lk.sparkx.ncms.models.Bed;
import lk.sparkx.ncms.models.Hospital;
import lk.sparkx.ncms.models.Patient;
import lk.sparkx.ncms.util.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
