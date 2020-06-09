package com.tactfactory.firemessenger.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tactfactory.firemessenger.database.FirebaseManager;

public class JsonUtil {

  private static JsonUtil INSTANCE = null;

  /**
   * Point d'accès pour l'instance unique du singleton
   *
   * @throws IOException
   */
  public static synchronized JsonUtil getInstance() throws IOException {
    if (INSTANCE == null) {
      INSTANCE = new JsonUtil();
    }
    return INSTANCE;
  }

  private JsonUtil() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
  }

  private final ObjectMapper objectMapper;

  public Object toObject(String json, Class<?> klazz)
      throws JsonParseException, JsonMappingException, IOException {
    return this.objectMapper.readValue(json, klazz);
  }

  public String toString(Object item) throws JsonGenerationException, JsonMappingException, IOException {
    return this.objectMapper.writeValueAsString(item);
  }
}
