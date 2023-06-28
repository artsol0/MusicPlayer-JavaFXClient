package com.artsolo.musicplayer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button likedSongsButton, yourAlbumsButton, searchButton, logoutButton, findButton;

    @FXML
    private Button findRockButton, findPopButton, findHipHopButton, findKpopButton, findElectronicButton, findSoulButton, findMetalButton, findFunkButton, findJazzButton, findClassicalButton, findCountryButton, findPunkButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        findButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String searchString = searchField.getText();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("searchByString-result.fxml"));
                try {
                    Parent root = loader.load();
                    StringResultController controller = loader.getController();
                    controller.setSearchString(searchString);

                    Scene scene = findButton.getScene();
                    scene.setRoot(root);

                    Platform.runLater(controller::initializeSearchString);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        findPopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                prepareSearch(1, findPopButton);
            }
        });

        findHipHopButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prepareSearch(2, findHipHopButton);
            }
        });

        findRockButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prepareSearch(3, findRockButton);
            }
        });

        findKpopButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prepareSearch(4, findKpopButton);
            }
        });

        findElectronicButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prepareSearch(5, findElectronicButton);
            }
        });

        findSoulButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prepareSearch(6, findSoulButton);
            }
        });

        findMetalButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prepareSearch(7, findMetalButton);
            }
        });

        findFunkButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prepareSearch(8, findFunkButton);
            }
        });

        findJazzButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prepareSearch(9, findJazzButton);
            }
        });

        findClassicalButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prepareSearch(10, findClassicalButton);
            }
        });

        findCountryButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prepareSearch(11, findCountryButton);
            }
        });

        findPunkButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prepareSearch(12, findPunkButton);
            }
        });

        likedSongsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("liked-songs.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = likedSongsButton.getScene();
                scene.setRoot(root);
            }
        });
        likedSongsButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                likedSongsButton.setTextFill(Paint.valueOf("#338aff"));
                likedSongsButton.setUnderline(true);
            }
        });
        likedSongsButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                likedSongsButton.setTextFill(Paint.valueOf("WHITE"));
                likedSongsButton.setUnderline(false);
            }
        });

        yourAlbumsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("your-albums.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene scene = searchButton.getScene();
                scene.setRoot(root);
            }
        });
        yourAlbumsButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                yourAlbumsButton.setTextFill(Paint.valueOf("#338aff"));
                yourAlbumsButton.setUnderline(true);
            }
        });
        yourAlbumsButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                yourAlbumsButton.setTextFill(Paint.valueOf("WHITE"));
                yourAlbumsButton.setUnderline(false);
            }
        });

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-form.fxml")));
                    Stage loginWindow = (Stage) logoutButton.getScene().getWindow();
                    loginWindow.setScene(new Scene(root));
                    loginWindow.centerOnScreen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void prepareSearch(int genreId, Button btn) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("searchByGenre-result.fxml"));
        try {
            Parent root = loader.load();
            GenreResultController controller = loader.getController();
            controller.setGenreId(genreId);

            Scene scene = btn.getScene();
            scene.setRoot(root);

            Platform.runLater(controller::initializeGenreId);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
