package org.miro.test.mirotest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Mirotest.class)
@AutoConfigureMockMvc
public class MirotestLayerTests {

    @Autowired
    private MockMvc mockMvc;
    private HashMap<Long, Widget> widgets;

    private void initWidgetList() {
        widgets = new HashMap<>();
        widgets.put(0L, new Widget(0L, 10, 10, -5, 1.0, 1.0));
        widgets.put(1L, new Widget(1L, 10, 10, -2, 1.0, 1.0));
        widgets.put(2L, new Widget(2L, 10, 10, -11, 1.0, 1.0));
        widgets.put(3L, new Widget(3L, 10, 10, -10, 1.0, 1.0));
        widgets.put(4L, new Widget(4L, 10, 10, -9, 1.0, 1.0));
        widgets.put(5L, new Widget(5L, 10, 10, 17, 1.0, 1.0));
    }

    @Test
    public void getMaxLayer() {
        initWidgetList();
        LayerHelper layerHelper = new LayerHelper();
        assertEquals(new Integer(17), layerHelper.getMaxZIndex(new ArrayList<>(widgets.values())));
    }

    @Test
    public void whenIndexIsBusy_thenAccepted() {
        initWidgetList();
        LayerHelper layerHelper = new LayerHelper();
        assertTrue(layerHelper.isBusyIndex(widgets.get(0L).getZIndex(), new ArrayList<>(widgets.values())));
        assertTrue(layerHelper.isBusyIndex(widgets.get(1L).getZIndex(), new ArrayList<>(widgets.values())));
        assertTrue(layerHelper.isBusyIndex(widgets.get(2L).getZIndex(), new ArrayList<>(widgets.values())));
        assertTrue(layerHelper.isBusyIndex(widgets.get(3L).getZIndex(), new ArrayList<>(widgets.values())));
        assertTrue(layerHelper.isBusyIndex(widgets.get(4L).getZIndex(), new ArrayList<>(widgets.values())));
        assertTrue(layerHelper.isBusyIndex(widgets.get(5L).getZIndex(), new ArrayList<>(widgets.values())));
        assertFalse(layerHelper.isBusyIndex(182, new ArrayList<>(widgets.values())));
        assertFalse(layerHelper.isBusyIndex(0, new ArrayList<>(widgets.values())));
        assertFalse(layerHelper.isBusyIndex(
                layerHelper.getMaxZIndex(new ArrayList<>(widgets.values())) + 1,
                new ArrayList<>(widgets.values())));
    }

    @Test
    public void moveWidgets() {
        initWidgetList();
        LayerHelper layerHelper = new LayerHelper();
        layerHelper.moveWidgets(-11, new ArrayList<>(widgets.values()));
        layerHelper.moveWidgets(-9, new ArrayList<>(widgets.values()));
        assertEquals(new Integer(-10), widgets.get(2L).getZIndex());
        assertEquals(new Integer(-8), widgets.get(3L).getZIndex());
        assertEquals(new Integer(-7), widgets.get(4L).getZIndex());
    }
}
