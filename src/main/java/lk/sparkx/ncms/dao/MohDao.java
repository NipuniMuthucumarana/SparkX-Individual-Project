package lk.sparkx.ncms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MohDao {
        public static boolean validate(String username, String password) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            Boolean status = false;

            try {
                connection = DBConnectionPool.getInstance().getConnection();
                ResultSet resultSet;

                // Step 2:Create a statement using connection object
                preparedStatement = connection.prepareStatement("SELECT * from user WHERE username=? AND password=?");
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                System.out.println(preparedStatement);
                // Step 3: Execute the query or update query
                resultSet = preparedStatement.executeQuery();
                status = resultSet.next();
                System.out.println(status);

            } catch (SQLException e) {
                System.out.println(e);
            }
            return status;
        }

    }
