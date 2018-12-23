package pt.ipleiria.object_detection_autopsy.comparator;

import java.util.Comparator;

public interface Containable<T> extends Comparator<T>
{
 public boolean contains(T haystack, T needle);
}
