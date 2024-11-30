package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Person;
import service.MyLogger;

import java.sql.*;

/**
 * Handles database connectivity and operations for user and employee management.
 * Provides methods for connecting to the database, managing users, and retrieving data.
 */
public class DbConnectivityClass {

    private static final String DB_NAME = "CSC311_BD_TEMP";
    private static final String SQL_SERVER_URL = "jdbc:mysql://csc311server.mysql.database.azure.com/";
    private static final String DB_URL = "jdbc:mysql://csc311server.mysql.database.azure.com/"+ DB_NAME;
    private static final String USERNAME = "csc311admin";
    private static final String PASSWORD = "Sam311project";

    private final ObservableList<Person> data = FXCollections.observableArrayList();
    private final MyLogger logger = new MyLogger();

    /**
     * Connects to the database, creating the database and users table if they do not exist.
     *
     * @return true if the database contains at least one registered user; false otherwise
     */
    public boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;

        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the server and create the database if not present
            try (Connection connection = DriverManager.getConnection(SQL_SERVER_URL, USERNAME, PASSWORD);
                 Statement statement = connection.createStatement()) {

                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            }

            // Connect to the database and create the users table if not present
            try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                 Statement statement = connection.createStatement()) {

                String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "first_name VARCHAR(100) NOT NULL, " +
                        "last_name VARCHAR(100) NOT NULL, " +
                        "username VARCHAR(150) UNIQUE NOT NULL, " +
                        "password VARCHAR(200) NOT NULL, " +
                        "department VARCHAR(100), " +
                        "performance_rating DECIMAL(4,2), " +
                        "email VARCHAR(200) UNIQUE NOT NULL, " +
                        "image_url TEXT)";
                statement.executeUpdate(createTableSQL);

                // Check if the users table has any records
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    hasRegisteredUsers = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasRegisteredUsers;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return an ObservableList of Person objects representing the users
     */
    public ObservableList<Person> getData() {
        data.clear();
        connectToDatabase();

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Person person = new Person(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("department"),
                        resultSet.getDouble("performance_rating"),
                        resultSet.getString("email")
                );
                data.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Registers a new user in the database without encrypting the password.
     *
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param username the username of the user
     * @param email the email of the user
     * @param password the plain text password of the user
     * @return true if the user is successfully registered; false otherwise
     */
    public boolean registerUser(String firstName, String lastName, String username, String email, String password) {
        connectToDatabase();

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            if (emailExists(email) || usernameExists(username)) {
                return false;
            }

            String insertSQL = "INSERT INTO users (first_name, last_name, username, email, password) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, username);
                statement.setString(4, email);
                statement.setString(5, password); // Storing password as plain text

                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies the username and password for login.
     *
     * @param username the username to verify
     * @param password the plain text password to verify
     * @return true if the username and password match; false otherwise
     */
    public boolean verifyUser(String username, String password) {
        connectToDatabase();

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT password FROM users WHERE username = ?")) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return password.equals(storedPassword); // Compare plain text password
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Checks if a username already exists in the database.
     *
     * @param username the username to check
     * @return true if the username exists; false otherwise
     */
    public boolean usernameExists(String username) {
        return checkExistence("SELECT COUNT(*) FROM users WHERE username = ?", username);
    }

    /**
     * Checks if an email already exists in the database.
     *
     * @param email the email to check
     * @return true if the email exists; false otherwise
     */
    public boolean emailExists(String email) {
        return checkExistence("SELECT COUNT(*) FROM users WHERE email = ?", email);
    }

    /**
     * Helper method to check if a record exists in the database based on a query.
     *
     * @param query the SQL query
     * @param parameter the parameter for the query
     * @return true if the record exists; false otherwise
     */
    private boolean checkExistence(String query, String parameter) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, parameter);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Connection openConnection() {
        return null;
    }
}
