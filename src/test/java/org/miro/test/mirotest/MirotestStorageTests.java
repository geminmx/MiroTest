package org.miro.test.mirotest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.miro.test.mirotest.widget.Widget;
import org.miro.test.mirotest.widget.WidgetStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static java.sql.Types.NULL;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Mirotest.class)
public class MirotestStorageTests {

    private WidgetStorage widgets;

    private void initWidgetList() {
        widgets = new WidgetStorage();
    }

    @Test
    public void addWidgets() {
        initWidgetList();
        Integer nullZ = null;
        assertEquals(0L, widgets.add(new Widget(0L, 10, 10, -5, 1.0, 1.0)));
        assertEquals(1L,widgets.add(new Widget(1L, 10, 10, -2, 1.0, 1.0)));
        assertEquals(2L,widgets.add(new Widget(2L, 10, 10, -11, 1.0, 1.0)));
        assertEquals(3L,widgets.add(new Widget(3L, 10, 10, nullZ, 1.0, 1.0)));
        assertEquals(4L,widgets.add(new Widget(4L, 10, 10, -9, 1.0, 1.0)));
        assertEquals(5L,widgets.add(new Widget(5L, 10, 10, 17, 1.0, 1.0)));
        assertEquals(6, widgets.size());
    }

    @Test
    public void saveWidgetById() {
        initWidgetList();
        Integer nullInt = null;
        Double nullDouble = null;
        Long nullLong = null;
        widgets.add(new Widget(0L, 10, 10, -5, 1.0, 1.0));
        widgets.add(new Widget(1L, 10, 10, -2, 1.0, 1.0));
        widgets.save(1L, new Widget(5L, NULL, 1, NULL, 2.0, 0.0));
        widgets.save(1L, new Widget(5L, nullInt, nullInt, -5, nullDouble, nullDouble));
        assertEquals(new Double(2.0), widgets.getById(1L).getWidth());
        assertEquals(new Double(0.0), widgets.getById(1L).getHeight());
        assertEquals(new Integer(1), widgets.getById(1L).getY());
        assertEquals(new Integer(-4), widgets.getById(0L).getZ());
    }

    @Test
    public void deleteWidgetById() {
        initWidgetList();
        widgets.add(new Widget(0L, 10, 10, -5, 1.0, 1.0));
        widgets.add(new Widget(1L, 10, 10, -2, 1.0, 1.0));
        widgets.deleteById(1L);
        Widget widget = null;
        assertEquals(widget, widgets.getById(1L));
        assertEquals(1, widgets.size());
    }
}
