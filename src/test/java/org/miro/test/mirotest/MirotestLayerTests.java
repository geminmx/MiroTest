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
    private ArrayList<Widget> widgets;

    private void initWidgetList() {
        widgets = new ArrayList<>();
        widgets.add(new Widget(1L, 10, 10, -5, 1.0, 1.0));
        widgets.add(new Widget(1L, 10, 10, -2, 1.0, 1.0));
        widgets.add(new Widget(1L, 10, 10, -11, 1.0, 1.0));
        widgets.add(new Widget(1L, 10, 10, -10, 1.0, 1.0));
        widgets.add(new Widget(1L, 10, 10, -9, 1.0, 1.0));
        widgets.add(new Widget(1L, 10, 10, 17, 1.0, 1.0));
    }

    @Test
    @Order(1)
    public void getMaxLayer() {
        initWidgetList();
        LayerHelper layerHelper = new LayerHelper();
        assertEquals(new Integer(17), layerHelper.getMaxZIndex(widgets));
    }
}
