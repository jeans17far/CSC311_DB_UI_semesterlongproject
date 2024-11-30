package viewmodel;

import dao.DbConnectivityClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Simplified Controller for managing the GUI interface and database interactions.
 */
public class DB_GUI_Controller {

    @FXML
    private TableView<Person> tableView;

    @FXML
    private TableColumn<Person, Integer> colId;

    @FXML
    private TableColumn<Person, String> colFirstName, colLastName, colDepartment, colEmail;

    @FXML
    private TableColumn<Person, Double> colPerformanceRating;

    @FXML
    private TextField firstNameField, lastNameField, emailField, performanceRatingField;

    @FXML
    private ComboBox<String> departmentComboBox;

    @FXML
    private Button addButton, deleteButton;

    private final ObservableList<Person> personData = FXCollections.observableArrayList();
    private final DbConnectivityClass database = new DbConnectivityClass();

    /**
     * Initializes the controller, sets up the table, and loads data from the database.
     */
    public void initialize() {
        setupTable();
        loadDataFromDatabase();
        setupComboBox();
    }

    /**
     * Configures the table columns.
     */
    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colPerformanceRating.setCellValueFactory(new PropertyValueFactory<>("performanceRating"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableView.setItems(personData);
    }

    /**
     * Populates the department ComboBox with predefined values.
     */
    private void setupComboBox() {
        departmentComboBox.setItems(FXCollections.observableArrayList("HR", "IT", "Finance", "Marketing", "Operations"));
    }

    /**
     * Loads data from the database and updates the table view.
     */
    private void loadDataFromDatabase() {
        personData.clear();

        try (Connection connection = database.openConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                personData.add(new Person(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("department"),
                        resultSet.getDouble("performance_rating"),
                        resultSet.getString("email")
                ));
            }

            statement.close();
        } catch (Exception e) {
            showError("Error loading data: " + e.getMessage());
        }
    }

    /**
     * Adds a new record to the database and refreshes the table.
     */
    @FXML
    private void addRecord() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String department = departmentComboBox.getValue();
        String email = emailField.getText();
        double performanceRating;

        try {
            performanceRating = Double.parseDouble(performanceRatingField.getText());
        } catch (NumberFormatException e) {
            showError("Performance rating must be a number.");
            return;
        }

        try (Connection connection = database.openConnection()) {
            String sql = "INSERT INTO users (first_name, last_name, department, performance_rating, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, department);
            statement.setDouble(4, performanceRating);
            statement.setString(5, email);
            statement.executeUpdate();

            statement.close();
            loadDataFromDatabase();
            clearForm();
            showInfo("Record added successfully!");
        } catch (Exception e) {
            showError("Error adding record: " + e.getMessage());
        }
    }

    /**
     * Deletes the selected record from the database and refreshes the table.
     */
    @FXML
    private void deleteRecord() {
        Person selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) {
            showError("No record selected for deletion.");
            return;
        }

        try (Connection connection = database.openConnection()) {
            String sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, selectedPerson.getId());
            statement.executeUpdate();

            statement.close();
            loadDataFromDatabase();
            clearForm();
            showInfo("Record deleted successfully!");
        } catch (Exception e) {
            showError("Error deleting record: " + e.getMessage());
        }
    }

    /**
     * Clears all input fields.
     */
    @FXML
    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        departmentComboBox.getSelectionModel().clearSelection();
        performanceRatingField.clear();
        emailField.clear();
        tableView.getSelectionModel().clearSelection();
    }

    /**
     * Displays an informational message.
     *
     * @param message the message to display
     */
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an error message.
     *
     * @param message the message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
