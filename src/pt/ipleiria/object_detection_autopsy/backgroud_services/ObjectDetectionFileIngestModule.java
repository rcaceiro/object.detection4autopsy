package pt.ipleiria.object_detection_autopsy.backgroud_services;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.http.client.ClientProtocolException;
import org.sleuthkit.autopsy.casemodule.services.Blackboard;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.ingest.FileIngestModule;
import org.sleuthkit.autopsy.ingest.IngestJobContext;
import org.sleuthkit.autopsy.ingest.IngestServices;
import org.sleuthkit.autopsy.ingest.ModuleDataEvent;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.TskCoreException;
import pt.ipleiria.object_detection_autopsy.ObjectDetectionIngestModuleFactory;
import pt.ipleiria.object_detection_autopsy.model.Detection;
import pt.ipleiria.object_detection_autopsy.model.ImageDetection;
import pt.ipleiria.object_detection_autopsy.model.VideoDetection;
import pt.ipleiria.object_detection_autopsy.processors.Processor;

public class ObjectDetectionFileIngestModule implements FileIngestModule
{
 private final ObjectDetectionAutopsyIngestModuleIngestSettings jobSettings;
 private final Processor processor;
 private final Blackboard blackboard;

 public ObjectDetectionFileIngestModule(ObjectDetectionAutopsyIngestModuleIngestSettings ingestOptions, Processor processor)
 {
  this.jobSettings = ingestOptions;
  this.processor = processor;
  this.blackboard = Case.getCurrentCase().getServices().getBlackboard();
 }

 @Override
 public ProcessResult process(AbstractFile af)
 {
  if (af.getLocalAbsPath() == null)
  {
   return ProcessResult.OK;
  }
  File autopsyFile = new File(af.getLocalAbsPath());
  if (this.jobSettings.isImages() && this.processor.isImageFile(autopsyFile))
  {
   return this.processImage(autopsyFile, af);
  }
  if (this.jobSettings.isVideos() && this.processor.isVideoFile(autopsyFile))
  {
   return this.processVideo(autopsyFile,af);
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

 private ProcessResult processImage(File file, AbstractFile af)
 {
  try
  {
   ImageDetection imageDetection = this.processor.processImage(file);
   BlackboardArtifact artifact = this.createArtifact(af);
   if (imageDetection == null)
   {
    this.publishAttribute(artifact, "no objects found");
    this.indexArtifact(artifact);
    return ProcessResult.OK;
   }
   for (Detection detection : imageDetection.getDetections())
   {
    this.publishAttribute(artifact, detection.getClassName());
   }
   this.indexArtifact(artifact);
   return ProcessResult.OK;
  }
  catch (IllegalArgumentException | IOException | TskCoreException | Blackboard.BlackboardException ex)
  {
   ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
   return ProcessResult.ERROR;
  }
 }

 private ProcessResult processVideo(File file, AbstractFile af)
 {
  try
  {
   VideoDetection[] videoDetections = this.processor.processVideo(file);
   BlackboardArtifact artifact = this.createArtifact(af);
   if (videoDetections == null || videoDetections.length == 0)
   {
    this.publishAttribute(artifact, "no objects found");
    this.indexArtifact(artifact);
    return ProcessResult.OK;
   }
   for (VideoDetection videoDetection : videoDetections)
   {
    for (Detection detection : videoDetection.getDetections())
    {
     this.publishAttribute(artifact, detection.getClassName());
    }
   }
   this.indexArtifact(artifact);
   return ProcessResult.OK;
  }  
  catch (IllegalArgumentException | IOException | TskCoreException | Blackboard.BlackboardException ex)
  {
   ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
   return ProcessResult.ERROR;
  }
 }
 
 private BlackboardArtifact createArtifact(AbstractFile file) throws TskCoreException
 {
  return file.newArtifact(BlackboardArtifact.ARTIFACT_TYPE.TSK_INTERESTING_FILE_HIT);
 }
 
 private void publishAttribute(BlackboardArtifact artifact,String attributeTitle) throws TskCoreException
 {
  BlackboardAttribute blackboardAttribute = new BlackboardAttribute(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_SET_NAME, ObjectDetectionIngestModuleFactory.ModuleName, attributeTitle);
  artifact.addAttribute(blackboardAttribute);
 }
 
 private void indexArtifact(BlackboardArtifact artifact) throws TskCoreException, Blackboard.BlackboardException
 {
   blackboard.indexArtifact(artifact);
   IngestServices.getInstance().fireModuleDataEvent(new ModuleDataEvent(ObjectDetectionIngestModuleFactory.ModuleName, BlackboardArtifact.ARTIFACT_TYPE.TSK_INTERESTING_FILE_HIT));
 }
}
