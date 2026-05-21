package com.moneyFlow.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDataBase {
    
    private static final String URL = "jdbc:sqlite:moneyFlow/src/resources/db/database.db";

    private static Connection conn = null;

    public static Connection getConnectionWithDataBase() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL);

                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("PRAGMA journal_mode=WAL;");
                    stmt.execute("PRAGMA foreign_keys=ON;");
                }

                System.out.println("Data Base Mensage: Conexao com SQLite concedida!!");
            }
            return conn;
        } catch (SQLException e) {
            System.out.println("Data Base Mensage: Conexao falhou e esse foi o motivo: ");
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
                System.out.println("Data Base Mensage: Conexao com SQLite encerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}