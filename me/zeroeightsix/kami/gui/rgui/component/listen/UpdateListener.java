package me.zeroeightsix.kami.gui.rgui.component.listen;

import me.zeroeightsix.kami.gui.rgui.component.Component;

public interface UpdateListener {

    void updateSize(Component component, int i, int j);

    void updateLocation(Component component, int i, int j);
}
