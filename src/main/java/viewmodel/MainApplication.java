package viewmodel;

import dao.DbConnectivityClass;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main entry point for the Employee Data Management System application.
 * This class initializes the application, sets up the primary stage,
 * and transitions between scenes.
 */
public class MainApplication extends Application {

    private static Scene primaryScene;
    private static DbConnectivityClass databaseUtil;
    private Stage primaryStage;

    /**
     * Main method to launch the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        databaseUtil = new DbConnectivityClass(); // Initialize database utility
        launch(args);
    }

    /**
     * Starts the JavaFX application and initializes the primary stage.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Set application icon and title
        Image appIcon = new Image(getClass().getResourceAsStream("/images/appIcon.png"));
        this.primaryStage.getIcons().add(appIcon);
        this.primaryStage.setTitle("Employee Manager Pro");
        this.primaryStage.setResizable(false);

        loadSplashScreen(); // Load the splash screen
    }

    /**
     * Loads the splash screen and initiates a transition to the login interface.
     */
    private void loadSplashScreen() {
        try {
            Parent splashRoot = FXMLLoader.load(getClass().getResource("/view/splashscreen.fxml"));
            Scene splashScene = new Scene(splashRoot, 600, 400);
            splashScene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());

            primaryStage.setScene(splashScene);
            primaryStage.show();

            transitionToLogin(); // Trigger a fade transition
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fades out the splash screen and transitions to the login interface.
     */
    private void transitionToLogin() {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene loginScene = new Scene(loginRoot, 600, 400);
            loginScene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), primaryScene.getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                primaryStage.setScene(loginScene);
                primaryStage.show();
            });

            fadeOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
