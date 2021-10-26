package me.zeroeightsix.kami.gui.rgui.component.container;

import java.util.ArrayList;
import me.zeroeightsix.kami.gui.rgui.component.Component;

public interface Container extends Component {

    ArrayList getChildren();

    Component getComponentAt(int i, int j);

    Container addChild(Component... acomponent);

    Container removeChild(Component component);

    boolean hasChild(Component component);

    void renderChildren();

    int getOriginOffsetX();

    int getOriginOffsetY();

    boolean penetrateTest(int i, int j);
}
