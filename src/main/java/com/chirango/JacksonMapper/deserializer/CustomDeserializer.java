package com.chirango.JacksonMapper.deserializer;

import com.chirango.JacksonMapper.model.Man;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CustomDeserializer extends StdDeserializer<Man> {

  public CustomDeserializer(Class<Man> t) {
    super(t);
  }

  public CustomDeserializer() {
    this(null);
  }

  @Override
  public Man deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    Man man = new Man();
    JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
    String name = jsonNode.get("name").asText();

    String[] array = name.split(" ");
    man.setFirstName(array[0]);
    man.setLastName(array[1]);
    return man;
  }
}
