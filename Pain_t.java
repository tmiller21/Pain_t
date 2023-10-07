package com.example.paint;
import javafx.application.Application;
import javafx.stage.Stage;

public class Pain_t extends Application{

    @Override
    public void start(Stage stage) {
        /*
         TODO Auto-generated method stub
            -At least 3 unit tests (DONE)
            -Timer that allows for autosave (threading recommended) (DONE)
            -Timer is opotionally visible to user (DONE) (optional - reset timer on user save (DONE))
            -JavaDoc commenting (DONE)
            -Save in alternative format, warn for losses (DONE)
            -(In deliverables) prove data loss can happen (DONE - explain SVGs)

            NOTES:
            Add more options for text input
            Figure out how to show slightly transparent version of shape when dragging mouse
            Fill extended area with white when extending canvas
        */

        //Create paint window
        Paint_mainWindow mainWindow = new Paint_mainWindow(stage);
    }
}

