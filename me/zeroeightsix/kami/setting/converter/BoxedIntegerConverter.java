package me.zeroeightsix.kami.setting.converter;

import com.google.gson.JsonElement;

public class BoxedIntegerConverter extends AbstractBoxedNumberConverter {

    protected Integer doBackward(JsonElement s) {
        return Integer.valueOf(s.getAsInt());
    }
}
