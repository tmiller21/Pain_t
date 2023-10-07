package com.example.paint;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 *Paint_accordionPane adds content/options to tool Panes & sets drawOption for Paint_drawingCanvas
 *Class extends Accordion, which is a type of Node
 *
 *Author: Trent Miller
 *
*/

public class Paint_accordionPane extends Accordion {

    int drawOption;

    //Creating slider width labels for boxes
    Label eraserSliderWidth = new Label(1 + " pt");
    Label squareSliderWidth = new Label(1 + " pt");
    Label ellipseSliderWidth = new Label(1 + " pt");
    Label circleSliderWidth = new Label(1 + " pt");

    //Creaiting graphic context
    GraphicsContext gc;

    //Creating color picker
    ColorPicker colorPicker = new ColorPicker();

    //Creating button for color grabber
    Button colorGrabberButton = new Button("Grab Color");

    //sharp and round square buttons
    ToggleButton squareSharpButton = new ToggleButton("Sharp");
    ToggleButton squareRoundedButton = new ToggleButton("Rounded");

    //copy/cut & paste buttons
    ToggleButton copySelectionButton = new ToggleButton("Copy");
    ToggleButton cutSelectionButton = new ToggleButton("Cut");
    ToggleButton pasteButton = new ToggleButton("Paste");

    //initiate tool panes
    Paint_lineToolPane pane1 = new Paint_lineToolPane();
    TitledPane pane2 = new TitledPane("Color", new Label("N/A"));
    TitledPane pane3 = new TitledPane("Erase", new Label("N/A"));
    Paint_rectangleToolPane pane4 = new Paint_rectangleToolPane();
    TitledPane pane5 = new TitledPane("Square", new Label("N/A"));
    Paint_triangleToolPane pane6 = new Paint_triangleToolPane();
    TitledPane pane7 = new TitledPane("Ellipse", new Label("N/A"));
    TitledPane pane8 = new TitledPane("Circle", new Label("N/A"));
    Paint_polygonToolPane pane9 = new Paint_polygonToolPane();
    TitledPane pane10 = new TitledPane("Text", new Label("Draw Custom Text"));
    TitledPane pane0 = new TitledPane("Selection Tool", new Label("Selection Tool"));

    //thickness sliders
    Slider eraserSlider = new Slider(1, 72, 1);
    Slider squareSlider = new Slider(1, 48, 1);
    Slider ellipseSlider = new Slider(1, 48, 1);
    Slider circleSlider = new Slider(1, 48, 1);

    //solid dashed switches
    Paint_solidDashedSwitch squareSdSwitch = new Paint_solidDashedSwitch();
    Paint_solidDashedSwitch ellipseSdSwitch = new Paint_solidDashedSwitch();
    Paint_solidDashedSwitch circleSdSwitch = new Paint_solidDashedSwitch();

