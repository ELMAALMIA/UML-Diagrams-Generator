package org.mql.java.ui;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.mql.java.models.RelationModel;



public class RelationUi extends JPanel{
    private static final long serialVersionUID = 1L;
    
    private RelationModel relation;
    private ProjectUi projectUi;
    
    private Point p1, p2;
    
    public RelationUi(RelationModel relation, ProjectUi projectUi) {
        this.relation = relation;
        this.projectUi = projectUi;
        
        setSize(500, 500);
        drawRelation();
    }
    
    private void drawRelation() {
//        UMLC childUmlClassifier = projectUi.getUmlClassifier(umlRelation.getChild());
//        UMLClassifier parentUmlClassifier = projectUi.getUmlClassifier(umlRelation.getParent());
                
//        p1 = new Point(childUmlClassifier.getX(), childUmlClassifier.getY());
//        p2 = new Point(parentUmlClassifier.getX(), parentUmlClassifier.getY());
        
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }


    public void move(MouseEvent e) {

    }
}
