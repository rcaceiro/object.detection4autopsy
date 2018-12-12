package pt.ipleiria.object_detection_autopsy.backgroud_services;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.logging.Level;
import org.sleuthkit.autopsy.coreutils.ModuleSettings;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;
import pt.ipleiria.object_detection_autopsy.ObjectDetectionIngestModuleFactory;
import pt.ipleiria.object_detection_autopsy.comparator.StringComparator;

public class ObjectDetectionAutopsyIngestModuleIngestSettings implements IngestModuleIngestJobSettings
{

 private boolean configExists;
 private boolean remote;
 private String address;
 private int port;
 private boolean globalSettingsHasChanged;
 private boolean rememberJobSettings;
 private boolean images;
 private boolean videos;

 private TreeMap<String, Boolean> availableDetections;

 public ObjectDetectionAutopsyIngestModuleIngestSettings() throws ExceptionInInitializerError
 {
  this.availableDetections = new TreeMap<>(new StringComparator());
  this.globalSettingsHasChanged = false;
  this.configExists = ModuleSettings.configExists(ObjectDetectionIngestModuleFactory.ModuleName);
  if (!configExists)
  {
   this.remote = DefaultSettings.REMOTE;
   this.address = DefaultSettings.ADDRESS;
   this.port = DefaultSettings.PORT;
  }
  else
  {
   if (!ModuleSettings.settingExists(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.REMOTE))
   {
    this.remote = DefaultSettings.REMOTE;
   }
   else
   {
    this.remote = Boolean.parseBoolean(ModuleSettings.getConfigSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.REMOTE));
   }

   if (!ModuleSettings.settingExists(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.ADDRESS))
   {
    this.address = DefaultSettings.ADDRESS;
   }
   else
   {
    this.address = ModuleSettings.getConfigSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.ADDRESS);
   }

