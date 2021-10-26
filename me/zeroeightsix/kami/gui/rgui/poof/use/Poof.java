package me.zeroeightsix.kami.gui.rgui.poof.use;

import java.lang.reflect.ParameterizedType;
import me.zeroeightsix.kami.gui.rgui.poof.IPoof;

public abstract class Poof implements IPoof {

    private Class componentclass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private Class infoclass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    public Class getComponentClass() {
        return this.componentclass;
    }

    public Class getInfoClass() {
        return this.infoclass;
    }
}
