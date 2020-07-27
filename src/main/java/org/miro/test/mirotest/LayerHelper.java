package org.miro.test.mirotest;

import org.miro.test.mirotest.widget.Widget;

public class LayerHelper {

    public synchronized void moveWidgets(Integer busyZIndex, Iterable<Widget> widgetList) {
        for (Widget widget: widgetList) {
            if (widget.getZ().equals(busyZIndex)) {
                widget.setZ(busyZIndex + 1);
                busyZIndex++;
            }
        }
    }
}
