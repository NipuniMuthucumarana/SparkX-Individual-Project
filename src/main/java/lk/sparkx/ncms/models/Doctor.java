package lk.sparkx.ncms.models;

import com.google.gson.JsonObject;
import lk.sparkx.ncms.dao.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.io.Serializable;

public class Doctor implements Serializable {
    private String id;
    private String name;
    private String hospitalId;
    private boolean isDirector;

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

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public boolean isDirector() {
        return isDirector;
    }

    public void setDirector(boolean director) {
        isDirector = director;
    }

    public JsonObject serialize() {
        JsonObject data = new JsonObject();

        data.addProperty("id", this.id);
        data.addProperty("name", this.name);
        data.addProperty("hospitalId", this.hospitalId);
        data.addProperty("isDirector", this.isDirector);

        return data;
    }

    public void getModel() {
        try {
            Connection connection = DBConnectionPool.getInstance().getConnection();
            PreparedStatement statement;
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM doctor WHERE id=?");
            //statement.setString(1, this.id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                this.id = resultSet.getString("id");
                this.name = resultSet.getString("name");
                this.hospitalId = resultSet.getString("hospital_id");
                this.isDirector = resultSet.getBoolean("is_director");
            }

            connection.close();
        } catch (Exception exception) {

        }
    }

}