package pt.ipleiria.object_detection_autopsy.ui;

import org.sleuthkit.autopsy.ingest.IngestModuleGlobalSettingsPanel;
import pt.ipleiria.object_detection_autopsy.backgroud_services.ObjectDetectionAutopsyIngestModuleIngestSettings;

public class ObjectDetectionIngestModuleGlobalSettingsPanel extends IngestModuleGlobalSettingsPanel
{
 private final ObjectDetectionAutopsyIngestModuleIngestSettings settings;
 
 public ObjectDetectionIngestModuleGlobalSettingsPanel(ObjectDetectionAutopsyIngestModuleIngestSettings settings)
 {
  super();
  this.settings = settings;
 }
 
 @Override
 public void saveSettings()
 {
  
 }
}