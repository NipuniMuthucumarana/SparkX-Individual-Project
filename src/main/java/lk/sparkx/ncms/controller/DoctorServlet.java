package lk.sparkx.ncms.controller;

import lk.sparkx.ncms.dao.DBConnectionPool;
import lk.sparkx.ncms.dao.DoctorDao;
import lk.sparkx.ncms.models.Doctor;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "DoctorServlet")
public class DoctorServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String hospitalId = request.getParameter("hospitalId");
        Boolean isDirector = Boolean.valueOf(request.getParameter("isDirector"));

        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setName(name);
        doctor.setHospitalId(hospitalId);
        doctor.setDirector(isDirector);

        DoctorDao doctorDao = new DoctorDao();
        String doctorRegistered = doctorDao.registerDoctor(doctor);

        if(doctorRegistered.equals("SUCCESS"))   //On success, you can display a message to user on Home page
        {
            System.out.println("Success");
        }
        else   //On Failure, display a meaningful message to the User.
        {
            System.out.println("Failed");
        }

        try {
            doctorDao.registerDoctor(doctor);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM doctor WHERE id=?");
            statement.setString(1, id);
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String hospitalId = resultSet.getString("hospital_id");
                Boolean isDirector = resultSet.getBoolean("is_director");

                System.out.println("Id: " + id);
                System.out.println("Name: " + name);
                System.out.println("HospitalId: " + hospitalId);
                System.out.println("Is Director: " + isDirector);
                System.out.println("doGet doctor success");

                PrintWriter printWriter = response.getWriter();

                printWriter.println("Id: " + id);
                printWriter.println("Name: " + name);
                printWriter.println("HospitalId: " + hospitalId);
                printWriter.println("Is Director: " + isDirector);
                System.out.println("doGet doctor success");

                JSONObject obj = new JSONObject();

                obj.put("id",id);
                obj.put("name",name);
                obj.put("hospitalId",hospitalId);
                obj.put("isDirector",isDirector);

                StringWriter out = new StringWriter();
                obj.writeJSONString(out);

                String jsonText = out.toString();
                System.out.print(jsonText);
            }
            connection.close();

        } catch (Exception exception) {

        }
    }
}
