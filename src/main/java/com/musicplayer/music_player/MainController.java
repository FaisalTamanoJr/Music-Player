package com.musicplayer.music_player;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    @FXML private AnchorPane ap;
    private Stage stage;
    @FXML
    private Label currentSongLabel;
    @FXML
    private TableView<Song> songsTable;
    @FXML
    private TableColumn<Song, String> titleColumn, artistColumn, albumColumn;
    @FXML
    private TableColumn<Song, Integer> orderColumn;
    @FXML
    private Integer songCount;
    @FXML
    private Media media;
    @FXML
    private Slider volume_slider;
    @FXML
    private MediaPlayer mediaPlayer;
    @FXML
    private Deque<Song> songQueue;
    @FXML
    private Stack<Song> songPrevious;
    @FXML
    private Timer timer;
    @FXML
    TimerTask task;
    private boolean running;
    @FXML
    private ProgressBar song_progressBar;
    @FXML
    private Integer shuffle;
    @FXML
    private ArrayList<Song> shuffleList;
    @FXML
    private Deque<Song> shuffleQueue;
    @FXML
    private Stack<Song> shufflePrevious;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shuffle = -1;
        songQueue = new ArrayDeque<>();
        songPrevious = new Stack<>();
        shuffleList = new ArrayList<>();
        shuffleQueue = new ArrayDeque<>();
        shufflePrevious = new Stack<>();
        songCount = 1; // used for displaying the order of songs to play in the table
        currentSongLabel.setText("Current Song: ");

        /* set cell factory is for the table view. it puts items in different columns depending on the property
        * of the song class */
        titleColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("album"));
        orderColumn.setCellValueFactory(new PropertyValueFactory<Song, Integer>("order"));

        volume_slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volume_slider.getValue()*0.01);
            }
        });
        song_progressBar.setStyle("-fx-accent: #00FF00");
    }
    public void addSong() throws TikaException, IOException, SAXException {
        // for opening and selecting an mp3 file
        stage = (Stage) ap.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // for adding a song to the table
        Song song = new Song(selectedFile, AudioParser.getMetadata(selectedFile),songCount);
        ObservableList<Song> songs = songsTable.getItems();
        songs.add(song);
        songQueue.add(song);
        shuffleList.add(song);
        songsTable.setItems(songs);

        // update the song counter to show the order of songs playing
        songCount++;
    }
    public void playSong(){
        beginTimer();
        media = new Media(songQueue.peek().getFile().toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        if (shuffle == -1) {
            mediaPlayer.play();
            currentSongLabel.setText("Current Song: " + songQueue.peek().getArtist() + " - " + songQueue.peek().getTitle());
        } else {
            media = new Media(shuffleQueue.peek().getFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            currentSongLabel.setText("Current Song: " + shuffleQueue.peek().getArtist() + " - " + shuffleQueue.peek().getTitle());
        }
    }
    public void stopSong(){
        cancelTimer();
        mediaPlayer.pause();
    }
    public void nextSong(){
        if (running){
            cancelTimer();
        }
        mediaPlayer.stop();
        if (shuffle == -1){
            songPrevious.push(songQueue.pop());
        } else {
            shufflePrevious.push(shuffleQueue.pop());
        }
        playSong();
    }
    public void previousSong(){
        mediaPlayer.stop();
        if (shuffle == -1){
            songQueue.addFirst(songPrevious.pop());
        } else {
            shuffleQueue.addFirst(shufflePrevious.pop());
        }
        playSong();
    }

    public void beginTimer(){
            timer = new Timer();
            task = new TimerTask() {
                public void run() {
                    running = true;
                    double current = 0;
                    current = mediaPlayer.getCurrentTime().toSeconds();
                    double end = media.getDuration().toSeconds();
                    song_progressBar.setProgress(current / end);

                    if (current / end == 1) {
                        cancelTimer();
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 1000, 1000);
    }
    public void cancelTimer(){
        running = false;
        timer.cancel();
    }
    public void shuffle(){
        if (this.mediaPlayer != null){
            mediaPlayer.stop();
        }
        shuffle *= -1;
        if (shuffle == 1){
            Collections.shuffle(shuffleList);
            shuffleQueue.addAll(shuffleList);
        }
    }
}