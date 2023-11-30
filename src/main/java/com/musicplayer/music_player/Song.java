package com.musicplayer.music_player;

import java.io.File;

public class Song {
    private File file;
    private String title;
    private String artist;
    private String album;
    private Integer order;

    public Song(File audioFile,String[] metadata,Integer orderNo){
        this.file = audioFile;
        this.title = metadata[0];
        this.artist = metadata[1];
        this.album = metadata[2];
        this.order = orderNo;
    }

    /*
    a number of getter methods needed for the setcellfactory methods to work
    */
    public File getFile(){
        return file;
    }
    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }
    public String getAlbum(){
        return album;
    }
    public Integer getOrder(){
        return order;
    }
}
