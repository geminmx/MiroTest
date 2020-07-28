package org.miro.test.mirotest.widget;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class WidgetStorage {

    private Map<Long, Widget> widgetMap = new HashMap<>();
    private long idCounter = 1L;
    private int nextZIndex = 1;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public WidgetStorage() {
    }

    public Iterable<Widget> getAll() {
        lock.readLock().lock();
        try {
            return widgetMap.values().stream().map(Widget::new)
                    .sorted(Comparator.comparingInt(Widget::getZ))
                    .collect(Collectors.toCollection(ArrayList::new));
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean existZIndex(Integer zIndex) {
        return widgetMap.values().stream().anyMatch(w -> w.getZ().equals(zIndex));
    }

    public Widget add(Widget widget) {
        lock.writeLock().lock();
        try {
            Long id = widget.getId();
            if (id == null) {
                id = idCounter++;
            } else {
                idCounter = Long.max(id + 1, idCounter);
            }
            if (widget.getZ() == null) {
                widget.setZ(nextZIndex);
                nextZIndex++;
            } else if (existZIndex(widget.getZ())) {
                widgetMap.values().stream()
                        .filter(w -> w.getZ() >= widget.getZ())
                        .forEach(w -> w.setZ(w.getZ() + 1));
                nextZIndex++;
            } else if (widget.getZ() >= nextZIndex) {
                nextZIndex = widget.getZ() + 1;
            }
            widget.setId(id);
            widget.setLastModified();
            widgetMap.put(id, widget);
            return new Widget(widget);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Widget save(long id, Widget widget) {
        lock.writeLock().lock();
        try {
            Widget existingWidget = widgetMap.get(id);
            if (existingWidget == null) {
                return null;
            }
            if (existZIndex(widget.getZ()) && !widget.getZ().equals(existingWidget.getZ())) {
                widgetMap.values().stream()
                        .filter(w -> w.getZ() >= widget.getZ())
                        .forEach(w -> w.setZ(w.getZ() + 1));
                nextZIndex++;
            }
            if (widget.getX() != null) {
                existingWidget.setX(widget.getX());
            }
            if (widget.getY() != null) {
                existingWidget.setY(widget.getY());
            }
            if (widget.getZ() != null) {
                existingWidget.setZ(widget.getZ());
                if (widget.getZ() >= nextZIndex) {
                    nextZIndex = widget.getZ() + 1;
                }
            }
            if (widget.getWidth() != null) {
                existingWidget.setWidth(widget.getWidth());
            }
            if (widget.getHeight() != null) {
                existingWidget.setHeight(widget.getHeight());
            }
            existingWidget.setLastModified();
            return new Widget(existingWidget);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Widget getById(long id) {
        lock.readLock().lock();
        try {
            Widget widget = widgetMap.get(id);
            if (widget == null) {
                return null;
            }
            return new Widget(widget);
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
            return widgetMap.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
