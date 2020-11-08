package lk.sparkx.ncms.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.sparkx.ncms.models.Hospital;
import lk.sparkx.ncms.util.DBConnectionPool;

import java.sql.*;

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
            System.out.println(e);
        }
        return "Oops.. Something went wrong there..!"; // On failure, send a message from here.
    }

    //view hospital details
    public JsonArray viewHospital() {
        JsonArray hospitalArray = new JsonArray();

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM hospital");
            //statement.setString(1, district);
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String district = resultSet.getString("district");
                int locationX = resultSet.getInt("location_x");
                int locationY = resultSet.getInt("location_y");
                Date buildDate = resultSet.getDate("build_date");

                //PrintWriter printWriter = response.getWriter();

                JsonObject hospitalDetails = new JsonObject();
                hospitalDetails.addProperty("Id", id);
                hospitalDetails.addProperty("name", name);
                hospitalDetails.addProperty("district", district);
                hospitalDetails.addProperty("locationX", locationX);
                hospitalDetails.addProperty("locationY", locationY);
                hospitalDetails.addProperty("buildDate", String.valueOf(buildDate));
                hospitalArray.add(hospitalDetails);

                /*printWriter.println("Id: " + id);
                printWriter.println("Name: " + name);
                printWriter.println("District: " + district);
                printWriter.println("Location_X: " + locationX);
                printWriter.println("Location_Y: " + locationY);
                printWriter.println("Build Date: " + buildDate);
                System.out.println("doGet doctor success");*/
            }
            /*response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(hospitalArray.toString());
            System.out.println(hospitalArray.toString());*/
            connection.close();

            connection.close();

        } catch (Exception exception) {

        }
        return hospitalArray;
    }
}
