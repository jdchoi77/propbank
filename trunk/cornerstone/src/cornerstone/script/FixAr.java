package cornerstone.script;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import cornerstone.chinese.ChLib;

public class FixAr
{
	Document     doc;
	PrintStream  fout;
	
	public void fix(String inputFile, String outputFile)
	{
		init(inputFile, outputFile);
		print();
	}
	
	private void init(String inputFile, String outputFile)
	{
		DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
		
		try
		{
			DocumentBuilder builder = dFactory.newDocumentBuilder();
			doc  = builder.parse(new FileInputStream(inputFile));
			fout = new PrintStream(new FileOutputStream(outputFile));
		}
		catch (Exception e) {e.printStackTrace();System.exit(1);}
	}
	
	private void print()
	{
		fout.println("<!DOCTYPE verb SYSTEM \"verb.dtd\">");
		
		Element eVerb = doc.getDocumentElement();
		fout.println(getVerb(eVerb).trim());
	
		fout.close();
	}
	
	private String getVerb(Element eVerb)
	{
		StringBuilder build = new StringBuilder();
		
		// id
		Element eId = getChildElements(eVerb, ChLib.ID).get(0);
		build.append(getId(eId));

		// comment
		build.append(getComment(eVerb));
		
		// frameset*
		for (Element eFrameset : getChildElements(eVerb, ChLib.FRAMESET))
			build.append(getFrameset(eFrameset));
		
		return getText(eVerb.getTagName(), build.toString(), true);
	}
	
	private String getId(Element eId)
	{
		return getText(eId.getTagName(), getTextContent(eId), false);
	}
	
	private String getFrameset(Element eFrameset)
	{
		StringBuilder build = new StringBuilder();
		
		// role*
		for (Element eRole : getChildElements(eFrameset, ChLib.ROLE))
			build.append(getRole(eRole));
		
		// frame*
		for (Element eFrame : getChildElements(eFrameset, ChLib.FRAME))
			build.append(getFrame(eFrame));
		
		// comment
		build.append(getComment(eFrameset));
		
		return getTextAttr(eFrameset, build.toString(), true);
	}
	
	private String getRole(Element eRole)
	{
		return getAttr(eRole, "/");
	}
	
	private String getFrame(Element eFrame)
	{
		StringBuilder build = new StringBuilder();
		
		// comment
		build.append(getComment(eFrame));
		
		// mapping?
		for (Element eMapping : getChildElements(eFrame, ChLib.MAPPING))
			build.append(getMapping(eMapping));
		
		// example*
		for (Element eExample : getChildElements(eFrame, ChLib.EXAMPLE))
			build.append(getExample(eExample));
		
		return getText(eFrame.getTagName(), build.toString(), true);
	}
	
	private String getMapping(Element eMapping)
	{
		StringBuilder build = new StringBuilder();
		
		ArrayList<Element> list = getChildElements(eMapping, ChLib.MAPITEM);
		if (list.size() > 0)
		{
			// V
			build.append(getV());
			
			// mapitem*
			for (Element eMapitem : list)
				build.append(getMapitem(eMapitem));
		}
		
		// comment		
		build.append(getComment(eMapping));
		
		return getText(eMapping.getTagName(), build.toString(), true);
	}
	
	private String getV()
	{
		return "<V/>\n";
	}
	
	private String getMapitem(Element eMapitem)
	{
		return getAttr(eMapitem, "/");
	}
	
	private String getExample(Element eExample)
	{
		StringBuilder build = new StringBuilder();
		
		// parse
		Element eParse = getChildElements(eExample, ChLib.PARSE).get(0);
		String text = getParse(eParse);
		if (text.isEmpty())		return "";
		build.append(text);
		
		ArrayList<Element> list = getChildElements(eExample, ChLib.ARG+"|"+ChLib.V);
		if (list.size() == 0)	return "";
		
		// arg
		build.append(getArg(list.get(0)));
		
		// comment
		build.append(getComment(eExample));
		
		// (arg|V)*
		for (int i=1; i<list.size(); i++)
			build.append(getArg(list.get(i)));
		
		return getText(eExample.getTagName(), build.toString(), true);
	}
	
	private String getParse(Element eParse)
	{
		return getText(eParse.getTagName(), getTextContent(eParse), true);
	}
	
	private String getArg(Element eArg)
	{
		if (eArg.getTagName().equals(ChLib.V))
			return getV();
		else
			return getTextAttr(eArg, getTextContent(eArg), false);
	}
	
	private String getComment(Element parent)
	{
		ArrayList<Element> elements = getChildElements(parent, ChLib.COMMENT);
		if (elements.size() == 0)	return "";
		
		return getText(ChLib.COMMENT, getTextContent(elements.get(0)), true);
	}
	
	private ArrayList<Element> getChildElements(Element parent, String tagName)
	{
		NodeList list = parent.getChildNodes();
		ArrayList<Element> elements = new ArrayList<Element>();
		Element            element;
		
		for (int i=0; i<list.getLength(); i++)
		{
			if (list.item(i) instanceof Element)
			{
				element = (Element)list.item(i);
				
				if (element.getTagName().matches(tagName))
					elements.add(element);
			}
		}
		
		return elements;
	}
	
	private String getTextContent(Element element)
	{
		return getTextContent(element.getTextContent());
	}
	
	private String getTextContent(String text)
	{
		text = text.replaceAll("<", "&gt;");
		text = text.replaceAll(">", "&lt;");
		text = text.replaceAll("\"", "&quot;");
		text = text.replaceAll("&", "&amp;");
		
		return text;
	}
	
	private String getText(String tagName, String text, boolean isEnter)
	{
		if (text.equals(""))	return "";
		StringBuilder build = new StringBuilder();
		
		build.append("<");	build.append(tagName);	build.append(">");
		if (isEnter) build.append("\n");
		build.append(text.trim());
		if (isEnter) build.append("\n");
		build.append("</");	build.append(tagName);	build.append(">");
		build.append("\n");
		
		return build.toString();
	}
	
	private String getAttr(Element element, String end)
	{
		StringBuilder build = new StringBuilder();
		NamedNodeMap  list  = element.getAttributes();
		Attr           attr;
		String         name, value;
		
		build.append("<");	build.append(element.getTagName());
		
		for (int i=0; i<list.getLength(); i++)
		{
			attr = (Attr)list.item(i);
			
			build.append(" ");
			name = attr.getName();
			build.append(name);
			build.append("=\"");
			value = getTextContent(attr.getValue());
			if      (name.equals(ChLib.ARGNUM) && value.isEmpty())	value = "9";
			else if (name.equals(ChLib.ARGROLE) && value.isEmpty())	value = "DUMMY"; 
			build.append(value);
			build.append("\"");
		}
	
		build.append(end);
		build.append(">\n");
		
		return build.toString();
	}
	
	private String getTextAttr(Element element, String text, boolean isEnter)
	{
		StringBuilder build = new StringBuilder();

		build.append(getAttr(element, "").trim());
		if (isEnter) build.append("\n");
		build.append(text.trim());
		if (isEnter) build.append("\n");
		build.append("</");	build.append(element.getTagName());	build.append(">");
		build.append("\n");
		
		return build.toString();
	}
	
	static public void main(String[] args)
	{
		FixAr fixar = new FixAr();
		fixar.fix(args[0], args[1]);
	}
}
