package pt.ipleiria.object_detection_autopsy.list_model;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import javax.swing.AbstractListModel;
import pt.ipleiria.object_detection_autopsy.ObjectDetectionIngestModuleFactory;
import pt.ipleiria.object_detection_autopsy.comparator.Containable;

public class SortedListModel<T extends Comparable<T>> extends AbstractListModel<T>
{
 private final TreeMap<T,Boolean> modelData;
 private final Containable<T> containable;
 private T filter;
 
 public SortedListModel(Containable<T> containable)
 {
  this.filter = null;
  this.containable=containable;
  this.modelData=new TreeMap<>(containable);
 }
 
 @Override
 public int getSize()
 {
  return this.modelData.size();
 }

 @Override
 public T getElementAt(int index)
 {
  Iterator<Entry<T,Boolean>> iterator;
  int floatingIndex=this.getSize();
  int multiplier;
  T filter = this.filter;
  
  if(index<floatingIndex/2)
  {
   iterator=this.modelData.entrySet().parallelStream().filter((t) ->
   {
    if(filter == null)
    {
     return true;
    }
    return this.containable.contains(t.getKey(), filter) && t.getValue();
   }).iterator();
   floatingIndex=0;
   multiplier=1;
  }
  else
  {
   iterator=this.modelData.descendingMap().entrySet().parallelStream().filter((t) ->
   {
    if(filter == null)
    {
     return true;
    }
    return this.containable.contains(t.getKey(), filter) && t.getValue();
   }).iterator();
   floatingIndex--;
   multiplier=-1;
  }
  
  while (iterator.hasNext())
  {
   Entry<T,Boolean> element=iterator.next();
   if(floatingIndex==index)
   {
    return element.getKey();
   }
   floatingIndex+=multiplier;
  }
  return null;
 }
 
 public void cleanAddAll(Iterator<T> iterator)
 {
  int lenght=this.getSize();
  this.modelData.clear();
  this.fireIntervalRemoved(this, 0, lenght);
  T entry;
  while (iterator.hasNext())
  {
   entry = iterator.next();
   if (entry == null)
   {
    continue;
   }
   this.modelData.put(entry,Boolean.TRUE);
  }
  this.fireIntervalAdded(this, 0, this.getSize());
 }
 
 public T remove(int index)
 {
  Iterator<T> iterator;
  int floatingIndex=this.getSize();
  int multiplier;

  if(index<floatingIndex/2)
  {
   iterator=this.modelData.keySet().iterator();
   floatingIndex=0;
   multiplier=1;
  }
  else
  {
   iterator=this.modelData.descendingKeySet().iterator();
   floatingIndex--;
   multiplier=-1;
  }
  
  while (iterator.hasNext())
  {
   T element=iterator.next();
   if(floatingIndex==index)
   {
    iterator.remove();
    this.fireIntervalRemoved(this, index, index);
    return element;
   }
   floatingIndex+=multiplier;
  }
  return null;
 }
 
 public void addElement(T element)
 {
  this.modelData.put(element, Boolean.TRUE);
  Iterator<T> iterator = this.modelData.keySet().iterator();
  T key;
  int index=0;
  while (iterator.hasNext())
  {
   key = iterator.next();
   if(this.modelData.comparator().compare(key, element)==0)
   {
    break;
   }
   index++;
  }
  this.fireIntervalAdded(this, index, index);
 }
 
 public boolean isEmpty()
 {
  return this.modelData.isEmpty();
 }

 public void setFilter(T filter)
 {
  this.filter = filter;
  int i=0;
  Iterator<Entry<T,Boolean>> iterator = this.modelData.entrySet().iterator();
  
  while(iterator.hasNext())
  {
   Entry<T,Boolean> entry = iterator.next();
   boolean result = this.containable.contains(entry.getKey(), filter);
   if(result && !entry.getValue())
   {
    this.fireIntervalAdded(this, i, i);
   }
   else if(!result && entry.getValue())
   {
    this.fireIntervalRemoved(this, i, i);
   }
   entry.setValue(result);
   i++;
  }
 }
}
