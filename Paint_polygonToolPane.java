package com.example.paint;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Paint_polygonToolPane extends TitledPane {

    //Creating a slider
    Slider polygonSlider = new Slider(1,48, 1);
    //Label for slider
    Label polygonSliderWidth = new Label(1 + " pt");

    //Making dotted/dashed switch
    Paint_solidDashedSwitch polygonSdSwitch = new Paint_solidDashedSwitch();

    //text field for polygon sides
    TextField polygonSidesField = new TextField();

    public Paint_polygonToolPane(){
        this.setText("Polygon  ⎔");
        polygonSlider.setMajorTickUnit(18);
        polygonSlider.setMinorTickCount(9);
        polygonSlider.setBlockIncrement(1);
        polygonSlider.setShowTickMarks(true);
        polygonSlider.setSnapToTicks(true);

        //Making hbox for slider & labels
        HBox polygonSliderPane = new HBox();
        polygonSliderPane.getChildren().addAll(polygonSlider, polygonSliderWidth);

        //Making labeled text field for polygon sides
        HBox polygonSidesPane = new HBox();
        Label polySidesLabel = new Label("Sides: ");
        polygonSidesField.setMaxWidth(60);
        polygonSidesPane.setAlignment(Pos.CENTER_LEFT);
        polygonSidesPane.setSpacing(5);
        polygonSidesPane.getChildren().addAll(polySidesLabel, polygonSidesField);

        //Putting slider display in vbox in linePane
        VBox polygonPane = new VBox();
        polygonPane.getChildren().addAll(polygonSliderPane, polygonSidesPane, polygonSdSwitch);
        //Set spacing and padding
        polygonPane.setPadding(new Insets(10, 10, 10, 10));
        polygonPane.setSpacing(10);
        this.setContent(polygonPane);
    }
}
