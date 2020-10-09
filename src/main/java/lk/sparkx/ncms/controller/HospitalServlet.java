package lk.sparkx.ncms.controller;

import lk.sparkx.ncms.dao.DBConnectionPool;
import lk.sparkx.ncms.dao.HospitalDao;
import lk.sparkx.ncms.models.Bed;
import lk.sparkx.ncms.models.Doctor;
import lk.sparkx.ncms.models.Hospital;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "HospitalServlet")
public class HospitalServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String district = request.getParameter("district");
        int locationX = Integer.parseInt(request.getParameter("locationX"));
        int locationY = Integer.parseInt(request.getParameter("locationY"));
        java.util.Date dateBuild = new java.util.Date();
        Date buildDate = new Date(dateBuild.getTime());

        Hospital hospital = new Hospital();
        hospital.setId(id);
        hospital.setName(name);
        hospital.setDistrict(district);
        hospital.setLocationX(locationX);
        hospital.setLocationY(locationY);
        hospital.setBuildDate(buildDate);

        HospitalDao hospitalDao = new HospitalDao();
        String hospitalRegistered = hospitalDao.registerHospital(hospital);

        if(hospitalRegistered.equals("SUCCESS"))   //On success, you can display a message to user on Home page
        {
            System.out.println("Success");
        }
        else   //On Failure, display a meaningful message to the User.
        {
            System.out.println("Failed");
        }

        try {
            hospitalDao.registerHospital(hospital);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String district = request.getParameter("district");

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            //PreparedStatement statement;
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM hospital WHERE district=?");
            statement.setString(1, district);
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                //String district = resultSet.getString("district");
                int locationX = resultSet.getInt("location_x");
                int locationY = resultSet.getInt("location_y");
                Date buildDate = resultSet.getDate("build_date");

                /*System.out.println("Id: " + id);
                System.out.println("Name: " + name);
                System.out.println("HospitalId: " + dis);
                System.out.println("Is Director: " + isDirector);
                System.out.println("doGet doctor success");*/

                PrintWriter printWriter = response.getWriter();

                printWriter.println("Id: " + id);
                printWriter.println("Name: " + name);
                printWriter.println("District: " + district);
                printWriter.println("Location_X: " + locationX);
                printWriter.println("Location_Y: " + locationY);
                printWriter.println("Build Date: " + buildDate);
                System.out.println("doGet doctor success");

                /*JSONObject hospitalObj = new JSONObject();

                hospitalObj.put("id",id);
                hospitalObj.put("name",name);
                hospitalObj.put("district",district);
                hospitalObj.put("locationX",locationX);
                hospitalObj.put("locationY",locationY);
                hospitalObj.put("buildDate",buildDate);

                StringWriter out = new StringWriter();
                hospitalObj.writeJSONString(out);

                String jsonText = out.toString();
                System.out.print(jsonText);*/
            }



            connection.close();

        } catch (Exception exception) {

        }
    }

    /* Discharge patient by director and make the bed available for other patients */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        PreparedStatement statement = null;

        String patientId = request.getParameter("id");
        String hospitalId = request.getParameter("hospital_id");

        Doctor doctor = new Doctor();
        doctor.dischargePatients(patientId, hospitalId);

        Bed bed = new Bed();
        bed.makeAvailable(patientId);
    }



}
