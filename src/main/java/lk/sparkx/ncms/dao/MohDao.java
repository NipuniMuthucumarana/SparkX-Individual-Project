package lk.sparkx.ncms.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.sparkx.ncms.util.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MohDao {
        //login
        public static boolean validate(String username, String password) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            Boolean status = false;

            try {
                connection = DBConnectionPool.getInstance().getConnection();
                ResultSet resultSet;

                // Step 2:Create a statement using connection object
                preparedStatement = connection.prepareStatement("SELECT * from user WHERE username=? AND password=?");
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                System.out.println(preparedStatement);
                // Step 3: Execute the query or update query
                resultSet = preparedStatement.executeQuery();
                status = resultSet.next();
                System.out.println(status);

            } catch (SQLException e) {
                System.out.println(e);
            }
            return status;
        }

    //view queue details
    public JsonArray viewQueue() {
        JsonArray queueArray = new JsonArray();
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result = 0;
        int queueLength = 4;
        int queueId = 0;
        int[] queue = new int[4];

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            statement = connection.prepareStatement("SELECT patient_queue.id, patient_id, district FROM patient_queue INNER JOIN patient on patient_queue.patient_id = patient.id");
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String patientId = resultSet.getString("patient_id");
                String district = resultSet.getString("district");

                JsonObject queueObj = new JsonObject();
                queueObj.addProperty("id", id);
                queueObj.addProperty("patientId", patientId);
                queueObj.addProperty("district", district);
                queueArray.add(queueObj);

            }
            connection.close();

        } catch (Exception exception) {

        }
        return queueArray;
    }

}
