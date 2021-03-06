package lk.sparkx.ncms.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.sparkx.ncms.models.Doctor;
import lk.sparkx.ncms.util.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorDao {
    //doctor registration
    public String registerDoctor(Doctor doctor) {
        String INSERT_USERS_SQL = "INSERT INTO doctor (id, name, hospital_id, is_director) VALUES (?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();

            // Step 2:Create a statement using connection object
            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, doctor.getId());
            preparedStatement.setString(2, doctor.getName());
            preparedStatement.setString(3, doctor.getHospitalId());
            preparedStatement.setBoolean(4, doctor.isDirector());

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            result = preparedStatement.executeUpdate();

            if (result != 0)  //Just to ensure data has been inserted into the database
                return "SUCCESS";

        } catch (SQLException e) {
            // process sql exception
            printSQLException(e);
        }
        return "Oops.. Something went wrong there..!"; // On failure, send a message from here.
    }

    //view doctor details
    public JsonArray viewDoctor() {
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

                //PrintWriter printWriter = response.getWriter();

                JsonObject doctorDetails = new JsonObject();
                doctorDetails.addProperty("Id", id);
                doctorDetails.addProperty("name", name);
                doctorDetails.addProperty("hospitalId", hospitalId);
                doctorDetails.addProperty("isDirector", isDirector);
                doctorArray.add(doctorDetails);

            }
            connection.close();

        } catch (Exception exception) {

        }
        return doctorArray;
    }

    //admit patients
    public void admitPatients(String patientId, String DoctorId, String severityLevel) {
        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;

            statement = connection.prepareStatement("UPDATE patient SET severity_level='"+severityLevel+"', admit_date=?, admitted_by='"+DoctorId+"' WHERE id='"+patientId+"'");
            statement.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
            result = statement.executeUpdate();
            System.out.println(statement);

            connection.close();
        } catch (SQLException e) {
            // process sql exception
            printSQLException(e);
        }
    }

    public void dischargePatients(String patientId, String hospitalId) {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;

            statement2 = connection.prepareStatement("SELECT * FROM doctor WHERE hospital_id='" +hospitalId + "' AND is_director=1");
            resultSet = statement2.executeQuery();
            System.out.println(statement2);
            while (resultSet.next()) {
                String director = resultSet.getString("id");
                System.out.println(director);
                statement = connection.prepareStatement("UPDATE patient SET discharge_date=? , discharged_by= '" + director + "' WHERE id = '" + patientId + "'");
                statement.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
                System.out.println(statement);
                result = statement.executeUpdate();

                if(result!=0) {
                    System.out.println("success");
                } else
                    System.out.println("Failed");
            }
            connection.close();
        } catch (SQLException e) {
            // process sql exception
            printSQLException(e);
        }
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
