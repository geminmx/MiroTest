package org.miro.test.mirotest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.miro.test.mirotest.widget.Widget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Mirotest.class)
@AutoConfigureMockMvc
public class MirotestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void crudWidget() throws Exception {
        String uri = "/widgets";
        Widget widgetToCreate = new Widget();
        widgetToCreate.setXCoordinate(10);
        widgetToCreate.setYCoordinate(10);
        widgetToCreate.setHeight(1.0);
        widgetToCreate.setZIndex(-5);

        String inputJson = mapToJson(widgetToCreate);
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andExpect(status().isBadRequest());
        widgetToCreate.setWidth(1.0);

        inputJson = mapToJson(widgetToCreate);
        MvcResult mvcResult = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
        Widget createdWidget = mapFromJson(mvcResult.getResponse().getContentAsString(), Widget.class);
        assertEquals(widgetToCreate.getX(), createdWidget.getX());
        assertEquals(widgetToCreate.getY(), createdWidget.getY());
        assertEquals(widgetToCreate.getZ(), createdWidget.getZ());
        assertEquals(widgetToCreate.getHeight(), createdWidget.getHeight());
        assertEquals(widgetToCreate.getWidth(), createdWidget.getWidth());
        assertNotNull(createdWidget.getLastModified());
        assertNotNull(createdWidget.getId());

        uri = "/widgets/555";
        mockMvc.perform(get(uri)).andExpect(status().isBadRequest());

        uri = "/widgets/" + createdWidget.getId();
        mockMvc.perform(get(uri)).andExpect(status().isOk());
        mvcResult = mockMvc.perform(get(uri)).andReturn();
        Widget readWidget = mapFromJson(mvcResult.getResponse().getContentAsString(), Widget.class);
        assertEquals(createdWidget.getX(), readWidget.getX());
        assertEquals(createdWidget.getY(), readWidget.getY());
        assertEquals(createdWidget.getZ(), readWidget.getZ());
        assertEquals(createdWidget.getHeight(), readWidget.getHeight());
        assertEquals(createdWidget.getWidth(), readWidget.getWidth());
        assertEquals(createdWidget.getLastModified(), readWidget.getLastModified());
        assertEquals(createdWidget.getId(), readWidget.getId());

        Widget widgetToUpdate = createdWidget;
        widgetToUpdate.setWidth(15.0);

        inputJson = mapToJson(widgetToUpdate);

        uri = "/widgets/555";
        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andExpect(status().isBadRequest());
        uri = "/widgets/" + createdWidget.getId();
        mvcResult = mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
        Widget updatedWidget = mapFromJson(mvcResult.getResponse().getContentAsString(), Widget.class);
        assertEquals(widgetToUpdate.getX(), updatedWidget.getX());
        assertEquals(widgetToUpdate.getY(), updatedWidget.getY());
        assertEquals(widgetToUpdate.getZ(), updatedWidget.getZ());
        assertEquals(widgetToUpdate.getHeight(), updatedWidget.getHeight());
        assertEquals(widgetToUpdate.getWidth(), updatedWidget.getWidth());
        assertEquals(widgetToUpdate.getId(), updatedWidget.getId());
        assertTrue(widgetToUpdate.getLastModified().before(updatedWidget.getLastModified()));

        uri = "/widgets/555";
        mockMvc.perform(delete(uri)).andExpect(status().isBadRequest());
        uri = "/widgets/" + createdWidget.getId();
        mockMvc.perform(delete(uri)).andExpect(status().isOk());
    }

    @Test
    public void getWidgetsList() throws Exception {
        String uri = "/widgets";
        Widget widget = new Widget();
        widget.setXCoordinate(10);
        widget.setYCoordinate(10);
        widget.setHeight(1.0);
        widget.setWidth(1.0);
        widget.setZIndex(-5);

        String inputJson = mapToJson(widget);
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson));
        }
        MvcResult mvcResult = mockMvc.perform(get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Widget[] widgets = mapFromJson(content, Widget[].class);
        assertEquals(10, widgets.length);
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    private <T> T mapFromJson(String json, Class<T> c) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, c);
    }
}
