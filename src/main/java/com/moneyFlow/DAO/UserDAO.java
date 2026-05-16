package com.moneyFlow.DAO;

import com.moneyFlow.config.ConnectionDataBase;
import com.moneyFlow.model.UserModel;
import com.moneyFlow.security.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends GenericDAO<UserModel> {

    public static boolean isUsernameTaken(String username) {
        String sql = "SELECT count(*) FROM user WHERE access_name = ?";

        try (Connection conn = ConnectionDataBase.getConnectionWithDataBase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar disponibilidade de username: " + e.getMessage());
        }
        return false;
    }

    public static boolean saveUser(String access_name, String password) {
        String sql = "INSERT INTO user (access_name, password) VALUES (?, ?)";

        String hashedPassword = PasswordHasher.hash(password);

        try (Connection conn = ConnectionDataBase.getConnectionWithDataBase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, access_name);
            pstmt.setString(2, hashedPassword);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário no banco: " + e.getMessage());
            return false;
        }
    }

    public static boolean validateLogin(String username, String password) {
        String sql = "SELECT password FROM user WHERE access_name = ?";

        try (Connection conn = ConnectionDataBase.getConnectionWithDataBase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");

                    return PasswordHasher.check(password, storedHash);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao validar login: " + e.getMessage());
        }
        return false;
    }

}