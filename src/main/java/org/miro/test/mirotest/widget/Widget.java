package org.miro.test.mirotest.widget;

import java.util.Date;

public class Widget {

    private Long id;
    private Integer x, y, z;
    private Double width, height;
    private Date lastModified;

    public Widget() {}

    public Widget(Long id, Integer x, Integer y, Integer z, Double width, Double height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.lastModified = new Date();
    }

    public Long getId() {
        return id;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    public Date getLastModified() {
        return lastModified;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public void setXCoordinate(Integer xCoordinate) {
        this.x = xCoordinate;
    }

    public void setYCoordinate(Integer yCoordinate) {
        this.y = yCoordinate;
    }

    public void setZIndex(Integer zIndex) {
        this.z = zIndex;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    protected void setLastModified() {
        this.lastModified = new Date();
    }
}