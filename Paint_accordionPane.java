package com.example.paint;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

public class Paint_accordionPane extends Accordion {

    //Initiating init and final positions for drawing
    double x1, x2, y1, y2, xf, yf;

    int drawOption;

    //Determining whether line is dashed or solid. Only need one - can't be drawing multiple things at once
    boolean isSolid;
    boolean isStraight, isCurved;

    //Creating slider width labels for boxes
    Label lineSliderWidth = new Label(1 + " pt");
    Label eraserSliderWidth = new Label(1 + " pt");
    Label rectangleSliderWidth = new Label(1 + " pt");
    Label squareSliderWidth = new Label(1 + " pt");
    Label triangleSliderWidth = new Label(1 + " pt");
    Label ellipseSliderWidth = new Label(1 + " pt");
    Label circleSliderWidth = new Label(1 + " pt");

    //Creaiting graphic context
    GraphicsContext gc;

    //Creating color picker
    ColorPicker colorPicker = new ColorPicker();
    Color lineColor;

    //Creating button for color grabber
    Button colorGrabberButton = new Button("Grab Color");

    //Straight and curved buttons
    ToggleButton lineCurvedButton = new ToggleButton("Curved");
    ToggleButton lineStraightButton = new ToggleButton("Straight");

    ToggleButton rectSharpButton = new ToggleButton("Sharp");
    ToggleButton rectRoundedButton = new ToggleButton("Rounded");
    ToggleButton squareSharpButton = new ToggleButton("Sharp");
    ToggleButton squareRoundedButton = new ToggleButton("Rounded");


