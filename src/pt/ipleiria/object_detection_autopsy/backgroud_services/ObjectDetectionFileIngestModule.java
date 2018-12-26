package pt.ipleiria.object_detection_autopsy.backgroud_services;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.http.client.ClientProtocolException;
import org.sleuthkit.autopsy.ingest.FileIngestModule;
import org.sleuthkit.autopsy.ingest.IngestJobContext;
import org.sleuthkit.datamodel.AbstractFile;
import pt.ipleiria.object_detection_autopsy.ObjectDetectionIngestModuleFactory;
import pt.ipleiria.object_detection_autopsy.model.ImageDetection;
import pt.ipleiria.object_detection_autopsy.model.VideoDetection;
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
  if(af.getLocalAbsPath() == null)
  {
      return ProcessResult.OK;
  }
  File autopsyFile = new File(af.getLocalAbsPath());
  if (this.jobSettings.isImages() && this.processor.isImageFile(autopsyFile))
  {
   return this.processImage(autopsyFile);
  }
  if (this.jobSettings.isVideos() && this.processor.isVideoFile(autopsyFile))
  {
   return this.processVideo(autopsyFile);
  }
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
 
 private ProcessResult processImage(File file)
 {
  try
  {
   ImageDetection imageDetection = this.processor.processImage(file);
   return ProcessResult.OK;
  }
  catch (IllegalArgumentException ex)
   {
    ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, ex.getLocalizedMessage(),ex);
    return ProcessResult.ERROR;
   }
  catch (ClientProtocolException ex)
  {
   ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, ex.getLocalizedMessage(),ex);
   return ProcessResult.ERROR;
  }
  catch (IOException ex)
  {
   ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, ex.getLocalizedMessage(),ex);
   return ProcessResult.ERROR;
  }
 }
 
 private ProcessResult processVideo(File file)
 {
  try
  {
   VideoDetection[] videoDetection = this.processor.processVideo(file);
   return ProcessResult.OK;
  }
  catch (IllegalArgumentException ex)
   {
    ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, ex.getLocalizedMessage(),ex);
    return ProcessResult.ERROR;
   }
  catch (ClientProtocolException ex)
  {
   ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, ex.getLocalizedMessage(),ex);
   return ProcessResult.ERROR;
  }
  catch (IOException ex)
  {
   ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, ex.getLocalizedMessage(),ex);
   return ProcessResult.ERROR;
  }
 }
}
