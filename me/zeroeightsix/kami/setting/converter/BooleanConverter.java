package me.zeroeightsix.kami.setting.converter;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class BooleanConverter extends Converter {

    protected JsonElement doForward(Boolean aBoolean) {
        return new JsonPrimitive(aBoolean);
    }

    protected Boolean doBackward(JsonElement s) {
        return Boolean.valueOf(s.getAsBoolean());
    }
}
