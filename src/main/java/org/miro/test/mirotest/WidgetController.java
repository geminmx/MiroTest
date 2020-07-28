package org.miro.test.mirotest;

import org.miro.test.mirotest.widget.Widget;
import org.miro.test.mirotest.widget.WidgetStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WidgetController {

    private final WidgetStorage widgetStorage;

    @Autowired
    public WidgetController(WidgetStorage widgetStorage) {
        this.widgetStorage = widgetStorage;
    }

    @GetMapping("/test")
    public String test() {
        return ApplicationProperties.getProperty("test");
    }

    @GetMapping("/widgets")
    public ResponseEntity<Iterable<Widget>> getAllWidgets() {
        return ResponseEntity.ok(widgetStorage.getAll());
    }

    @PostMapping("/widgets")
    public ResponseEntity<Widget> postWidget(@RequestBody Widget newWidget) {
        if (newWidget.getX() == null
                || newWidget.getY() == null
                || newWidget.getHeight() == null
                || newWidget.getWidth() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(widgetStorage.add(newWidget));
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
        Widget widget = widgetStorage.save(id, newWidget);
        if (widget != null) {
            return ResponseEntity.ok(widget);
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
