package jdchoi.cornerstone;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * <b>Last update:</b> 06/19/2009
 * @author Jinho D. Choi
 */
public class EditorTemplate extends JFrame
{
	protected String SYS_PATH    = "sys/";
	protected String CONFIG_PATH = "config/";
	
	public static    int WIDTH;
	protected        int HEIGHT;
	protected     String TITLE;
	public static String LANGUAGE;
	protected     String USER_ID;
	protected     String CURR_DIR  = ".";
	protected     String CURR_FILE = null;
	
	protected DocumentBuilder d_builder;
	public static    Document doc;
	
	public EditorTemplate(int width, int height, String title, String language, String userId)
	{
		WIDTH    = width;
		HEIGHT   = height;
		TITLE    = title;
		LANGUAGE = language;
		USER_ID  = userId;
		
		try
		{
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			d_builder = dFactory.newDocumentBuilder();
			
			File file = new File(CONFIG_PATH + userId);
			if (file.exists())
			{
				Scanner scan = new Scanner(file);
				CURR_DIR = scan.nextLine().trim();
			}
		}
		catch (Exception e) {e.printStackTrace();}
		
		addComponentListener(new ResizeAdapter());
	}
	
	/**
	 * Returns an array of data in {@link filename} (one value per line).
	 * If ({@link addEmpty}), add an empty string at the front.
	 * @param filename name of the file that contains data
	 * @return array of data in {@link filename} (one value per line)
	 */
	public String[] getArray(String filename)
	{
		String[] arr = new String[0];
		
		try
		{
			Scanner scan = new Scanner(new File(filename));
			ArrayList<String> arrList = new ArrayList<String>();
			
			while (scan.hasNext())	arrList.add(scan.next());
			arr = new String[arrList.size()];
			arrList.toArray(arr);
		}
		catch (IOException e) {e.printStackTrace();}
		
		return arr;
	}
	
	protected void updateTitle(String filename)
	{
		CURR_FILE = filename;
		setTitle(TITLE+" - "+CURR_FILE.substring(CURR_FILE.lastIndexOf('/')+1));	
	}
	
	protected void saveDir()
	{
		try
		{
			PrintStream fout = new PrintStream(new FileOutputStream(CONFIG_PATH+USER_ID));
			fout.println(CURR_DIR);
		}
		catch (IOException e) {e.printStackTrace();}
	}
	
	protected String showInputDialog(String text, String title, String value)
	{
		String input = (String)new JOptionPane().showInputDialog(this, text, title, JOptionPane.QUESTION_MESSAGE, null, null, value);

		if (input == null)		return null;	input = input.trim();
		if (input.equals(""))	return null;
		
		return input;
	}
	
	protected void showErrorDialog(String message)
	{
		new JOptionPane().showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Creates an empty element with 'tagName'.
	 * @param tagName name of the element
	 * @return empty element
	 */
	static public Element createElement(String tagName)
	{
		return doc.createElement(tagName);
	}
	
	private class ResizeAdapter extends ComponentAdapter
	{
		public void componentResized(ComponentEvent e)
		{
			setSize(new Dimension(WIDTH, getHeight()));
		}
	}
}
