package me.zeroeightsix.kami.util.other;

public class Value {

    private String displayName;
    private String[] alias;
    private Object value;
    private Object type;
    private Value parent;

    public Value() {}

    public Value(String displayName, String[] alias, Object value) {
        this.displayName = displayName;
        this.alias = alias;
        this.value = value;
    }

    public Value(String displayName, String[] alias, Object value, Object type) {
        this.displayName = displayName;
        this.alias = alias;
        this.value = value;
        this.type = type;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public void setAlias(String[] alias) {
        this.alias = alias;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getType() {
        return this.type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Value getParent() {
        return this.parent;
    }

    public void setParent(Value parent) {
        this.parent = parent;
    }
}
