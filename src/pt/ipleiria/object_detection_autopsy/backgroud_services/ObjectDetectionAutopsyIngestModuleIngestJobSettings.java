package pt.ipleiria.object_detection_autopsy.backgroud_services;

import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;

public class ObjectDetectionAutopsyIngestModuleIngestJobSettings implements IngestModuleIngestJobSettings
{
 public ObjectDetectionAutopsyIngestModuleIngestJobSettings()
 {
  
 }
 
 @Override
 public long getVersionNumber()
 {
  return 1;
 }
}