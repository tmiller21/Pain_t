package com.example.paint;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Paint_POPUPcropCanvas {

    double imageWidth,imageHeight;

    public Paint_POPUPcropCanvas(Canvas drawingCanvas){
        //Create new stage and scene for text field
        Stage cropCanvasStage = new Stage();
        //Make hbox for labels size by size
        HBox fieldLabelBox = new HBox();
        Text widthText = new Text("Width");
        Text heightText = new Text("Height");
        fieldLabelBox.getChildren().addAll(widthText, heightText);
        //Center and space text
        fieldLabelBox.setSpacing(70);
        fieldLabelBox.setAlignment(Pos.CENTER);

        //Make hbox for fields side by side
        HBox fieldBox = new HBox();
        //Create text fields
        TextField widthField = new TextField(Double.toString(drawingCanvas.getWidth()));
        //Make x to go between height and width
        Text fieldX = new Text("X");
        TextField heightField = new TextField(Double.toString(drawingCanvas.getHeight()));
        //Space out box
        fieldBox.setSpacing(10);
        fieldBox.setPadding(new Insets(10,10,10,10));
        //Add text fields to hbox
        fieldBox.getChildren().addAll(widthField, fieldX, heightField);
        fieldBox.setAlignment(Pos.CENTER);

        //Make resize button
        Button resizeButton = new Button("Resize");
        //Make button resize image to what is in fields when it is a double
        resizeButton.setOnAction(evenhanded ->{
            try{
                imageWidth = Double.parseDouble(widthField.getText());
                imageHeight = Double.parseDouble(heightField.getText());
                //before setting new width and height, check if canvas is growing
                if(imageWidth>drawingCanvas.getHeight()){
                    //if width is growing, fill in width
                }
                if(imageHeight>drawingCanvas.getHeight()){
                    //if height is growing, fill in height
                }
                //set canvas width and height
                drawingCanvas.setWidth(imageWidth);
                drawingCanvas.setHeight(imageHeight);
                //fill in area added to photo
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        });

        //Vertical box for resize fields and resize button
        VBox resizeBox = new VBox();
        resizeBox.getChildren().addAll(fieldLabelBox, fieldBox, resizeButton);
        resizeBox.setAlignment(Pos.CENTER);

        Scene stageScene = new Scene(resizeBox, 200, 100);
        //Set title and resizeable and show
        cropCanvasStage.setTitle("Resize Canvas");
        cropCanvasStage.setResizable(false);
        cropCanvasStage.setScene(stageScene);
        cropCanvasStage.show();
    }
}
