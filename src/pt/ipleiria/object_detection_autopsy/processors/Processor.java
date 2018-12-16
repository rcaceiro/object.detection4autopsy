package pt.ipleiria.object_detection_autopsy.processors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import pt.ipleiria.object_detection_autopsy.model.ImageDetection;
import pt.ipleiria.object_detection_autopsy.model.VideoDetection;

public interface Processor
{
 public ImageDetection processImage(String path) throws IOException, FileNotFoundException;
 public VideoDetection processVideo(String path) throws IOException, FileNotFoundException;
 public boolean isProcessorAvailable() throws MalformedURLException, IOException, IllegalAccessException;
 public void close();
}
