package org.miro.test.mirotest.widget;

import org.miro.test.mirotest.LayerHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WidgetStorage {

    private ConcurrentHashMap<Long, Widget> widgetMap = new ConcurrentHashMap<>();
    private LayerHelper layerHelper = new LayerHelper();
    private final AtomicLong idCounter = new AtomicLong(1);
    ReadWriteLock lock = new ReentrantReadWriteLock();

    public Iterable<Widget> getAll() {
        lock.readLock().lock();
        try {
            ArrayList<Widget> sortedWidgets = new ArrayList<>(widgetMap.values());
            sortedWidgets.sort(Comparator.comparingInt(Widget::getZ));
            return sortedWidgets;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean existZIndex(Integer zIndex) {
        lock.readLock().lock();
        try {
            return widgetMap.values().stream().anyMatch(w -> w.getZ().equals(zIndex));
        } finally {
            lock.readLock().unlock();
        }
    }

    public long add(Widget widget) {
        lock.writeLock().lock();
        try {
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
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean save(long id, Widget widget) {
        lock.writeLock().lock();
        try {
            Widget existingWidget = widgetMap.get(id);
            if (existingWidget == null) {
                return false;
            }
            if (existZIndex(widget.getZ()) && !widget.getZ().equals(existingWidget.getZ())) {
                layerHelper.moveWidgets(widget.getZ(), getAll());
            }
            if (widget.getX() != null) {
                existingWidget.setX(widget.getX());
            }
            if (widget.getY() != null) {
                existingWidget.setY(widget.getY());
            }
            if (widget.getZ() != null) {
                existingWidget.setZ(widget.getZ());
            }
            if (widget.getWidth() != null) {
                existingWidget.setWidth(widget.getWidth());
            }
            if (widget.getHeight() != null) {
                existingWidget.setHeight(widget.getHeight());
            }
            existingWidget.setLastModified();
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Widget getById(long id) {
        lock.readLock().lock();
        try {
            return widgetMap.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean deleteById(long id) {
        lock.writeLock().lock();
        try {
            if (!widgetMap.containsKey(id)) {
                return false;
            }
            widgetMap.remove(id);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public long size() {
        lock.readLock().lock();
        try {
            return widgetMap.mappingCount();
        } finally {
            lock.readLock().unlock();
        }
    }
}
