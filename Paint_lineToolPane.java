package com.example.paint;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Paint_lineToolPane extends TitledPane {

    //Creating a slider
    Slider lineSlider = new Slider(1, 48, 1);
    //Label for slider width
    Label lineSliderWidth = new Label(1 + " pt");

    //Straight and curved buttons
    ToggleButton lineCurvedButton = new ToggleButton("Curved");
    ToggleButton lineStraightButton = new ToggleButton("Straight");

    //Making dotted/dashed switch
    Paint_solidDashedSwitch lineSdSwitch = new Paint_solidDashedSwitch();

    public Paint_lineToolPane(){
        this.setText("Line  \uD83D\uDD8A");
        lineSlider.setMajorTickUnit(18);
        lineSlider.setMinorTickCount(9);
        lineSlider.setBlockIncrement(1);
        lineSlider.setShowTickMarks(true);
        lineSlider.setSnapToTicks(true);

        //Making Hbox for slider & labels
        HBox lineSliderPane = new HBox();
        lineSliderPane.getChildren().addAll(lineSlider, lineSliderWidth);

        //Making straight/curved switch
        HBox lineScSwitch = new HBox();
        ToggleGroup lineScGroup = new ToggleGroup();
        lineStraightButton.setToggleGroup(lineScGroup);
        lineCurvedButton.setToggleGroup(lineScGroup);
        lineScSwitch.getChildren().addAll(lineStraightButton, lineCurvedButton);

        //Putting slider display and color picker in vbox in linePane
        VBox linePane = new VBox();
        linePane.getChildren().addAll(lineSliderPane, lineScSwitch, lineSdSwitch);
        //Set spacing and padding
        linePane.setPadding(new Insets(10, 10, 10, 10));
        linePane.setSpacing(10);

        //set this titled pane's content to the line pane
        this.setContent(linePane);
    }
}