    public Paint_accordionPane(Canvas drawingCanvas, ScrollPane workspace){

        //=========================Creating content within "Line" pane=================================================
        TitledPane pane1 = new TitledPane("Line", new Label("Line"));
        //Creating a slider
        Slider lineSlider = new Slider(1, 48, 1);
        lineSlider.setMajorTickUnit(18);
        lineSlider.setMinorTickCount(9);
        lineSlider.setBlockIncrement(1);
        lineSlider.setShowTickMarks(true);
        lineSlider.setSnapToTicks(true);

        //Adding actionListener for when value changes (to change text, width of circle and line)
        lineSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            lineSliderWidth.setText(newVal.intValue() + " pt");
            gc = drawingCanvas.getGraphicsContext2D();
            gc.setLineWidth(newVal.doubleValue());
        });

        //Making Hbox for slider & labels
        HBox lineSliderPane = new HBox();
        lineSliderPane.getChildren().addAll(lineSlider, lineSliderWidth);

        //Making dotted/dashed switch
        Paint_solidDashedSwitch lineSdSwitch = new Paint_solidDashedSwitch();

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
        pane1.setContent(linePane);

        //Add actionlistener to panel
        pane1.setOnMousePressed(mousePressed -> {
           drawOption = 1;
        });

        //==================================Creating Content inside Color Pane=========================================
        TitledPane pane2 = new TitledPane("Color", new Label("N/A"));

        //Creating box for color picker and grabber
        VBox colorPane = new VBox();
        colorPane.setPadding(new Insets(10,10,10,10));
        colorPane.setSpacing(10);

        //set default color to black
        colorPicker.setValue(Color.BLACK);

        //when button is pressed change drawingCanvas cursor to crosshair until
        colorGrabberButton.setOnAction(mousePressed -> {
            gc = drawingCanvas.getGraphicsContext2D();
            drawOption = 2;
            drawingCanvas.setCursor(Cursor.CROSSHAIR);
        });

        colorPane.getChildren().addAll(colorPicker, colorGrabberButton);

        //Display color pane in "Color" pane
        pane2.setContent(colorPane);

        //==================================Creating Content inside Erase Pane=========================================
        TitledPane pane3 = new TitledPane("Erase", new Label("N/A"));

        //Creating a slider - goes to larger value for eraser
        Slider eraserSlider = new Slider(1, 48, 1);
        eraserSlider.setMajorTickUnit(18);
        eraserSlider.setMinorTickCount(9);
        eraserSlider.setBlockIncrement(1);
        eraserSlider.setShowTickMarks(true);
        eraserSlider.setSnapToTicks(true);

        //Adding actionListener for when value changes (to change text, width of circle and line)
        eraserSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            lineSliderWidth.setText(newVal.intValue() + " pt");
            gc = drawingCanvas.getGraphicsContext2D();
            gc.setLineWidth(newVal.doubleValue());
        });

        //Making Hbox for slider & labels
        HBox eraserSliderPane = new HBox();
        eraserSliderPane.getChildren().addAll(eraserSlider, eraserSliderWidth);
        eraserSliderPane.setPadding(new Insets(10,10,10,10));
        eraserSliderPane.setSpacing(10);

        //Putting slider display in vbox in linePane
        VBox eraserPane = new VBox();
        eraserPane.getChildren().addAll(eraserSliderPane);
        eraserSliderPane.setPadding(new Insets(10,10,10,10));
        eraserSliderPane.setSpacing(10);

        pane3.setContent(eraserSliderPane);

        pane3.setOnMousePressed(mousePressed -> {
            gc = drawingCanvas.getGraphicsContext2D();
            drawOption = 3;
        });

        //==================================Creating Content inside Rectangle Pane=====================================
        TitledPane pane4 = new TitledPane("Rectangle", new Label("N/A"));

        //Creating a slider
        Slider rectangleSlider = new Slider(1,48, 1);
        rectangleSlider.setMajorTickUnit(18);
        rectangleSlider.setMinorTickCount(9);
        rectangleSlider.setBlockIncrement(1);
        rectangleSlider.setShowTickMarks(true);
        rectangleSlider.setSnapToTicks(true);

        //Adding actionListener for when value changes (to change text, width of line)
        rectangleSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            rectangleSliderWidth.setText(newVal.intValue() + " pt");
            gc = drawingCanvas.getGraphicsContext2D();
            gc.setLineWidth(newVal.doubleValue());
        });

        //Making hbox for slider & labels
        HBox rectangleSliderPane = new HBox();
        rectangleSliderPane.getChildren().addAll(rectangleSlider, rectangleSliderWidth);

        //making sharp/rounded switch
        HBox rectangleStraightRoundedSwitch = new HBox();
        ToggleGroup rectangleStraightRoundedGroup = new ToggleGroup();
        rectRoundedButton.setToggleGroup(rectangleStraightRoundedGroup);
        rectSharpButton.setToggleGroup(rectangleStraightRoundedGroup);
        rectangleStraightRoundedSwitch.getChildren().addAll(rectSharpButton, rectRoundedButton);

        //Making Hbox for dotted/dashed switch & draw button
        Paint_solidDashedSwitch rectangleSdSwitch = new Paint_solidDashedSwitch();

        //Putting slider display in vbox in linePane
        VBox rectanglePane = new VBox();
        rectanglePane.getChildren().addAll(rectangleSliderPane, rectangleStraightRoundedSwitch, rectangleSdSwitch);
        //Set spacing and padding
        rectanglePane.setPadding(new Insets(10, 10, 10, 10));
        rectanglePane.setSpacing(10);
        pane4.setContent(rectanglePane);

        pane4.setOnMousePressed(mousePressed -> {
            gc = drawingCanvas.getGraphicsContext2D();
            drawOption = 4;
        });

        //==================================Creating Content inside Square Pane=========================================
        TitledPane pane5 = new TitledPane("Square", new Label("N/A"));

        //Creating a slider
        Slider squareSlider = new Slider(1,48, 1);
        squareSlider.setMajorTickUnit(18);
        squareSlider.setMinorTickCount(9);
        squareSlider.setBlockIncrement(1);
        squareSlider.setShowTickMarks(true);
        squareSlider.setSnapToTicks(true);

        //Adding actionListener for when value changes (to change text, width of line)
        squareSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            squareSliderWidth.setText(newVal.intValue() + " pt");
            gc = drawingCanvas.getGraphicsContext2D();
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

        //Making Hbox for dotted/dashed switch & draw button
        Paint_solidDashedSwitch squareSdSwitch = new Paint_solidDashedSwitch();

        //Putting slider display in vbox in linePane
        VBox squarePane = new VBox();
        squarePane.getChildren().addAll(squareSliderPane,squareStraightRoundedSwitch,squareSdSwitch);
        //Set spacing and padding
        squarePane.setPadding(new Insets(10, 10, 10, 10));
        squarePane.setSpacing(10);
        pane5.setContent(squarePane);

        pane5.setOnMousePressed(mousePressed -> {
            gc = drawingCanvas.getGraphicsContext2D();
            drawOption = 5;
        });

        //==================================Creating Content inside Triangle Pane=======================================
        TitledPane pane6 = new TitledPane("Triangle", new Label("N/A"));

        //Creating a slider
        Slider triangleSlider = new Slider(1,48, 1);
        triangleSlider.setMajorTickUnit(18);
        triangleSlider.setMinorTickCount(9);
        triangleSlider.setBlockIncrement(1);
        triangleSlider.setShowTickMarks(true);
        triangleSlider.setSnapToTicks(true);

        //Adding actionListener for when value changes (to change text, width of line)
        triangleSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            triangleSliderWidth.setText(newVal.intValue() + " pt");
            gc = drawingCanvas.getGraphicsContext2D();
            gc.setLineWidth(newVal.doubleValue());
        });

        //Making hbox for slider & labels
        HBox triangleSliderPane = new HBox();
        triangleSliderPane.getChildren().addAll(triangleSlider, triangleSliderWidth);

        //Making Hbox for dotted/dashed switch & draw button
        Paint_solidDashedSwitch triangleSdSwitch = new Paint_solidDashedSwitch();

        //Putting slider display in vbox in linePane
        VBox trianglePane = new VBox();
        trianglePane.getChildren().addAll(triangleSliderPane, triangleSdSwitch);
        //Set spacing and padding
        trianglePane.setPadding(new Insets(10, 10, 10, 10));
        trianglePane.setSpacing(10);
        pane6.setContent(trianglePane);

        pane6.setOnMousePressed(mousePressed -> {
            gc = drawingCanvas.getGraphicsContext2D();
            drawOption = 6;
        });

        //==================================Creating Content inside Ellipse Pane========================================
        TitledPane pane7 = new TitledPane("Ellipse", new Label("N/A"));

        //Creating a slider
        Slider ellipseSlider = new Slider(1,48, 1);
        ellipseSlider.setMajorTickUnit(18);
        ellipseSlider.setMinorTickCount(9);
        ellipseSlider.setBlockIncrement(1);
        ellipseSlider.setShowTickMarks(true);
        ellipseSlider.setSnapToTicks(true);

        //Adding actionListener for when value changes (to change text, width of line)
        ellipseSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            ellipseSliderWidth.setText(newVal.intValue() + " pt");
            gc = drawingCanvas.getGraphicsContext2D();
            gc.setLineWidth(newVal.doubleValue());
        });

        //Making hbox for slider & labels
        HBox ellipseSliderPane = new HBox();
        ellipseSliderPane.getChildren().addAll(ellipseSlider, ellipseSliderWidth);

        //Making Hbox for dotted/dashed switch & draw button
        Paint_solidDashedSwitch ellipseSdSwitch = new Paint_solidDashedSwitch();

        //Putting slider display in vbox in linePane
        VBox ellipsePane = new VBox();
        ellipsePane.getChildren().addAll(ellipseSliderPane, ellipseSdSwitch);
        //Set spacing and padding
        ellipsePane.setPadding(new Insets(10, 10, 10, 10));
        ellipsePane.setSpacing(10);
        pane7.setContent(ellipsePane);

        pane7.setOnMousePressed(mousePressed -> {
            gc = drawingCanvas.getGraphicsContext2D();
            drawOption = 7;
        });

        //==================================Creating Content inside Circle Pane=========================================
        TitledPane pane8 = new TitledPane("Circle", new Label("N/A"));

        //Creating a slider
        Slider circleSlider = new Slider(1,48, 1);
        circleSlider.setMajorTickUnit(18);
        circleSlider.setMinorTickCount(9);
        circleSlider.setBlockIncrement(1);
        circleSlider.setShowTickMarks(true);
        circleSlider.setSnapToTicks(true);

        //Adding actionListener for when value changes (to change text, width of line)
        circleSlider.valueProperty().addListener((observable, newVal, oldVal) -> {
            circleSliderWidth.setText(newVal.intValue() + " pt");
            gc = drawingCanvas.getGraphicsContext2D();
            gc.setLineWidth(newVal.doubleValue());
        });

        //Making hbox for slider & labels
        HBox circleSliderPane = new HBox();
        circleSliderPane.getChildren().addAll(circleSlider, circleSliderWidth);

        //Making Hbox for dotted/dashed switch & draw button
        Paint_solidDashedSwitch circleSdSwitch = new Paint_solidDashedSwitch();

        //Putting slider display in vbox in linePane
        VBox circlePane = new VBox();
        circlePane.getChildren().addAll(circleSliderPane, circleSdSwitch);
        //Set spacing and padding
        circlePane.setPadding(new Insets(10, 10, 10, 10));
        circlePane.setSpacing(10);
        pane8.setContent(circlePane);

        pane8.setOnMousePressed(mousePressed -> {
            gc = drawingCanvas.getGraphicsContext2D();
            drawOption = 8;
        });

        //=================================CREATING LOGIC FOR ACCORDION PANE===========================================
        //  0=nothing,1=line,2=colorGrabber,3=erase,4=rectangle,5=square,6=triangle,7=ellipse,8=circle

        //upon first click in workspace, figure out what needs to be drawn
        drawingCanvas.setOnMousePressed(mousePressed -> {

            //Get first position
            x1 = mousePressed.getX();
            y1 = mousePressed.getY();

            //Get graphics context, line color, and line width
            gc = drawingCanvas.getGraphicsContext2D();
            lineColor = colorPicker.getValue();
            gc.setStroke(lineColor);

            switch (drawOption) {

                //Case 1 = draw line
                case 1:

                    //set cursor back to normal
                    drawingCanvas.setCursor(Cursor.DEFAULT);

                    //get slider value
                    gc.setLineWidth(lineSlider.getValue());

                    //start path for straight line
                    gc.beginPath();
                    gc.moveTo(x1, y1);

                    //if straight is pressed
                    if (lineStraightButton.isSelected()) {
                        drawingCanvas.setOnMouseDragged(mouseDragged -> {
                        });
                        //When released, get ending position and draw path
                        drawingCanvas.setOnMouseReleased(mouseReleased -> {
                            //solid straight line
                            if (lineSdSwitch.solidButton.isSelected()) {
                                //set dashes to zero
                                gc.setLineDashes();
                            } else if (lineSdSwitch.dashedButton.isSelected()) {
                                //dashed straight line
                                gc.setLineDashes(30);
                            }
                            gc.lineTo(mouseReleased.getX(), mouseReleased.getY());
                            gc.stroke();
                        });
                    }

                    if (lineCurvedButton.isSelected()) {
                        //When released, get ending position and draw path
                        drawingCanvas.setOnMouseDragged(mouseDragged -> {
                            if (lineSdSwitch.solidButton.isSelected()) {
                                //set dashes to zero
                                gc.setLineDashes();
                            } else if (lineSdSwitch.dashedButton.isSelected()) {
                                gc.setLineDashes(30);
                            }
                            gc.lineTo(mouseDragged.getX(), mouseDragged.getY());
                            gc.stroke();
                        });
                        drawingCanvas.setOnMouseReleased(mouseReleased -> {
                            gc.lineTo(mouseReleased.getX(), mouseReleased.getY());
                            gc.stroke();
                        });
                    }
                    break;


                //Case 2 = grab color
                case 2:

                    //make robot to get color at position
                    Robot robot = new Robot();
                    Color grabbedColor = robot.getPixelColor(x1, y1);

                    //setting color picker value to grab color
                    colorPicker.setValue(grabbedColor);

                    //set cursor back to normal
                    drawingCanvas.setCursor(Cursor.DEFAULT);
                    break;


                //Case 3 = erase
                case 3:

                    //set default cursor
                    drawingCanvas.setCursor(Cursor.DEFAULT);

                    //set color to white & dashes to null
                    gc.setStroke(Color.WHITE);
                    gc.setLineDashes();

                    //set width to slider value
                    gc.setLineWidth(eraserSlider.getValue());

                    //start path for eraser line
                    gc.beginPath();
                    gc.moveTo(x1, y1);

                    drawingCanvas.setOnMouseDragged(mouseDragged -> {
                        gc.lineTo(mouseDragged.getX(), mouseDragged.getY());
                        gc.stroke();
                    });
                    drawingCanvas.setOnMouseReleased(mouseReleased -> {
                        gc.lineTo(mouseReleased.getX(), mouseReleased.getY());
                        gc.stroke();
                    });
                    break;


                    //Case 4 = rectangle
                case 4:

                    //set cursor back to normal
                    drawingCanvas.setCursor(Cursor.DEFAULT);

                    //get width
                    gc.setLineWidth(rectangleSlider.getValue());

                    //start path for straight line
                    gc.beginPath();
                    gc.moveTo(x1, y1);

                    drawingCanvas.setOnMouseDragged(mouseDragged -> {
                    });

                    //When released, get ending position and draw path
                    drawingCanvas.setOnMouseReleased(mouseReleased -> {
                        if (rectangleSdSwitch.solidButton.isSelected()) {
                            //set dashes to zero
                            gc.setLineDashes();
                        } else if (rectangleSdSwitch.dashedButton.isSelected()) {
                            //dashed rectangle
                            gc.setLineDashes(30);
                        }

                        //Ensuring that rectangle is always drawn in direction user draws it
                        xf = Math.min(mouseReleased.getX(), x1);
                        yf = Math.min(mouseReleased.getY(), y1);
                        //get height and width
                        double rectWidth = Math.abs(mouseReleased.getX() - x1);
                        double rectHeight = Math.abs(mouseReleased.getY() - y1);

                        if(rectSharpButton.isSelected()) {
                            gc.strokeRect(xf, yf, rectWidth, rectHeight);
                        }else if(rectRoundedButton.isSelected()){
                            gc.strokeRoundRect(xf, yf, rectWidth, rectHeight,rectWidth/5,rectHeight/5);
                        }
                    });
                    break;


                    //Case 5 = square
                case 5:

                    //set cursor back to normal
                    drawingCanvas.setCursor(Cursor.DEFAULT);

                    //get width
                    gc.setLineWidth(squareSlider.getValue());

                    //start path for straight line
                    gc.beginPath();
                    gc.moveTo(x1, y1);

                    drawingCanvas.setOnMouseDragged(mouseDragged -> {
                    });

                    //When released, get ending position and draw path
                    drawingCanvas.setOnMouseReleased(mouseReleased -> {
                            //solid square
                        if (squareSdSwitch.solidButton.isSelected()) {
                            //set dashes to zero
                            gc.setLineDashes();
                        } else if (squareSdSwitch.dashedButton.isSelected()) {
                            //dashed square
                            gc.setLineDashes(30);
                        }

                        //Ensuring that square is always drawn in direction user draws it
                        xf = Math.min(mouseReleased.getX(), x1);
                        yf = Math.min(mouseReleased.getY(), y1);
                        //get height and width
                        double squareDim = Math.abs(mouseReleased.getX() - x1);

                        if(squareSharpButton.isSelected()) {
                            gc.strokeRect(xf, yf, squareDim, squareDim);
                        }else if(squareRoundedButton.isSelected()){
                            gc.strokeRoundRect(xf, yf, squareDim, squareDim,squareDim/5,squareDim/5);
                        }
                    });
                    break;


                    //Case 6 = triangle
                case 6:

                    //set cursor back to normal
                    drawingCanvas.setCursor(Cursor.DEFAULT);

                    //get width
                    gc.setLineWidth(triangleSlider.getValue());

                    //start path for straight line
                    gc.beginPath();
                    gc.moveTo(x1, y1);

                    drawingCanvas.setOnMouseDragged(mouseDragged -> {
                    });

                    drawingCanvas.setOnMouseReleased(mouseReleased -> {
                        //Ensuring that rectangle is always drawn in direction user draws it
                        x2 = mouseReleased.getX();
                        y2 = mouseReleased.getY();

                        if (triangleSdSwitch.solidButton.isSelected()) {
                            //set dashes to zero
                            gc.setLineDashes();
                        } else if (triangleSdSwitch.dashedButton.isSelected()) {
                            //dashed
                            gc.setLineDashes(30);
                        }
                        //making path for triangle
                        //Go from first spot, to right angle, to second spot, and complete
                        gc.lineTo(x2,y1);
                        gc.lineTo(x2,y2);
                        gc.closePath();
                        gc.stroke();
                    });
                    break;

                    //Case 7 = ellipse
                case 7:

                    //set cursor back to normal
                    drawingCanvas.setCursor(Cursor.DEFAULT);

                    //get slider value
                    gc.setLineWidth(ellipseSlider.getValue());

                    //start path for straight line
                    gc.beginPath();
                    gc.moveTo(x1, y1);

                    drawingCanvas.setOnMouseDragged(mouseDragged -> {
                    });

                    drawingCanvas.setOnMouseReleased(mouseReleased -> {
                        //Ensuring that rectangle is always drawn in direction user draws it
                        x2 = mouseReleased.getX();
                        y2 = mouseReleased.getY();

                        if (ellipseSdSwitch.solidButton.isSelected()) {
                            //set dashes to zero
                            gc.setLineDashes();
                        } else if (ellipseSdSwitch.dashedButton.isSelected()) {
                            //dashed
                            gc.setLineDashes(30);
                        }

                        //Ensuring that ellipse is always drawn in direction user draws it
                        xf = Math.min(x2, x1);
                        yf = Math.min(y2, y1);

                        //get length of line
                        double lineLength = Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2));

                        //get angle of line
                        double ellipseAngle = Math.toDegrees(Math.atan2(y2-y1, x2-x1));
                        if (ellipseAngle<0){
                            ellipseAngle += 360;
                        }

                        //get height and width from angle
                        double ellipseWidth = 2 * lineLength * Math.cos(Math.toRadians(ellipseAngle));
                        double ellipseHeight = 2 * lineLength * Math.sin(Math.toRadians(ellipseAngle));

                        //draw oval
                        gc.strokeOval(xf, yf, Math.abs(ellipseWidth), Math.abs(ellipseHeight));
                    });
                    break;

                    //Case 8 = circle
                case 8:

                    //set cursor back to normal
                    drawingCanvas.setCursor(Cursor.DEFAULT);

                    //get slider value
                    gc.setLineWidth(circleSlider.getValue());

                    //start path for straight line
                    gc.beginPath();
                    gc.moveTo(x1, y1);

                    drawingCanvas.setOnMouseDragged(mouseDragged -> {
                    });

                    drawingCanvas.setOnMouseReleased(mouseReleased -> {
                        //Ensuring that rectangle is always drawn in direction user draws it
                        x2 = mouseReleased.getX();
                        y2 = mouseReleased.getY();

                        if (circleSdSwitch.solidButton.isSelected()) {
                            //set dashes to zero
                            gc.setLineDashes();
                        } else if (circleSdSwitch.dashedButton.isSelected()) {
                            //dashed
                            gc.setLineDashes(30);
                        }

                        //Ensuring that ellipse is always drawn in direction user draws it
                        xf = Math.min(x2, x1);
                        yf = Math.min(y2, y1);

                        //get length of line
                        double lineLength = Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2));

                        //get angle of line
                        double circleAngle = Math.toDegrees(Math.atan2(y2-y1, x2-x1));
                        if (circleAngle<0){
                            circleAngle += 360;
                        }

                        //get height and width from angle
                        double circleWidth = 2 * lineLength * Math.cos(Math.toRadians(circleAngle));
                        double circleHeight = 2 * lineLength * Math.sin(Math.toRadians(circleAngle));

                        //draw oval
                        gc.strokeOval(xf, yf, Math.abs(circleWidth), Math.abs(circleWidth));
                    });
                    break;
            }
        });

        //Add panes to accordion
        super.getPanes().addAll(pane1, pane2, pane3, pane4, pane5, pane6, pane7, pane8);
    }
}
