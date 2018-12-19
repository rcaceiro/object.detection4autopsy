package pt.ipleiria.object_detection_autopsy.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.text.JTextComponent;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettingsPanel;
import pt.ipleiria.object_detection_autopsy.backgroud_services.ObjectDetectionAutopsyIngestModuleIngestSettings;
import pt.ipleiria.object_detection_autopsy.comparator.StringComparator;
import pt.ipleiria.object_detection_autopsy.list_model.SortedListModel;

public class ObjectDetectionIngestModuleJobSettingsPanel extends IngestModuleIngestJobSettingsPanel
{

 private final ObjectDetectionAutopsyIngestModuleIngestSettings settings;
 private JList<String> listAvailableDetections;
 private JList<String> listChoosedDetections;
 private final SortedListModel<String> modelAvailableDetections;
 private final SortedListModel<String> modelChoosedDetections;

 public ObjectDetectionIngestModuleJobSettingsPanel(ObjectDetectionAutopsyIngestModuleIngestSettings settings)
 {
  super();
  this.settings = settings;
  StringComparator comparator = new StringComparator();
  this.modelAvailableDetections = new SortedListModel<>(comparator);
  this.modelChoosedDetections = new SortedListModel<>(comparator);
  this.listAvailableDetections = null;
  this.listChoosedDetections = null;
  this.modelAvailableDetections.cleanAddAll(this.settings.getAvailableDetections());
  this.modelChoosedDetections.cleanAddAll(this.settings.getChoosedDetections());
  this.buildJobUI();
 }

 @Override
 public IngestModuleIngestJobSettings getSettings()
 {
  return this.settings;
 }

