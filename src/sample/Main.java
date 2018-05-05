package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        BorderPane borderPane = new BorderPane();

        primaryStage.setTitle("Синтезатор мови");
        primaryStage.setScene(new Scene(borderPane, 400, 300));
        primaryStage.show();

        Button sendButton = new Button("Відтворити");
        TextArea text = new TextArea();

        Label label = new Label("Введіть текст:");
        FlowPane flowPane = new FlowPane(label,sendButton);
        label.setMinWidth(330);
        borderPane.setCenter(text);
        borderPane.setTop(flowPane);

        sendButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Solver solver = new Solver(text.getText());
                try {
                    solver.createResult();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

}
