package pt.ipleiria.object_detection_autopsy.list_model;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.AbstractListModel;

public class SortedListModel<T> extends AbstractListModel<T>
{
 private final TreeSet<T> modelData;
 
 public SortedListModel(Comparator<? super T> comparator)
 {
  this.modelData=new TreeSet<>(comparator);
 }
 
 @Override
 public int getSize()
 {
  return this.modelData.size();
 }

 @Override
 public T getElementAt(int index)
 {
  Iterator<T> iterator;
  int floatingIndex=this.getSize();
  int multiplier;

  if(index<floatingIndex/2)
  {
   iterator=this.modelData.iterator();
   floatingIndex=0;
   multiplier=1;
  }
  else
  {
   iterator=this.modelData.descendingIterator();
   floatingIndex--;
   multiplier=-1;
  }
  
  while (iterator.hasNext())
  {
   T element=iterator.next();
   if(floatingIndex==index)
   {
    return element;
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
   this.modelData.add(entry);
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
   iterator=this.modelData.iterator();
   floatingIndex=0;
   multiplier=1;
  }
  else
  {
   iterator=this.modelData.descendingIterator();
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
  this.modelData.add(element);
  Iterator<T> iterator = this.modelData.iterator();
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
}
