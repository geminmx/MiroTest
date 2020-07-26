package org.miro.test.mirotest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WidgetController {

    Map<Long, Widget> widgetMap = new ConcurrentHashMap<>();
    private final Object widgetMapLocker = new Object();
    private final AtomicLong idCounter = new AtomicLong();
    private final LayerHelper layerHelper = new LayerHelper();

    public WidgetController() {
    }

    @GetMapping("/test")
    public String test() {
        return ApplicationProperties.getProperty("test");
    }

    @GetMapping("/widgets")
    public ResponseEntity<Collection<Widget>> getAllWidgets() {
        return ResponseEntity.ok(widgetMap.values());
    }

    @GetMapping(value = "/widgets", params = {"page", "size"})
    public ResponseEntity<Collection<Widget>> getPaginatedWidgets(@RequestParam("page") int page,
                                                                  @RequestParam("size") int size) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping("/widgets")
    public ResponseEntity<Widget> postWidget(@RequestBody Widget newWidget) {
        long id = idCounter.getAndIncrement();
        if (newWidget.getZIndex() == null) {
            newWidget.setZIndex(layerHelper.getMaxZIndex(new ArrayList<>(widgetMap.values())) + 1);
        }
        Widget widget = new Widget(
                id,
                newWidget.getX(),
                newWidget.getY(),
                newWidget.getZIndex(),
                newWidget.getWidth(),
                newWidget.getHeight());
        synchronized (widgetMapLocker) {
            if (layerHelper.isBusyIndex(newWidget.getZIndex(), new ArrayList<>(widgetMap.values()))) {
                layerHelper.moveWidgets(newWidget.getZIndex(), new ArrayList<>(widgetMap.values()));
            }
        }
        widgetMap.put(id, widget);
        return ResponseEntity.ok(widget);
    }

    @GetMapping("/widgets/{id}")
    public ResponseEntity<Widget> getWidget(@PathVariable long id) {
        Widget widget = widgetMap.get(id);
        if (widget != null) {
            return ResponseEntity.ok(widget);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/widgets/{id}")
    public ResponseEntity<Widget> putWidget(@PathVariable long id,
                                            @RequestBody Widget newWidget) {
        Widget widget = widgetMap.get(id);
        if (widget != null) {
            if (newWidget.getZIndex() != null && layerHelper.isBusyIndex(newWidget.getZIndex(), new ArrayList<>(widgetMap.values()))) {
                layerHelper.moveWidgets(newWidget.getZIndex(), new ArrayList<>(widgetMap.values()));
            }
            if (newWidget.getX() != null) {
                widget.setXCoordinate(newWidget.getX());
            }
            if (newWidget.getY() != null) {
                widget.setYCoordinate(newWidget.getY());
            }
            if (newWidget.getZIndex() != null) {
                widget.setZIndex(newWidget.getZIndex());
            }
            if (newWidget.getWidth() != null) {
                widget.setWidth(newWidget.getWidth());
            }
            if (newWidget.getHeight() != null) {
                widget.setHeight(newWidget.getHeight());
            }
            widget.setLastModified();
            return ResponseEntity.ok(widget);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/widgets/{id}")
    public ResponseEntity<Widget> deleteWidget(@PathVariable long id) {
        Widget widget = widgetMap.get(id);
        if (widget != null) {
            return ResponseEntity.ok(widgetMap.remove(id));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
