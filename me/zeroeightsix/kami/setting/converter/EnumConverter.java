package me.zeroeightsix.kami.setting.converter;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class EnumConverter extends Converter {

    Class clazz;

    public EnumConverter(Class clazz) {
        this.clazz = clazz;
    }

    protected JsonElement doForward(Enum anEnum) {
        return new JsonPrimitive(anEnum.toString());
    }

    protected Enum doBackward(JsonElement jsonElement) {
        return Enum.valueOf(this.clazz, jsonElement.getAsString());
    }
}
