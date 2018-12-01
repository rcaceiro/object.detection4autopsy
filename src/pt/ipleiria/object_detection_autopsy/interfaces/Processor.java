package pt.ipleiria.object_detection_autopsy.interfaces;

import pt.ipleiria.object_detection_autopsy.model.ImageDetection;
import pt.ipleiria.object_detection_autopsy.model.VideoDetection;

public interface Processor
{
 public ImageDetection processImage(String path);
 public VideoDetection processVideo(String path);
 public boolean isProcessorAvailable();
}
