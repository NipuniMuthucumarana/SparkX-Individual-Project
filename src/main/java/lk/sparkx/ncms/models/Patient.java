package lk.sparkx.ncms.models;

import com.google.gson.JsonObject;
import lk.sparkx.ncms.dao.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.io.Serializable;

public class Patient implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String district;
    private int locationX;
    private int locationY;
    private String severityLevel;
    private String gender;
    private String contact;
    private String email;
    private int age;
    private Date admitDate;
    private String admittedBy;
    private Date dischargeDate;
    private String dischargedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getAdmitDate() {
        return admitDate;
    }

    public void setAdmitDate(Date admitDate) {
        this.admitDate = admitDate;
    }

    public String getAdmittedBy() {
        return admittedBy;
    }

    public void setAdmittedBy(String admittedBy) {
        this.admittedBy = admittedBy;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getDischargedBy() {
        return dischargedBy;
    }

    public void setDischargedBy(String dischargedBy) {
        this.dischargedBy = dischargedBy;
    }

    public JsonObject serialize() {
        JsonObject data = new JsonObject();

        data.addProperty("id", this.id);
        data.addProperty("firstName", this.firstName);
        data.addProperty("lastName", this.lastName);
        data.addProperty("district", this.district);
        data.addProperty("locationX", this.locationX);
        data.addProperty("locationY", this.locationY);
        data.addProperty("district", this.district);
        data.addProperty("severityLevel", this.severityLevel);
        data.addProperty("gender", this.gender);
        data.addProperty("contact", this.contact);
        data.addProperty("email", this.email);
        data.addProperty("age", this.age);
        data.addProperty("admitDate", this.admitDate != null ? this.admitDate.toString() : null);
        data.addProperty("admittedBy", this.admittedBy);
        data.addProperty("dischargeDate", this.dischargeDate != null ? this.dischargeDate.toString() : null);
        data.addProperty("dischargedBy", this.dischargedBy);

        return data;
    }

    public void getModel() {
        try {
            Connection connection = DBConnectionPool.getInstance().getConnection();
            PreparedStatement statement;
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM patients WHERE id=? LIMIT 1");
            statement.setInt(1, this.id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                this.id = resultSet.getInt("id");
                this.firstName = resultSet.getString("first_name");
                this.lastName = resultSet.getString("last_name");
                this.district = resultSet.getString("district");
                this.locationX = resultSet.getInt("location_x");
                this.locationY = resultSet.getInt("location_y");
                this.severityLevel = resultSet.getString("severity_level");
                this.gender = resultSet.getString("gender");
                this.contact = resultSet.getString("contact");
                this.email = resultSet.getString("email");
                this.age = resultSet.getInt("age");
                this.admitDate = resultSet.getDate("admit_date");
                this.admittedBy = resultSet.getString("admitted_by");
                this.dischargeDate = resultSet.getDate("discharge_date");
                this.dischargedBy = resultSet.getString("discharged_by");
            }

            connection.close();
        } catch (Exception exception) {

        }
    }
}
