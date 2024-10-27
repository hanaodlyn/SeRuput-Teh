package teh.controller;

import teh.AppContext;
import teh.data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserController {

    private final AppContext context;

    public UserController(AppContext context) {
        this.context = context;
    }

    public Optional<User> login(String username, String password) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return Optional.of(toUser(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User register(String username, String password, String phoneNum, String address, String role, String gender) {
        try {
            String userId = generateId(role.equals("Admin"));

            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO user (userID, username, password, role, address, phone_num, gender) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, role);
            preparedStatement.setString(5, address);
            preparedStatement.setString(6, phoneNum);
            preparedStatement.setString(7, gender);

            preparedStatement.executeUpdate();

            return new User(
                    userId, username, password, role, address, phoneNum, gender
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateId(boolean admin) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT CONCAT(?, LPAD(CAST(SUBSTRING(userID, 3) as UNSIGNED) + 1, 3, '0')) FROM user WHERE role = ? ORDER BY userID DESC LIMIT 1;");

            preparedStatement.setString(1, admin ? "AD" : "CU");
            preparedStatement.setString(2, admin ? "Admin" : "User");

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }

            return admin ? "AD001" : "CU001";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User resolveUser(String userId) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE UserID = ?");

            preparedStatement.setString(1, userId);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return toUser(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User toUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("UserID"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("address"),
                rs.getString("phone_num"),
                rs.getString("gender")
        );
    }

}
