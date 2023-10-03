package com.example.paint;
import javafx.scene.control.TabPane;

import java.util.ArrayList;
import java.util.List;

public class Paint_tabPane extends TabPane {

    List<Paint_newTab> tabList = new ArrayList<>();

    public Paint_tabPane(){
    }

    public void addPaintTab(Paint_newTab newTab){
        newTab.setTabIndex(this.getSelectionModel().getSelectedIndex()+1);
        tabList.add(newTab);
        this.getTabs().add(tabList.get(this.getSelectionModel().getSelectedIndex()+1));
    }

    public void removeCurrentPaintTab(){
        this.getTabs().remove(this.getSelectionModel().getSelectedIndex());
    }

    public void removePaintTab(int removeIndex){
        this.getTabs().remove(removeIndex);
    }

    public Paint_newTab getCurrentTab(){
        return tabList.get(this.getSelectionModel().getSelectedIndex());
    }

}
