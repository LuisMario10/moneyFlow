package com.moneyFlow.DAO;

import java.util.*;

import com.moneyFlow.model.UserModel;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import com.moneyFlow.config.ConnectionDataBase;

public class UserDAO implements IGenericDAO<UserModel> {
    private String sql;

    public void create(UserModel user) {
        this.sql = "INSERT INTO user (access_name, password) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<UserModel> findAll() {
        this.sql = "SELECT * FROM user";
        try {
            PreparedStatement pstmt = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            List<UserModel> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new UserModel(rs.getInt("id"), rs.getString("access_name"), "", rs.getString("password")));
            }
            return users;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public UserModel findByID(int id) {
        this.sql = "SELECT * FROM user WHERE id = ?";
        try {
            PreparedStatement pstmt = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new UserModel(rs.getInt("id"), rs.getString("access_name"), "", rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public UserModel findByAccessName(String accessName) {
        this.sql = "SELECT * FROM user WHERE access_name = ?";
        try {
            PreparedStatement pstmt = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(sql);
            pstmt.setString(1, accessName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new UserModel(rs.getInt("id"), rs.getString("access_name"), "", rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    

    public void update(UserModel user) {
        this.sql = "UPDATE user SET access_name = ?, password = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(int id) {
        this.sql = "DELETE FROM user WHERE id = ?";
        try {
            PreparedStatement pstmt = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}