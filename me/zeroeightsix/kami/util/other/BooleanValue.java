package me.zeroeightsix.kami.util.other;

public class BooleanValue extends Value {

    public BooleanValue() {}

    public BooleanValue(String displayName, String[] alias, Object value) {
        super(displayName, alias, value);
    }

    public boolean getBoolean() {
        return ((Boolean) this.getValue()).booleanValue();
    }

    public void setBoolean(boolean val) {
        this.setValue(Boolean.valueOf(val));
    }
}
