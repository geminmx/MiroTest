package org.miro.test.mirotest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.miro.test.mirotest.widget.Widget;
import org.miro.test.mirotest.widget.WidgetStorage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Mirotest.class)
public class MirotestLayerTests {

    private WidgetStorage widgets;

    private void initWidgetList() {
        widgets = new WidgetStorage();
        widgets.add(new Widget(0L, 10, 10, -5, 1.0, 1.0));
        widgets.add(new Widget(1L, 10, 10, -2, 1.0, 1.0));
        widgets.add(new Widget(2L, 10, 10, -11, 1.0, 1.0));
        widgets.add(new Widget(3L, 10, 10, -10, 1.0, 1.0));
        widgets.add(new Widget(4L, 10, 10, -9, 1.0, 1.0));
        widgets.add(new Widget(5L, 10, 10, 17, 1.0, 1.0));
    }

    @Test
    public void checkBusyIndexes() {
        initWidgetList();
        assertTrue(widgets.existZIndex(widgets.getById(0L).getZ()));
        assertTrue(widgets.existZIndex(widgets.getById(1L).getZ()));
        assertTrue(widgets.existZIndex(widgets.getById(2L).getZ()));
        assertTrue(widgets.existZIndex(widgets.getById(3L).getZ()));
        assertTrue(widgets.existZIndex(widgets.getById(4L).getZ()));
        assertTrue(widgets.existZIndex(widgets.getById(5L).getZ()));
        assertFalse(widgets.existZIndex(182));
        assertFalse(widgets.existZIndex(0));
    }

    @Test
    public void moveWidgets() {
        initWidgetList();
        widgets.add(new Widget(10, 10, -11, 1.0, 1.0));
        widgets.add(new Widget(10, 10, -9, 1.0, 1.0));
        widgets.save(0L, new Widget(1, 1, 20, 1.0, 1.0));
        Widget widget = widgets.add(new Widget(1, 1, null, 1.0, 1.0));
        assertEquals(new Integer(-10), widgets.getById(2L).getZ());
        assertEquals(new Integer(-8), widgets.getById(3L).getZ());
        assertEquals(new Integer(-7), widgets.getById(4L).getZ());
        assertEquals(new Integer(21), widgets.getById(widget.getId()).getZ());
    }
}
