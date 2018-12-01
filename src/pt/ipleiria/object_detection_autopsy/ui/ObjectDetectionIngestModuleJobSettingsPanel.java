package pt.ipleiria.object_detection_autopsy.ui;

import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettingsPanel;
import pt.ipleiria.object_detection_autopsy.backgroud_services.ObjectDetectionAutopsyIngestModuleIngestJobSettings;

public class ObjectDetectionIngestModuleJobSettingsPanel extends IngestModuleIngestJobSettingsPanel
{
 private final ObjectDetectionAutopsyIngestModuleIngestJobSettings settings;
 
 public ObjectDetectionIngestModuleJobSettingsPanel(ObjectDetectionAutopsyIngestModuleIngestJobSettings settings)
 {
  super();
  this.settings = settings;
 }

 @Override
 public IngestModuleIngestJobSettings getSettings()
 {
  return this.settings;
 }
}