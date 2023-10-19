package com.example.paint;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Paint_POPUPsmartSave {

    //SMART SAVE
    Stage smartSaveStage = new Stage();

    //If ok button is pressed, save as normal
    Button okButton = new Button("Ok");

    //If no button is pressed, close window
    Button noButton = new Button("No");

    public Paint_POPUPsmartSave(){

        //HBox for text
        HBox textBox = new HBox();
        Text oracleText = new Text("File Oracle: The Gods have told me that you haven't saved! Save it, just save it!");
        oracleText.setWrappingWidth(150);
        textBox.getChildren().addAll(oracleText);
        textBox.setAlignment(Pos.CENTER);
        textBox.setSpacing(10);
        textBox.setPadding(new Insets(10, 10, 10, 10));

        //HBox for buttons
        HBox okNoBox = new HBox();

        //Design button box
        okNoBox.getChildren().addAll(okButton, noButton);
        okNoBox.setAlignment(Pos.CENTER);
        okNoBox.setSpacing(20);
        okNoBox.setPadding(new Insets(10, 10, 10, 10));
        okButton.setPrefWidth(50);

        //VBox for text and buttons
        VBox smartSaveBox = new VBox();
        smartSaveBox.getChildren().addAll(textBox, okNoBox);
        smartSaveBox.setAlignment(Pos.CENTER);
        smartSaveBox.setSpacing(10);

        //Create scene
        Scene smartSaveScene = new Scene(smartSaveBox, 225, 150);

        //set stage name and show
        smartSaveStage.setTitle("File Oracle");
        smartSaveStage.setResizable(false);
        smartSaveStage.setScene(smartSaveScene);
        smartSaveStage.show();
    }
}
