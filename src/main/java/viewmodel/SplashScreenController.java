package viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.animation.FadeTransition;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Controller class for the splash screen.
 * Displays an introductory message with animations.
 */
public class SplashScreenController {

    @FXML
    private Label welcomeText;

    /**
     * Initializes the splash screen with a fade-in animation for the welcome text.
     */
    @FXML
    public void initialize() {
        try {
            // Set font and size for the welcome text
            welcomeText.setFont(new Font("Arial Bold", 24));
            welcomeText.setText("Welcome to Employee Manager Pro!");

            // Add fade-in animation
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), welcomeText);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setOnFinished(event -> welcomeText.setText("Managing Employees, Made Easy!"));
            fadeIn.play();
        } catch (Exception e) {
            System.err.println("Error initializing SplashScreenController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Changes the text when a button is clicked (if implemented in the future).
     */
    @FXML
    protected void onButtonClick() {
        welcomeText.setText("Let's Get Started!");
    }
}
