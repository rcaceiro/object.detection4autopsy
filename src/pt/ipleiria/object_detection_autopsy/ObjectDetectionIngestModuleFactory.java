package pt.ipleiria.object_detection_autopsy;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.ingest.DataSourceIngestModule;
import org.sleuthkit.autopsy.ingest.FileIngestModule;
import org.sleuthkit.autopsy.ingest.IngestModuleFactory;
import org.sleuthkit.autopsy.ingest.IngestModuleGlobalSettingsPanel;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettingsPanel;
import pt.ipleiria.object_detection_autopsy.backgroud_services.ObjectDetectionAutopsyIngestModuleIngestSettings;
import pt.ipleiria.object_detection_autopsy.backgroud_services.ObjectDetectionFileIngestModule;
import pt.ipleiria.object_detection_autopsy.processors.Processor;
import pt.ipleiria.object_detection_autopsy.processors.WebProcessor;
import pt.ipleiria.object_detection_autopsy.ui.ObjectDetectionIngestModuleGlobalSettingsPanel;
import pt.ipleiria.object_detection_autopsy.ui.ObjectDetectionIngestModuleJobSettingsPanel;

@ServiceProvider(service = IngestModuleFactory.class)
public class ObjectDetectionIngestModuleFactory implements IngestModuleFactory
{
 private ObjectDetectionAutopsyIngestModuleIngestSettings settings;
 public static final String ModuleName = "pt.ipleiria.object_detection autopsy";
 public static final Logger ObjectDetectionLogger = Logger.getLogger(ObjectDetectionIngestModuleFactory.ModuleName+".logger");
 
 public ObjectDetectionIngestModuleFactory()
 {
  this.settings = new ObjectDetectionAutopsyIngestModuleIngestSettings();
 }
 
 @Override
 public String getModuleVersionNumber()
 {
  return "1.0.0";
 }

 @Override
 public String getModuleDisplayName()
 {
  return "Object Detection for Autopsy";
 }

 @Override
 public String getModuleDescription()
 {
  return "This module has the functionality to classify images on photos and videos using state of the art yolov3 convolutional networks";
 }

 @Override
 public FileIngestModule createFileIngestModule(IngestModuleIngestJobSettings ingestOptions)
 {
  return new ObjectDetectionFileIngestModule(this.settings,null);
 }

 @Override
 public boolean isFileIngestModuleFactory()
 {
  return true;
 }

 @Override
 public DataSourceIngestModule createDataSourceIngestModule(IngestModuleIngestJobSettings ingestOptions)
 {
  return null;
 }

 @Override
 public boolean isDataSourceIngestModuleFactory()
 {
  return false;
 }

 @Override
 public IngestModuleIngestJobSettingsPanel getIngestJobSettingsPanel(IngestModuleIngestJobSettings settings)
 {
  return new ObjectDetectionIngestModuleJobSettingsPanel(this.settings);
 }

 @Override
 public boolean hasIngestJobSettingsPanel()
 {
  return true;
 }

 @Override
 public synchronized IngestModuleIngestJobSettings getDefaultIngestJobSettings()
 {
  return this.settings;
 }

 @Override
 public IngestModuleGlobalSettingsPanel getGlobalSettingsPanel()
 {
  return new ObjectDetectionIngestModuleGlobalSettingsPanel(this,(ObjectDetectionAutopsyIngestModuleIngestSettings) this.getDefaultIngestJobSettings());
 }

 @Override
 public boolean hasGlobalSettingsPanel()
 {
  return true;
 }
 
 public Processor getProcessor(String address, int port, boolean remote)
 {
  if(remote)
  {
   try
   {
    return new WebProcessor(address,port);
   }
   catch (MalformedURLException ex)
   {
    ObjectDetectionLogger.log(Level.SEVERE,ex.getMessage(),ex);
   }
  }
  return null;
 }
 
 public Processor getProcessor()
 {
  if(!(this.getDefaultIngestJobSettings() instanceof ObjectDetectionAutopsyIngestModuleIngestSettings))
  {
   return null;
  }
  ObjectDetectionAutopsyIngestModuleIngestSettings localSettings=(ObjectDetectionAutopsyIngestModuleIngestSettings) this.getDefaultIngestJobSettings();
  return this.getProcessor(localSettings.getAddress(), localSettings.getPort(), localSettings.isRemote());
 }
}
