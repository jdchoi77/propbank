package jubilee.hindi;
import jdsl.core.algo.traversals.*;
import java.awt.*;
import java.awt.geom.*;
import jdsl.core.api.*;

/**
	* @author Lucy Perry (lep)
	* @version JDSL 2
	*/
public class TreeDrawer extends EulerTour {
  // Traversal of a tree that is specialized to draw a
  // tree within an applet.
  protected int Yoffset = 40;       // Y offset from (0,0)
  protected int Xoffset = 20;       // X offset from (0,0)
  protected Graphics g;             // where to draw the tree
  protected Color background;       // fill color
  protected int totalShift;         // a running total to shift bounding boxes.  The shift
                                    //    distance is the sum of the shifts stored at 
                                    //    ancestors.

  final static float dash[] = {2.0f};
  final static BasicStroke dashed = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash, 0.0f);
  final static BasicStroke solid  = new BasicStroke();
  
  protected Graphics2D g2d;
  
  public int i_width;
  public int i_height;
  
  public TreeDrawer(Graphics gg) {
    g=gg;
    g2d = (Graphics2D) g.create();
    background = g.getColor();
    i_width = i_height = 0;
  }
  
  /**
   * When visiting a node for the first time we shift x by totalShift
   */
  protected void visitFirstTime(Position pos){
    int x = ((Integer)pos.get("x")).intValue();
    int shift = ((Integer)pos.get("shift")).intValue();
    pos.set("x", new Integer(x+totalShift));
    totalShift += shift;
  }
  
  /**
   * When visiting a node for the last time we draw the node.
   */
  protected void visitLastTime(Position pos){
    int shift = ((Integer)pos.get("shift")).intValue();
    if(!tree_.isRoot(pos)) {
      //Draw the edge to the parent
      String str = pos.element().toString();
      
      if (str.startsWith("NULL"))	g2d.setStroke(dashed);
      else							g2d.setStroke(solid);

      g2d.setColor(Color.lightGray);
      g2d.drawLine(xPos(pos), yPos(pos), xPos(tree_.parent(pos)),yPos(tree_.parent(pos)));
    }
    
    drawString(pos);
    totalShift -= shift;
    cleanup(pos);
  }
  
  /**
   * External nodes are drawn in the same manner as internal nodes
   */
  protected void visitExternal( Position pos ) { 
    visitFirstTime(pos);
    visitLastTime(pos);
  }
  
  /**
   * Draw the string at its proper location.
   */
  private void drawString(Position pos) {
    int ascent  = ((Integer)pos.get("ascent")).intValue();
    int descent = ((Integer)pos.get("descent")).intValue();
    Rectangle2D bounds = ((Rectangle2D)pos.get("bounds"));
    int width  = (int)bounds.getWidth();
    
    int dx = xPos(pos);
    
    int x = dx-width/2;
    int y = yPos(pos)-ascent/2;
    g.setColor(background);
    g.fillRect(x,y,width,ascent+descent);
    y += ascent;
    
    if (x+width > i_width)				i_width  = x+width;
    if (y+ascent+descent > i_height)	i_height = y+ascent+descent;
    
    String str = pos.element().toString();
    
    if (str.equals("ROOT"))
    {
    	g.setColor(Color.black);
        g.drawString(str,x,y);
    }
    else
    {
    	String[] arr = str.split(":");
    	g.setColor(Color.blue);
    	g.drawString(arr[0]+" - "+arr[2],x,y);
    	g.setColor(Color.red);
    	
    	int px = xPos(tree_.parent(pos));
    	int gap = (px-dx) / 5;	
    	
        g.drawString(arr[1],dx+gap,y-15);
    }
  }
  
  private int xPos(Position p) {
    int x = ((Integer)p.get("x")).intValue();
    int width = ((Integer)p.get("width")).intValue();
    return x + width/2 + Xoffset;
  }
  
  private int yPos(Position p) {
    return ((Integer)p.get("y")).intValue()+Yoffset;
  }

  private void cleanup(Position p) {
    p.destroy("x");
    p.destroy("y");
    p.destroy("shift");
    p.destroy("ascent");
    p.destroy("descent");
    p.destroy("bounds");
  }
}
