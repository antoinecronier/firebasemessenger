package com.tactfactory.firemessenger.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static Object toObject(String json, Class<?> klazz)
      throws JsonParseException, JsonMappingException, IOException {
    return objectMapper.readValue(json, klazz);
  }

  public static String toString(Object item) throws JsonGenerationException, JsonMappingException, IOException {
    return objectMapper.writeValueAsString(item);
  }
}