    public Paint_accordionPane() {

        //=========================Creating logic within "Line" pane=================================================
        //Adding actionListener for when value changes (to change text, width of circle and line)
        pane1.lineSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            pane1.lineSliderWidth.setText(newVal.intValue() + " pt");
            gc.setLineWidth(newVal.doubleValue());
        });
        //Add actionlistener to panel
        pane1.setOnMousePressed(mousePressed -> {
            drawOption = 1;
            System.out.println("draw option changed to " + drawOption);
        });
        //==================================Creating Content/Logic inside Color Pane=========================================
        //Creating box for color picker and grabber
        VBox colorPane = new VBox();
        colorPane.setPadding(new Insets(10, 10, 10, 10));
        colorPane.setSpacing(10);

        //set default color to black
        colorPicker.setValue(Color.BLACK);

        //when button is pressed change drawingCanvas cursor to crosshair until
        colorGrabberButton.setOnAction(mousePressed -> {
            drawOption = 2;
        });

        colorPane.getChildren().addAll(colorPicker, colorGrabberButton);

        //Display color pane in "Color" pane
        pane2.setContent(colorPane);

        pane2.setOnMousePressed(eventHandler -> {
            drawOption = 0;
        });
        //==================================Creating Content/Logic inside Erase Pane=========================================
        //Creating a slider - goes to larger value for eraser
        eraserSlider.setMajorTickUnit(18);
        eraserSlider.setMinorTickCount(9);
        eraserSlider.setBlockIncrement(1);
        eraserSlider.setShowTickMarks(true);
        eraserSlider.setSnapToTicks(true);

        //Adding actionListener for when value changes (to change text, width of circle and line)
        eraserSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            eraserSliderWidth.setText(newVal.intValue() + " pt");
            gc.setLineWidth(newVal.doubleValue());
        });

        //Making Hbox for slider & labels
        HBox eraserSliderPane = new HBox();
        eraserSliderPane.getChildren().addAll(eraserSlider, eraserSliderWidth);
        eraserSliderPane.setPadding(new Insets(10, 10, 10, 10));
        eraserSliderPane.setSpacing(10);

        //Putting slider display in vbox in linePane
        VBox eraserPane = new VBox();
        eraserPane.getChildren().addAll(eraserSliderPane);
        eraserSliderPane.setPadding(new Insets(10, 10, 10, 10));
        eraserSliderPane.setSpacing(10);

        pane3.setContent(eraserSliderPane);

        pane3.setOnMousePressed(mousePressed -> {
            drawOption = 3;
        });
        //==================================Creating Logic inside Rectangle Pane=====================================
        //Adding actionListener for when value changes (to change text, width of line)
        pane4.rectangleSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            pane4.rectangleSliderWidth.setText(newVal.intValue() + " pt");
            gc.setLineWidth(newVal.doubleValue());
        });

        pane4.setOnMousePressed(mousePressed -> {
            drawOption = 4;
        });
        //==================================Creating Content/Logic inside Square Pane=========================================
        //Creating a slider
        squareSlider.setMajorTickUnit(18);
        squareSlider.setMinorTickCount(9);
        squareSlider.setBlockIncrement(1);
        squareSlider.setShowTickMarks(true);
        squareSlider.setSnapToTicks(true);

        //Adding actionListener for when value changes (to change text, width of line)
        squareSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            squareSliderWidth.setText(newVal.intValue() + " pt");
            gc.setLineWidth(newVal.doubleValue());
        });

        //Making hbox for slider & labels
        HBox squareSliderPane = new HBox();
        squareSliderPane.getChildren().addAll(squareSlider, squareSliderWidth);

        //making sharp/rounded switch
        HBox squareStraightRoundedSwitch = new HBox();
        ToggleGroup squareStraightRoundedGroup = new ToggleGroup();
        squareRoundedButton.setToggleGroup(squareStraightRoundedGroup);
        squareSharpButton.setToggleGroup(squareStraightRoundedGroup);
        squareStraightRoundedSwitch.getChildren().addAll(squareSharpButton, squareRoundedButton);

        //Putting slider display in vbox in linePane
        VBox squarePane = new VBox();
        squarePane.getChildren().addAll(squareSliderPane, squareStraightRoundedSwitch, squareSdSwitch);
        //Set spacing and padding
        squarePane.setPadding(new Insets(10, 10, 10, 10));
        squarePane.setSpacing(10);
        pane5.setContent(squarePane);

        pane5.setOnMousePressed(mousePressed -> {
            drawOption = 5;
        });
        //==================================Creating Content inside Triangle Pane=======================================
        //Adding actionListener for when value changes (to change text, width of line)
        pane6.triangleSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            pane6.triangleSliderWidth.setText(newVal.intValue() + " pt");
            gc.setLineWidth(newVal.doubleValue());
        });

        pane6.setOnMousePressed(mousePressed -> {
            drawOption = 6;
        });
        //==================================Creating Content inside Ellipse Pane========================================
        ellipseSlider.setMajorTickUnit(18);
        ellipseSlider.setMinorTickCount(9);
        ellipseSlider.setBlockIncrement(1);
        ellipseSlider.setShowTickMarks(true);
        ellipseSlider.setSnapToTicks(true);

        //Adding actionListener for when value changes (to change text, width of line)
        ellipseSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            ellipseSliderWidth.setText(newVal.intValue() + " pt");
            gc.setLineWidth(newVal.doubleValue());
        });

        //Making hbox for slider & labels
        HBox ellipseSliderPane = new HBox();
        ellipseSliderPane.getChildren().addAll(ellipseSlider, ellipseSliderWidth);

        //Putting slider display in vbox in linePane
        VBox ellipsePane = new VBox();
        ellipsePane.getChildren().addAll(ellipseSliderPane, ellipseSdSwitch);
        //Set spacing and padding
        ellipsePane.setPadding(new Insets(10, 10, 10, 10));
        ellipsePane.setSpacing(10);
        pane7.setContent(ellipsePane);

        pane7.setOnMousePressed(mousePressed -> {
            drawOption = 7;
        });
        //==================================Creating Content inside Circle Pane=========================================
        circleSlider.setMajorTickUnit(18);
        circleSlider.setMinorTickCount(9);
        circleSlider.setBlockIncrement(1);
        circleSlider.setShowTickMarks(true);
        circleSlider.setSnapToTicks(true);

        //Adding actionListener for when value changes (to change text, width of line)
        circleSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            circleSliderWidth.setText(newVal.intValue() + " pt");
            gc.setLineWidth(newVal.doubleValue());
        });

        //Making hbox for slider & labels
        HBox circleSliderPane = new HBox();
        circleSliderPane.getChildren().addAll(circleSlider, circleSliderWidth);

        //Putting slider display in vbox in linePane
        VBox circlePane = new VBox();
        circlePane.getChildren().addAll(circleSliderPane, circleSdSwitch);
        //Set spacing and padding
        circlePane.setPadding(new Insets(10, 10, 10, 10));
        circlePane.setSpacing(10);
        pane8.setContent(circlePane);

        pane8.setOnMousePressed(mousePressed -> {
            drawOption = 8;
        });
        //==================================Creating Content inside Polygon Pane=========================================
        //Adding actionListener for when value changes (to change text, width of line)
        pane9.polygonSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            pane9.polygonSliderWidth.setText(newVal.intValue() + " pt");
            gc.setLineWidth(newVal.doubleValue());
        });

        pane9.setOnMousePressed(mousePressed -> {
            drawOption = 9;
        });
        //==================================Creating Content inside Text Pane=========================================
        //Creating a slider
        Slider textSlider = new Slider(1, 48, 1);
        textSlider.setMajorTickUnit(18);
        textSlider.setMinorTickCount(9);
        textSlider.setBlockIncrement(1);
        textSlider.setShowTickMarks(true);
        textSlider.setSnapToTicks(true);

        pane10.setOnMousePressed(mousePressed -> {
            drawOption = 10;
        });
        //==================================Creating Content inside Selection Tool Pane=================================
        //making vbox for cut/copy and paste buttons
        VBox cutCopyBox = new VBox();
        cutCopyBox.setSpacing(10);

        //making toggle group for copy and cut
        HBox cutCopyToggleBox = new HBox();
        ToggleGroup cutCopyToggleGroup = new ToggleGroup();
        cutCopyToggleBox.getChildren().addAll(cutSelectionButton,copySelectionButton);
        cutSelectionButton.setToggleGroup(cutCopyToggleGroup);
        copySelectionButton.setToggleGroup(cutCopyToggleGroup);

        //deselect paste button when copy or cut is pressed, deselect copy and cut when paste is pressed
        copySelectionButton.setOnMousePressed(mousePressed -> {pasteButton.setSelected(false);});
        cutSelectionButton.setOnMousePressed(mousePressed -> {pasteButton.setSelected(false);});
        pasteButton.setOnMousePressed(pastePressed -> {
            cutSelectionButton.setSelected(false);
            copySelectionButton.setSelected(false);
        });

        //style and add elements to box
        cutCopyBox.getChildren().addAll(cutCopyToggleBox,pasteButton);

        pane0.setOnMousePressed(mousePressed -> {
           drawOption = 11;
        });
        pane0.setContent(cutCopyBox);

        //Add panes to accordion
        super.getPanes().addAll(pane0, pane1, pane2, pane3, pane4, pane5, pane6, pane7, pane8, pane9, pane10);
    }

    public int getDrawOption(){
        return drawOption;
    }

}
