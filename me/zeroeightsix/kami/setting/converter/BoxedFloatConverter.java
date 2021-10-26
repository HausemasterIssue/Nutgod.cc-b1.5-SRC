package me.zeroeightsix.kami.setting.converter;

import com.google.gson.JsonElement;

public class BoxedFloatConverter extends AbstractBoxedNumberConverter {

    protected Float doBackward(JsonElement s) {
        return Float.valueOf(s.getAsFloat());
    }
}
