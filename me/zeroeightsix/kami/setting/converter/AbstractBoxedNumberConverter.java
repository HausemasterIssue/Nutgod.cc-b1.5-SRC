package me.zeroeightsix.kami.setting.converter;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public abstract class AbstractBoxedNumberConverter extends Converter {

    protected JsonElement doForward(Number t) {
        return new JsonPrimitive(t);
    }
}
