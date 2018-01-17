package com.jyss.bacon.entity;


import java.io.Serializable;
import java.util.List;

public class Category implements Serializable{


    private String dwName;
    private List<ItemCat> names;

    public String getDwName() {
        return dwName;
    }

    public void setDwName(String dwName) {
        this.dwName = dwName;
    }

    public List<ItemCat> getNames() {
        return names;
    }

    public void setNames(List<ItemCat> names) {
        this.names = names;
    }
}
