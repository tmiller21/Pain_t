package com.example.paint;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

import java.util.Stack;

/**
 * Paint_drawingCanvas is an extension of canvas that contains the logic for drawing on it
 * Canvas is a node
 *
 * Author: Trent Miller
 */

public class Paint_drawingCanvas extends Canvas {

    double x1, y1, x2, y2, xf, yf;
    Image copiedImage;
    Color lineColor;
    GraphicsContext gc;
    Paint_accordionPane accordionPane;
    int drawOption;
    Stack<Image> undoStack = new Stack<>();
    Stack<Image> redoStack = new Stack<>();

    public Paint_drawingCanvas(Paint_accordionPane mainAccordionPane){

        //make accordionPane for this canvas the main accordion pane
        accordionPane = mainAccordionPane;

        //Add drawing actions for canvas
        this.setOnMousePressed(mousePressed -> {

            //Get first position
            x1 = mousePressed.getX();
            y1 = mousePressed.getY();

            //call draw method
            draw(mainAccordionPane, x1, y1);
        });
    }

    public void draw(Paint_accordionPane accordionPane, double x1, double y1) {

        //get draw option
        drawOption = accordionPane.getDrawOption();

        //get graphics context
        gc = this.getGraphicsContext2D();

        //if using selection tool and paste is selected
        if(drawOption == 11) {
            if (accordionPane.pasteButton.isSelected()) {
                gc = this.getGraphicsContext2D();
                gc.drawImage(copiedImage, x1, y1);
                //take snapshot and push into redo stack
                WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                redoStack.push(redoImage);
            }
        }

        //push current snapshot into undo stack
        WritableImage undoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
        undoStack.push(undoImage);

        //set line color
        lineColor = accordionPane.colorPicker.getValue();
        gc.setStroke(lineColor);

        switch(drawOption){

            //case 0 = do nothing
            case 0:
                this.setOnMouseDragged(mouseDragged -> {
                });
                this.setOnMouseReleased(mouseReleased -> {
                });
                break;

            //Case 1 = draw line
            case 1:

                //set cursor back to normal
                this.setCursor(Cursor.DEFAULT);

                //get slider value
                gc.setLineWidth(accordionPane.pane1.lineSlider.getValue());

                //start path for straight line
                gc.beginPath();
                gc.moveTo(x1, y1);

                //if straight is pressed
                if (accordionPane.pane1.lineStraightButton.isSelected()) {
                    this.setOnMouseDragged(mouseDragged -> {
                    });
                    //When released, get ending position and draw path
                    this.setOnMouseReleased(mouseReleased -> {
                        //solid straight line
                        if (accordionPane.pane1.lineSdSwitch.solidButton.isSelected()) {
                            //set dashes to zero
                            gc.setLineDashes();
                        } else if (accordionPane.pane1.lineSdSwitch.dashedButton.isSelected()) {
                            //dashed straight line
                            gc.setLineDashes(30);
                        }
                        gc.lineTo(mouseReleased.getX(), mouseReleased.getY());
                        gc.stroke();
                        //take snapshot and push into redo stack
                        WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                        redoStack.push(redoImage);
                    });
                }

                if (accordionPane.pane1.lineCurvedButton.isSelected()) {
                    //When released, get ending position and draw path
                    this.setOnMouseDragged(mouseDragged -> {
                        if (accordionPane.pane1.lineSdSwitch.solidButton.isSelected()) {
                            //set dashes to zero
                            gc.setLineDashes();
                        } else if (accordionPane.pane1.lineSdSwitch.dashedButton.isSelected()) {
                            gc.setLineDashes(30);
                        }
                        gc.lineTo(mouseDragged.getX(), mouseDragged.getY());
                        gc.stroke();
                    });
                    this.setOnMouseReleased(mouseReleased -> {
                        gc.lineTo(mouseReleased.getX(), mouseReleased.getY());
                        gc.stroke();
                        //take snapshot and push into redo stack
                        WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                        redoStack.push(redoImage);
                    });
                }
                break;

            //Case 2 = grab color
            case 2:

                //make robot to get color at position
                Robot robot = new Robot();
                Color grabbedColor = robot.getPixelColor(x1, y1);

                //setting color picker value to grab color
                accordionPane.colorPicker.setValue(grabbedColor);

                //set cursor back to normal
                this.setCursor(Cursor.DEFAULT);

                this.setOnMouseDragged(mouseDragged ->{
                });
                this.setOnMouseReleased(mouseReleased -> {
                });
                break;

            //Case 3 = erase
            case 3:

                //set default cursor
                this.setCursor(Cursor.DEFAULT);

                //set color to white & dashes to null
                gc.setStroke(Color.WHITE);
                gc.setLineDashes();

                //set width to slider value
                gc.setLineWidth(accordionPane.eraserSlider.getValue());

                //start path for eraser line
                gc.beginPath();
                gc.moveTo(x1, y1);

                this.setOnMouseDragged(mouseDragged -> {
                    gc.lineTo(mouseDragged.getX(), mouseDragged.getY());
                    gc.stroke();
                });
                this.setOnMouseReleased(mouseReleased -> {
                    gc.lineTo(mouseReleased.getX(), mouseReleased.getY());
                    gc.stroke();
                    //take snapshot and push into redo stack
                    WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                    redoStack.push(redoImage);
                });
                break;

            //Case 4 = rectangle
            case 4:

                //set cursor back to normal
                this.setCursor(Cursor.DEFAULT);

                //get width
                gc.setLineWidth(accordionPane.pane4.rectangleSlider.getValue());

                //start path for straight line
                gc.beginPath();
                gc.moveTo(x1, y1);

                this.setOnMouseDragged(mouseDragged -> {
                });

                //When released, get ending position and draw path
                this.setOnMouseReleased(mouseReleased -> {
                    if (accordionPane.pane4.rectangleSdSwitch.solidButton.isSelected()) {
                        //set dashes to zero
                        gc.setLineDashes();
                    } else if (accordionPane.pane4.rectangleSdSwitch.dashedButton.isSelected()) {
                        //dashed rectangle
                        gc.setLineDashes(30);
                    }

                    //Ensuring that rectangle is always drawn in direction user draws it
                    xf = Math.min(mouseReleased.getX(), x1);
                    yf = Math.min(mouseReleased.getY(), y1);
                    //get height and width
                    double rectWidth = Math.abs(mouseReleased.getX() - x1);
                    double rectHeight = Math.abs(mouseReleased.getY() - y1);

                    if (accordionPane.pane4.rectSharpButton.isSelected()) {
                        gc.strokeRect(xf, yf, rectWidth, rectHeight);
                    } else if (accordionPane.pane4.rectRoundedButton.isSelected()) {
                        gc.strokeRoundRect(xf, yf, rectWidth, rectHeight, rectWidth / 5, rectHeight / 5);
                    }
                    //take snapshot and push into redo stack
                    WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                    redoStack.push(redoImage);
                });
                break;

            //Case 5 = square
            case 5:

                //set cursor back to normal
                this.setCursor(Cursor.DEFAULT);

                //get width
                gc.setLineWidth(accordionPane.squareSlider.getValue());

                //start path for straight line
                gc.beginPath();
                gc.moveTo(x1, y1);

                this.setOnMouseDragged(mouseDragged -> {
                });

                //When released, get ending position and draw path
                this.setOnMouseReleased(mouseReleased -> {
                    //solid square
                    if (accordionPane.squareSdSwitch.solidButton.isSelected()) {
                        //set dashes to zero
                        gc.setLineDashes();
                    } else if (accordionPane.squareSdSwitch.dashedButton.isSelected()) {
                        //dashed square
                        gc.setLineDashes(30);
                    }

                    //Ensuring that square is always drawn in direction user draws it
                    xf = Math.min(mouseReleased.getX(), x1);
                    yf = Math.min(mouseReleased.getY(), y1);
                    //get height and width
                    double squareDim = Math.abs(mouseReleased.getX() - x1);

                    if (accordionPane.squareSharpButton.isSelected()) {
                        gc.strokeRect(xf, yf, squareDim, squareDim);
                    } else if (accordionPane.squareRoundedButton.isSelected()) {
                        gc.strokeRoundRect(xf, yf, squareDim, squareDim, squareDim / 5, squareDim / 5);
                    }
                    //take snapshot and push into redo stack
                    WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                    redoStack.push(redoImage);
                });
                break;

            //Case 6 = triangle
            case 6:

                //set cursor back to normal
                this.setCursor(Cursor.DEFAULT);

                //get width
                gc.setLineWidth(accordionPane.pane6.triangleSlider.getValue());

                //start path for straight line
                gc.beginPath();
                gc.moveTo(x1, y1);

                this.setOnMouseDragged(mouseDragged -> {
                });

                this.setOnMouseReleased(mouseReleased -> {
                    //Ensuring that rectangle is always drawn in direction user draws it
                    x2 = mouseReleased.getX();
                    y2 = mouseReleased.getY();

                    if (accordionPane.pane6.triangleSdSwitch.solidButton.isSelected()) {
                        //set dashes to zero
                        gc.setLineDashes();
                    } else if (accordionPane.pane6.triangleSdSwitch.dashedButton.isSelected()) {
                        //dashed
                        gc.setLineDashes(30);
                    }
                    //making path for triangle
                    //Go from first spot, to right angle, to second spot, and complete
                    gc.lineTo(x2, y1);
                    gc.lineTo(x2, y2);
                    gc.closePath();
                    gc.stroke();
                    //take snapshot and push into redo stack
                    WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                    redoStack.push(redoImage);
                });
                break;

            //Case 7 = ellipse
            case 7:

                //set cursor back to normal
                this.setCursor(Cursor.DEFAULT);

                //get slider value
                gc.setLineWidth(accordionPane.ellipseSlider.getValue());

                //start path for straight line
                gc.beginPath();
                gc.moveTo(x1, y1);

                this.setOnMouseDragged(mouseDragged -> {
                });

                this.setOnMouseReleased(mouseReleased -> {
                    //Ensuring that rectangle is always drawn in direction user draws it
                    x2 = mouseReleased.getX();
                    y2 = mouseReleased.getY();

                    if (accordionPane.ellipseSdSwitch.solidButton.isSelected()) {
                        //set dashes to zero
                        gc.setLineDashes();
                    } else if (accordionPane.ellipseSdSwitch.dashedButton.isSelected()) {
                        //dashed
                        gc.setLineDashes(30);
                    }

                    //Ensuring that ellipse is always drawn in direction user draws it
                    xf = Math.min(x2, x1);
                    yf = Math.min(y2, y1);

                    //get length of line
                    double lineLength = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));

                    //get angle of line
                    double ellipseAngle = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
                    if (ellipseAngle < 0) {
                        ellipseAngle += 360;
                    }

                    //get height and width from angle
                    double ellipseWidth = 2 * lineLength * Math.cos(Math.toRadians(ellipseAngle));
                    double ellipseHeight = 2 * lineLength * Math.sin(Math.toRadians(ellipseAngle));

                    //draw oval
                    gc.strokeOval(xf, yf, Math.abs(ellipseWidth), Math.abs(ellipseHeight));
                    //take snapshot and push into redo stack
                    WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                    redoStack.push(redoImage);
                });
                break;

