package me.zeroeightsix.kami.setting.converter;

import com.google.gson.JsonElement;

public class BoxedDoubleConverter extends AbstractBoxedNumberConverter {

    protected Double doBackward(JsonElement s) {
        return Double.valueOf(s.getAsDouble());
    }
}
