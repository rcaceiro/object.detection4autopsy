package pt.ipleiria.object_detection_autopsy.processors;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.http.client.ClientProtocolException;
import pt.ipleiria.object_detection_autopsy.model.ImageDetection;
import pt.ipleiria.object_detection_autopsy.model.VideoDetection;
public interface Processor extends Closeable
{
 public ImageDetection processImage(File file) throws IllegalArgumentException, ClientProtocolException, IOException;

 public VideoDetection[] processVideo(File file) throws IllegalArgumentException, ClientProtocolException, IOException;

 public boolean isProcessorAvailable() throws IllegalArgumentException, ClientProtocolException, IOException;

 @Override
 public void close();

 public default boolean isImageFile(File file)
 {
  String mimeType;
  mimeType = this.getFileMimeType(file);
  if(mimeType == null)
  {
      return false;
  }
  return mimeType.contains("image");
 }

 public default boolean isVideoFile(File file)
 {
  String mimeType;
  mimeType = this.getFileMimeType(file);
  if(mimeType == null)
  {
      return false;
  }
  return mimeType.contains("video") || mimeType.contains("application/x-mpegURL");
 }
 
 public default String getFileMimeType(File file)
 {
  Path path = file.toPath();
     try {
         return Files.probeContentType(path);
     } catch (IOException ex) {
         return null;
     }
 }
}
