package org.miro.test.mirotest.widget;

import org.miro.test.mirotest.LayerHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class WidgetStorage {

    private ConcurrentHashMap<Long, Widget> widgetMap = new ConcurrentHashMap<>();
    private LayerHelper layerHelper = new LayerHelper();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Iterable<Widget> getAll() {
        ArrayList<Widget> sortedWidgets = new ArrayList<>(widgetMap.values());
        sortedWidgets.sort(Comparator.comparingInt(Widget::getZ));
        return sortedWidgets;
    }

    public boolean existZIndex(Integer zIndex) {
        return widgetMap.values().stream().anyMatch(w -> w.getZ().equals(zIndex));
    }

    public long add(Widget widget) {
        Long id = widget.getId();
        if (id == null) {
            id = idCounter.getAndIncrement();
        }
        if (widget.getZ() == null) {
            ArrayList<Widget> sortedWidgets = new ArrayList<>(widgetMap.values());
            sortedWidgets.sort(Comparator.comparingInt(Widget::getZ).reversed());
            int nextZIndex = sortedWidgets.get(0).getZ() + 1;
            widget.setZ(nextZIndex);
        } else if (existZIndex(widget.getZ())) {
            layerHelper.moveWidgets(widget.getZ(), getAll());
        }
        widget.setId(id);
        widget.setLastModified();
        widgetMap.put(id, widget);
        return id;
    }

    public boolean save(long id, Widget widget) {
        Widget existingWidget = widgetMap.get(id);
        if (existingWidget == null ) {
            return false;
        }
        widget.setId(id);
        if (widget.getX() == null) {
            widget.setX(existingWidget.getX());
        }
        if (widget.getY() == null) {
            widget.setY(existingWidget.getY());
        }
        if (widget.getZ() == null) {
            widget.setZ(existingWidget.getZ());
        }
        if (widget.getWidth() == null) {
            widget.setWidth(existingWidget.getWidth());
        }
        if (widget.getHeight() == null) {
            widget.setHeight(existingWidget.getHeight());
        }
        widget.setLastModified();
        widgetMap.replace(id, widget);
        if (existZIndex(widget.getZ()) && !widget.getZ().equals(existingWidget.getZ())) {
            layerHelper.moveWidgets(widget.getZ(), getAll());
        }
        return true;
    }

    public Widget getById(long id) {
        return widgetMap.get(id);
    }

    public boolean deleteById(long id) {
        if (!widgetMap.containsKey(id)) {
            return false;
        }
        widgetMap.remove(id);
        return true;
    }

    public int size() {
        return widgetMap.size();
    }
}
