package com.example.ginshinimpact_project2_cs310.MySQLConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLConnection {
    public void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(
                            "jdbc:mysql://your_server_ip:3306/test_db",
                            "your_username",
                            "your_password"
                    );

                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email);
                    }

                    resultSet.close();
                    statement.close();
                    connection.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}