package cornerstone.toolkit;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <b>Last update:</b> 06/19/2009
 * @author Jinho D. Choi
 */
public class EditorTemplate extends JFrame
{
	private final Pattern P_TYPE = Pattern.compile("-[nvj]$");
	
	protected String SYS_PATH    = "sys"+File.separator;
	protected String CONFIG_PATH = "config"+File.separator;
	
	public static    int WIDTH;
	protected        int HEIGHT;
	protected     String TITLE;
	public static String LANGUAGE;
	protected     String USER_ID;
	protected     String CURR_DIR  = ".";
	protected     String CURR_FILE = null;
	
	protected DocumentBuilder d_builder;
	public static Document doc;
	
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
		CURR_FILE = filename.trim();
		int extIndex = CURR_FILE.length() - 4;
		
		if (extIndex < 0 || !CURR_FILE.substring(extIndex).equalsIgnoreCase(".xml"))
			CURR_FILE += ".xml";
		
		setTitle(TITLE+" - "+CURR_FILE.substring(CURR_FILE.lastIndexOf(File.separator)+1));	
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
	
	protected String getLemma()
	{
		String lemma = CURR_FILE.substring(CURR_FILE.lastIndexOf(File.separator)+1, CURR_FILE.lastIndexOf('.'));
		
		if (P_TYPE.matcher(lemma).find())
			return lemma.substring(0, lemma.length()-2);
		else
			return lemma;
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
	
	static public boolean isLanguage(String language)
	{
		return LANGUAGE.equals(language);
	}
}
