package me.zeroeightsix.kami.gui.rgui.component.listen;

public interface KeyListener {

    void onKeyDown(KeyListener.KeyEvent keylistener_keyevent);

    void onKeyUp(KeyListener.KeyEvent keylistener_keyevent);

    public static class KeyEvent {

        int key;

        public KeyEvent(int key) {
            this.key = key;
        }

        public int getKey() {
            return this.key;
        }

        public void setKey(int key) {
            this.key = key;
        }
    }
}
