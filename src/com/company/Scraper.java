package com.company;
import com.sun.speech.freetts.*;
import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.sound.sampled.AudioFileFormat;
import java.util.Observable;

public class Scraper extends Observable {

    AudioPlayer player = null;
    String mainArticle;

    public void getArticle(String URL){
        String title;
        mainArticle = "";
        try {
            Document doc = Jsoup.connect(URL).get();
            title = doc.title();
            System.out.println(title);
            Elements article = doc.select("div.story-body");
            for(Element body : article.select("p.story-body-text.story-content")){
                mainArticle += ("\n" + body.text());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(mainArticle);
        setChanged();
        notifyObservers(mainArticle);
    }

    public void speak(String article){
        String VOICENAME = "kevin16";
        Voice voice;
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice(VOICENAME);

        voice.allocate();

        try{
            voice.speak(article);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Couldn't speak");
        }
    }

    public void checkSpeak(){
        if(mainArticle != ""){
            setChanged();
            notifyObservers();
        }
    }

    public void checkDownload(){
        if(mainArticle != ""){
            setChanged();
            notifyObservers();
        }
    }

    public void writeToFile(String article){
        player = new SingleFileAudioPlayer("C://Users//MC//Desktop//Text2Audio/ArticleAudio", AudioFileFormat.Type.WAVE);
        String VOICENAME = "kevin16";
        Voice voice;
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice(VOICENAME);

        voice.allocate();
        voice.setAudioPlayer(player);

        try{
            voice.speak(article);
            voice.deallocate();
            player.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Couldn't speak");
        }

    }
}


