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

  public TreeDrawer(Graphics gg) {
    g=gg;
    background = g.getColor();
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
      g.setColor(Color.blue);
      g.drawLine(xPos(pos), yPos(pos), xPos(tree_.parent(pos)),
		 yPos(tree_.parent(pos)));
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
    int ascent = ((Integer)pos.get("ascent")).intValue();
    int descent = ((Integer)pos.get("descent")).intValue();
    Rectangle2D bounds = ((Rectangle2D)pos.get("bounds"));
 // int height = (int)bounds.getHeight();
    int width = (int)bounds.getWidth();
    int x = xPos(pos)-width/2;
    int y = yPos(pos)-ascent/2;
    g.setColor(background);
    g.fillRect(x,y,width,ascent+descent);
    y += ascent;
    
    String str = pos.element().toString();
    if (str.equals("ROOT"))
    {
    	g.setColor(Color.black);
        g.drawString(str,x,y);
    }
    else
    {
    	String[] arr = str.split(":");
        g.setColor(Color.red);
        g.drawString(arr[0]+":",x,y);
        g.setColor(Color.black);
        g.drawString(arr[1],x+(arr[0].length()*10),y);
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
