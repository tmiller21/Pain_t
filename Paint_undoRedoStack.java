package com.example.paint;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.Stack;

public class Paint_undoRedoStack {

    Stack<Image> undoStack = new Stack<>();
    Stack<Image> redoStack = new Stack<>();

    Canvas tempCanvas = new Canvas();

    public Paint_undoRedoStack() {
    }

    //when undo canvas is pushed, take snapshot of canvas
    public void pushUndoCanvas(Paint_drawingCanvas undoCanvas){
        System.out.println("undo Canvas pushed");
        WritableImage undoImage = new WritableImage(undoCanvas.snapshot(null,null).getPixelReader(), 0,0,(int)undoCanvas.getWidth(),(int)undoCanvas.getHeight());
        undoStack.push(undoImage);
    }

    //when redo canvas is pushed, take snapshot of canvas
    public void pushRedoCanvas(Paint_drawingCanvas redoCanvas){
        System.out.println("redo Canvas pushed");
        WritableImage redoImage = new WritableImage(redoCanvas.snapshot(null,null).getPixelReader(), 0,0,(int)redoCanvas.getWidth(),(int)redoCanvas.getHeight());
        redoStack.push(redoImage);
    }

    public Image popCanvas(){
        return undoStack.pop();
    }

    public Image getUndoCanvas(){
        return undoStack.pop();
    }

    public Image getRedoCanvas(){
        return redoStack.pop();
    }

}
