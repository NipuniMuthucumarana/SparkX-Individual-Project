package lk.sparkx.ncms.dao;

import lk.sparkx.ncms.models.Hospital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HospitalDao {
    public String registerHospital(Hospital hospital) {
        String INSERT_USERS_SQL = "INSERT INTO hospital (id, name, district, location_x, location_y, build_date) VALUES (?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();

            // Step 2:Create a statement using connection object
            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, hospital.getId());
            preparedStatement.setString(2, hospital.getName());
            preparedStatement.setString(3, hospital.getDistrict());
            preparedStatement.setInt(4, hospital.getLocationX());
            preparedStatement.setInt(5, hospital.getLocationY());
            preparedStatement.setDate(6, hospital.getBuildDate());

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

    public  void buildHospital(String patientId){

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
