package pt.ipleiria.object_detection_autopsy.model;

public class Box
{
 private double x;
 private double y;
 private double width;
 private double height;

 public Box()
 {
  this(0, 0, 0, 0);
 }

 public Box(double x, double y, double width, double height)
 {
  super();
  this.x = x;
  this.y = y;
  this.width = width;
  this.height = height;
 }

 public double getX()
 {
  return x;
 }

 public double getY()
 {
  return y;
 }

 public double getWidth()
 {
  return width;
 }

 public double getHeight()
 {
  return height;
 }
}