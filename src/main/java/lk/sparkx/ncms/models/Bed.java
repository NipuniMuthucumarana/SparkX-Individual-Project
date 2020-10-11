package lk.sparkx.ncms.models;

import lk.sparkx.ncms.dao.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;


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

    public void makeAvailable(String patientId, String hospitalId) {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        PreparedStatement statement4 = null;
        PreparedStatement statement5 = null;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            int result=0;
            int result2=0;
            ResultSet resultSet;
            ResultSet resultSet2;
            Map<Integer,String> queueDetails = new HashMap<Integer,String>();

            statement = connection.prepareStatement("DELETE FROM hospital_bed WHERE patient_id='"+patientId+"'");
            System.out.println(statement);
            result = statement.executeUpdate();

            statement2 = connection.prepareStatement("SELECT * FROM patient_queue ORDER BY id ASC");
            System.out.println(statement2);
            resultSet = statement2.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String patient = resultSet.getString("patient_id");
                //queueDetails.put(id,patient);
                allocateBed(hospitalId, patient);

                statement3 = connection.prepareStatement("DELETE FROM patient_queue where patient_id = '"+patient+"'");
                System.out.println(statement3);
                result2 = statement3.executeUpdate();

                statement4 = connection.prepareStatement("SELECT * FROM patient_queue ORDER BY id ASC");
                System.out.println(statement4);
                resultSet2 = statement4.executeQuery();

                while(resultSet2.next()) {
                    int currentId = resultSet2.getInt("id");
                    int nextId = currentId-1;
                    System.out.println(nextId);
                    statement5 = connection.prepareStatement("UPDATE patient_queue SET id=" + nextId + " Where id="+ currentId);
                    System.out.println(statement5);
                    statement5.executeUpdate();
                }
            }
            connection.close();

        } catch (Exception exception) {

        }
    }
}
