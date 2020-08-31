package lk.sparkx.ncms.models;

import com.google.gson.JsonObject;
import lk.sparkx.ncms.dao.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.io.Serializable;

public class Hospital {
    private String id;
    private String name;
    private String district;
    private int locationX;
    private int locationY;
    private Date buildDate;

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

    public void getModel() {
        try {
            Connection connection = DBConnectionPool.getInstance().getConnection();
            PreparedStatement statement;
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM patients WHERE id=? LIMIT 1");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                this.id = resultSet.getString("id");
                this.name = resultSet.getString("name");
                this.district = resultSet.getString("district");
                this.locationX = resultSet.getInt("location_x");
                this.locationY = resultSet.getInt("location_y");
                this.buildDate = resultSet.getDate("build_date");
            }

            connection.close();
        } catch (Exception exception) {

        }
    }
}
