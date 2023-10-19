package com.example.paint;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Paint_POPUPsaveLossWarning {

    //SMART SAVE
    Stage lossWarningStage = new Stage();

    //If ok button is pressed, save as normal
    Button yesButton = new Button("Yes");

    //If no button is pressed, close window
    Button noButton = new Button("No");

    public Paint_POPUPsaveLossWarning(){
        //HBox for text
        HBox textBox = new HBox();
        Text oracleText = new Text("Warning: Saving in this file format may cause losses! Save anyway?");
        oracleText.setWrappingWidth(150);
        textBox.getChildren().addAll(oracleText);
        textBox.setAlignment(Pos.CENTER);
        textBox.setSpacing(10);
        textBox.setPadding(new Insets(10, 10, 10, 10));

        //HBox for buttons
        HBox okNoBox = new HBox();

        //setting width of buttons
        yesButton.setPrefWidth(50);
        noButton.setPrefWidth(50);

        //Design button box
        okNoBox.getChildren().addAll(yesButton, noButton);
        okNoBox.setAlignment(Pos.CENTER);
        okNoBox.setSpacing(20);
        okNoBox.setPadding(new Insets(10, 10, 10, 10));
        yesButton.setPrefWidth(50);

        //VBox for text and buttons
        VBox smartSaveBox = new VBox();
        smartSaveBox.getChildren().addAll(textBox, okNoBox);
        smartSaveBox.setAlignment(Pos.CENTER);
        smartSaveBox.setSpacing(10);

        //Create scene
        Scene smartSaveScene = new Scene(smartSaveBox, 225, 150);

        //set stage name and show
        lossWarningStage.setTitle("File Oracle");
        lossWarningStage.setResizable(false);
        lossWarningStage.setScene(smartSaveScene);
        lossWarningStage.show();
    }

}