            //Case 8 = circle
            case 8:

                //set cursor back to normal
                this.setCursor(Cursor.DEFAULT);

                //get slider value
                gc.setLineWidth(accordionPane.circleSlider.getValue());

                //start path for straight line
                gc.beginPath();
                gc.moveTo(x1, y1);

                this.setOnMouseDragged(mouseDragged -> {
                });

                this.setOnMouseReleased(mouseReleased -> {
                    //Ensuring that rectangle is always drawn in direction user draws it
                    x2 = mouseReleased.getX();
                    y2 = mouseReleased.getY();

                    if (accordionPane.circleSdSwitch.solidButton.isSelected()) {
                        //set dashes to zero
                        gc.setLineDashes();
                    } else if (accordionPane.circleSdSwitch.dashedButton.isSelected()) {
                        //dashed
                        gc.setLineDashes(30);
                    }

                    //Ensuring that ellipse is always drawn in direction user draws it
                    xf = Math.min(x2, x1);
                    yf = Math.min(y2, y1);

                    //get length of line
                    double lineLength = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));

                    //get angle of line
                    double circleAngle = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
                    if (circleAngle < 0) {
                        circleAngle += 360;
                    }

                    //get height and width from angle
                    double circleWidth = 2 * lineLength * Math.cos(Math.toRadians(circleAngle));
                    double circleHeight = 2 * lineLength * Math.sin(Math.toRadians(circleAngle));

