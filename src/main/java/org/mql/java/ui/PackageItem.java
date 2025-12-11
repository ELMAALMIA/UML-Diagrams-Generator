package org.mql.java.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

import javax.swing.JPanel;

import org.mql.java.models.AnnotationModel;
import org.mql.java.models.ClassModel;
import org.mql.java.models.Enumeration;
import org.mql.java.models.InterfaceModel;
import org.mql.java.models.PackageModel;
import org.mql.java.ui.components.ComponentFactory;
import org.mql.java.ui.components.ContentPanel;
import org.mql.java.ui.components.TitlePanel;
import org.mql.java.ui.theme.UITheme;

public class PackageItem extends JPanel {
	private static final long serialVersionUID = 1L;
	private PackageModel pkg;
	private String packageName;
	private Point lineFromParent;

	public PackageItem(PackageModel pkg) {
		this.pkg = pkg;
		this.packageName = pkg.getName();
		drawPackage();
		setPreferredSize(new Dimension(UITheme.getInstance().PACKAGE_WIDTH, getPreferredSize().height));
	}

	public void drawPackage() {
		setLayout(new BorderLayout());
		setOpaque(false);
		drawPackageTitle();
		drawData();
	}

	public void setLineFromParent(Point parentLocation) {
		this.lineFromParent = parentLocation;
	}

	public Point getLineFromParent() {
		return lineFromParent;
	}

	private void drawData() {
		UITheme theme = UITheme.getInstance();
		ContentPanel dataPanel = ComponentFactory.createContentPanel();

		// Show classes
		for (ClassModel classModel : pkg.getClasses()) {
			dataPanel.addItem("  " + classModel.getName(), theme.SMALL_FONT);
		}

		// Show interfaces
		for (InterfaceModel interfaceModel : pkg.getInterfaces()) {
			dataPanel.addItem("  «interface» " + interfaceModel.getInterfaceName(), theme.ITALIC_FONT);
		}

		// Show enumerations
		for (Enumeration enumModel : pkg.getEnumerations()) {
			dataPanel.addItem("  «enum» " + enumModel.getName(), theme.SMALL_FONT);
		}

		// Show annotations
		for (AnnotationModel annotation : pkg.getAnnotations()) {
			dataPanel.addItem("  «annotation» " + annotation.getName(), theme.SMALL_FONT);
		}

		// Show nested packages
		for (PackageModel p : pkg.getPackages()) {
			dataPanel.addItem("  «package» " + p.getName(), theme.SMALL_FONT);
		}

		// If empty, show placeholder
		if (pkg.getClasses().isEmpty() && pkg.getInterfaces().isEmpty() && 
		    pkg.getEnumerations().isEmpty() && pkg.getAnnotations().isEmpty() && 
		    (pkg.getPackages() == null || pkg.getPackages().isEmpty())) {
			dataPanel.addItem("  (empty)", theme.SMALL_FONT);
		}

		add(dataPanel, BorderLayout.CENTER);
	}

	public void drawPackageTitle() {
		TitlePanel titlePanel = ComponentFactory.createTitlePanel(
			"«package» " + packageName, "package");
		add(titlePanel, BorderLayout.NORTH);
	}
	
}
