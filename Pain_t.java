package com.example.paint;
import javafx.application.Application;
import javafx.stage.Stage;

public class Pain_t extends Application{

    @Override
    public void start(Stage stage) {
        /*
         TODO Auto-generated method stub
            -Blank canvas upon startup (DONE)
            -Clear canvas option w/ "are you sure?" check
            -Additional shape of my choice (DONE)
            -Eraser tool
            -Draw regular poly with n sides
            -Copy and paste piece of image
            -Select and move piece of image
            -Allow text with user-typed input
            -Undo and Redo using stack

            NOTES:
            Replace accordion with just icons at some point
            When closing image, ask if you want to save, and then clear canvas and open next image
            Add option to change image size
            Figure out how to show slightly transparent version of shape when dragging mouse
            Currently, opening new image deletes previous tabs. Need to save. Also need to close only active tab on exit
            To do above, make new canvas each time a new tab is opened.
        */

        //Create paint window
        Paint_mainWindow mainWindow = new Paint_mainWindow(stage);
    }
}

