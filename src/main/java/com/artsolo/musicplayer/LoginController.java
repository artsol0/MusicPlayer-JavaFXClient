package com.artsolo.musicplayer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameLogField;

    @FXML
    private PasswordField userPasswordLogField;

    @FXML
    private Label invalidLogDetails;

    @FXML
    private Button cancelButton, loginButton, signUpWindowButton;

    protected String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
                Platform.exit();
            }
        });

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (usernameLogField.getText().isBlank() && userPasswordLogField.getText().isBlank()) {
                    invalidLogDetails.setText("The Login fields are required!");
                    usernameLogField.setStyle(errorStyle);
                    userPasswordLogField.setStyle(errorStyle);

                } else if (usernameLogField.getText().isBlank()) {
                    usernameLogField.setStyle(errorStyle);
                    invalidLogDetails.setText("The Username is required!");
                    userPasswordLogField.setStyle(null);

                } else if (userPasswordLogField.getText().isBlank()) {
                    userPasswordLogField.setStyle(errorStyle);
                    invalidLogDetails.setText("The Password is required!");
                    usernameLogField.setStyle(null);

                } else {
                    userPasswordLogField.setStyle(null);
                    usernameLogField.setStyle(null);
                    invalidLogDetails.setText(null);

                    Gson gson = new Gson();
                    JsonObject login = new JsonObject();
                    login.addProperty("username", usernameLogField.getText());
                    login.addProperty("password", PasswordEncrypter.encryptPassword(userPasswordLogField.getText()));
                    String usrLoginMessage = gson.toJson(login);

                    try {
                        MusicService musicService = MusicServiceSingleton.getInstance().getMusicService();

                        boolean logResult = musicService.login(usrLoginMessage);

                        if (!logResult) {
                            invalidLogDetails.setText("Username or Password is incorrect");
                        } else {
                            try {
                                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("liked-songs.fxml")));
                                Stage musicPlayerWindow = (Stage) loginButton.getScene().getWindow();
                                musicPlayerWindow.setScene(new Scene(root));
                                musicPlayerWindow.centerOnScreen();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        signUpWindowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signup-form.fxml")));
                    Stage loginWindow = (Stage) signUpWindowButton.getScene().getWindow();
                    loginWindow.setScene(new Scene(root));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                loginButton.setStyle("-fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: transparent; -fx-border-color: #338aff;");
            }
        });

        loginButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                loginButton.setStyle("-fx-background-color: #338aff; -fx-border-radius: 5;");
            }
        });

    }
}
