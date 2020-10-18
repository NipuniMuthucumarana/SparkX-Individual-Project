package lk.sparkx.ncms.models;

import com.google.gson.JsonObject;
import lk.sparkx.ncms.dao.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {
    private String username;
    private String password;
    private String name;
    private boolean isMoh;
    private boolean isHospital;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMoh() {
        return isMoh;
    }

    public void setMoh(boolean moh) {
        isMoh = moh;
    }

    public boolean isHospital() {
        return isHospital;
    }

    public void setHospital(boolean hospital) {
        isHospital = hospital;
    }

    public JsonObject serialize() {
        JsonObject data = new JsonObject();

        data.addProperty("username", this.username);
        data.addProperty("password", this.password);
        data.addProperty("name", this.name);
        data.addProperty("isMoh", this.isMoh);
        data.addProperty("isHospital", this.isHospital);

        return data;
    }

    public void getModel() {
        try {
            Connection connection = DBConnectionPool.getInstance().getConnection();
            PreparedStatement statement;
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM user WHERE username=?");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                this.username = resultSet.getString("username");
                this.password = resultSet.getString("password");
                this.name = resultSet.getString("name");
                this.isMoh = resultSet.getBoolean("isMoh");
                this.isHospital = resultSet.getBoolean("isHospital");
            }
            connection.close();
        } catch (Exception exception) {

        }
    }
}
