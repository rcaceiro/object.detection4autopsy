package pt.ipleiria.object_detection_autopsy.backgroud_services;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;
import org.sleuthkit.autopsy.ingest.FileIngestModule;
import org.sleuthkit.autopsy.ingest.IngestJobContext;
import org.sleuthkit.datamodel.AbstractFile;

public class ObjectDetectionFileIngestModule implements FileIngestModule
{
 private ObjectDetectionAutopsyIngestModuleIngestJobSettings jobSettings;
 private Socket socketIO;
 
 public ObjectDetectionFileIngestModule(ObjectDetectionAutopsyIngestModuleIngestJobSettings ingestOptions) throws URISyntaxException
 {
  this.jobSettings = ingestOptions;
  socketIO = IO.socket("http://localhost");
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