package com.example.paint;
import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import javafx.application.Platform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class Paint_unitTesting {

    //initialize tab, accordion, & canvas
    Paint_newTab testTab;
    Paint_accordionPane testAccordion;
    Paint_drawingCanvas testCanvas;

    @Before
    public void setup(){
        new JFXPanel();
        testAccordion = new Paint_accordionPane();
        testCanvas = new Paint_drawingCanvas(testAccordion);
        testTab = new Paint_newTab("Test", testAccordion);
    }

    @Test
    //1st test pushes item into undo stack and checks if stack is empty
    public void test1(){
        Image testImage = new Image("C:\\\\Users\\\\Owner\\\\Pictures\\\\Spongebank.jpg");
        testCanvas.undoStack.push(testImage);
        assertFalse(testCanvas.undoStack.isEmpty());
    }

    @Test
    //2nd test makes sure drawOption is 0 upon startup
    public void test2(){
        assertEquals(0,testAccordion.getDrawOption());
    }

    @Test
    //3rd test makes sure setting and getting tab index works properly
    public void test3(){
        testTab.setTabIndex(13);
        assertEquals(13,testTab.getTabIndex());
    }

}
