package com.artsolo.musicplayer;

import javafx.animation.FadeTransition;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class YourAlbumsController implements Initializable {

    @FXML
    private Button likedSongsButton, yourAlbumsButton, searchButton, logoutButton, playBtn, pauseBtn, resetBtn, nextBtn, previousBtn, newAlbumButton, removeAlbumButton;

    @FXML
    private Label musicTitleLabel, notificationLabel;

    @FXML
    private HBox albumBox;

    @FXML
    private VBox songBox;

    private MediaPlayer currentMediaPlayer = null;
    private String currentMusicId = null, currentMusicTitle = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<String> playlist = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.valueOf("#338aff"));

        try {
            MusicService musicService = MusicServiceSingleton.getInstance().getMusicService();
            String[][] albumData = musicService.getAlbum();

            for (int i = 0; i < albumData.length; i++) {
                String albumId = albumData[i][0];
                String albumTitle = albumData[i][1];

                Button albumButton = new Button(albumTitle);
                albumButton.setPrefSize(100.0,93.0);
                albumButton.setCursor(Cursor.HAND);
                albumButton.setStyle("-fx-background-color: #202020; -fx-background-radius: 5; -fx-background-insets: 0;");
                albumButton.setWrapText(true);
                albumButton.setTextAlignment(TextAlignment.CENTER);
                albumButton.setTextFill(Paint.valueOf("WHITE"));
                albumButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {

                            if (currentMediaPlayer != null) {
                                currentMediaPlayer.stop();
                                playlist.removeAll(playlist);
                                titles.removeAll(titles);
                            }

                            songBox.getChildren().clear();

                            MusicService musicService = MusicServiceSingleton.getInstance().getMusicService();
                            String[][] musicData = musicService.getAlbumMusic(albumId);

                            for (int i = 0; i < musicData.length; i++) {
                                String musicId = musicData[i][0];
                                String musicTitle = musicData[i][1];
                                String musicPerformer = musicData[i][2];

                                playlist.add(musicId);
                                titles.add(musicTitle + " - " + musicPerformer);

                                HBox songBoxInner = new HBox();
                                songBoxInner.setPrefHeight(45.0);
                                songBoxInner.setPrefWidth(600.0);
                                songBoxInner.setStyle("-fx-padding: 2;");

                                Button chooseButton = new Button(musicPerformer + " - " + musicTitle);
                                chooseButton.setPrefSize(550.0,45.0);
                                chooseButton.setCursor(Cursor.HAND);
                                chooseButton.setStyle("-fx-background-color: #202020; -fx-background-radius: 5 0 0 5; -fx-background-insets: 0;");
                                chooseButton.setTextFill(Paint.valueOf("WHITE"));
                                chooseButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        chooseButton.setStyle("-fx-background-color: #242424; -fx-background-radius: 5 0 0 5; -fx-background-insets: 0;");
                                    }
                                });
                                chooseButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        chooseButton.setStyle("-fx-background-color: #202020; -fx-background-radius: 5 0 0 5; -fx-background-insets: 0;");
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
                                            byte[] audioBytes = musicService.getMusicData(musicId);

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
                                            currentMusicId = musicId;
                                            currentMusicTitle = musicTitle + " - " + musicPerformer;

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

                                                    int currentIndex = playlist.indexOf(currentMusicId);
                                                    currentIndex++;
                                                    if (currentIndex == playlist.size()) {
                                                        currentIndex = 0;
                                                    }
                                                    currentMusicId = playlist.get(currentIndex);

                                                    try {
                                                        byte[] bytes = musicService.getMusicData(currentMusicId);

                                                        // Створення тимчасового файлу
                                                        File tempFile = File.createTempFile("music", ".mp3");
                                                        tempFile.deleteOnExit();

                                                        // Запис масиву байтів у тимчасовий файл
                                                        FileOutputStream fos = new FileOutputStream(tempFile);
                                                        fos.write(bytes);
                                                        fos.close();

                                                        // Створення медіа з тимчасового файлу
                                                        Media media = new Media(tempFile.toURI().toString());

                                                        // Створення медіаплеєра
                                                        currentMediaPlayer = new MediaPlayer(media);

                                                        currentMediaPlayer.play();

                                                        currentIndex = titles.indexOf(currentMusicTitle);
                                                        currentIndex++;
                                                        if (currentIndex == titles.size()) {
                                                            currentIndex = 0;
                                                        }
                                                        currentMusicTitle = titles.get(currentIndex);
                                                        musicTitleLabel.setText(currentMusicTitle);

                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });

                                            previousBtn.setOnAction(new EventHandler<ActionEvent>() {
                                                @Override
                                                public void handle(ActionEvent actionEvent) {
                                                    currentMediaPlayer.stop();

                                                    int currentIndex = playlist.indexOf(currentMusicId);
                                                    currentIndex--;
                                                    if (currentIndex < 0) {
                                                        currentIndex = playlist.size()-1;
                                                    }
                                                    currentMusicId = playlist.get(currentIndex);

                                                    try {
                                                        byte[] bytes = musicService.getMusicData(currentMusicId);

                                                        // Створення тимчасового файлу
                                                        File tempFile = File.createTempFile("music", ".mp3");
                                                        tempFile.deleteOnExit();

                                                        // Запис масиву байтів у тимчасовий файл
                                                        FileOutputStream fos = new FileOutputStream(tempFile);
                                                        fos.write(bytes);
                                                        fos.close();

                                                        // Створення медіа з тимчасового файлу
                                                        Media media = new Media(tempFile.toURI().toString());

                                                        currentMediaPlayer = new MediaPlayer(media);

                                                        currentMediaPlayer.play();

                                                        currentIndex = titles.indexOf(currentMusicTitle);
                                                        currentIndex--;
                                                        if (currentIndex < 0) {
                                                            currentIndex = playlist.size()-1;
                                                        }
                                                        currentMusicTitle = titles.get(currentIndex);
                                                        musicTitleLabel.setText(currentMusicTitle);

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

                                Button removeFromAlbumButton = new Button("-");
                                removeFromAlbumButton.setPrefSize(50.0,45.0);
                                removeFromAlbumButton.setCursor(Cursor.HAND);
                                removeFromAlbumButton.setStyle("-fx-background-color: #202020; -fx-background-radius: 0 5 5 0; -fx-background-insets: 0;");
                                removeFromAlbumButton.setTextFill(Paint.valueOf("343434"));
                                removeFromAlbumButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        removeFromAlbumButton.setStyle("-fx-background-color: #242424; -fx-background-radius: 0 5 5 0; -fx-background-insets: 0;");
                                    }
                                });
                                removeFromAlbumButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        removeFromAlbumButton.setStyle("-fx-background-color: #202020; -fx-background-radius: 0 5 5 0; -fx-background-insets: 0;");
                                    }
                                });
                                removeFromAlbumButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        try {
                                            String removeResult = musicService.removeFromAlbum(albumId, musicId);

                                            notificationLabel.setText(removeResult);

                                            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), notificationLabel);
                                            fadeIn.setToValue(1); // встановлюємо кінцеву прозорість елементу
                                            fadeIn.play(); // запускаємо анімацію появи

                                            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), notificationLabel);
                                            fadeOut.setToValue(0); // встановлюємо кінцеву прозорість елементу
                                            notificationLabel.setOpacity(1);

                                            // викликаємо анімацію зникнення через 3 секунди
                                            fadeOut.setDelay(Duration.seconds(3));

                                            fadeOut.play();

                                            playlist.remove(musicId);
                                            titles.remove(musicTitle + " - " + musicPerformer);

                                            removeFromAlbumButton.setVisible(false);
                                            chooseButton.setVisible(false);

                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                songBoxInner.getChildren().addAll(chooseButton, removeFromAlbumButton);
                                songBox.getChildren().add(songBoxInner);
                            }

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
                albumButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        albumButton.setStyle("-fx-background-color: #242424; -fx-background-radius: 5; -fx-background-insets: 0;");
                        albumButton.setEffect(dropShadow);
                    }
                });
                albumButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        albumButton.setStyle("-fx-background-color: #202020; -fx-background-radius: 5; -fx-background-insets: 0;");
                        albumButton.setEffect(null);
                    }
                });

                albumBox.getChildren().addAll(albumButton);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        newAlbumButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Create new album");
                dialog.setHeaderText("Enter your album title:");
                dialog.setContentText("Title:");

                Optional<String> newAlbumTitle = dialog.showAndWait();
                if (newAlbumTitle.isPresent()){
                    try {
                        MusicService musicService = MusicServiceSingleton.getInstance().getMusicService();
                        boolean creatingResult = musicService.createNewAlbum(newAlbumTitle.get());

                        if (creatingResult) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("your-albums.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Scene scene = newAlbumButton.getScene();
                            scene.setRoot(root);
                        } else {
                            notificationLabel.setText("Something went wrong");

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

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        newAlbumButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                newAlbumButton.setStyle("-fx-border-radius: 5; -fx-background-color: #338aff;");
            }
        });
        newAlbumButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                newAlbumButton.setStyle("-fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: transparent; -fx-border-color: #338aff;");
            }
        });

        removeAlbumButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                List<String> albumsList = new ArrayList<>();

                try {
                    MusicService musicService = MusicServiceSingleton.getInstance().getMusicService();
                    String[][] albumsData = musicService.getAlbum();

                    if (albumsData.length == 0) {
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
                        for (int i = 0; i < albumsData.length; i++) {
                            String albumTitle = albumsData[i][1];
                            albumsList.add(albumTitle);
                        }

                        ChoiceDialog<String> dialog = new ChoiceDialog<>(albumsList.get(0), albumsList);
                        dialog.setTitle("Remove album");
                        dialog.setHeaderText("Select the album you want to remove");
                        dialog.setContentText("Album:");

                        Optional<String> result = dialog.showAndWait();
                        if (result.isPresent()) {

                            String selectedAlbum = result.get();
                            String albumId = "";

                            for (String[] album : albumsData) {
                                if (album[1].equals(selectedAlbum)) {
                                    albumId = album[0];
                                    break;
                                }
                            }

                            boolean removingResult = musicService.removeAlbum(albumId);

                            if (removingResult) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("your-albums.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene scene = newAlbumButton.getScene();
                                scene.setRoot(root);
                            } else {
                                notificationLabel.setText("Something went wrong");

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
        removeAlbumButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                removeAlbumButton.setStyle("-fx-border-radius: 5; -fx-background-color: #ff0000;");
            }
        });
        removeAlbumButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                removeAlbumButton.setStyle("-fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: transparent; -fx-border-color: #ff0000;");
            }
        });

        likedSongsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (currentMediaPlayer != null) {
                    currentMediaPlayer.stop();
                }

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

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (currentMediaPlayer != null) {
                    currentMediaPlayer.stop();
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("search.fxml"));
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

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root = null;
                if (currentMediaPlayer != null) {
                    currentMediaPlayer.stop();
                    currentMediaPlayer = null;
                }
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
}
