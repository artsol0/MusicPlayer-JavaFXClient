package com.artsolo.musicplayer;

import com.artsolo.musicplayer.services.MusicService;
import com.artsolo.musicplayer.services.UserService;
import com.artsolo.musicplayer.singletons.MusicServiceSingleton;
import com.artsolo.musicplayer.singletons.UserServiceSingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController implements Initializable {

    @FXML
    private TextField usernameSignField;

    @FXML
    private TextField emailSignField;

    @FXML
    private PasswordField passwordSignField;

    @FXML
    private Label invalidSignDetails;

    @FXML
    private Button signUnButton, logInWindowButton;

    protected String errorMessage = String.format("-fx-text-fill: RED;");
    protected String successfulMessage = String.format("-fx-text-fill: #338aff;");
    protected String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    protected String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(emailSignField.getText());

                if (usernameSignField.getText().isBlank() && emailSignField.getText().isBlank() && passwordSignField.getText().isBlank()) {
                    invalidSignDetails.setText("The Registration fields are required!");
                    usernameSignField.setStyle(errorStyle);
                    emailSignField.setStyle(errorStyle);
                    passwordSignField.setStyle(errorStyle);

                } else if (usernameSignField.getText().isBlank()) {
                    invalidSignDetails.setText("The Username is required!");
                    usernameSignField.setStyle(errorStyle);
                    emailSignField.setStyle(null);
                    passwordSignField.setStyle(null);

                } else if (emailSignField.getText().isBlank()) {
                    invalidSignDetails.setText("The Email is required!");
                    emailSignField.setStyle(errorStyle);
                    usernameSignField.setStyle(null);
                    passwordSignField.setStyle(null);

                } else if (passwordSignField.getText().isBlank()) {
                    invalidSignDetails.setText("The Password is required!");
                    passwordSignField.setStyle(errorStyle);
                    usernameSignField.setStyle(null);
                    emailSignField.setStyle(null);

                } else if (!matcher.matches()) {
                    invalidSignDetails.setText("Invalid email address!");
                    emailSignField.setStyle(errorStyle);
                    usernameSignField.setStyle(null);
                    passwordSignField.setStyle(null);

                } else if (passwordSignField.getText().length() < 8) {
                    invalidSignDetails.setText("The Password can't be less than 8 characters!");
                    passwordSignField.setStyle(errorStyle);
                    usernameSignField.setStyle(null);
                    emailSignField.setStyle(null);

                } else {
                    Gson gson = new Gson();
                    JsonObject registration = new JsonObject();
                    registration.addProperty("username",usernameSignField.getText());
                    registration.addProperty("email", emailSignField.getText());
                    registration.addProperty("password", PasswordEncrypter.encryptPassword(passwordSignField.getText()));

                    String userRegistrationMessage = gson.toJson(registration);

                    try {
                        UserService userService = UserServiceSingleton.getInstance().getAlbumService();
                        String result = userService.registerNewUser(userRegistrationMessage);

                        if (result.equals("Registration was completed successfully")) {
                            invalidSignDetails.setStyle(successfulMessage);
                            invalidSignDetails.setText(result);
                        } else {
                            invalidSignDetails.setStyle(errorMessage);
                            invalidSignDetails.setText(result);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        logInWindowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-form.fxml")));
                    Stage loginWindow = (Stage) logInWindowButton.getScene().getWindow();
                    loginWindow.setScene(new Scene(root));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        signUnButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                signUnButton.setStyle("-fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: transparent; -fx-border-color: #338aff;");
            }
        });

        signUnButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                signUnButton.setStyle("-fx-background-color: #338aff; -fx-border-radius: 5;");
            }
        });
    }


}
