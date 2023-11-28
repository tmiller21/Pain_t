package com.example.paint;
import javafx.application.Application;
import javafx.stage.Stage;

public class Pain_t extends Application{

    @Override
    public void start(Stage stage) {
        //Create paint window
        Paint_mainWindow mainWindow = new Paint_mainWindow(stage);
    }
}

