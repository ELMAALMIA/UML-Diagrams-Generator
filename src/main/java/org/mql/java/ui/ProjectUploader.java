package org.mql.java.ui;

import javax.swing.*;

import org.mql.java.models.PackageModel;
import org.mql.java.parsers.parserImp.ProjectParser;
import org.mql.java.utils.ProjectCompiler;
import org.mql.java.xml.XMIGenerator;
import org.mql.java.xml.XMLGenerator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ProjectUploader extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton uploadButton;
	private JButton downloadXMLButton;
	private JButton downloadXMIButton;
	private JButton  showPackageDiagramButton;
	private XMLGenerator xmlGenerator;
	private XMIGenerator xmiGenerator;
	private String xmlOutput;
	private String xmlOutputXmi;
    private JButton showConsoleResultButton;
    private ProjectParser projectParser; 
    private JButton showClassDiagramButton;

	public ProjectUploader() {
		xmlGenerator = new XMLGenerator();
		xmiGenerator = new XMIGenerator();
		createUI();
	}

	private void createUI() {
	    org.mql.java.ui.theme.UITheme theme = org.mql.java.ui.theme.UITheme.getInstance();
	    
	    // Main panel with consistent styling
	    JPanel mainPanel = org.mql.java.ui.components.ComponentFactory.createPanel();
	    mainPanel.setLayout(new GridBagLayout());
	    
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new java.awt.Insets(theme.PADDING_SMALL, theme.PADDING_MEDIUM, 
	                                     theme.PADDING_SMALL, theme.PADDING_MEDIUM);
	    gbc.gridx = 0;
	    gbc.gridy = 0;

	    uploadButton = org.mql.java.ui.components.ComponentFactory.createButton("Upload Project");
	    uploadButton.addActionListener(this::uploadButtonAction);
	    mainPanel.add(uploadButton, gbc);

	    downloadXMLButton = org.mql.java.ui.components.ComponentFactory.createButton("Download XML File");
	    downloadXMLButton.addActionListener(this::downloadXMLButtonAction);
	    downloadXMLButton.setEnabled(false);
	    gbc.gridy++;
	    mainPanel.add(downloadXMLButton, gbc);

	    downloadXMIButton = org.mql.java.ui.components.ComponentFactory.createButton("Download XMI File");
	    downloadXMIButton.addActionListener(this::downloadXMIButtonAction);
	    downloadXMIButton.setEnabled(false);
	    gbc.gridy++;
	    mainPanel.add(downloadXMIButton, gbc);

	    showPackageDiagramButton = org.mql.java.ui.components.ComponentFactory.createButton("Show Package Diagram");
	    showPackageDiagramButton.addActionListener(this::showPackageDiagramButtonAction);
	    showPackageDiagramButton.setEnabled(false);
	    gbc.gridy++;
	    mainPanel.add(showPackageDiagramButton, gbc);

	    showClassDiagramButton = org.mql.java.ui.components.ComponentFactory.createButton("Show Class Diagram");
	    showClassDiagramButton.addActionListener(this::showClassDiagramButtonAction);
	    showClassDiagramButton.setEnabled(false);
	    gbc.gridy++;
	    mainPanel.add(showClassDiagramButton, gbc);

	    showConsoleResultButton = org.mql.java.ui.components.ComponentFactory.createButton("Show Console Result");
	    showConsoleResultButton.addActionListener(this::showConsoleResultButtonAction);
	    showConsoleResultButton.setEnabled(false);
	    gbc.gridy++;
	    mainPanel.add(showConsoleResultButton, gbc);

	    this.add(mainPanel);
	    this.setTitle("UML Diagrams Generator");
	    this.setSize(450, 400);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    this.getContentPane().setBackground(theme.BACKGROUND_COLOR);
	}

	private void uploadButtonAction(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = fileChooser.showOpenDialog(ProjectUploader.this);

		if (option == JFileChooser.APPROVE_OPTION) {
			File selectedFolder = fileChooser.getSelectedFile();
			String projectPath = selectedFolder.getAbsolutePath();
			String projectName = selectedFolder.getName();

			// Check if bin directory exists
			File binDirectory = new File(projectPath + File.separator + "bin");
			
			// If bin doesn't exist, check if we can compile the project
			if (!binDirectory.exists() || !binDirectory.isDirectory() 
					|| (binDirectory.exists() && binDirectory.listFiles() != null && binDirectory.listFiles().length == 0)) {
				
				if (ProjectCompiler.needsCompilation(projectPath)) {
					// Ask user if they want to compile
					int compileOption = JOptionPane.showConfirmDialog(ProjectUploader.this,
							"The project needs to be compiled.\n\n" +
							"This application requires compiled Java classes (.class files).\n\n" +
							"Would you like to compile the project automatically?\n\n" +
							"Note: This requires JDK (not JRE) and may take a moment.",
							"Compile Project?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if (compileOption == JOptionPane.YES_OPTION) {
						// Show progress
						JOptionPane.showMessageDialog(ProjectUploader.this,
								"Compiling project...\n\n" +
								"Please wait. This may take a moment.",
								"Compiling", JOptionPane.INFORMATION_MESSAGE);
						
						boolean compiled = ProjectCompiler.compileProject(projectPath);
						
						if (!compiled) {
							JOptionPane.showMessageDialog(ProjectUploader.this,
									"Compilation failed!\n\n" +
									"Possible reasons:\n" +
									"- Missing dependencies\n" +
									"- Compilation errors in source files\n" +
									"- JDK not available (JRE only)\n\n" +
									"Please compile manually and try again.\n\n" +
									"Check console for details.",
									"Compilation Failed", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						// Verify bin directory was created
						if (!binDirectory.exists() || binDirectory.listFiles() == null || binDirectory.listFiles().length == 0) {
							JOptionPane.showMessageDialog(ProjectUploader.this,
									"Compilation completed but no class files were generated.\n\n" +
									"Please check for compilation errors.",
									"Warning", JOptionPane.WARNING_MESSAGE);
							return;
						}
					} else {
						// User declined compilation
						String message = "The project needs to be compiled.\n\n" +
								"This application requires compiled Java classes (.class files) in a 'bin' directory.\n\n" +
								"Please:\n" +
								"1. Compile your Java project first\n" +
								"2. Make sure the compiled classes are in a 'bin' directory\n" +
								"3. Then try uploading again\n\n" +
								"Selected path: " + projectPath;
						JOptionPane.showMessageDialog(ProjectUploader.this, message,
								"Project Not Ready", JOptionPane.WARNING_MESSAGE);
						return;
					}
				} else {
					// No source files found
					String message = "The selected project does not have a 'bin' directory with compiled classes,\n" +
							"and no Java source files were found to compile.\n\n" +
							"This application requires compiled Java classes (.class files) in a 'bin' directory.\n\n" +
							"Selected path: " + projectPath;
					JOptionPane.showMessageDialog(ProjectUploader.this, message,
							"Project Not Ready", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}

			try {
				projectParser = new ProjectParser(projectPath);
				projectParser.parse();

				// Check if any packages were found
				if (projectParser.getProject().getPackagesList().isEmpty()) {
					JOptionPane.showMessageDialog(ProjectUploader.this,
							"No packages or classes found in the project.\n" +
							"Make sure the 'bin' directory contains compiled .class files.",
							"No Classes Found", JOptionPane.WARNING_MESSAGE);
					return;
				}

				xmlOutput = projectPath + File.separator + "resources" + File.separator + "xmlDocument" + File.separator
						+ projectName + "-UML.xml";
				xmlGenerator.generateXML(projectParser.getProject(), xmlOutput);

				xmlOutputXmi = projectPath + File.separator + "resources" + File.separator + "xmlDocument" + File.separator + projectName + "-Xmi-UML.xml";
				xmiGenerator.generateXMI(projectParser.getProject(), xmlOutputXmi);

				downloadXMLButton.setEnabled(true);
				downloadXMIButton.setEnabled(true);
				showPackageDiagramButton.setEnabled(true);
				showConsoleResultButton.setEnabled(true);
				showClassDiagramButton.setEnabled(true);

				JOptionPane.showMessageDialog(ProjectUploader.this, "XML files generated successfully!\n\n" +
						"XML: " + xmlOutput + "\n" +
						"XMI: " + xmlOutputXmi,
						"Success", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ProjectUploader.this,
						"Error processing project: " + ex.getMessage() + "\n\n" +
						"Please check the console for details.",
						"Error", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
	}

	private void downloadXMLButtonAction(ActionEvent e) {
		if (xmlOutput == null || xmlOutput.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No XML file available to download.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File(xmlOutput));
		fileChooser.setDialogTitle("Save XML File");
		int userSelection = fileChooser.showSaveDialog(this);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			try {
				Files.copy(Paths.get(xmlOutput), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
				JOptionPane.showMessageDialog(this, "File saved to: " + fileToSave.getAbsolutePath(), "File Saved",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void downloadXMIButtonAction(ActionEvent e) {
		if (xmlOutputXmi == null || xmlOutputXmi.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No XMI file available to download.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File(xmlOutputXmi));
		fileChooser.setDialogTitle("Save XMI File");
		int userSelection = fileChooser.showSaveDialog(this);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			try {
				Files.copy(Paths.get(xmlOutputXmi), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
				JOptionPane.showMessageDialog(this, "XMI file saved to: " + fileToSave.getAbsolutePath(), "File Saved",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error saving XMI file: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void showConsoleResultButtonAction(ActionEvent e) {
	    if (projectParser != null && projectParser.getProject() != null) {
	        List<PackageModel> packagesList = projectParser.getProject().getPackagesList();
	        String  name = projectParser.getProject().getName();
	        String packageDetails = parse(packagesList);

	        JTextArea textArea = new JTextArea(30, 60);
	        textArea.setText(packageDetails);
	        textArea.setEditable(false);
	        JScrollPane scrollPane = new JScrollPane(textArea);

	        JDialog dialog = new JDialog(this, "Project Details"+name , Dialog.ModalityType.APPLICATION_MODAL);
	        dialog.getContentPane().add(scrollPane);
	        dialog.pack();
	        dialog.setLocationRelativeTo(this);
	        dialog.setVisible(true);
	    } else {
	        JOptionPane.showMessageDialog(this, "No project details available.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}



	// Removed unused method

	public String parse(List<PackageModel> packages) {
	    StringBuilder sb = new StringBuilder();
	    for (PackageModel packageModel : packages) {
	        sb.append(packageModel);
	        if (packageModel.getPackages() != null) {
	            sb.append(parse(packageModel.getPackages()));
	        }
	    }
	    return sb.toString();
	}


	private void showPackageDiagramButtonAction(ActionEvent e) {
		if (projectParser != null && projectParser.getProject() != null) {
            List<PackageModel> rootPackages = projectParser.getProject().getPackagesList();
            String projectName = projectParser.getProject().getName();
            PackageUi.displayPackageDiagram(projectName, rootPackages);
        } else {
            JOptionPane.showMessageDialog(null, "Please parse a project first.", "No Project Data", JOptionPane.ERROR_MESSAGE);
        }
	}

	
	private void showClassDiagramButtonAction(ActionEvent e) {
	    if (projectParser != null && projectParser.getProject() != null) {
	        List<PackageModel> rootPackages = projectParser.getProject().getPackagesList();
	        ClassDiagramUi.displayClassDiagram(projectParser.getProject().getName(), rootPackages);
	    } else {
	        JOptionPane.showMessageDialog(this, "No project data available for class diagram.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new ProjectUploader().setVisible(true));
	}
}
