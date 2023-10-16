package com.chirango.JacksonMapper;

import com.chirango.JacksonMapper.deserializer.CustomDeserializer;
import com.chirango.JacksonMapper.model.DatePOJO;
import com.chirango.JacksonMapper.model.Employee;
import com.chirango.JacksonMapper.model.Man;
import com.chirango.JacksonMapper.serializer.CustomSerializer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class JacksonMapperApplication {

  public static void main(String[] args) {
    SpringApplication.run(JacksonMapperApplication.class, args);

    try {
      ObjectMapper objectMapper = new ObjectMapper();

      // when there are additional fields in Json String which aren't in POJO - use configure method
      // or @JsonIgnoreProperties(ignoreUnknown = true)
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      // Serialization - Object -> Json String
      log.info("Serialization - Object -> Json String");
      Employee employee =
          new Employee("Sanjay", "Seattle", "Computer Science", "Software Engineer");

      objectMapper.writeValue(new File("src/main/java/emp.json"), employee);
      System.out.println(objectMapper.writeValueAsString(employee));
      // Print the Json with pretty print
      System.out.println(
          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employee));

      // De-Serialization - Json String -> Object
      log.info("De-Serialization - Json String -> Object");
      String empJson =
          "{\"name\":\"Sanjay\",\"city\":\"Seattle\",\"department\":\"Computer Science\",\"designation\":\"Software Engineer\",\"phone\":\"3452761875\"}";
      Employee employee1 = objectMapper.readValue(empJson, Employee.class);
      System.out.println(employee1);

      // Json String Array ->  List of Objects
      log.info("Json String Array ->  List of Objects");
      String empJsonArray =
          "[{\"name\":\"Karthik\",\"city\":\"California\",\"department\":\"Film Making\",\"designation\":\"Director\"},"
              + "{\"name\":\"Rocky\",\"city\":\"KGF\",\"department\":\"Gold Digging\",\"designation\":\"Owner\"}]";
      List<Employee> employeeList = objectMapper.readValue(empJsonArray, new TypeReference<>() {});
      employeeList.forEach(System.out::println);

      // Json String Array ->  Array of Objects
      log.info("Json String Array ->  Array of Objects");
      Employee[] employeeArray = objectMapper.readValue(empJsonArray, Employee[].class);
      for (Employee emp : employeeArray) {
        System.out.println(emp);
      }

      // Json String -> Map of Objects
      log.info("Json String -> Map of Objects");
      Map<String, String> empMap = objectMapper.readValue(empJson, new TypeReference<>() {});
      empMap.entrySet().forEach(System.out::println);

      // JsonNode - when we don't have a Java Object/POJO class to map from String Json
      JsonNode jsonNode = objectMapper.readTree(empJson);
      String empName = jsonNode.get("name").asText();
      String empCity = jsonNode.get("city").asText();
      String empDepartment = jsonNode.get("department").asText();
      String empDesignation = jsonNode.get("designation").asText();

      objectMapper.writeValue(
          new File("src/main/java/empNode.txt"),
          empName + "," + empCity + "," + empDepartment + "," + empDesignation);

      // Other way to declare a Json String
      log.info("Other way to declare a Json String");
      String jsonString =
          """
                              {
                              "name": 123,
                              "city": "Bhaagi",
                              "department": "dsvdfds",
                              "designation": "sdcfsdfcdsc"
                              }
                              """;
      System.out.println(objectMapper.readValue(jsonString, Employee.class));

      // Custom Serializer Example - using below 3 lines or @JsonSerialize(using =
      // CustomSerializer.class)
      log.info("Custom Serializer Example");
      SimpleModule serializerModule =
          new SimpleModule("CustomSerializer", new Version(1, 0, 0, null, null, null));
      serializerModule.addSerializer(new CustomSerializer(Man.class));
      objectMapper.registerModule(serializerModule);

      Man man = new Man("Sanjay", "Sahu");
      String manJson = objectMapper.writeValueAsString(man);
      System.out.println(manJson);

      // Custom De-Serializer Example - using below 3 lines or @JsonDeserialize(using =
      // CustomSerializer.class)
      log.info("Custom De-Serializer Example");
      SimpleModule deserializerModule =
          new SimpleModule("CustomSerializer", new Version(1, 0, 0, null, null, null));
      deserializerModule.addDeserializer(Man.class, new CustomDeserializer(Man.class));
      objectMapper.registerModule(deserializerModule);

      String manJsonString = "{\"name\":\"Sanjay Sahu\"}";
      Man man1 = objectMapper.readValue(manJsonString, Man.class);
      System.out.println(man1);

      // Date Serialization Example with Formatting YYYY-MM-dd HH:mm a
      // using setDateFormat() or @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd
      // HH:mm a")
      log.info("Date Serialization Example with Formatting YYYY-MM-dd HH:mm a");
      DatePOJO datePOJO = new DatePOJO(new Date());
      DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm a");

      String dateString = objectMapper.setDateFormat(dateFormat).writeValueAsString(datePOJO);
      System.out.println(dateString);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