   if (!ModuleSettings.settingExists(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.PORT))
   {
    this.port = DefaultSettings.PORT;
   }
   else
   {
    try
    {
     this.port = Integer.parseInt(ModuleSettings.getConfigSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.PORT));
    }
    catch (NumberFormatException nfex)
    {
     this.port = DefaultSettings.PORT;
    }
   }

   if (!ModuleSettings.settingExists(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.REMEMBER_JOB_SETTINGS))
   {
    this.rememberJobSettings = DefaultSettings.REMEMBER_JOB_SETTINGS;
   }
   else
   {
    this.rememberJobSettings = Boolean.parseBoolean(ModuleSettings.getConfigSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.REMEMBER_JOB_SETTINGS));
   }

   if (!ModuleSettings.settingExists(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.IMAGES))
   {
    this.images = DefaultSettings.IMAGES;
   }
   else
   {
    this.images = Boolean.parseBoolean(ModuleSettings.getConfigSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.IMAGES));
   }

   if (!ModuleSettings.settingExists(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.VIDEOS))
   {
    this.videos = DefaultSettings.VIDEOS;
   }
   else
   {
    this.videos = Boolean.parseBoolean(ModuleSettings.getConfigSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.VIDEOS));
   }
  }

  this.loadDetectionsSettings();
 }

 @Override
 public long getVersionNumber()
 {
  return 1;
 }

 public boolean isRemote()
 {
  return remote;
 }

 public void setIsRemote(boolean remote)
 {
  if (this.remote == remote)
  {
   return;
  }
  this.globalSettingsHasChanged = true;
  this.remote = remote;
 }

 public String getAddress()
 {
  return address;
 }

 public void setAddress(String address)
 {
  if (address == null || address.isEmpty() || address.compareToIgnoreCase("") == 0)
  {
   return;
  }
  if (this.address.compareTo(address) == 0)
  {
   return;
  }
  this.globalSettingsHasChanged = true;
  this.address = address;
 }

 public int getPort()
 {
  return port;
 }

 public void setPort(String port)
 {
  if (port == null || port.isEmpty() || port.compareToIgnoreCase("") == 0)
  {
   return;
  }

  int intPort = this.port;
  try
  {
   intPort = Integer.parseInt(port, 10);
  }
  catch (NumberFormatException nfex)
  {
   return;
  }

  this.setPort(intPort);
 }

 public void setPort(int port)
 {
  if (this.port == port)
  {
   return;
  }
  this.globalSettingsHasChanged = true;
  this.port = port;
 }

 public boolean isRememberJobSettings()
 {
  return rememberJobSettings;
 }

 public void setRememberJobSettings(boolean rememberJobSettings) throws ExceptionInInitializerError
 {
  this.rememberJobSettings = rememberJobSettings;
  this.saveSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.REMEMBER_JOB_SETTINGS, Boolean.toString(this.rememberJobSettings));

  if (this.rememberJobSettings)
  {
   this.saveSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.IMAGES, Boolean.toString(this.images));
   this.saveSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.VIDEOS, Boolean.toString(this.videos));
   this.setValueToAllKeys(null);
  }
  else
  {
   this.saveSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.IMAGES, Boolean.toString(false));
   this.saveSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.VIDEOS, Boolean.toString(false));
   this.setValueToAllKeys(true);
  }
 }

 public boolean isImages()
 {
  return images;
 }

 public void setImages(boolean images) throws ExceptionInInitializerError
 {
  this.images = images;
  if (this.rememberJobSettings)
  {
   this.saveSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.IMAGES, Boolean.toString(this.images));
  }
 }

 public boolean isVideos()
 {
  return videos;
 }

 public void setVideos(boolean videos) throws ExceptionInInitializerError
 {
  this.videos = videos;
  if (this.rememberJobSettings)
  {
   this.saveSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.VIDEOS, Boolean.toString(this.videos));
  }
 }

 public void setValueToKey(String key, boolean value)
 {
  if(key == null)
  {
   return;
  }
  if (!this.availableDetections.containsKey(key))
  {
   return;
  }
  this.availableDetections.put(key, value);
  if (this.rememberJobSettings)
  {
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, key, Boolean.toString(value));
  }
 }

 public void setValueToAllKeys(Boolean value)
 {
  if (!ModuleSettings.configExists(SettingsString.DETECTIONS_CONFIG_FILE))
  {
   this.save(SettingsString.DETECTIONS_CONFIG_FILE);
  }
  
  Iterator<String> detectionsNameIterator = this.availableDetections.keySet().iterator();
  String key;
  while (detectionsNameIterator.hasNext())
  {
   key = detectionsNameIterator.next();
   if (key == null)
   {
    continue;
   }
   if (value == null)
   {
    this.setValueToKey(key, this.availableDetections.get(key));
   }
   else
   {
    this.setValueToKey(key, value);
   }
  }
 }
 
 public Iterator<String> getAvailableDetections()
 {
  return this.availableDetections.keySet().parallelStream().filter(new Predicate<String>()
  {
   @Override
   public boolean test(String predicate)
   {
    return !availableDetections.get(predicate);
   }
  }).iterator();
 }

 public Iterator<String> getChoosedDetections()
 {
  return this.availableDetections.keySet().parallelStream().filter(new Predicate<String>()
  {
   @Override
   public boolean test(String predicate)
   {
    return availableDetections.get(predicate);
   }
  }).iterator();
 }

 public void saveGlobal() throws ExceptionInInitializerError
 {
  if (!this.globalSettingsHasChanged)
  {
   return;
  }
  this.saveSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.REMOTE, Boolean.toString(this.remote));
  this.saveSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.ADDRESS, this.address);
  this.saveSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.PORT, String.valueOf(this.port));
 }

 private void saveSetting(String moduleName, String settingName, String value) throws ExceptionInInitializerError
 {
  if (!this.configExists)
  {
   this.save(moduleName);
  }
  ModuleSettings.setConfigSetting(moduleName, settingName, value);
 }

 private void save(String moduleName) throws ExceptionInInitializerError
 {
  if (!ModuleSettings.configExists(moduleName))
  {
   if (!ModuleSettings.makeConfigFile(moduleName))
   {
    throw new ExceptionInInitializerError("Cannot initialize config file");
   }
  }
  if (ObjectDetectionIngestModuleFactory.ModuleName.compareToIgnoreCase(moduleName) == 0)
  {
   this.configExists = true;
  }
 }

 private void loadDetectionsSettings()
 {
  if (!ModuleSettings.configExists(SettingsString.DETECTIONS_CONFIG_FILE))
  {
   if (!ModuleSettings.makeConfigFile(SettingsString.DETECTIONS_CONFIG_FILE))
   {
    ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, "Cannot create detection names config file.");
    return;
   }
   String value = Boolean.toString(true);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "person", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "bicycle", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "car", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "motorbike", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "aeroplane", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "bus", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "train", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "truck", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "boat", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "traffic light", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "fire hydrant", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "stop sign", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "parking meter", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "bench", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "bird", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "cat", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "dog", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "horse", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "sheep", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "cow", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "elephant", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "bear", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "zebra", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "giraffe", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "backpack", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "umbrella", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "handbag", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "tie", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "suitcase", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "frisbee", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "skis", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "snowboard", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "sports ball", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "kite", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "baseball bat", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "baseball glove", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "skateboard", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "surfboard", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "tennis racket", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "bottle", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "wine glass", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "cup", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "fork", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "knife", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "spoon", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "bowl", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "banana", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "apple", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "sandwich", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "orange", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "broccoli", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "carrot", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "hot dog", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "pizza", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "donut", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "cake", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "chair", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "sofa", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "pottedplant", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "bed", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "diningtable", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "toilet", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "tvmonitor", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "laptop", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "mouse", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "remote", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "keyboard", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "cell phone", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "microwave", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "oven", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "toaster", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "sink", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "refrigerator", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "book", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "clock", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "vase", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "scissors", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "teddy bear", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "hair drier", value);
   this.saveSetting(SettingsString.DETECTIONS_CONFIG_FILE, "toothbrush", value);
  }
  
  Iterator<Map.Entry<String,String>> iterator = ModuleSettings.getConfigSettings(SettingsString.DETECTIONS_CONFIG_FILE).entrySet().iterator();
  Map.Entry<String,String> key;
  while (iterator.hasNext())
  {
   key = iterator.next();
   if (key == null)
   {
    continue;
   }
   if(key.getKey() == null)
   {
    continue;
   }
   this.availableDetections.put(key.getKey(), Boolean.parseBoolean(key.getValue()));
  }
 }
 
 private interface DefaultSettings
 {

  static final boolean REMOTE = true;
  final static String ADDRESS = "127.0.0.1";
  final static int PORT = 3000;
  final static boolean REMEMBER_JOB_SETTINGS = true;
  final static boolean IMAGES = true;
  final static boolean VIDEOS = true;
 }

 private interface SettingsString
 {

  final static String DETECTIONS_CONFIG_FILE = ObjectDetectionIngestModuleFactory.ModuleName + ".detections";
  final static String REMOTE = "remote";
  final static String ADDRESS = "address";
  final static String PORT = "port";
  final static String REMEMBER_JOB_SETTINGS = "remember_job_settings";
  final static String IMAGES = "images";
  final static String VIDEOS = "videos";
 }
}
