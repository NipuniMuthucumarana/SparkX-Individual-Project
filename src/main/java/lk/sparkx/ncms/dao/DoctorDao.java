package lk.sparkx.ncms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import lk.sparkx.ncms.models.Doctor;
import lk.sparkx.ncms.models.Patient;

public class DoctorDao {
    public String registerDoctor(Doctor doctor) {
        String INSERT_USERS_SQL = "INSERT INTO doctor (id, name, hospital_id, is_director) VALUES (?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        //Class.forName("com.mysql.jdbc.Driver");

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
