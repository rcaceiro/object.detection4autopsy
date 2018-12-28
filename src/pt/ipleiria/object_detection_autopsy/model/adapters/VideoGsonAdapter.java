package pt.ipleiria.object_detection_autopsy.model.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import pt.ipleiria.object_detection_autopsy.model.Detection;
import pt.ipleiria.object_detection_autopsy.model.VideoDetection;

public class VideoGsonAdapter implements JsonDeserializer<VideoDetection>
{
 @Override
 public VideoDetection deserialize(JsonElement je, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
 {
  if (!je.isJsonObject())
  {
   throw new JsonParseException("cannot parse a video detection with a non object json");
  }
  
  JsonObject jsonObject;
  JsonElement jsonElement;
  
  double timeSpentForClassification;
  Detection[] detections;
  long frame;
  double millisecond;
  
  try
  {
   jsonObject = je.getAsJsonObject();
   jsonElement = jsonObject.get("timeSpentForClassification");
   if (jsonElement == null)
   {
    throw new JsonParseException("cannot get timeSpentForClassification property");
   }
   timeSpentForClassification = jsonElement.getAsDouble();
   
   jsonElement = jsonObject.get("detections");
   if (jsonElement == null)
   {
    throw new JsonParseException("cannot get detections property");
   }
   detections = context.deserialize(jsonElement, Detection[].class);
   
   jsonElement = jsonObject.get("frame_number");
   if (jsonElement == null)
   {
    throw new JsonParseException("cannot get frame property");
   }
   frame = jsonElement.getAsLong();
   
   jsonElement = jsonObject.get("milisecond");
   if (jsonElement == null)
   {
    throw new JsonParseException("cannot get millisecond property");
   }
   millisecond = jsonElement.getAsDouble();
  }
  catch (IllegalStateException | ClassCastException ex)
  {
   throw new JsonParseException("cannot get double property", ex);
  }
  return new VideoDetection(frame, millisecond, timeSpentForClassification, detections);
 }
}
