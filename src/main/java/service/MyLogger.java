package service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides logging functionality for the application.
 * Logs messages with timestamps to a designated log file.
 */
public class MyLogger {

    private static final String LOG_FILE = "application.log";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs a message to the log file with a timestamp.
     *
     * @param message the message to log
     */
    public void log(String message) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String logEntry = timestamp + " - " + message;

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(logEntry);
        } catch (IOException e) {
            System.err.println("Failed to write log entry: " + e.getMessage());
        }
    }

    /**
     * Logs an error message to the log file with a timestamp.
     *
     * @param error the error message to log
     */
    public void logError(String error) {
        log("ERROR: " + error);
    }

    /**
     * Logs an informational message to the log file with a timestamp.
     *
     * @param info the informational message to log
     */
    public void logInfo(String info) {
        log("INFO: " + info);
    }

    /**
     * Logs a warning message to the log file with a timestamp.
     *
     * @param warning the warning message to log
     */
    public void logWarning(String warning) {
        log("WARNING: " + warning);
    }
}
