package com.chirango.JacksonMapper.serializer;

import com.chirango.JacksonMapper.model.Man;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class CustomSerializer extends StdSerializer<Man> {

  public CustomSerializer(Class<Man> t) {
    super(t);
  }

  public CustomSerializer() {
    this(null);
  }

  @Override
  public void serialize(Man man, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("name", man.getFirstName() + " " + man.getLastName());
    jsonGenerator.writeEndObject();
  }
}
