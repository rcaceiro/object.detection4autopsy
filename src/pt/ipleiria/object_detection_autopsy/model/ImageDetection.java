package pt.ipleiria.object_detection_autopsy.model;

public class ImageDetection
{
 private double timeSpentForClassification;
 private Detection[] detections;

 public ImageDetection()
 {
  this(-1);
 }

 public ImageDetection(double timeSpentForClassification)
 {
  this(timeSpentForClassification,null);
 }

 public ImageDetection(double timeSpentForClassification, Detection[] detections)
 {
  this.timeSpentForClassification = timeSpentForClassification;
  this.detections = detections;
 }
}
