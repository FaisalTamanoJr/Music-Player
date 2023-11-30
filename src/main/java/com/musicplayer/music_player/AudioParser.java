package com.musicplayer.music_player;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AudioParser {
    /*
    What this method does:
    It takes a song or an mp3 file as an input, then gets its title, artist, and album name and returns it as an
    array of strings.
     */
    public static String[] getMetadata(File audioFile) throws TikaException, IOException, SAXException {
        InputStream input = new FileInputStream(audioFile);
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new Mp3Parser();
        ParseContext parseCtx = new ParseContext();
        parser.parse(input, handler, metadata, parseCtx);
        input.close();
        return new String[]{metadata.get("title"),metadata.get("xmpDM:artist"),metadata.get("xmpDM:album")};
    }
}
