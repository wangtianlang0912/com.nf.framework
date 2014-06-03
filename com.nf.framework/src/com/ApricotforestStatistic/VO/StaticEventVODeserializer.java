package com.ApricotforestStatistic.VO;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class StaticEventVODeserializer implements JsonDeserializer<StaticEventVO> {
	@Override
	public StaticEventVO deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		if (json != null) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			return gson.fromJson(json, typeOfT);
		}
		return null;
	}
}
