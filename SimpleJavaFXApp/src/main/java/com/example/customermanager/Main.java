package com.example.customermanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Customer Manager App Running!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // This automatically kicks off the start() method above
    }
}