 private void buildJobUI()
 {
  setLayout(new BorderLayout(0, 0));

  JPanel panelMedia = new JPanel();
  add(panelMedia, BorderLayout.NORTH);

  JCheckBox checkboxImages = new JCheckBox("Images");
  checkboxImages.setSelected(this.settings.isImages());
  checkboxImages.setToolTipText("I am to process image files");
  checkboxImages.addChangeListener((ChangeEvent e) ->
  {
   if (!(e.getSource() instanceof JToggleButton))
   {
    return;
   }
   JToggleButton toggleButton = (JToggleButton) e.getSource();
   settings.setImages(toggleButton.isSelected());
  });

  JCheckBox checkboxVideos = new JCheckBox("Videos");
  checkboxVideos.setSelected(this.settings.isVideos());
  checkboxVideos.setToolTipText("I am to process videos files");
  checkboxVideos.addChangeListener((ChangeEvent e) ->
  {
   if (!(e.getSource() instanceof JToggleButton))
   {
    return;
   }
   JToggleButton toggleButton = (JToggleButton) e.getSource();
   settings.setVideos(toggleButton.isSelected());
  });

  JCheckBox checkBoxRememberSettings = new JCheckBox("Remember Job Settings");
  checkBoxRememberSettings.setSelected(this.settings.isRememberJobSettings());
  checkBoxRememberSettings.setToolTipText("I want that module remember my settings");
  checkBoxRememberSettings.addChangeListener((ChangeEvent e) ->
  {
   if (!(e.getSource() instanceof JToggleButton))
   {
    return;
   }
   JToggleButton toggleButton = (JToggleButton) e.getSource();
   settings.setRememberJobSettings(toggleButton.isSelected());
  });

  GroupLayout gl_panelMedia = new GroupLayout(panelMedia);
  gl_panelMedia.setHorizontalGroup(
          gl_panelMedia.createParallelGroup(Alignment.LEADING)
                  .addGroup(gl_panelMedia.createSequentialGroup()
                          .addContainerGap()
                          .addComponent(checkboxImages)
                          .addGap(39)
                          .addComponent(checkboxVideos, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
                          .addGap(18)
                          .addComponent(checkBoxRememberSettings, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
                          .addContainerGap())
  );
  gl_panelMedia.setVerticalGroup(
          gl_panelMedia.createParallelGroup(Alignment.LEADING)
                  .addGroup(gl_panelMedia.createParallelGroup(Alignment.BASELINE)
                          .addComponent(checkboxImages)
                          .addComponent(checkBoxRememberSettings)
                          .addComponent(checkboxVideos))
  );
  panelMedia.setLayout(gl_panelMedia);

  JPanel panelDetections = new JPanel();
  add(panelDetections, BorderLayout.CENTER);

  JButton buttonAdd = new JButton("Process >");
  buttonAdd.addActionListener((ActionEvent e) ->
  {
   int index;
   String key;
   while ((index = listAvailableDetections.getSelectedIndex()) != -1)
   {
    key = modelAvailableDetections.remove(index);
    modelChoosedDetections.addElement(key);
    settings.setValueToKey(key, true);
   }
  });

  JButton buttonRemove = new JButton("< Remove");
  buttonRemove.addActionListener((ActionEvent e) ->
  {
   int index;
   String key;
   while ((index = listChoosedDetections.getSelectedIndex()) != -1)
   {
    key = modelChoosedDetections.remove(index);
    modelAvailableDetections.addElement(key);
    settings.setValueToKey(key, false);
   }
  });

  JButton buttonRemoveAll = new JButton("< All");
  buttonRemoveAll.addActionListener((ActionEvent e) ->
  {
   settings.setValueToAllKeys(Boolean.FALSE);
   while (!modelChoosedDetections.isEmpty())
   {
    modelAvailableDetections.addElement(modelChoosedDetections.remove(0));
   }
  });

  JButton buttonAddAll = new JButton("All >");
  buttonAddAll.addActionListener((ActionEvent e) ->
  {
   settings.setValueToAllKeys(Boolean.TRUE);
   while (!modelAvailableDetections.isEmpty())
   {
    modelChoosedDetections.addElement(modelAvailableDetections.remove(0));
   }
  });

  JTextField textFieldSearchOnAvailable = new JTextField();
  textFieldSearchOnAvailable.setToolTipText("Search the available detections;");
  //TODO
  textFieldSearchOnAvailable.setEnabled(false);
  textFieldSearchOnAvailable.setColumns(10);
  textFieldSearchOnAvailable.addKeyListener(new KeyAdapter()
  {
   @Override
   public void keyTyped(KeyEvent e)
   {
    super.keyTyped(e);

    JTextComponent textComponent = (JTextComponent) e.getSource();
    listAvailableDetections.updateUI();
    modelAvailableDetections.setFilter(textComponent.getText());
   }
  });

  JTextField textFieldSearchOnChoosed = new JTextField();
  textFieldSearchOnChoosed.setToolTipText("Search the choosed detections");
  textFieldSearchOnChoosed.setColumns(10);
//TODO
  textFieldSearchOnChoosed.setEnabled(false);
  JScrollPane scrollPaneChoosedDetections = new JScrollPane();

  JScrollPane scrollPaneAvailableDetections = new JScrollPane();
  GroupLayout gl_panelDetections = new GroupLayout(panelDetections);
  gl_panelDetections.setHorizontalGroup(
          gl_panelDetections.createParallelGroup(Alignment.TRAILING)
                  .addGroup(gl_panelDetections.createSequentialGroup()
                          .addGroup(gl_panelDetections.createParallelGroup(Alignment.LEADING)
                                  .addComponent(textFieldSearchOnAvailable, 144, 144, 144)
                                  .addGroup(gl_panelDetections.createSequentialGroup()
                                          .addGap(6)
                                          .addComponent(scrollPaneAvailableDetections, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)))
                          .addPreferredGap(ComponentPlacement.UNRELATED)
                          .addGroup(gl_panelDetections.createParallelGroup(Alignment.LEADING, false)
                                  .addComponent(buttonRemoveAll, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                                  .addComponent(buttonAddAll, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                                  .addComponent(buttonAdd, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                                  .addComponent(buttonRemove, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
                          .addPreferredGap(ComponentPlacement.RELATED)
                          .addGroup(gl_panelDetections.createParallelGroup(Alignment.TRAILING)
                                  .addComponent(textFieldSearchOnChoosed, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                                  .addComponent(scrollPaneChoosedDetections, 0, 0, Short.MAX_VALUE))
                          .addContainerGap())
  );
  gl_panelDetections.setVerticalGroup(
          gl_panelDetections.createParallelGroup(Alignment.LEADING)
                  .addGroup(gl_panelDetections.createSequentialGroup()
                          .addGroup(gl_panelDetections.createParallelGroup(Alignment.LEADING)
                                  .addGroup(gl_panelDetections.createSequentialGroup()
                                          .addGroup(gl_panelDetections.createParallelGroup(Alignment.BASELINE)
                                                  .addComponent(textFieldSearchOnAvailable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                  .addComponent(textFieldSearchOnChoosed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                          .addPreferredGap(ComponentPlacement.RELATED)
                                          .addGroup(gl_panelDetections.createParallelGroup(Alignment.LEADING)
                                                  .addComponent(scrollPaneAvailableDetections, GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                                                  .addComponent(scrollPaneChoosedDetections, GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)))
                                  .addGroup(gl_panelDetections.createSequentialGroup()
                                          .addGap(50)
                                          .addComponent(buttonAddAll)
                                          .addGap(18)
                                          .addComponent(buttonAdd)
                                          .addGap(18)
                                          .addComponent(buttonRemove)
                                          .addGap(18)
                                          .addComponent(buttonRemoveAll)))
                          .addContainerGap())
  );

  this.listAvailableDetections = new JList<>();
  scrollPaneAvailableDetections.setViewportView(listAvailableDetections);
  listAvailableDetections.setModel(this.modelAvailableDetections);

  this.listChoosedDetections = new JList<>();
  scrollPaneChoosedDetections.setViewportView(listChoosedDetections);
  listChoosedDetections.setModel(this.modelChoosedDetections);

  panelDetections.setLayout(gl_panelDetections);
  this.setMinimumSize(new Dimension(450, 100));
  this.setMaximumSize(new Dimension(450, 300));
 }
}
