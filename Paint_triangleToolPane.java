package com.example.paint;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Paint_triangleToolPane extends TitledPane {

    //Creating a slider
    Slider triangleSlider = new Slider(1,48, 1);
    //label for slider
    Label triangleSliderWidth = new Label(1 + " pt");

    //Making triangle solid/dashed switch
    Paint_solidDashedSwitch triangleSdSwitch = new Paint_solidDashedSwitch();

    public Paint_triangleToolPane(){
        this.setText("Triangle");
        triangleSlider.setMajorTickUnit(18);
        triangleSlider.setMinorTickCount(9);
        triangleSlider.setBlockIncrement(1);
        triangleSlider.setShowTickMarks(true);
        triangleSlider.setSnapToTicks(true);

        //Making hbox for slider & labels
        HBox triangleSliderPane = new HBox();
        triangleSliderPane.getChildren().addAll(triangleSlider, triangleSliderWidth);

        //Putting slider display in vbox in linePane
        VBox trianglePane = new VBox();
        trianglePane.getChildren().addAll(triangleSliderPane, triangleSdSwitch);
        //Set spacing and padding
        trianglePane.setPadding(new Insets(10, 10, 10, 10));
        trianglePane.setSpacing(10);
        this.setContent(trianglePane);
    }
}
