package com.artsolo.musicplayer;

import com.artsolo.musicplayer.models.Album;
import com.artsolo.musicplayer.models.Music;
import com.artsolo.musicplayer.models.User;
import com.artsolo.musicplayer.services.AlbumService;
import com.artsolo.musicplayer.services.MusicService;
import com.artsolo.musicplayer.singletons.AlbumServiceSingleton;
import com.artsolo.musicplayer.singletons.MusicServiceSingleton;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class LikedSongsController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button yourAlbumsButton, searchButton, logoutButton, playBtn, pauseBtn, resetBtn, nextBtn, previousBtn, findButton;

    @FXML
    private Label musicTitleLabel, notificationLabel;

    @FXML
    private VBox songBox;

    private MediaPlayer currentMediaPlayer = null;
    private int currentMusicIndex;
    private String currentMusicTitle = null;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {
            try {

                MusicService musicService = MusicServiceSingleton.getInstance().getMusicService();
                AlbumService albumService = AlbumServiceSingleton.getInstance().getAlbumService();
                List<Music> playlist = musicService.getMusic(user.getId());

                for (Music music : playlist) {
                    HBox songBoxInner = new HBox();
                    songBoxInner.setPrefHeight(45.0);
                    songBoxInner.setPrefWidth(600.0);
                    songBoxInner.setStyle("-fx-padding: 2;");

                    Button addToAlbumButton = new Button("+");
                    addToAlbumButton.setPrefSize(50.0,45.0);
                    addToAlbumButton.setCursor(Cursor.HAND);
                    addToAlbumButton.setStyle("-fx-background-color: #202020; -fx-background-radius: 5 0 0 5; -fx-background-insets: 0;");
                    addToAlbumButton.setTextFill(Paint.valueOf("343434"));

                    addToAlbumButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            addToAlbumButton.setStyle("-fx-background-color: #242424; -fx-background-radius: 5 0 0 5; -fx-background-insets: 0;");
                        }
                    });

                    addToAlbumButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            addToAlbumButton.setStyle("-fx-background-color: #202020; -fx-background-radius: 5 0 0 5; -fx-background-insets: 0;");
                        }
                    });

                    addToAlbumButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {

                            try {
                                List<Album> albums = albumService.getAlbum(user.getId());

                                if (albums.isEmpty()) {
                                    notificationLabel.setText("You have not any albums");

                                    FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), notificationLabel);
                                    fadeIn.setToValue(1); // встановлюємо кінцеву прозорість елементу
                                    fadeIn.play(); // запускаємо анімацію появи

                                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), notificationLabel);
                                    fadeOut.setToValue(0); // встановлюємо кінцеву прозорість елементу
                                    notificationLabel.setOpacity(1);

                                    // викликаємо анімацію зникнення через 3 секунди
                                    fadeOut.setDelay(Duration.seconds(3));

                                    fadeOut.play();

                                } else {
                                    List<String> albumsTitles = albums.stream().map(Album::getName).toList();

                                    ChoiceDialog<String> dialog = new ChoiceDialog<>(albumsTitles.get(0), albumsTitles);
                                    dialog.setTitle("Add song to album");
                                    dialog.setHeaderText("Select the album you want to add the song to");
                                    dialog.setContentText("Album:");

                                    Optional<String> result = dialog.showAndWait();
                                    if (result.isPresent()) {
                                        Album selectedAlbum = albums.stream().filter(album -> album.getName().equals(result.get())).findFirst().orElse(null);
                                        if (selectedAlbum != null) {
                                            String addToAlbumResult = albumService.addMusicToAlbum(selectedAlbum.getId(), music.getId());

                                            notificationLabel.setText(addToAlbumResult);

                                            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), notificationLabel);
                                            fadeIn.setToValue(1); // встановлюємо кінцеву прозорість елементу
                                            fadeIn.play(); // запускаємо анімацію появи

                                            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), notificationLabel);
                                            fadeOut.setToValue(0); // встановлюємо кінцеву прозорість елементу
                                            notificationLabel.setOpacity(1);

                                            // викликаємо анімацію зникнення через 3 секунди
                                            fadeOut.setDelay(Duration.seconds(3));

                                            fadeOut.play();
                                        }
                                    }
                                }

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    Button chooseButton = new Button(music.getPerformer() + " - " + music.getTitle());
                    chooseButton.setPrefSize(500.0,45.0);
                    chooseButton.setCursor(Cursor.HAND);
                    chooseButton.setStyle("-fx-background-color: #202020; -fx-background-radius:0; -fx-background-insets: 0;");
                    chooseButton.setTextFill(Paint.valueOf("WHITE"));

                    chooseButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            chooseButton.setStyle("-fx-background-color: #242424; -fx-background-radius: 0; -fx-background-insets: 0;");
                        }
                    });
                    chooseButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            chooseButton.setStyle("-fx-background-color: #202020; -fx-background-radius: 0; -fx-background-insets: 0;");
                        }
                    });

                    chooseButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            try {
                                // Зупинка поточного медіаплеєра, якщо він є
                                if (currentMediaPlayer != null) {
                                    currentMediaPlayer.stop();
                                }

                                // Завантаження аудіофайлу у форматі байтів
                                byte[] audioBytes = musicService.getMusicInBytes(music.getId());

                                // Створення тимчасового файлу
                                File tempFile = File.createTempFile("music", ".mp3");
                                tempFile.deleteOnExit();

                                // Запис масиву байтів у тимчасовий файл
                                FileOutputStream fos = new FileOutputStream(tempFile);
                                fos.write(audioBytes);
                                fos.close();

                                // Створення медіа з тимчасового файлу
                                Media media = new Media(tempFile.toURI().toString());

                                // Створення медіаплеєра
                                currentMediaPlayer = new MediaPlayer(media);

                                currentMediaPlayer.play();

                                // Зберігання посилання на поточний код та назву музики
                                currentMusicIndex = playlist.indexOf(music);
                                currentMusicTitle = music.getTitle() + " - " + music.getPerformer();

                                musicTitleLabel.setText(currentMusicTitle);

                                playBtn.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        currentMediaPlayer.play();
                                    }
                                });

                                pauseBtn.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        currentMediaPlayer.pause();
                                    }
                                });

                                resetBtn.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        currentMediaPlayer.seek(Duration.seconds(0));
                                    }
                                });

                                nextBtn.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        currentMediaPlayer.stop();

                                        if (currentMusicIndex++ > playlist.size()) {
                                            currentMusicIndex = 0;
                                        } else {
                                            currentMusicIndex++;
                                        }

                                        try {
                                            byte[] bytes = musicService.getMusicInBytes(playlist.get(currentMusicIndex).getId());

                                            File tempFile = File.createTempFile("music", ".mp3");
                                            tempFile.deleteOnExit();
                                            FileOutputStream fos = new FileOutputStream(tempFile);
                                            fos.write(bytes);
                                            fos.close();

                                            Media media = new Media(tempFile.toURI().toString());
                                            currentMediaPlayer = new MediaPlayer(media);
                                            currentMediaPlayer.play();

                                            musicTitleLabel.setText(playlist.get(currentMusicIndex).getPerformer() + playlist.get(currentMusicIndex).getTitle());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                                previousBtn.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        currentMediaPlayer.stop();

                                        if (currentMusicIndex-- < 0) {
                                            currentMusicIndex = playlist.size()-1;
                                        } else {
                                            currentMusicIndex--;
                                        }

                                        try {
                                            byte[] bytes = musicService.getMusicInBytes(playlist.get(currentMusicIndex).getId());

                                            File tempFile = File.createTempFile("music", ".mp3");
                                            tempFile.deleteOnExit();
                                            FileOutputStream fos = new FileOutputStream(tempFile);
                                            fos.write(bytes);
                                            fos.close();

                                            Media media = new Media(tempFile.toURI().toString());
                                            currentMediaPlayer = new MediaPlayer(media);
                                            currentMediaPlayer.play();

                                            musicTitleLabel.setText(playlist.get(currentMusicIndex).getPerformer() + playlist.get(currentMusicIndex).getTitle());
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    Button likeButton = new Button("♥");
                    likeButton.setPrefSize(50.0,45.0);
                    likeButton.setCursor(Cursor.HAND);
                    likeButton.setStyle("-fx-background-color: #202020; -fx-background-radius: 0 5 5 0; -fx-background-insets: 0;");
                    likeButton.setTextFill(Paint.valueOf("#338aff"));
                    likeButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            likeButton.setStyle("-fx-background-color: #242424; -fx-background-radius: 0 5 5 0; -fx-background-insets: 0;");
                        }
                    });
                    likeButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            likeButton.setStyle("-fx-background-color: #202020; -fx-background-radius: 0 5 5 0; -fx-background-insets: 0;");
                        }
                    });

                    likeButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            try {
                                String removeResult = musicService.removeMusicFromLiked(music.getId(), user.getId());

                                if (removeResult.equals("Song was removed from Liked List")) {
                                    likeButton.setTextFill(Paint.valueOf("WHITE"));
                                }

                                notificationLabel.setText(removeResult);

                                FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), notificationLabel);
                                fadeIn.setToValue(1);
                                fadeIn.play();
                                FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), notificationLabel);
                                fadeOut.setToValue(0);
                                notificationLabel.setOpacity(1);
                                fadeOut.setDelay(Duration.seconds(3));
                                fadeOut.play();

                                playlist.remove(music);

                                addToAlbumButton.setVisible(false);
                                chooseButton.setVisible(false);
                                likeButton.setVisible(false);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    songBoxInner.getChildren().addAll(addToAlbumButton, chooseButton, likeButton);
                    songBox.getChildren().add(songBoxInner);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        findButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String searchString = searchField.getText();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("searchInLiked-result.fxml"));
                try {
                    Parent root = loader.load();
                    LikedResultController controller = loader.getController();
                    controller.setSearchString(searchString);
                    controller.setUser(user);

                    Scene scene = findButton.getScene();
                    scene.setRoot(root);

                    Platform.runLater(controller::initializeSearchString);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (currentMediaPlayer != null) currentMediaPlayer.stop();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("search.fxml"));
                    Parent root = loader.load();

                    SearchController searchController = loader.getController();
                    searchController.setUser(user);

                    Scene scene = searchButton.getScene();
                    scene.setRoot(root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        searchButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                searchButton.setTextFill(Paint.valueOf("#338aff"));
                searchButton.setUnderline(true);
            }
        });
        searchButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                searchButton.setTextFill(Paint.valueOf("WHITE"));
                searchButton.setUnderline(false);
            }
        });

        yourAlbumsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (currentMediaPlayer != null) currentMediaPlayer.stop();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("your-albums.fxml"));
                    Parent root = loader.load();

                    YourAlbumsController yourAlbumsController = loader.getController();
                    yourAlbumsController.setUser(user);

                    Scene scene = yourAlbumsButton.getScene();
                    scene.setRoot(root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
                if (currentMediaPlayer != null) {
                    currentMediaPlayer.stop();
                    currentMediaPlayer = null;
                }
                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-form.fxml")));
                    Stage loginWindow = (Stage) logoutButton.getScene().getWindow();
                    loginWindow.setScene(new Scene(root));
                    loginWindow.centerOnScreen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
