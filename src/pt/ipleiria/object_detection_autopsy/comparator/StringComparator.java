package pt.ipleiria.object_detection_autopsy.comparator;

import java.util.Comparator;

public class StringComparator implements Comparator<String>
{
 @Override
 public int compare(String o1, String o2)
 {
  if(o1 == null)
  {
   return o2==null?0:1;
  }
  return o1.compareTo(o2);
 }
}
