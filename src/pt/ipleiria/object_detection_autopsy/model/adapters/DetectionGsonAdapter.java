package pt.ipleiria.object_detection_autopsy.model.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import pt.ipleiria.object_detection_autopsy.model.Box;
import pt.ipleiria.object_detection_autopsy.model.Detection;

public class DetectionGsonAdapter implements JsonDeserializer<Detection>
{
 @Override
 public Detection deserialize(JsonElement je, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
 {
  if (!je.isJsonObject())
  {
   throw new JsonParseException("cannot parse a detection with a non object json");
  }
  JsonObject jsonObject;
  JsonElement jsonElement;
  String className;
  double probability;
  Box box;
  try
  {
   jsonObject = je.getAsJsonObject();
   jsonElement = jsonObject.get("className");
   if (jsonElement == null)
   {
    throw new JsonParseException("cannot get className property");
   }
   className = jsonElement.getAsString();
   
   jsonElement = jsonObject.get("probability");
   if (jsonElement == null)
   {
    throw new JsonParseException("cannot get probability property");
   }
   probability = jsonElement.getAsDouble();
   
   jsonElement = jsonObject.get("box");
   if (jsonElement == null)
   {
    throw new JsonParseException("cannot get box property");
   }
   box = context.deserialize(jsonElement, Box.class);
  }
  catch (IllegalStateException | ClassCastException ex)
  {
   throw new JsonParseException("cannot get double property", ex);
  }
  return new Detection(className, probability, box);
 }
}
