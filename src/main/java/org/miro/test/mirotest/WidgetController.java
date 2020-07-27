package org.miro.test.mirotest;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import org.miro.test.mirotest.widget.Widget;
import org.miro.test.mirotest.widget.WidgetStorage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WidgetController {

    private WidgetStorage widgetStorage = new WidgetStorage();

    public WidgetController() {
    }

    @GetMapping("/test")
    public String test() {
        return ApplicationProperties.getProperty("test");
    }

    @GetMapping("/widgets")
    public ResponseEntity<Iterable<Widget>> getAllWidgets() {
        return ResponseEntity.ok(widgetStorage.getAll());
    }

    @GetMapping(value = "/widgets", params = {"page", "size"})
    public ResponseEntity<Collection<Widget>> getPaginatedWidgets(@RequestParam("page") int page,
                                                                  @RequestParam("size") int size) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping("/widgets")
    public ResponseEntity<Widget> postWidget(@RequestBody Widget newWidget) {
        if (newWidget.getX() == null
                || newWidget.getY() == null
                || newWidget.getHeight() == null
                || newWidget.getWidth() == null) {
            return ResponseEntity.badRequest().build();
        }
        long id = widgetStorage.add(newWidget);
        return ResponseEntity.ok(widgetStorage.getById(id));
    }

    @GetMapping("/widgets/{id}")
    public ResponseEntity<Widget> getWidget(@PathVariable long id) {
        Widget widget = widgetStorage.getById(id);
        if (widget != null) {
            return ResponseEntity.ok(widget);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/widgets/{id}")
    public ResponseEntity<Widget> putWidget(@PathVariable long id,
                                            @RequestBody Widget newWidget) {
        if (widgetStorage.save(id, newWidget)) {
            return ResponseEntity.ok(widgetStorage.getById(id));
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/widgets/{id}")
    public ResponseEntity deleteWidget(@PathVariable long id) {
        if (widgetStorage.deleteById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
