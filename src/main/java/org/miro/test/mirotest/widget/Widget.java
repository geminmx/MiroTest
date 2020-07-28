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

    public Widget(Integer x, Integer y, Integer z, Double width, Double height) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.lastModified = new Date();
    }

    public Widget(Widget widget) {
        this.id = widget.getId();
        this.x = widget.getX();
        this.y = widget.getY();
        this.z = widget.getZ();
        this.width = widget.getWidth();
        this.height = widget.getHeight();
        this.lastModified = widget.getLastModified();
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

    public void setX(Integer xCoordinate) {
        this.x = xCoordinate;
    }

    public void setY(Integer yCoordinate) {
        this.y = yCoordinate;
    }

    public void setZ(Integer zIndex) {
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
