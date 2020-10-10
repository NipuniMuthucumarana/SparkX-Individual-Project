package lk.sparkx.ncms.models;

import lk.sparkx.ncms.dao.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Bed {
    private int id;
    private String hospitalId;
    private String patientId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int allocateBed(String hospitalId, String patientId) {
        setHospitalId(hospitalId);
        setPatientId(patientId);
        int bedId = 0;
        int noOfBeds = 10;
        int bedCount = 0;

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        int result =0;
        int [] bed = new int[10];

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            statement = connection.prepareStatement("SELECT * FROM hospital_bed where hospital_id= '" + getHospitalId() + "'");
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                bed[id-1]=id;
            }
            for(int i=0; i< noOfBeds; i++){
                if(bed[i]==0){
                    bedId = i+1;
                    bedCount = bedId;
                    break;
                }
            }
            if(bedId!=0) {
                statement2 = connection.prepareStatement("INSERT INTO hospital_bed (id, hospital_id, patient_id) VALUES (" + bedId + ",'" + hospitalId + "','" + patientId + "')");
                System.out.println(statement2);
                result = statement2.executeUpdate();
            }
            connection.close();

        } catch (Exception exception) {

        }

        return  bedId;
    }

    public void makeAvailable(String patientId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            int result=0;

            statement = connection.prepareStatement("DELETE FROM hospital_bed WHERE patient_id='"+patientId+"'");
            System.out.println(statement);
            result = statement.executeUpdate();

            connection.close();

        } catch (Exception exception) {

        }
    }
}
