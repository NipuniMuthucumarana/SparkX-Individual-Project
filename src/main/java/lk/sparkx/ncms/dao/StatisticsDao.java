package lk.sparkx.ncms.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.sparkx.ncms.util.DBConnectionPool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StatisticsDao {
    //view statistics
    public JsonArray viewStatistics(String method) {
        JsonObject stats = new JsonObject(); //create a JSON Object stats.
        JsonArray jArray = new JsonArray(); //create a JSON Array jArray.

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        PreparedStatement statement4 = null;
        PreparedStatement statement5 = null;
        PreparedStatement statement6 = null;
        PreparedStatement statement7 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            ResultSet resultSet2;
            ResultSet resultSet3;
            ResultSet resultSet4;
            ResultSet resultSet5;
            ResultSet resultSet6;
            ResultSet resultSet7;

            if (method.equals("hospitalLevel")) {
                statement = connection.prepareStatement("SELECT name FROM hospital");
                System.out.println(statement);
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String hospital = resultSet.getString("name");
                    statement6 = connection.prepareStatement("SELECT COUNT(hospital_bed.id) AS hospitalLevel FROM hospital_bed INNER JOIN hospital ON hospital_bed.hospital_id = hospital.id WHERE hospital.name ='" + hospital + "'");
                    System.out.println(statement6);
                    resultSet6 = statement6.executeQuery();
                    while (resultSet6.next()) {
                        int patientCount = resultSet6.getInt("hospitalLevel");
                        System.out.println(patientCount);

                        JsonObject hospitalLevel = new JsonObject();
                        hospitalLevel.addProperty("name", hospital);
                        hospitalLevel.addProperty("statistics", patientCount);
                        jArray.add(hospitalLevel);
                    }
                }
                stats.add("hospitalLevelStats", jArray);
            }else if (method.equals("districtLevel")) {
                statement7 = connection.prepareStatement("SELECT distinct district FROM hospital");
                System.out.println(statement7);
                resultSet7 = statement7.executeQuery();
                while (resultSet7.next()) {
                    String district = resultSet7.getString("district");
                    statement2 = connection.prepareStatement("SELECT COUNT(hospital_bed.id) AS districtLevel FROM hospital_bed INNER JOIN hospital ON hospital_bed.hospital_id = hospital.id WHERE hospital.district ='" + district + "'");
                    statement5 = connection.prepareStatement("SELECT COUNT(patient_queue.id) AS queueDistrictLevel FROM patient_queue INNER JOIN patient ON patient.id = patient_queue.patient_id WHERE patient.district ='" + district + "'");
                    System.out.println(statement2);
                    System.out.println(statement5);
                    resultSet2 = statement2.executeQuery();
                    resultSet5 = statement5.executeQuery();

                    while (resultSet2.next()) {
                        int disPatientCount = resultSet2.getInt("districtLevel");
                        while (resultSet5.next()) {
                            int queueDisPatient = resultSet5.getInt("queueDistrictLevel");
                            int districtPatientCount = disPatientCount + queueDisPatient;
                            System.out.println(districtPatientCount);

                            JsonObject districtLevel = new JsonObject();
                            districtLevel.addProperty("name", district);
                            districtLevel.addProperty("statistics", districtPatientCount);
                            jArray.add(districtLevel);

                        }
                    }
                }
                stats.add("districtLevelStats", jArray);
            } else if (method.equals("countryLevel")) {

                statement3 = connection.prepareStatement("SELECT COUNT(id) AS countryLevel FROM hospital_bed");
                statement4 = connection.prepareStatement("SELECT COUNT(id) AS countryLevelQueue FROM patient_queue");
                System.out.println(statement3);
                resultSet3 = statement3.executeQuery();
                resultSet4 = statement4.executeQuery();

                while (resultSet3.next()) {
                    int hospitalPatientCount = resultSet3.getInt("countryLevel");
                    while (resultSet4.next()) {
                        int queuePatientCount = resultSet4.getInt("countryLevelQueue");
                        int countryPatientCount = hospitalPatientCount + queuePatientCount;
                        System.out.println(countryPatientCount);

                        JsonObject countryLevel = new JsonObject();
                        countryLevel.addProperty("name", "country");
                        countryLevel.addProperty("statistics", countryPatientCount);
                        jArray.add(countryLevel);
                    }
                }
                stats.add("countryLevelStats", jArray);
            } else if (method.equals("dateWise")) {

                statement3 = connection.prepareStatement("SELECT distinct admit_date FROM patient WHERE admit_date!= 'null'");
                System.out.println(statement3);
                resultSet3 = statement3.executeQuery();

                while (resultSet3.next()) {
                    Date admitDate = resultSet3.getDate("admit_date");
                    statement4 = connection.prepareStatement("SELECT COUNT(id) AS dayLevel FROM patient where admit_date='"+admitDate+"'");
                    System.out.println(statement4);
                    resultSet4 = statement4.executeQuery();
                    while (resultSet4.next()) {
                        int patientCount = resultSet4.getInt("dayLevel");
                        System.out.println(patientCount);

                        JsonObject dailyCount = new JsonObject();
                        dailyCount.addProperty("name", String.valueOf(admitDate));
                        dailyCount.addProperty("statistics", patientCount);
                        jArray.add(dailyCount);
                    }
                }
                stats.add("dailyLevelStats", jArray);
            } else if (method.equals("recovered")) {

                statement3 = connection.prepareStatement("SELECT distinct discharge_date FROM patient WHERE discharge_date!= 'null'");
                System.out.println(statement3);
                resultSet3 = statement3.executeQuery();

                while (resultSet3.next()) {
                    Date dischargeDate = resultSet3.getDate("discharge_date");
                    statement4 = connection.prepareStatement("SELECT COUNT(id) AS recover FROM patient where discharge_date='"+dischargeDate+"'");
                    System.out.println(statement4);
                    resultSet4 = statement4.executeQuery();
                    while (resultSet4.next()) {
                        int recoverCount = resultSet4.getInt("recover");
                        System.out.println(recoverCount);

                        JsonObject recovery = new JsonObject();
                        recovery.addProperty("name", String.valueOf(dischargeDate));
                        recovery.addProperty("statistics", recoverCount);
                        jArray.add(recovery);
                    }
                }
                stats.add("recoveryLevelStats", jArray);
            }
            connection.close();

        } catch (Exception exception) {

        }
        return jArray;
    }
}
