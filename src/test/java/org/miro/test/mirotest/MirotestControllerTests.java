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
    public void createWidget() throws Exception {
        String uri = "/widgets";
        Widget widgetToCreate = new Widget();
        widgetToCreate.setX(10);
        widgetToCreate.setY(10);
        widgetToCreate.setHeight(1.0);
        widgetToCreate.setZ(-5);

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
    }

    @Test
    public void readWidget() throws Exception {
        String uri = "/widgets";
        Widget widgetToCreate = new Widget(10, 10, -5, 1.0, 2.0);
        String inputJson = mapToJson(widgetToCreate);
        MvcResult mvcResult = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        Widget createdWidget = mapFromJson(mvcResult.getResponse().getContentAsString(), Widget.class);
        uri = "/widgets/" + (createdWidget.getId() + 1);
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
    }

    @Test
    public void updateWidget() throws Exception {
        String uri = "/widgets";
        Widget widgetToCreate = new Widget(10, 10, -5, 1.0, 2.0);
        String inputJson = mapToJson(widgetToCreate);
        MvcResult mvcResult = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        Widget createdWidget = mapFromJson(mvcResult.getResponse().getContentAsString(), Widget.class);
        uri = "/widgets/" + (createdWidget.getId() + 1);
        mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andExpect(status().isBadRequest());

        Widget widgetToUpdate = createdWidget;
        uri = "/widgets/" + widgetToUpdate.getId();
        widgetToUpdate.setWidth(15.0);
        inputJson = mapToJson(widgetToUpdate);
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
    }

    @Test
    public void deleteWidget() throws Exception {
        String uri = "/widgets";
        Widget widgetToCreate = new Widget(10, 10, -5, 1.0, 2.0);
        String inputJson = mapToJson(widgetToCreate);
        MvcResult mvcResult = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        Widget createdWidget = mapFromJson(mvcResult.getResponse().getContentAsString(), Widget.class);
        uri = "/widgets/" + (createdWidget.getId() + 1);
        mockMvc.perform(delete(uri)).andExpect(status().isBadRequest());

        uri = "/widgets/" + createdWidget.getId();
        mockMvc.perform(delete(uri)).andExpect(status().isOk());
    }

    @Test
    public void getWidgetsList() throws Exception {
        String uri = "/widgets";
        Widget widget = new Widget();
        widget.setX(10);
        widget.setY(10);
        widget.setHeight(1.0);
        widget.setWidth(1.0);
        widget.setZ(-5);

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
