package com.example.paint;
import javafx.application.Application;
import javafx.stage.Stage;

public class Pain_t extends Application{

    @Override
    public void start(Stage stage) {
        /*
         TODO Auto-generated method stub
            -Blank canvas upon startup (DONE)
            -Clear canvas option w/ "are you sure?" check (DONE)
            -Additional shape of my choice (DONE)
            -Eraser tool (DONE)
            -Draw regular poly with n sides (DONE)
            -Copy and paste piece of image (DONE)
            -Select and move piece of image (DONE)
            -Allow text with user-typed input (DONE - needs more options)
            -UNDO AND REDO USING STACK(S) (DONE - degrades image)
            -TABS (CAN DRAW ON BOTH AND OPEN NEW IMAGE, NEED MENU FUNCTIONS TO WORK ON BOTH)
            -BONUS: rounded square and rect, blank on new, canvas fills when growing

            NOTES:
            Replace accordion with just icons at some point
            When closing image, ask if you want to save, and then clear canvas and open next image
            Add option to change image size
            Figure out how to show slightly transparent version of shape when dragging mouse
            Currently, opening new image deletes previous tabs. Need to save. Also need to close only active tab on exit
            To do above, make new canvas each time a new tab is opened.
            Fill extended area with white when extending canvas
        */

        //Create paint window
        Paint_mainWindow mainWindow = new Paint_mainWindow(stage);
    }
}

