package com.example.paint;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Timer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.TimerTask;

/**
 *
 * Paint_mainWindow adds content to main window and arranges panes. Contains file menu functions
 * Author: Trent Miller
 *
 */

public class Paint_mainWindow {

    File saveFile;
    String imageType;
    Image image;

    double imageWidth;
    double imageHeight;

    boolean imageOpen = false;

    //Drawing canvas goes inside scroll pane which goes inside tab pane
    Paint_drawingCanvas currentCanvas;

    Paint_tabPane tabPane = new Paint_tabPane();
    GraphicsContext gc;

    //Creating Vbox to contain accordion and go into scroll pane
    VBox leftPane = new VBox();
    //Create scroll pane so tool pane can scroll if needed (when window resized)
    ScrollPane leftScrollPane = new ScrollPane();

    //Create accordionPane
    Paint_accordionPane accordionPane = new Paint_accordionPane();

    //Create int for time - starts at 15 mins (900 s)
    int secondsToAutoSave = 10;

    //Autosave Timer
    Label timerLabel = new Label("Autosaving in: "+secondsToAutoSave/60+":"+secondsToAutoSave%60);

    public Paint_mainWindow(Stage stage) {

        //Creating menu bar
        MenuBar menubar = new MenuBar();
        menubar.setPrefHeight(25);
        menubar.setPrefWidth(Double.MAX_VALUE);

        //Creating an imageView to save file path and file type
        ImageView imageView = new ImageView();

        //Start blank canvas and workspace
        Paint_drawingCanvas startCanvas = new Paint_drawingCanvas(accordionPane);
        ScrollPane startWorkspace = new ScrollPane(startCanvas);
        leftScrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        leftScrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        //first tab upon open
        Paint_newTab startTab = new Paint_newTab("Blank", accordionPane);
        //Add tab to tabPane
        tabPane.addPaintTab(startTab);
        //Set workspace content of tab and canvas content of workspace
        startTab.setWorkspace(startWorkspace);
        startTab.setCanvas(startCanvas);

        //setting workspace content to white blank square upon startup
        int defaultHeightAndWidth = 800;
        startTab.setCanvasWidthAndHeight(defaultHeightAndWidth, defaultHeightAndWidth);
        gc = startTab.drawingCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,defaultHeightAndWidth,defaultHeightAndWidth);

        //set current canvas as start canvas (TEMP - for menu actions)
        currentCanvas = startTab.getCanvas();

