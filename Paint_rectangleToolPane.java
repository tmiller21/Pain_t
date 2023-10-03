package com.example.paint;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Paint_rectangleToolPane extends TitledPane {

    //Creating a slider
    Slider rectangleSlider = new Slider(1,48, 1);

    //label for slider
    Label rectangleSliderWidth = new Label(1 + " pt");

    //sharp and round switch
    ToggleButton rectSharpButton = new ToggleButton("Sharp");
    ToggleButton rectRoundedButton = new ToggleButton("Rounded");

    //Making dotted/dashed switch
    Paint_solidDashedSwitch rectangleSdSwitch = new Paint_solidDashedSwitch();

    public Paint_rectangleToolPane(){
        this.setText("Rectangle");
        rectangleSlider.setMajorTickUnit(18);
        rectangleSlider.setMinorTickCount(9);
        rectangleSlider.setBlockIncrement(1);
        rectangleSlider.setShowTickMarks(true);
        rectangleSlider.setSnapToTicks(true);

        //Making hbox for slider & labels
        HBox rectangleSliderPane = new HBox();
        rectangleSliderPane.getChildren().addAll(rectangleSlider, rectangleSliderWidth);

        //making sharp/rounded switch
        HBox rectangleStraightRoundedSwitch = new HBox();
        ToggleGroup rectangleStraightRoundedGroup = new ToggleGroup();
        rectRoundedButton.setToggleGroup(rectangleStraightRoundedGroup);
        rectSharpButton.setToggleGroup(rectangleStraightRoundedGroup);
        rectangleStraightRoundedSwitch.getChildren().addAll(rectSharpButton, rectRoundedButton);

        //Putting slider display in vbox in linePane
        VBox rectanglePane = new VBox();
        rectanglePane.getChildren().addAll(rectangleSliderPane, rectangleStraightRoundedSwitch, rectangleSdSwitch);
        //Set spacing and padding
        rectanglePane.setPadding(new Insets(10, 10, 10, 10));
        rectanglePane.setSpacing(10);
        this.setContent(rectanglePane);
    }
}
