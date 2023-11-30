package com.musicplayer.music_player;

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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private AnchorPane ap;
    Stage stage;
    @FXML
    Label currentSongLabel, nextSongLabel;
    @FXML
    private TableView<Song> songsTable;
    @FXML
    private TableColumn<Song, String> titleColumn, artistColumn, albumColumn;
    @FXML
    private TableColumn<Song, Integer> orderColumn;
    @FXML
    private Integer songCount;
    @FXML
    Media media;
    @FXML
    MediaPlayer mediaPlayer;
    @FXML
    ArrayList<Song> songQueue;
    @FXML
    int currentSongIndex;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songQueue = new ArrayList<>();
        songCount = 1; // used for displaying the order of songs to play in the table
        currentSongLabel.setText("Current Song: ");
        nextSongLabel.setText("Next Song: ");

        /* set cell factory is for the table view. it puts items in different columns depending on the property
        * of the song class */
        titleColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("album"));
        orderColumn.setCellValueFactory(new PropertyValueFactory<Song, Integer>("order"));

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
        songsTable.setItems(songs);

        // update the song counter to show the order of songs playing
        songCount++;
    }
    public void playSong(){
        media = new Media(songQueue.get(currentSongIndex).getFile().toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        currentSongLabel.setText("Current Song: "+ songQueue.get(currentSongIndex).getArtist() + " - " + songQueue.get(currentSongIndex).getTitle());
        if (songQueue.get(currentSongIndex+1)!=null){
            currentSongIndex++;
            nextSongLabel.setText("Next Song: "+ songQueue.get(currentSongIndex).getArtist() + " - " + songQueue.get(currentSongIndex).getTitle());
        } else {
            nextSongLabel.setText("Next Song: ");
            currentSongIndex-=1;
        }
    }
}