        //upon startup, show auto save timer
        //make timer font italic
        timerLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 10));
        //create small box for autosave timer
        HBox timerBox = new HBox();
        timerBox.getChildren().addAll(timerLabel);
        timerBox.setAlignment(Pos.CENTER);

        //add items to left Pane
        leftPane.getChildren().addAll(accordionPane, timerBox);
        //add leftPane to scrollPane
        leftPane.setPrefWidth(200);
        leftPane.setSpacing(10);
        leftScrollPane.setContent(leftPane);

        //Building & Sizing window
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        root.setTop(menubar);
        root.setCenter(tabPane);
        root.setLeft(leftScrollPane);

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

                //Create new tab for image
                String tabName = file.getPath().substring(file.toString().lastIndexOf("\\") + 1);
                Paint_newTab newTab = new Paint_newTab(tabName, accordionPane);

                //create and set workspace and canvas for newTab
                newTab.setWorkspace(new ScrollPane());
                newTab.setCanvas(new Paint_drawingCanvas(accordionPane));
                //set index of new tab to current index + 1
                newTab.setTabIndex(tabPane.getSelectionModel().getSelectedIndex()+1);

                //getting the string following the last period in the string
                imageType = file.getPath().substring(file.toString().lastIndexOf(".") + 1);
                //Getting the chosen image and placing it in the workspace
                Image imageOpened = new Image(stream);
                image = imageOpened;

                //Set height and width of canvas to image
                imageHeight = imageOpened.getHeight();
                imageWidth = imageOpened.getWidth();
                newTab.setCanvasWidthAndHeight(imageWidth, imageHeight);

                //Add tab to tabPane
                tabPane.addPaintTab(newTab);
                gc = newTab.drawingCanvas.getGraphicsContext2D();
                gc.drawImage(imageOpened, 0, 0);

                //set imageOpen to true
                imageOpen = true;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        //===============Save===========================================
        filemenu3.setOnAction(event -> {

            //if saveFile (filepath) is null, go to save as
            if(saveFile==null){
                //Open save as dialog
                FileChooser saveAs = new FileChooser();
                saveAs.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"), new ExtensionFilter("BMP File", "*.bmp"), new ExtensionFilter("JPEG Image", "*.jpg"), new ExtensionFilter("PNG File", "*.png"), new ExtensionFilter("SVG File", "*.svg"));
                File outputFile = saveAs.showSaveDialog(stage);
                String saveAsType = outputFile.getPath().substring(outputFile.toString().lastIndexOf(".") + 1);
                System.out.println(saveAsType);
                //if image save type is svg, warn user of potential losses
                if(saveAsType.equals("svg")){
                    Paint_saveLossWarningPOPUP lossWarningPOPUP = new Paint_saveLossWarningPOPUP();
                    //if save is pressed, save as normal
                    lossWarningPOPUP.yesButton.setOnMousePressed(savePressed ->{
                        WritableImage writableImage1 = new WritableImage((int) currentCanvas.getWidth(), (int) currentCanvas.getHeight());
                        currentCanvas.snapshot(null, writableImage1);
                        RenderedImage renderedImage1 = SwingFXUtils.fromFXImage(writableImage1, null);
                        try {
                            ImageIO.write(renderedImage1, "png", outputFile);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        lossWarningPOPUP.lossWarningStage.close(); //close popup
                        saveFile = outputFile; //set savefile as path selected in save dialog
                    });
                    //if cancel is pressed
                    lossWarningPOPUP.noButton.setOnMousePressed(cancelPressed -> {
                        lossWarningPOPUP.lossWarningStage.close();
                    });
                }else{
                    WritableImage writableImage2 = new WritableImage((int) currentCanvas.getWidth(), (int) currentCanvas.getHeight());
                    currentCanvas.snapshot(null, writableImage2);
                    RenderedImage renderedImage2 = SwingFXUtils.fromFXImage(writableImage2, null);
                    try {
                        ImageIO.write(renderedImage2, "png", outputFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //set saveFile as outputFile
                    saveFile = outputFile;
                }
            }else {
                //else, save as normal
                WritableImage writableImage = new WritableImage((int) currentCanvas.getWidth(), (int) currentCanvas.getHeight());
                currentCanvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                try {
                    //jpg is placeholder here and at smart save, can't get saveType to work
                    ImageIO.write(renderedImage, "png", saveFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            secondsToAutoSave = 900; //reset timer
        });

        //===============Save As========================================
        filemenu4.setOnAction(event -> {
            FileChooser saveAs = new FileChooser();
            saveAs.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"), new ExtensionFilter("BMP File", "*.bmp"), new ExtensionFilter("JPEG Image", "*.jpg"), new ExtensionFilter("PNG File", "*.png"), new ExtensionFilter("SVG File", "*.svg"));
            File outputFile = saveAs.showSaveDialog(stage);
            String saveAsType = outputFile.getPath().substring(outputFile.toString().lastIndexOf(".") + 1);
            System.out.println(saveAsType);
            //if image save type is svg, warn user of potential losses
            if(saveAsType.equals("svg")){
                Paint_saveLossWarningPOPUP lossWarningPOPUP = new Paint_saveLossWarningPOPUP();
                //if save is pressed
                lossWarningPOPUP.yesButton.setOnMousePressed(savePressed ->{
                    try {
                        WritableImage writableImage = new WritableImage((int) currentCanvas.getWidth(), (int) currentCanvas.getHeight());
                        currentCanvas.snapshot(null, writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                        ImageIO.write(renderedImage, "png", outputFile);
                        secondsToAutoSave = 900; //reset timer
                        lossWarningPOPUP.lossWarningStage.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    saveFile = outputFile;
                });
                //if cancel is pressed
                lossWarningPOPUP.noButton.setOnMousePressed(cancelPressed -> {
                   lossWarningPOPUP.lossWarningStage.close();
                });
            }else{
                try {
                    WritableImage writableImage = new WritableImage((int) currentCanvas.getWidth(), (int) currentCanvas.getHeight());
                    currentCanvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "svg", outputFile);
                    secondsToAutoSave = 900; //reset timer
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //===============Exit===========================================
        //Handling the event of the exit menu item
        filemenu5.setOnAction(event ->{

            Paint_smartSavePOPUP smartSave = new Paint_smartSavePOPUP();

            //get graphics context
            gc = currentCanvas.getGraphicsContext2D();

                smartSave.okButton.setOnAction(eventHandled -> {
                    WritableImage writableImage = new WritableImage((int) currentCanvas.getWidth(), (int) currentCanvas.getHeight());
                    currentCanvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    try {
                        ImageIO.write(renderedImage, "png", saveFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //close smart save
                    smartSave.smartSaveStage.close();
                    //Clear canvas and close tab(s)
                    gc.clearRect(0, 0, currentCanvas.getWidth(), currentCanvas.getHeight());
                    //Placeholder, don't want to clear all tabs, only one that is currently open
                    tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
                    imageOpen = false;
                });

                smartSave.noButton.setPrefWidth(50);
                //clear if no button is pressed
                smartSave.noButton.setOnAction(eventHandler -> {
                    //close smart save
                    smartSave.smartSaveStage.close();
                    //Clear canvas and close tab(s)
                    gc.clearRect(0, 0, currentCanvas.getWidth(), currentCanvas.getHeight());
                    //Placeholder, don't want to clear all tabs, only one that is currently open
                    tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
                    imageOpen = false;
                });
        });


        //Edit Menu
        Menu EditMenu = new Menu("_Edit");
        //Options on the edit menu
        MenuItem EditMenu1 = new MenuItem("_Undo");
        EditMenu1.setOnAction(undoPressed ->{
            //before undoing, put current canvas in redo stack
            WritableImage redoImage = new WritableImage(startCanvas.snapshot(null,null).getPixelReader(), 0,0,(int)startTab.drawingCanvas.getWidth(),(int)startTab.drawingCanvas.getHeight());
            startTab.drawingCanvas.redoStack.push(redoImage);

            //when undo button is pressed, get previous canvas and draw on canvas
            Image drawUndoImage = startTab.drawingCanvas.undoStack.pop();
            gc = startTab.drawingCanvas.getGraphicsContext2D();
            gc.drawImage(drawUndoImage, 0, 0);
        });

        MenuItem EditMenu2 = new MenuItem("_Redo");
        EditMenu2.setOnAction(undoPressed ->{
            //before redoing, put current canvas in undo stack
            WritableImage undoImage = new WritableImage(startCanvas.snapshot(null,null).getPixelReader(), 0,0,(int)startTab.drawingCanvas.getWidth(),(int)startTab.drawingCanvas.getHeight());
            startTab.drawingCanvas.undoStack.push(undoImage);

            //when redo button is pressed, get redo canvas and draw on canvas
            Image drawRedoImage = startTab.drawingCanvas.redoStack.pop();
            gc = startTab.drawingCanvas.getGraphicsContext2D();
            gc.drawImage(drawRedoImage, 0, 0);
        });

        MenuItem EditMenu3 = new MenuItem("Paste");

        //Options menu
        Menu OptionsMenu = new Menu("Options");

        //Making image size option resize canvas
        MenuItem OptionsMenu1 = new MenuItem("Resize Canvas");
        OptionsMenu1.setOnAction(eventHandler -> {
            gc = currentCanvas.getGraphicsContext2D();
            Paint_resizeCanvasPOPUP resizeCanvasPOPUP = new Paint_resizeCanvasPOPUP(currentCanvas);
        });

        //clear canvas option
        MenuItem OptionsMenu2 = new MenuItem("Clear Canvas");
        OptionsMenu2.setOnAction(eventHandler -> {
            //Create new stage and scene for text field
            Stage clearCanvasStage = new Stage();
            //Make hbox for labels size by size
            HBox clearCanvasTextBox = new HBox();
            Text clearText = new Text("Clear Canvas?");
            clearCanvasTextBox.getChildren().addAll(clearText);
            //Center and space text
            clearCanvasTextBox.setSpacing(70);
            clearCanvasTextBox.setAlignment(Pos.CENTER);

            //Make hbox for yes and no buttons
            HBox clearYesNoBox = new HBox();
            //Create buttons
            Button clearYesButton = new Button("Yes");
            clearYesButton.setOnAction(eventHandler1 -> {
                gc = currentCanvas.getGraphicsContext2D();
                gc.setFill(Color.WHITE);
                gc.fillRect(0,0, currentCanvas.getWidth(), currentCanvas.getHeight());
                clearCanvasStage.close();
            });
            Button clearNoButton = new Button("No");
            clearNoButton.setOnAction(eventHandler2 -> {
                clearCanvasStage.close();
            });
            //Space out box
            clearYesNoBox.setSpacing(10);
            clearYesNoBox.setPadding(new Insets(10, 10, 10, 10));
            //Add text fields to hbox
            clearYesNoBox.getChildren().addAll(clearYesButton,clearNoButton);
            clearYesNoBox.setAlignment(Pos.CENTER);

            //Vertical box for resize fields and resize button
            VBox resizeBox = new VBox();
            resizeBox.getChildren().addAll(clearCanvasTextBox, clearYesNoBox);
            resizeBox.setAlignment(Pos.CENTER);

            Scene stageScene = new Scene(resizeBox, 200, 75);
            //Set title and resizeable and show
            clearCanvasStage.setTitle("Clear Canvas");
            clearCanvasStage.setResizable(false);
            clearCanvasStage.setScene(stageScene);
            clearCanvasStage.show();
        });


        //Resize image option
        MenuItem OptionsMenu3 = new MenuItem("Resize Image");
        OptionsMenu3.setOnAction(eventHandler -> {
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
                    gc = currentCanvas.getGraphicsContext2D();
                    WritableImage writableImage = new WritableImage((int) currentCanvas.getWidth(), (int) currentCanvas.getHeight());
                    currentCanvas.snapshot(null, writableImage);
                    //set width and height to what is entered in field
                    imageWidth = Double.parseDouble(widthField.getText());
                    imageHeight = Double.parseDouble(heightField.getText());
                    //set canvas width and height
                    currentCanvas.setWidth(imageWidth);
                    currentCanvas.setHeight(imageHeight);
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

        //hide/show autosave timer
        Menu OptionMenu4 = new Menu("Autosave Timer");
        CheckMenuItem showAutoSaveTimer = new CheckMenuItem("Show");
        //by default, show is selected
        showAutoSaveTimer.setSelected(true);
        CheckMenuItem hideAutoSaveTimer = new CheckMenuItem("Hide");
        //when show is pressed, deselect hide and show autosave box
        showAutoSaveTimer.setOnAction(eventHandler ->{
           hideAutoSaveTimer.setSelected(false);
           leftPane.getChildren().add(timerBox);
        });
        //when hide is pressed, deselect show and remove autosave box
        hideAutoSaveTimer.setOnAction(eventHandler ->{
            showAutoSaveTimer.setSelected(false);
            leftPane.getChildren().remove(timerBox);
        });
        //add menuitems to auto save timer submenu
        OptionMenu4.getItems().addAll(showAutoSaveTimer,hideAutoSaveTimer);

        //Create autosave timer and task
        Timer autoSaveTimer = new Timer();
        //update timer value every second
        autoSaveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //subtract off one from count every second & change label
                secondsToAutoSave = secondsToAutoSave - 1;
                //formatting seconds to always have two digits
                String timerRemainder = String.format("%02d", secondsToAutoSave%60);
                new JFXPanel();
                Platform.runLater(() -> timerLabel.setText("Autosaving in: "+secondsToAutoSave/60+":"+timerRemainder));

                //when 15 min is reached, save canvas and reset timer
                if(secondsToAutoSave==0){
                    //resetting timer
                    secondsToAutoSave = 900; //15 min = 900 s
                    //saving current canvas
                    new JFXPanel();
                    Platform.runLater(() ->{
                    WritableImage writableImage = new WritableImage((int) currentCanvas.getWidth(), (int) currentCanvas.getHeight());
                    currentCanvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    //make save file path
                        String timeStamp = new SimpleDateFormat("MM.dd.yyyy.HH.mm.ss").format(new java.util.Date());
                        timeStamp = "C:\\\\Users\\\\User\\\\Pictures\\\\" +"AUTOSAVE."+ timeStamp+ ".png";
                        try {
                        //jpg is placeholder here and at smart save, can't get saveType to work
                        ImageIO.write(renderedImage, "png", new File(timeStamp));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    });
                }
            }
        }, 1000, 1000); //1k = 1 s in ms

        //Help Menu
        Menu HelpMenu = new Menu("Help");
        //Options on the help menu
        MenuItem helpmenu1 = new MenuItem("Help");
        MenuItem helpmenu2 = new MenuItem("About");

        //Adding items to their respective menus
        FileMenu.getItems().addAll(filemenu1, filemenu2, new SeparatorMenuItem(), filemenu3, filemenu4, new SeparatorMenuItem(), filemenu5);
        EditMenu.getItems().addAll(EditMenu1, EditMenu2, new SeparatorMenuItem(), EditMenu3);
        OptionsMenu.getItems().addAll(OptionsMenu1, OptionsMenu2, new SeparatorMenuItem(), OptionsMenu3, new SeparatorMenuItem(), OptionMenu4);
        HelpMenu.getItems().addAll(helpmenu1, helpmenu2);
        //Adding menus to bar
        menubar.getMenus().addAll(FileMenu, EditMenu, OptionsMenu, HelpMenu);

        //Setting up window
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.setTitle("Paint");

        //stop execution of code when window is closed
        stage.setOnCloseRequest(stageClosed -> {
            System.exit(0);
        });

        //Showing Main Window
        stage.show();

    }
}
