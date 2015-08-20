package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by MC on 8/18/2015.
 */
public class ScraperGUI extends Application implements Observer {

    public Scraper scraper;
    boolean searched = false;
    boolean canSpeak = false;
    boolean canDownload = false;
    TextArea article;

    @Override
    public void init(){
        scraper = new Scraper();
        scraper.addObserver(this);
    }

    @Override
    public void start(Stage stage){

        BorderPane main = new BorderPane();
        HBox buttons = new HBox();
        article = new TextArea();
        article.setEditable(false);
        HBox searchBox = new HBox();
        searchBox.setMaxWidth(Double.MAX_VALUE);
        Button speak = new Button();
        Button download = new Button();
        TextField search = new TextField();
        Label searchLabel = new Label();

        search.setMaxWidth(Double.MAX_VALUE);
        searchLabel.setText("URL: ");
        speak.setText("    Speak   ");
        speak.setAlignment(Pos.BOTTOM_CENTER);
        speak.setMaxWidth(Double.MAX_VALUE);
        download.setText("Download");
        download.setAlignment(Pos.BOTTOM_CENTER);
        download.setMaxWidth(Double.MAX_VALUE);

        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searched = true;
                scraper.getArticle(search.getText());
            }
        });

        speak.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canSpeak = true;
                scraper.checkSpeak();
            }
        });

        download.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canDownload = true;
                scraper.checkDownload();
            }
        });

        buttons.setSpacing(5);
        buttons.getChildren().addAll(speak, download);
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        searchBox.setSpacing(5);
        searchBox.setHgrow(search, Priority.ALWAYS);
        searchBox.getChildren().addAll(searchLabel, search);

        main.setCenter(article);
        main.setBottom(buttons);
        main.setTop(searchBox);

        stage.setWidth(500);
        stage.setHeight(500);
        stage.setTitle("Article2Audio");
        stage.setScene(new Scene(main));
        stage.show();

    }

    @Override
    public void update(Observable o, Object arg ){
        if(searched){
            article.setText(arg.toString());
            searched = false;
        }
        if(canSpeak){
            scraper.speak(article.getText());
            canSpeak = false;
        }
        if(canDownload){
            scraper.writeToFile(article.getText());
            canDownload = false;
        }
    }

    public static void main(String args[]){
        Application.launch(args);
    }

}
