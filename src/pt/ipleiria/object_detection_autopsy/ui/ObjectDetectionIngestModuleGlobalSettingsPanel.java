package pt.ipleiria.object_detection_autopsy.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.text.NumberFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Level;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.sleuthkit.autopsy.ingest.IngestModuleGlobalSettingsPanel;
import pt.ipleiria.object_detection_autopsy.ObjectDetectionIngestModuleFactory;
import pt.ipleiria.object_detection_autopsy.backgroud_services.ObjectDetectionAutopsyIngestModuleIngestSettings;
import pt.ipleiria.object_detection_autopsy.processors.Processor;

public class ObjectDetectionIngestModuleGlobalSettingsPanel extends IngestModuleGlobalSettingsPanel
{

 private final ObjectDetectionIngestModuleFactory factory;
 private final ObjectDetectionAutopsyIngestModuleIngestSettings settings;
 private JTextField textFieldAddress;
 private JFormattedTextField formattedTextFieldPort;
 private JLabel labelMessage;

 public ObjectDetectionIngestModuleGlobalSettingsPanel(ObjectDetectionIngestModuleFactory factory, ObjectDetectionAutopsyIngestModuleIngestSettings settings)
 {
  super();
  this.textFieldAddress = null;
  this.formattedTextFieldPort = null;
  this.factory = factory;
  this.settings = settings;
  this.buildGlobalUI();
 }

 @Override
 public void saveSettings()
 {
  this.settings.setAddress(this.textFieldAddress.getText());
  this.settings.setPort(this.formattedTextFieldPort.getText());
  this.settings.setIsRemote(true);
  this.settings.saveGlobal();
 }

