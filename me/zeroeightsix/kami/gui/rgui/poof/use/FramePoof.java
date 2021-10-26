package me.zeroeightsix.kami.gui.rgui.poof.use;

import me.zeroeightsix.kami.gui.rgui.poof.PoofInfo;

public abstract class FramePoof extends Poof {

    public static enum Action {

        MINIMIZE, MAXIMIZE, CLOSE;
    }

    public static class FramePoofInfo extends PoofInfo {

        private FramePoof.Action action;

        public FramePoofInfo(FramePoof.Action action) {
            this.action = action;
        }

        public FramePoof.Action getAction() {
            return this.action;
        }
    }
}
