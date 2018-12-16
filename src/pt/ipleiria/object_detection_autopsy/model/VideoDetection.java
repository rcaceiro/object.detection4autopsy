package pt.ipleiria.object_detection_autopsy.model;

public class VideoDetection extends ImageDetection
{
 private long frame;
 private double millisecond;

 public VideoDetection()
 {
  this(-1,-1,-1);
 }

 public VideoDetection(long frame, double millisecond, double timeSpentForClassification)
 {
  this(-1,-1,-1,null);
 }

 public VideoDetection(long frame, double millisecond, double timeSpentForClassification, Detection[] detections)
 {
  super(timeSpentForClassification, detections);
  this.frame = frame;
  this.millisecond = millisecond;
 }

 public long getFrame()
 {
  return frame;
 }

 public double getMillisecond()
 {
  return millisecond;
 }
}