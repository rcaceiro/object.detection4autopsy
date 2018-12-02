package pt.ipleiria.object_detection_autopsy;

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
import pt.ipleiria.object_detection_autopsy.ui.ObjectDetectionIngestModuleGlobalSettingsPanel;
import pt.ipleiria.object_detection_autopsy.ui.ObjectDetectionIngestModuleJobSettingsPanel;

@ServiceProvider(service = IngestModuleFactory.class)
public class ObjectDetectionIngestModuleFactory implements IngestModuleFactory
{
 private ObjectDetectionAutopsyIngestModuleIngestSettings settings;
 public static final Logger ObjectDetectionLogger = Logger.getLogger("ObjectDetectionforAutopsyLogger");
 
 public ObjectDetectionIngestModuleFactory()
 {
  this.settings = null;
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
  if (!(ingestOptions instanceof ObjectDetectionAutopsyIngestModuleIngestSettings))
  {
   throw new IllegalArgumentException("on createFileIngestModule: IngestModuleIngestJobSettings should be a ObjectDetectionAutopsyIngestModuleIngestJobSettings");
  }
  
  return new ObjectDetectionFileIngestModule((ObjectDetectionAutopsyIngestModuleIngestSettings) ingestOptions,null);
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
  if (!(settings instanceof ObjectDetectionAutopsyIngestModuleIngestSettings))
  {
   throw new IllegalArgumentException("on getIngestJobSettingsPanel: IngestModuleIngestJobSettings should be a ObjectDetectionAutopsyIngestModuleIngestJobSettings");
  }
  return new ObjectDetectionIngestModuleJobSettingsPanel((ObjectDetectionAutopsyIngestModuleIngestSettings) settings);
 }

 @Override
 public boolean hasIngestJobSettingsPanel()
 {
  return true;
 }

 @Override
 public IngestModuleIngestJobSettings getDefaultIngestJobSettings()
 {
  if(this.settings == null)
  {
   this.settings = new ObjectDetectionAutopsyIngestModuleIngestSettings();
  }
  return this.settings;
 }

 @Override
 public IngestModuleGlobalSettingsPanel getGlobalSettingsPanel()
 {
  return new ObjectDetectionIngestModuleGlobalSettingsPanel((ObjectDetectionAutopsyIngestModuleIngestSettings) this.getDefaultIngestJobSettings());
 }

 @Override
 public boolean hasGlobalSettingsPanel()
 {
  return true;
 }
}
