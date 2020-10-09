package lk.sparkx.ncms.models;

import com.google.gson.JsonObject;
import lk.sparkx.ncms.dao.DBConnectionPool;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    public void dischargePatients(String patientId, String hospitalId) {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result = 0;

        //Doctor dr = new Doctor();
        try {
            connection = DBConnectionPool.getInstance().getConnection();
            //PreparedStatement statement;
            ResultSet resultSet;

            statement2 = connection.prepareStatement("SELECT * FROM doctor WHERE hospital_id='" +hospitalId + "' AND is_director=1");
            resultSet = statement2.executeQuery();
            System.out.println(statement2);
            while (resultSet.next()) {
                String director = resultSet.getString("id");
                System.out.println(director);
                statement = connection.prepareStatement("UPDATE patient SET discharge_date=? , discharged_by= '" + director + "' WHERE id = '" + patientId + "'");


                statement.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
                //statement.setString(2, directorId);

                System.out.println(statement);
                result = statement.executeUpdate();
                if(result!=0){
                    System.out.println("success");
                }else
                    System.out.println("Failed");
            }
            /*while (resultSet.next()) {
                this.id = resultSet.getString("id");
                this.name = resultSet.getString("name");
                this.hospitalId = resultSet.getString("hospital_id");
                //this.isDirector = resultSet.getBoolean("is_director");

                /*JSONObject obj = new JSONObject();

                obj.put("id",id);
                obj.put("name",name);
                obj.put("hospitalId",hospitalId);
                obj.put("isDirector",isDirector);

                StringWriter out = new StringWriter();
                obj.writeJSONString(out);

                String jsonText = out.toString();
                System.out.print(jsonText);*/


            //}



            System.out.println("Id: " + id);
            System.out.println("Name: " + name);
            System.out.println("HospitalId: " + hospitalId);
            System.out.println("Is Director: " + isDirector);
            System.out.println("doGet doctor success");

            connection.close();


        } catch (Exception exception) {

        }


    }

}