 private void checkSettings(ActionEvent e)
 {
  JComponent button = null;
  if (e != null && e.getSource() != null && (e.getSource() instanceof JComponent))
  {
   button = (JComponent) e.getSource();
   button.setEnabled(false);
  }
  this.labelMessage.setText("Testing connection..."); 
 
  final JComponent disabledButton = button;
  CompletableFuture<Boolean> promise = new CompletableFuture<>();

  Executors.newCachedThreadPool().submit(() ->
  {
   Processor temp = this.factory.getProcessor(this.textFieldAddress.getText(), Integer.valueOf(this.formattedTextFieldPort.getText(), 10), true);
   try
   {
    promise.complete(temp.isProcessorAvailable());
   }
   catch (ConnectException ex)
   {
    ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, ex.getLocalizedMessage());
    promise.completeExceptionally(new ConnectException("Connection isn't established."));
   }
   catch (IOException ex)
   {
    ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, ex.getMessage(), ex);
    promise.completeExceptionally(new IOException("Error on open connection."));
   }
   catch (IllegalAccessException ex)
   {
    ObjectDetectionIngestModuleFactory.ObjectDetectionLogger.log(Level.SEVERE, ex.getMessage(), ex);
    promise.completeExceptionally(new IllegalAccessException("Error getting a proper connection."));
   }
   finally
   {
    temp.close();
   }
  });

  promise.whenComplete((hasConnection, error) ->
  {
   if(error != null)
   {
    this.labelMessage.setText(error.getLocalizedMessage());
   }
   else if (hasConnection)
   {
    this.labelMessage.setText("Connection is established");
   }
   else
   {
    this.labelMessage.setText("Connection isn't established");
   }
   
   if(disabledButton != null)
   {
    disabledButton.setEnabled(true);
   }
  });
 }

 private void buildGlobalUI()
 {
  NumberFormat format = NumberFormat.getInstance();
  format.setGroupingUsed(false);
  format.setRoundingMode(RoundingMode.HALF_UP);
  format.setMaximumIntegerDigits(65535);
  format.setMinimumIntegerDigits(1);
  format.setParseIntegerOnly(true);

  GridBagLayout gridBagLayout = new GridBagLayout();
  gridBagLayout.columnWidths = new int[]
  {
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
  };
  gridBagLayout.rowHeights = new int[]
  {
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
  };
  gridBagLayout.columnWeights = new double[]
  {
   0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
  };
  gridBagLayout.rowWeights = new double[]
  {
   0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
  };
  setLayout(gridBagLayout);

  JRadioButton radioButtonLocal = new JRadioButton("Local");
  radioButtonLocal.setSelected(!this.settings.isRemote());
  radioButtonLocal.setEnabled(false);
  GridBagConstraints gbc_radioButtonLocal = new GridBagConstraints();
  gbc_radioButtonLocal.fill = GridBagConstraints.HORIZONTAL;
  gbc_radioButtonLocal.anchor = GridBagConstraints.NORTHEAST;
  gbc_radioButtonLocal.insets = new Insets(0, 0, 5, 5);
  gbc_radioButtonLocal.gridx = 1;
  gbc_radioButtonLocal.gridy = 1;
  gbc_radioButtonLocal.gridheight = 1;
  gbc_radioButtonLocal.gridwidth = 3;
  this.add(radioButtonLocal, gbc_radioButtonLocal);

  JRadioButton radioButtonRemote = new JRadioButton("Remote");
  radioButtonRemote.setSelected(this.settings.isRemote());
  GridBagConstraints gbc_radioButtonRemote = new GridBagConstraints();
  gbc_radioButtonRemote.fill = GridBagConstraints.HORIZONTAL;
  gbc_radioButtonRemote.anchor = GridBagConstraints.NORTHEAST;
  gbc_radioButtonRemote.insets = new Insets(0, 0, 5, 5);
  gbc_radioButtonRemote.gridx = 1;
  gbc_radioButtonRemote.gridy = 3;
  gbc_radioButtonRemote.gridheight = 1;
  gbc_radioButtonRemote.gridwidth = 3;
  this.add(radioButtonRemote, gbc_radioButtonRemote);

  ButtonGroup radioButtonsGroup = new ButtonGroup();
  radioButtonsGroup.add(radioButtonLocal);
  radioButtonsGroup.add(radioButtonRemote);

  JPanel panelRemote = new JPanel();
  GridBagConstraints gbc_panelRemote = new GridBagConstraints();
  gbc_panelRemote.anchor = GridBagConstraints.NORTHEAST;
  gbc_panelRemote.gridheight = 6;
  gbc_panelRemote.gridwidth = 15;
  gbc_panelRemote.insets = new Insets(0, 0, 5, 5);
  gbc_panelRemote.fill = GridBagConstraints.BOTH;
  gbc_panelRemote.gridx = 1;
  gbc_panelRemote.gridy = 4;
  add(panelRemote, gbc_panelRemote);
  GridBagLayout gbl_panelRemote = new GridBagLayout();
  gbl_panelRemote.columnWidths = new int[]
  {
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
  };
  gbl_panelRemote.rowHeights = new int[]
  {
   0, 0, 0, 0, 0, 0, 0, 0
  };
  gbl_panelRemote.columnWeights = new double[]
  {
   0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
  };
  gbl_panelRemote.rowWeights = new double[]
  {
   0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
  };
  panelRemote.setLayout(gbl_panelRemote);

  JLabel labelAddress = new JLabel("Address:");
  GridBagConstraints gbc_labelAddress = new GridBagConstraints();
  gbc_labelAddress.insets = new Insets(0, 0, 5, 5);
  gbc_labelAddress.anchor = GridBagConstraints.EAST;
  gbc_labelAddress.gridx = 1;
  gbc_labelAddress.gridy = 1;
  gbc_labelAddress.gridheight = 1;
  gbc_labelAddress.gridwidth = 3;
  panelRemote.add(labelAddress, gbc_labelAddress);

  this.textFieldAddress = new JTextField();
  this.textFieldAddress.setToolTipText("Enter the server address");
  this.textFieldAddress.setText(this.settings.getAddress());
  GridBagConstraints gbc_formattedTextFieldAddress = new GridBagConstraints();
  gbc_formattedTextFieldAddress.insets = new Insets(0, 0, 5, 5);
  gbc_formattedTextFieldAddress.gridwidth = 5;
  gbc_formattedTextFieldAddress.fill = GridBagConstraints.HORIZONTAL;
  gbc_formattedTextFieldAddress.gridx = 4;
  gbc_formattedTextFieldAddress.gridy = 1;
  gbc_formattedTextFieldAddress.gridheight = 1;
  gbc_formattedTextFieldAddress.gridwidth = 10;
  panelRemote.add(this.textFieldAddress, gbc_formattedTextFieldAddress);

  JLabel labelPort = new JLabel("Port:");
  GridBagConstraints gbc_labelPort = new GridBagConstraints();
  gbc_labelPort.insets = new Insets(0, 0, 5, 5);
  gbc_labelPort.anchor = GridBagConstraints.EAST;
  gbc_labelPort.gridx = 1;
  gbc_labelPort.gridy = 2;
  gbc_labelPort.gridheight = 1;
  gbc_labelPort.gridwidth = 3;
  panelRemote.add(labelPort, gbc_labelPort);

  this.formattedTextFieldPort = new JFormattedTextField(format);
  this.formattedTextFieldPort.setToolTipText("Enter the server port");
  this.formattedTextFieldPort.setText(Integer.toString(this.settings.getPort(), 10));
  GridBagConstraints gbc_formattedTextFieldPort = new GridBagConstraints();
  gbc_formattedTextFieldPort.insets = new Insets(0, 0, 5, 5);
  gbc_formattedTextFieldPort.gridwidth = 5;
  gbc_formattedTextFieldPort.fill = GridBagConstraints.HORIZONTAL;
  gbc_formattedTextFieldPort.gridx = 4;
  gbc_formattedTextFieldPort.gridy = 2;
  gbc_formattedTextFieldPort.gridheight = 1;
  gbc_formattedTextFieldPort.gridwidth = 10;
  panelRemote.add(this.formattedTextFieldPort, gbc_formattedTextFieldPort);

  JButton buttonCheckConfigs = new JButton("Check connection");
  buttonCheckConfigs.addActionListener(new ActionListener()
  {
   @Override
   public void actionPerformed(ActionEvent e)
   {
    checkSettings(e);
   }
  });
  GridBagConstraints gbc_buttonCheckConfigs = new GridBagConstraints();
  gbc_buttonCheckConfigs.gridwidth = 6;
  gbc_buttonCheckConfigs.fill = GridBagConstraints.HORIZONTAL;
  gbc_buttonCheckConfigs.anchor = GridBagConstraints.NORTH;
  gbc_buttonCheckConfigs.insets = new Insets(0, 0, 5, 5);
  gbc_buttonCheckConfigs.gridx = 2;
  gbc_buttonCheckConfigs.gridy = 4;
  panelRemote.add(buttonCheckConfigs, gbc_buttonCheckConfigs);

  this.labelMessage = new JLabel();
  GridBagConstraints gbc_labelMessage = new GridBagConstraints();
  gbc_labelMessage.insets = new Insets(0, 0, 5, 5);
  gbc_labelMessage.anchor = GridBagConstraints.NORTH;
  gbc_labelMessage.gridx = 2;
  gbc_labelMessage.gridy = 5;
  gbc_labelMessage.gridheight = 1;
  gbc_labelMessage.gridwidth = 7;
  panelRemote.add(this.labelMessage, gbc_labelMessage);
  this.checkSettings(null);
 }
}
