package pt.ipleiria.object_detection_autopsy.model;

public class Detection
{
 private String className;
 private double probability;
 private Box box;

 public Detection()
 {
  this(null, -1, null);
 }

 public Detection(String className, double probability, double x, double y, double width, double height)
 {
  this(className, probability, new Box(x, y, width, height));
 }
 
 public Detection(String className, double probability, Box box)
 {
  this.className = className;
  this.probability = probability;
  this.box = box;
 }

 public String getClassName()
 {
  return className;
 }

 public double getProbability()
 {
  return probability;
 }

 public Box getBox()
 {
  return box;
 }
}