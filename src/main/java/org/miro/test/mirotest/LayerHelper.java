package org.miro.test.mirotest;

import org.miro.test.mirotest.widget.Widget;

public class LayerHelper {

    public void moveWidgets(Integer busyZIndex, Iterable<Widget> widgetList) {
        for (Widget widget: widgetList) {
            if (widget.getZ().equals(busyZIndex)) {
                widget.setZIndex(busyZIndex + 1);
                busyZIndex++;
            }
        }
    }
}
