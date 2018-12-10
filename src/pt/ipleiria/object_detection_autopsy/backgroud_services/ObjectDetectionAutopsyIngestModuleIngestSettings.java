package pt.ipleiria.object_detection_autopsy.backgroud_services;

import org.sleuthkit.autopsy.coreutils.ModuleSettings;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;
import pt.ipleiria.object_detection_autopsy.ObjectDetectionIngestModuleFactory;

public class ObjectDetectionAutopsyIngestModuleIngestSettings implements IngestModuleIngestJobSettings
{

 private boolean remote;
 private String address;
 private int port;
 private boolean globalSettingsHasChanged;

 public ObjectDetectionAutopsyIngestModuleIngestSettings() throws ExceptionInInitializerError
 {
  this.globalSettingsHasChanged = false;
  if (!ModuleSettings.configExists(ObjectDetectionIngestModuleFactory.ModuleName))
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
  }
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
  if (address == null || address.isEmpty() || address.compareToIgnoreCase("")==0)
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
  if (port == null || port.isEmpty() || port.compareToIgnoreCase("")==0)
  {
   return;
  }
  
  int intPort = this.port;
  try 
  {
   intPort = Integer.parseInt(port, 10);
  }
  catch(NumberFormatException nfex)
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

 public void saveGlobal() throws ExceptionInInitializerError
 {
  this.save();
  if (!this.globalSettingsHasChanged)
  {
   return;
  }
  ModuleSettings.setConfigSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.REMOTE, Boolean.toString(this.remote));
  ModuleSettings.setConfigSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.ADDRESS, this.address);
  ModuleSettings.setConfigSetting(ObjectDetectionIngestModuleFactory.ModuleName, SettingsString.PORT, String.valueOf(this.port));
 }
 
 private void save() throws ExceptionInInitializerError
 {
  if (!ModuleSettings.configExists(ObjectDetectionIngestModuleFactory.ModuleName))
  {
   if (!ModuleSettings.makeConfigFile(ObjectDetectionIngestModuleFactory.ModuleName))
   {
    throw new ExceptionInInitializerError("Cannot initialize config file");
   }
  }
 }

 private interface DefaultSettings
 {

  static final boolean REMOTE = true;
  final static String ADDRESS = "127.0.0.1";
  final static int PORT = 3000;
 }

 private interface SettingsString
 {

  final static String REMOTE = "remote";
  final static String ADDRESS = "address";
  final static String PORT = "port";
 }
}
