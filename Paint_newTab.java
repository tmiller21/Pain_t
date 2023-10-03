package com.example.paint;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Paint_newTab extends Tab {

    Paint_drawingCanvas drawingCanvas = new Paint_drawingCanvas();
    ScrollPane workspace = new ScrollPane();
    int tabIndex;

    public Paint_newTab(String title){
        //set title to argument
        this.setText(title);

        //workspace can pan, fits image to its height, and only shows scroll bars as needed
        workspace.setPannable(false);
        workspace.setFitToHeight(true);
        workspace.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        workspace.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public Paint_drawingCanvas getCanvas(){return drawingCanvas;}

    public void setWorkspace(ScrollPane newWorkspace) {
        workspace = newWorkspace;
        super.setContent(workspace);
    }

    public void setCanvas(Paint_drawingCanvas newCanvas){
        drawingCanvas = newCanvas;
        workspace.setContent(drawingCanvas);
    }

    public void setCanvasWidthAndHeight(double width, double height){
        drawingCanvas.setWidth(width);
        drawingCanvas.setHeight(height);
    }

    public void setTabIndex(int index){tabIndex = index;}

    public int getTabIndex(){return tabIndex;}

}
