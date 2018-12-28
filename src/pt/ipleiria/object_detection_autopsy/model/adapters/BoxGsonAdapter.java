package pt.ipleiria.object_detection_autopsy.model.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import pt.ipleiria.object_detection_autopsy.model.Box;

public class BoxGsonAdapter implements JsonDeserializer<Box>
{
 @Override
 public Box deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
 {
  if (!je.isJsonObject())
  {
   throw new JsonParseException("cannot parse a box with a non object json");
  }
  JsonObject jsonObject;
  JsonElement jsonElement;
  double x;
  double y;
  double width;
  double height;
  
  try
  {
   jsonObject=je.getAsJsonObject();
   
   jsonElement = jsonObject.get("x");
   if(jsonElement==null)
   {
    throw new JsonParseException("cannot get x property");
   }
   x = jsonElement.getAsDouble();
   
   jsonElement = jsonObject.get("y");
   if(jsonElement==null)
   {
    throw new JsonParseException("cannot get y property");
   }
   y = jsonElement.getAsDouble();
   
   jsonElement = jsonObject.get("w");
   if(jsonElement==null)
   {
    throw new JsonParseException("cannot get width property");
   }
   width = jsonElement.getAsDouble();
   
   jsonElement = jsonObject.get("h");
   if(jsonElement==null)
   {
    throw new JsonParseException("cannot get height property");
   }
   height = jsonElement.getAsDouble();
  }
  catch(IllegalStateException | ClassCastException ex)
  {
   throw new JsonParseException("cannot get double property",ex);
  }
  
  return new Box(x, y, width, height);
 }
}
