package org.miro.test.mirotest;

import java.util.ArrayList;
import java.util.Comparator;

public class LayerHelper {

    public Integer getMaxZIndex(ArrayList<Widget> widgetList) {
        Integer zIdnex = Integer.MIN_VALUE;
        for (Widget widget : widgetList) {
            if (widget.getZIndex() > zIdnex) {
                zIdnex = widget.getZIndex();
            }
        }
        return zIdnex;
    }

    public boolean isBusyIndex(Integer zIndex, ArrayList<Widget> widgetList) {
        return widgetList.stream().anyMatch(w -> w.getZIndex().equals(zIndex));
    }

    public void moveWidgets(Integer busyZIndex, ArrayList<Widget> widgetList) {
        widgetList.sort(Comparator.comparingInt(Widget::getZIndex));
        for (Widget widget: widgetList) {
            if (widget.getZIndex().equals(busyZIndex)) {
                widget.setZIndex(busyZIndex + 1);
                busyZIndex++;
            }
        }
    }
}
