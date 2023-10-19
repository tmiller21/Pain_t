package com.example.paint;
import javafx.application.Application;
import javafx.stage.Stage;

public class Pain_t extends Application{

    @Override
    public void start(Stage stage) {
        /*
         TODO Auto-generated method stub
            -rotate whole image (DONE) or selection (DONE)
            -mirror whole image horiz & vert (DONE)
            -icons (DONE)
            -notifications w show/hide ("for autosave - trigger or on off") (DONE)
            -tool tips (DONE)
            -threaded logging (just edit text file) (DONE)

            NOTES:
            Add more options for text input
            Figure out how to show slightly transparent version of shape when dragging mouse
            Fill extended area with white when extending canvas
            Account for rotation/mirroring when drawing/placing text
            Ensure mirrored/rotated image is also getting pushed to undo/redo stack
            Copy and cut once canvas is mirrored needs to work right
            FILE FUNCTIONS ON ALL CANVASES (TABS)
            Add option for autosave notification to be shown or not
        */

        //Create paint window
        Paint_mainWindow mainWindow = new Paint_mainWindow(stage);
    }
}

