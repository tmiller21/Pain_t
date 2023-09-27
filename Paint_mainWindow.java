package com.example.paint;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Paint_mainWindow {

    File saveFile;
    String imageType;
    Image image;

    double imageWidth;
    double imageHeight;

    boolean imageOpen = false;

    //Drawing canvas goes inside scroll pane which goes inside tab pane
    Canvas drawingCanvas = new Canvas();
    ScrollPane workspace = new ScrollPane(drawingCanvas);
    TabPane tabPane = new TabPane();
    GraphicsContext gc;

    public Paint_mainWindow(Stage stage) {

        //Creating menu bar
        MenuBar menubar = new MenuBar();
        menubar.setPrefHeight(25);
        menubar.setPrefWidth(Double.MAX_VALUE);

        //Creating an imageView to save file path and file type
        ImageView imageView = new ImageView();

        //Creating Vbox to contain accordion and go into scroll pane
        VBox toolPane = new VBox();
        //Create scroll pane so tool pane can scroll if needed (when window resized)
        ScrollPane toolScrollPane = new ScrollPane(toolPane);
        toolScrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        toolScrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        //Create accordion (see pain_accordionPane) and add to toolPane
        Paint_accordionPane accordionPane = new Paint_accordionPane(drawingCanvas, workspace);
        toolPane.getChildren().addAll(accordionPane);
        toolPane.setPrefWidth(200);

        //workspace can pan, fits image to its height, and only shows scroll bars as needed
        workspace.setPannable(false);
        workspace.setFitToHeight(true);
        workspace.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        workspace.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        //first tab upon open
        Tab startTab = new Tab("Blank", new Label("Canvas"));
        //Add tab to tabPane
        tabPane.getTabs().addAll(startTab);
        //Set workspace content of tab and canvas content of workspace
        startTab.setContent(workspace);
        workspace.setContent(drawingCanvas);
        //setting workspace content to white blank square upon startup
        /*int defaultHeightAndWidth = 800;
        drawingCanvas.setWidth(defaultHeightAndWidth);
        drawingCanvas.setHeight(defaultHeightAndWidth);
        gc = drawingCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,defaultHeightAndWidth,defaultHeightAndWidth);*/

        //Building & Sizing window
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        root.setTop(menubar);
        root.setCenter(tabPane);
        root.setLeft(toolScrollPane);

        //File Menu
        Menu FileMenu = new Menu("_File");
        //Options on the file menu
        MenuItem filemenu1 = new MenuItem("_New");
        MenuItem filemenu2 = new MenuItem("_Open Image");
        MenuItem filemenu3 = new MenuItem("_Save");
        MenuItem filemenu4 = new MenuItem("Save As");
        MenuItem filemenu5 = new MenuItem("_Exit");

        //===============Open Image=============================================
        //Creating a File chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"), new ExtensionFilter("BMP File", "*.bmp"), new ExtensionFilter("JPEG Image", "*.jpg"), new ExtensionFilter("PNG File", "*.png"));
        //Setting the image view parameters
        imageView.setPreserveRatio(true);
        //Handling the event of the Open Image
        filemenu2.setOnAction(event -> {
            try {
                //Creating file chooser, getting file and filetype
                File file = fileChooser.showOpenDialog(stage);
                fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"), new ExtensionFilter("BMP File", "*.bmp"), new ExtensionFilter("JPEG Image", "*.jpg"), new ExtensionFilter("PNG File", "*.png"));
                saveFile = file;
                InputStream stream = new FileInputStream(file);
                //getting the string following the last period in the string
                imageType = file.getPath().substring(file.toString().lastIndexOf(".") + 1);
                //Getting the chosen image and placing it in the workspace
                Image imageOpened = new Image(stream);
                image = imageOpened;
                //Set height and width of canvas to image
                imageHeight = imageOpened.getHeight();
                drawingCanvas.setHeight(imageHeight);
                imageWidth = imageOpened.getWidth();
                drawingCanvas.setWidth(imageWidth);
                //Create new tab for image
                String tabName = file.getPath().substring(file.toString().lastIndexOf("\\") + 1);
                Tab newTab = new Tab(tabName, new Label(image.getUrl()));
                //Add tab to tabPane
                tabPane.getTabs().addAll(newTab);
                //Set workspace content of tab and canvas content of workspace
                newTab.setContent(workspace);
                workspace.setContent(drawingCanvas);
                gc = drawingCanvas.getGraphicsContext2D();
                gc.drawImage(imageOpened, 0, 0);
                //set imageOpen to true
                imageOpen = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        //===============Save===========================================
        filemenu3.setOnAction(event -> {
            WritableImage writableImage = new WritableImage((int) drawingCanvas.getWidth(), (int) drawingCanvas.getHeight());
            drawingCanvas.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            try {
                //jpg is placeholder here and at smart save, can't get saveType to work
                ImageIO.write(renderedImage, "png", saveFile);
                System.out.println(imageType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //===============Save As========================================
        filemenu4.setOnAction(event -> {
            FileChooser saveAs = new FileChooser();
            saveAs.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"), new ExtensionFilter("BMP File", "*.bmp"), new ExtensionFilter("JPEG Image", "*.jpg"), new ExtensionFilter("PNG File", "*.png"));
            File outputFile = saveAs.showSaveDialog(stage);
            //BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
            WritableImage writableImage = new WritableImage((int) drawingCanvas.getWidth(), (int) drawingCanvas.getHeight());
            drawingCanvas.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            try {
                ImageIO.write(renderedImage, "png", outputFile);
                System.out.println("." + imageType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            saveFile = outputFile;
        });

        //===============Exit===========================================
        //Handling the event of the exit menu item
        filemenu5.setOnAction(event ->{

            if (imageOpen) {
                //SMART SAVE
                Stage smartSaveStage = new Stage();

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

                //If ok button is pressed, save as normal
                Button okButton = new Button("Ok");
                okButton.setPrefWidth(50);
                //Save and clear if ok button is pressed
                okButton.setOnAction(eventHandled -> {
                    WritableImage writableImage = new WritableImage((int) drawingCanvas.getWidth(), (int) drawingCanvas.getHeight());
                    drawingCanvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    try {
                        ImageIO.write(renderedImage, "png", saveFile);
                        System.out.println("save!!");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //close smart save
                    smartSaveStage.close();
                    //Clear canvas and close tab(s)
                    gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
                    //Placeholder, don't want to clear all tabs, only one that is currently open
                    tabPane.getTabs().clear();
                    imageOpen = false;
                });

                //If no button is pressed, close window
                Button noButton = new Button("No");
                noButton.setPrefWidth(50);
                //clear if no button is pressed
                noButton.setOnAction(eventHandler -> {
                    //close smart save
                    smartSaveStage.close();
                    //Clear canvas and close tab(s)
                    gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
                    //Placeholder, don't want to clear all tabs, only one that is currently open
                    tabPane.getTabs().clear();
                    imageOpen = false;
                });

                //Design button box
                okNoBox.getChildren().addAll(okButton, noButton);
                okNoBox.setAlignment(Pos.CENTER);
                okNoBox.setSpacing(20);
                okNoBox.setPadding(new Insets(10, 10, 10, 10));

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
        });

        //Edit Menu
        Menu EditMenu = new Menu("Edit");
        //Options on the edit menu
        MenuItem EditMenu1 = new MenuItem("Cut");
        MenuItem EditMenu2 = new MenuItem("Copy");
        MenuItem EditMenu3 = new MenuItem("Paste");

        //Options menu
        Menu OptionsMenu = new Menu("Options");

        //Making image size option resize canvas
        MenuItem OptionsMenu1 = new MenuItem("Resize Canvas");
        OptionsMenu1.setOnAction(eventHandler -> {
            //Create new stage and scene for text field
            Stage resizeCanvasStage = new Stage();
            //Make hbox for labels size by size
            HBox fieldLabelBox = new HBox();
            Text widthText = new Text("Width");
            Text heightText = new Text("Height");
            fieldLabelBox.getChildren().addAll(widthText, heightText);
            //Center and space text
            fieldLabelBox.setSpacing(70);
            fieldLabelBox.setAlignment(Pos.CENTER);

            //Make hbox for fields side by side
            HBox fieldBox = new HBox();
            //Create text fields
            TextField widthField = new TextField(Double.toString(drawingCanvas.getWidth()));
            //Make x to go between height and width
            Text fieldX = new Text("X");
            TextField heightField = new TextField(Double.toString(drawingCanvas.getHeight()));
            //Space out box
            fieldBox.setSpacing(10);
            fieldBox.setPadding(new Insets(10,10,10,10));
            //Add text fields to hbox
            fieldBox.getChildren().addAll(widthField, fieldX, heightField);
            fieldBox.setAlignment(Pos.CENTER);

            //Make resize button
            Button resizeButton = new Button("Resize");
            //Make button resize image to what is in fields when it is a double
            resizeButton.setOnAction(evenhanded ->{
                try{
                    imageWidth = Double.parseDouble(widthField.getText());
                    imageHeight = Double.parseDouble(heightField.getText());
                    //get graphic context
                    gc = drawingCanvas.getGraphicsContext2D();
                    //set canvas width and height
                    drawingCanvas.setWidth(imageWidth);
                    drawingCanvas.setHeight(imageHeight);
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
            });

            //Vertical box for resize fields and resize button
            VBox resizeBox = new VBox();
            resizeBox.getChildren().addAll(fieldLabelBox, fieldBox, resizeButton);
            resizeBox.setAlignment(Pos.CENTER);

            Scene stageScene = new Scene(resizeBox, 200, 100);
            //Set title and resizeable and show
            resizeCanvasStage.setTitle("Resize Canvas");
            resizeCanvasStage.setResizable(false);
            resizeCanvasStage.setScene(stageScene);
            resizeCanvasStage.show();
        });

        //Resize image option
        MenuItem OptionsMenu2 = new MenuItem("Resize Image");
        OptionsMenu2.setOnAction(eventHandler -> {
            //Create new stage and scene for text field
            Stage resizeImageStage = new Stage();
            //Make hbox for labels size by size
            HBox fieldLabelBox = new HBox();
            Text widthText = new Text("Width");
            Text heightText = new Text("Height");
            fieldLabelBox.getChildren().addAll(widthText, heightText);
            //Center and space text
            fieldLabelBox.setSpacing(70);
            fieldLabelBox.setAlignment(Pos.CENTER);

            //Make hbox for fields side by side
            HBox fieldBox = new HBox();
            //Create text fields
            TextField widthField = new TextField(Double.toString(imageWidth));
            //Make x to go between height and width
            Text fieldX = new Text("X");
            TextField heightField = new TextField(Double.toString(imageHeight));
            //Space out box
            fieldBox.setSpacing(10);
            fieldBox.setPadding(new Insets(10, 10, 10, 10));
            //Add text fields to hbox
            fieldBox.getChildren().addAll(widthField, fieldX, heightField);
            fieldBox.setAlignment(Pos.CENTER);

            //Make resize button
            Button resizeButton = new Button("Resize");
            //Make button resize image to what is in fields when it is a double
            resizeButton.setOnAction(evenhanded -> {
                try {
                    //Get context and snapshot from canvas before resizing
                    gc = drawingCanvas.getGraphicsContext2D();
                    WritableImage writableImage = new WritableImage((int) drawingCanvas.getWidth(), (int) drawingCanvas.getHeight());
                    drawingCanvas.snapshot(null, writableImage);
                    //set width and height to what is entered in field
                    imageWidth = Double.parseDouble(widthField.getText());
                    imageHeight = Double.parseDouble(heightField.getText());
                    //set canvas width and height
                    drawingCanvas.setWidth(imageWidth);
                    drawingCanvas.setHeight(imageHeight);
                    //redraw image at new size in resized canvas
                    writableImage.widthProperty().isEqualTo((long) imageWidth);
                    writableImage.heightProperty().isEqualTo((long) imageHeight);
                    RenderedImage resizedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    gc.drawImage((Image) resizedImage,0,0,imageWidth,imageHeight);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            //Vertical box for resize fields and resize button
            VBox resizeBox = new VBox();
            resizeBox.getChildren().addAll(fieldLabelBox, fieldBox, resizeButton);
            resizeBox.setAlignment(Pos.CENTER);

            Scene stageScene = new Scene(resizeBox, 200, 100);
            //Set title and resizeable and show
            resizeImageStage.setTitle("Resize Image");
            resizeImageStage.setResizable(false);
            resizeImageStage.setScene(stageScene);
            resizeImageStage.show();
        });


        //Help Menu
        Menu HelpMenu = new Menu("Help");
        //Options on the help menu
        MenuItem helpmenu1 = new MenuItem("Help");
        MenuItem helpmenu2 = new MenuItem("About");

        //Adding items to their respective menus
        EditMenu.getItems().addAll(EditMenu1, EditMenu2, EditMenu3);
        FileMenu.getItems().addAll(filemenu1, filemenu2, filemenu3, filemenu4, filemenu5);
        OptionsMenu.getItems().addAll(OptionsMenu1, OptionsMenu2);
        HelpMenu.getItems().addAll(helpmenu1, helpmenu2);
        //Adding menus to bar
        menubar.getMenus().addAll(FileMenu, EditMenu, OptionsMenu, HelpMenu);

        //Setting up window
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.setTitle("Paint");


        //Showing Main Window
        stage.show();
    }
}
