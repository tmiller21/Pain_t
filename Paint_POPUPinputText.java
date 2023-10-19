package com.example.paint;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Paint_POPUPinputText {

    public Paint_POPUPinputText(double x1, double y1, GraphicsContext gc){
        //create stage
        Stage textInputStage = new Stage();

        //VBox for all items
        VBox textInputPane = new VBox();

        //vbox for label, text field, and buttons
        Label inputTextLabel = new Label("Input Text:");

        //make text field
        TextField textInputField = new TextField();

        //Make hbox for yes and no buttons
        HBox clearYesNoBox = new HBox();
        //Create buttons
        Button textContinueButton = new Button("Continue");
        textContinueButton.setOnAction(eventHandler1 -> {
            gc.setLineWidth(1);
            gc.strokeText(textInputField.getText(),x1,y1);
            textInputStage.close();
        });
        Button textCancelButton = new Button("Cancel");
        textCancelButton.setOnAction(eventHandler2 -> {
            textInputStage.close();
        });
        //Space out box
        clearYesNoBox.setSpacing(10);
        clearYesNoBox.setPadding(new Insets(10, 10, 10, 10));
        //Add text fields to hbox
        clearYesNoBox.getChildren().addAll(textContinueButton,textCancelButton);
        clearYesNoBox.setAlignment(Pos.CENTER);

        //fill input pane with items & center
        textInputPane.getChildren().addAll(inputTextLabel,textInputField,clearYesNoBox);
        textInputPane.setAlignment(Pos.CENTER);

        Scene stageScene = new Scene(textInputPane, 200, 100);
        //Set title and resizeable and show
        textInputStage.setTitle("Text Input");
        textInputStage.setResizable(false);
        textInputStage.setScene(stageScene);
        textInputStage.show();
    }


}
