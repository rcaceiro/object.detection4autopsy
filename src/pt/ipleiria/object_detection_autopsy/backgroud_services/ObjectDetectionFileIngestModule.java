package pt.ipleiria.object_detection_autopsy.backgroud_services;

import org.sleuthkit.autopsy.ingest.FileIngestModule;
import org.sleuthkit.autopsy.ingest.IngestJobContext;
import org.sleuthkit.datamodel.AbstractFile;
import pt.ipleiria.object_detection_autopsy.processors.Processor;

public class ObjectDetectionFileIngestModule implements FileIngestModule
{
 private final ObjectDetectionAutopsyIngestModuleIngestSettings jobSettings;
 private final Processor processor;
 
 public ObjectDetectionFileIngestModule(ObjectDetectionAutopsyIngestModuleIngestSettings ingestOptions, Processor processor)
 {
  this.jobSettings = ingestOptions;
  this.processor = processor;
 }
 
 @Override
 public ProcessResult process(AbstractFile af)
 {
  return ProcessResult.OK;
 }

 @Override
 public void shutDown()
 {
  
 }

 @Override
 public void startUp(IngestJobContext ijc) throws IngestModuleException
 {
  
 }

}