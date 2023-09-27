package com.example.paint;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class Paint_solidDashedSwitch extends HBox {

    ToggleGroup lineSdGroup = new ToggleGroup();
    ToggleButton dashedButton = new ToggleButton("Dashed");
    ToggleButton solidButton = new ToggleButton("Solid");


    public Paint_solidDashedSwitch(){

        //Creating dotted/dashed switch
        solidButton.setToggleGroup(lineSdGroup);
        dashedButton.setToggleGroup(lineSdGroup);
        super.getChildren().addAll(solidButton, dashedButton);

    }
}
