package me.zeroeightsix.kami.gui.rgui.poof;

import me.zeroeightsix.kami.gui.rgui.component.Component;

public interface IPoof {

    void execute(Component component, PoofInfo poofinfo);

    Class getComponentClass();

    Class getInfoClass();
}