                    //draw oval
                    gc.strokeOval(xf, yf, Math.abs(circleWidth), Math.abs(circleWidth));
                    //take snapshot and push into redo stack
                    WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                    redoStack.push(redoImage);
                });
                break;

            //Case 9 = regular polygon
            case 9:

                //set cursor back to normal
                this.setCursor(Cursor.DEFAULT);

                //get slider value
                gc.setLineWidth(accordionPane.pane9.polygonSlider.getValue());

                //start path for straight line
                gc.beginPath();
                gc.moveTo(x1, y1);

                //get sides from field
                int polySides;
                polySides = Integer.parseInt(accordionPane.pane9.polygonSidesField.getText());

                this.setOnMouseDragged(mouseDragged -> {
                });

                this.setOnMouseReleased(mouseReleased -> {
                    //Ensuring that rectangle is always drawn in direction user draws it
                    x2 = mouseReleased.getX();
                    y2 = mouseReleased.getY();

                    if (accordionPane.pane9.polygonSdSwitch.solidButton.isSelected()) {
                        //set dashes to zero
                        gc.setLineDashes();
                    } else if (accordionPane.pane9.polygonSdSwitch.dashedButton.isSelected()) {
                        //dashed
                        gc.setLineDashes(30);
                    }

                    //creating x and y array for points
                    double[] xPoints = new double[polySides + 1];
                    double[] yPoints = new double[polySides + 1];

                    //get length of line
                    double lineLength = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));

                    //start & end at theta=0, first point = x1+length, y1 (go straight to the right from 1st point)
                    double xo = x1 + lineLength;
                    xPoints[polySides - 1] = xo; //last element is first point
                    double yo = y1;
                    yPoints[polySides - 1] = yo; //last element is first point

                    //go in circle grabbing n points at equal intervals and filling array
                    double deltaTheta = (2 * Math.PI) / polySides;
                    for (int i = 0; i < polySides; ++i) {
                        double x = x1 + (lineLength * Math.cos(deltaTheta * i));
                        double y = y1 - (lineLength * Math.sin(deltaTheta * i));
                        xPoints[i] = x;
                        yPoints[i] = y;
                    }

                    //draw polygon
                    gc.strokePolygon(xPoints, yPoints, polySides);
                    //take snapshot and push into redo stack
                    WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                    redoStack.push(redoImage);
                });
                break;

            //option 10 = user-input text
            case 10:

                //set cursor back to normal
                this.setCursor(Cursor.DEFAULT);
                this.setOnMouseDragged(mouseDragged -> {
                });
                this.setOnMouseReleased(mouseReleased -> {
                    //initiate string to be used for text
                    Paint_inputTextPOPUP inputTextPOPUP = new Paint_inputTextPOPUP(x1, y1, gc);
                    //take snapshot and push into redo stack
                    WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                    redoStack.push(redoImage);
                });
                break;

            //option 11 = select area & move or copy it
            case 11:

                //set cursor back to normal
                this.setCursor(Cursor.DEFAULT);
                this.setOnMouseDragged(mouseDragged -> {
                    //drag to move selected area
                });
                this.setOnMouseReleased(mouseReleased -> {
                    //get second position
                    x2 = mouseReleased.getX();
                    y2 = mouseReleased.getY();
                    //get position of area
                    double selectionX = Math.min(x1,x2);
                    double selectionY = Math.min(y1,y2);

                    //get width and height of area
                    double selectionWidth = Math.abs(x1-x2);
                    double selectionHeight = Math.abs(y1-y2);

                    //if paste button isn't selected, get image
                    if (!accordionPane.pasteButton.isSelected()){
                        //get image of selected area
                        copiedImage = new WritableImage(this.snapshot(null, null).getPixelReader(), (int) selectionX, (int) selectionY, (int) selectionWidth, (int) selectionHeight);

                        if (accordionPane.cutSelectionButton.isSelected()) {
                            //clear area selected if cutting
                            gc.setFill(Color.WHITE);
                            gc.fillRect(selectionX,selectionY,selectionWidth,selectionHeight);
                            //take snapshot and push into redo stack
                            WritableImage redoImage = new WritableImage(this.snapshot(null,null).getPixelReader(), 0,0,(int)this.getWidth(),(int)this.getHeight());
                            redoStack.push(redoImage);
                        }
                    }
                });
                break;
        }
    }
}
