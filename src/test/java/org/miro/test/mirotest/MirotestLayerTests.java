package org.miro.test.mirotest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.miro.test.mirotest.widget.Widget;
import org.miro.test.mirotest.widget.WidgetStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

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
        LayerHelper layerHelper = new LayerHelper();
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
        LayerHelper layerHelper = new LayerHelper();
        layerHelper.moveWidgets(-11, widgets.getAll());
        layerHelper.moveWidgets(-9, widgets.getAll());
        assertEquals(new Integer(-10), widgets.getById(2L).getZ());
        assertEquals(new Integer(-8), widgets.getById(3L).getZ());
        assertEquals(new Integer(-7), widgets.getById(4L).getZ());
    }
}
