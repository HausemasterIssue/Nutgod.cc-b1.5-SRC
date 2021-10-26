package me.zeroeightsix.kami.gui.kami.component;

import me.zeroeightsix.kami.gui.rgui.component.container.use.Frame;
import me.zeroeightsix.kami.gui.rgui.component.listen.RenderListener;
import me.zeroeightsix.kami.gui.rgui.component.use.Label;
import me.zeroeightsix.kami.gui.rgui.util.ContainerHelper;
import me.zeroeightsix.kami.gui.rgui.util.Docking;

public class ActiveModules extends Label {

    public boolean sort_up = true;

    public ActiveModules() {
        super("");
        this.addRenderListener(new RenderListener() {
            public void onPreRender() {
                Frame parentFrame = (Frame) ContainerHelper.getFirstParent(Frame.class, ActiveModules.this);

                if (parentFrame != null) {
                    Docking docking = parentFrame.getDocking();

                    if (docking.isTop()) {
                        ActiveModules.this.sort_up = true;
                    }

                    if (docking.isBottom()) {
                        ActiveModules.this.sort_up = false;
                    }

                }
            }

            public void onPostRender() {}
        });
    }
}
