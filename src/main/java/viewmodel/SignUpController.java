package viewmodel;

import dao.DbConnectivityClass;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.UserSession;

import java.util.regex.Pattern;

/**
 * Controller class for handling user registration.
 * Manages validation, user creation, and transitions back to the login interface.
 */
public class SignUpController {


    private TextField firstNameField;


    private TextField lastNameField;


    private TextField usernameField;


    private TextField emailField;


    private PasswordField passwordField;


    private Label statusLabel;

    private final DbConnectivityClass database = new DbConnectivityClass();

    // Validation patterns for input fields
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{1,50}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{4,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Handles the user registration process when the sign-up button is clicked.
     * Validates input fields and creates a new user in the database.
     *
     * @param event the ActionEvent triggered by the sign-up button
     */

    public void handleSignUp(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            if (database.usernameExists(usernameField.getText())) {
                updateStatusMessage("Username already exists. Please choose another.");
                return;
            }

            if (database.emailExists(emailField.getText())) {
                updateStatusMessage("Email already exists. Please use a different email.");
                return;
            }

            boolean success = database.registerUser(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    usernameField.getText(),
                    emailField.getText(),
                    passwordField.getText()
            );

            if (success) {
                UserSession.getInstance(usernameField.getText(), "USER").saveCredentials(
                        usernameField.getText(), passwordField.getText()
                );
                updateStatusMessage("Account created successfully!");
                goBackToLogin(event);
            } else {
                updateStatusMessage("Failed to create account. Please try again.");
            }
        } catch (Exception e) {
            updateStatusMessage("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Validates the input fields for registration.
     *
     * @return true if all fields are valid; false otherwise
     */
    private boolean validateFields() {
        if (isFieldEmpty(firstNameField) || isFieldEmpty(lastNameField) ||
                isFieldEmpty(usernameField) || isFieldEmpty(emailField) || isFieldEmpty(passwordField)) {
            updateStatusMessage("All fields are required.");
            return false;
        }

        if (!NAME_PATTERN.matcher(firstNameField.getText()).matches()) {
            updateStatusMessage("First name must contain only letters and spaces.");
            return false;
        }

        if (!NAME_PATTERN.matcher(lastNameField.getText()).matches()) {
            updateStatusMessage("Last name must contain only letters and spaces.");
            return false;
        }

        if (!USERNAME_PATTERN.matcher(usernameField.getText()).matches()) {
            updateStatusMessage("Username must be 4-20 characters and contain only letters, numbers, and underscores.");
            return false;
        }

        if (!EMAIL_PATTERN.matcher(emailField.getText()).matches()) {
            updateStatusMessage("Please enter a valid email address.");
            return false;
        }

        if (passwordField.getText().length() < 8) {
            updateStatusMessage("Password must be at least 8 characters long.");
            return false;
        }

        return true;
    }

    /**
     * Checks if a text field is empty.
     *
     * @param field the TextField to check
     * @return true if the field is empty; false otherwise
     */
    private boolean isFieldEmpty(TextField field) {
        return field.getText().trim().isEmpty();
    }

    /**
     * Updates the status message label with a given message.
     * The message fades out after a few seconds.
     *
     * @param message the message to display
     */
    private void updateStatusMessage(String message) {
        statusLabel.setText(message);
        statusLabel.setOpacity(1);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), statusLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            statusLabel.setText("");
            statusLabel.setOpacity(0);
        });
        fadeOut.play();
    }

    /**
     * Transitions the user back to the login interface.
     *
     * @param event the ActionEvent triggered by the back button
     */

    public void goBackToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            updateStatusMessage("Error loading login page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
