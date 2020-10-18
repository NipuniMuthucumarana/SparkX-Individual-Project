package lk.sparkx.ncms.models;

import com.google.gson.JsonObject;
import lk.sparkx.ncms.dao.DBConnectionPool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Comparator.comparingDouble;


public class Hospital {
    private String id;
    private String name;
    private String district;
    private int locationX;
    private int locationY;
    private Date buildDate;

    public Hospital(String district){
        this.district = district;
    }

    public Hospital() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public Date getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(Date buildDate) {
        this.buildDate = buildDate;
    }

    public JsonObject serialize() {
        JsonObject data = new JsonObject();

        data.addProperty("id", this.id);
        data.addProperty("name", this.name);
        data.addProperty("district", this.district);
        data.addProperty("locationX", this.locationX);
        data.addProperty("locationY", this.locationY);
        data.addProperty("buildDate", this.buildDate != null ? this.buildDate.toString() : null);

        return data;
    }

    public String assignHospital(int patientLocationX, int patientLocationY) {
        Connection connection = null;
        PreparedStatement statement = null;
        Map<String, Double> distance = new HashMap<String, Double>();

        double dist;
        String nearestHospital = "";

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM hospital");
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                int locationX = resultSet.getInt("location_x");
                int locationY = resultSet.getInt("location_y");
                int distanceX = Math.abs(locationX - patientLocationX);
                int distanceY = Math.abs(locationY - patientLocationY);

                dist = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
                distance.put(id,dist);
            }

            System.out.println(distance);
            System.out.println(Collections.min(distance.values()));
            nearestHospital = Collections.min(distance.entrySet(), comparingDouble(Map.Entry::getValue)).getKey();
            System.out.println(nearestHospital);
            connection.close();

        } catch (Exception exception) {

        }
        return nearestHospital;
    }


}
