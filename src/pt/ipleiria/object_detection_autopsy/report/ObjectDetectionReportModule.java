package pt.ipleiria.object_detection_autopsy.report;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.report.DefaultReportConfigurationPanel;
import org.sleuthkit.autopsy.report.GeneralReportModule;
import org.sleuthkit.autopsy.report.ReportProgressPanel;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.TskCoreException;
import pt.ipleiria.object_detection_autopsy.ObjectDetectionIngestModuleFactory;

//@ServiceProvider(service = GeneralReportModule.class)
public class ObjectDetectionReportModule implements GeneralReportModule
{
 public static final String ModuleName = "pt.ipleiria.object_detection_report4autopsy";
 public static final Logger ObjectDetectionReportLogger = Logger.getLogger(ObjectDetectionIngestModuleFactory.ModuleName + ".logger");

 @Override
 public void generateReport(String filePath, ReportProgressPanel pnl)
 {
  pnl.setIndeterminate(true);
  try
  {
   ExcelExporter excelExporter = new ExcelExporter(filePath+this.getRelativeFilePath());
   List<BlackboardArtifact> artifacts = Case.getCurrentCase().getSleuthkitCase().getBlackboardArtifacts(BlackboardArtifact.ARTIFACT_TYPE.TSK_INTERESTING_FILE_HIT);
   for (BlackboardArtifact artifact : artifacts)
   {
    List<BlackboardAttribute> attributes = artifact.getAttributes();
    for (BlackboardAttribute attribute : attributes)
    {
     excelExporter.write(attribute);
    }
   }
  }
  catch (TskCoreException | IOException | InvalidFormatException ex)
  {
   ObjectDetectionReportLogger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
   return;
  }
 }

 @Override
 public JPanel getConfigurationPanel()
 {
  return new DefaultReportConfigurationPanel();
 }

 @Override
 public String getName()
 {
  return "Object Detection Report for Autopsy";
 }

 @Override
 public String getDescription()
 {
  return "This module generate report with Object Detection for Autopsy module work";
 }

 @Override
 public String getRelativeFilePath()
 {
  return "od4a_report.xlsx";
 }
}
