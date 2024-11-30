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

/**
 * Controller class for handling login operations.
 * Manages user authentication and transitions to the main application interface.
 */
public class LoginController {


    private TextField usernameField;


    private PasswordField passwordField;


    private Label statusLabel;

    private final DbConnectivityClass database = new DbConnectivityClass();

    /**
     * Handles the login process when the login button is clicked.
     *
     * @param event the ActionEvent triggered by the login button
     */

    public void login(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            updateStatusMessage("Please enter both username and password.");
            return;
        }

        try {
            if (database.verifyUser(username, password)) {
                // Create and save the user session
                UserSession userSession = UserSession.getInstance(username, "USER");
                userSession.saveCredentials(username, password);

                // Transition to the main application interface
                loadMainInterface(event);
            } else {
                updateStatusMessage("Invalid username or password.");
            }
        } catch (Exception e) {
            updateStatusMessage("Error during login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Redirects the user to the sign-up interface.
     *
     * @param event the ActionEvent triggered by the sign-up button
     */

    public void signUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/signUp.fxml"));
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            updateStatusMessage("Error loading sign-up page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads the main application interface upon successful login.
     *
     * @param event the ActionEvent triggered by the login button
     * @throws Exception if the main interface fails to load
     */
    private void loadMainInterface(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/db_interface_gui.fxml"));
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/css/darkTheme.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
}
