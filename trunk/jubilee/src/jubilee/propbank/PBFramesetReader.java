/**
* Copyright (c) 2007-2009, Regents of the University of Colorado
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/
package jubilee.propbank;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * @since 09/13/07
 */
public class PBFramesetReader
{
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private String path, language;
	
	/**
	 * Opens a frameset file.
	 * @param framesetPath the name of the frameset.
	 */
	public PBFramesetReader()
	{
		factory = DocumentBuilderFactory.newInstance();
			
		try
		{
			builder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e){System.err.println(e);}
	}
	
	public void setProperties(String language, String framesetPath)
	{
		this.language = language;
		path = framesetPath;
	}
	
	public PBFrameset getFrameset(String lemma)
	{
		File file = new File(path+"/"+lemma+".xml");
		
		if (!file.exists())
			return null;
		if (language.equalsIgnoreCase("english"))		// english frameset
			return getFramesetEng(lemma);
		else
			return getFramesetOther(lemma);
	}
	
	public PBFrameset getFramesetEng(String lemma)
	{
		PBFrameset frameset = new PBFrameset();
		
		try
		{
			Document doc = builder.parse(new File(path+"/"+lemma+".xml"));
			
			NodeList ndPredicate = doc.getElementsByTagName("predicate");
			for (int i=0; i<ndPredicate.getLength(); i++)
			{
					Element emPredicate = (Element)ndPredicate.item(i);
					PBPredicate predicate = new PBPredicate();
					predicate.setLemma(emPredicate.getAttribute("lemma").trim());
					
					NodeList ndRoleset = emPredicate.getElementsByTagName("roleset");
					for (int j=0; j<ndRoleset.getLength(); j++)
					{
						Element emRoleset = (Element)ndRoleset.item(j);
						PBRoleset roleset = new PBRoleset();
						roleset.setId(emRoleset.getAttribute("id"));
						roleset.setName(emRoleset.getAttribute("name"));
						
						NodeList ndRole = emRoleset.getElementsByTagName("role");
						for (int k=0; k<ndRole.getLength(); k++)
						{
							Element emRole = (Element)ndRole.item(k);
							PBRole role = new PBRole();
							role.setN(emRole.getAttribute("n"));
							role.setDescr(emRole.getAttribute("descr"));
							roleset.addRole(role);
						}
						
						NodeList ndExample = emRoleset.getElementsByTagName("example");
						for (int k=0; k<ndExample.getLength(); k++)
						{
							Element emExample = (Element)ndExample.item(k);
							PBExample example = new PBExample();
							String name = emExample.getAttribute("name");
							
							NodeList ndText = emExample.getElementsByTagName("text");
							if (ndText.getLength() > 0)
								example.setText(ndText.item(0).getTextContent().trim());
							
							NodeList ndArg = emExample.getElementsByTagName("arg");
							for (int l=0; l<ndArg.getLength(); l++)
							{
								Element emArg = (Element)ndArg.item(l);
								PBRole role = new PBRole();
								role.setN(emArg.getAttribute("n"));
								role.setF(emArg.getAttribute("f"));
								role.setDescr(emArg.getTextContent().trim());
								example.addArg(role);
							}
							
							NodeList ndRel = emExample.getElementsByTagName("rel");
							if (ndRel.getLength() > 0)
								example.setRel(ndRel.item(0).getTextContent().trim() + "(" + name + ")");
					
							roleset.addExample(example);
						}
						
						predicate.addRoleset(roleset);
					}
					
					frameset.addPredicate(predicate);
				}
		}
		catch (Exception e){System.err.println(e);}
		
		return frameset;
	}
	
	public PBFrameset getFramesetOther(String lemma)
	{
		PBFrameset frameset = new PBFrameset();
		
		try
		{
			Document doc = builder.parse(new File(path+"/"+lemma+".xml"));
			
			NodeList ndId = doc.getElementsByTagName("id");
			PBPredicate predicate = new PBPredicate();
			predicate.setLemma(ndId.item(0).getTextContent().trim());
			
			NodeList ndRoleset = doc.getElementsByTagName("frameset");
			for (int i=0; i<ndRoleset.getLength(); i++)
			{
				Element emRoleset = (Element)ndRoleset.item(i);
				PBRoleset roleset = new PBRoleset();
				roleset.setId(lemma+"."+emRoleset.getAttribute("id"));
				roleset.setName(emRoleset.getAttribute("edef"));
				
				NodeList ndRole = emRoleset.getElementsByTagName("role");
				for (int j=0; j<ndRole.getLength(); j++)
				{
					Element emRole = (Element)ndRole.item(j);
					PBRole role = new PBRole();
					role.setN(emRole.getAttribute("argnum"));
					role.setDescr(emRole.getAttribute("argrole"));
					roleset.addRole(role);
				}
				
				NodeList ndExample = emRoleset.getElementsByTagName("example");
				for (int k=0; k<ndExample.getLength(); k++)
				{
					Element emExample = (Element)ndExample.item(k);
					PBExample example = new PBExample();
					
					NodeList ndParse = emExample.getElementsByTagName("parse");
					if (ndParse.getLength() > 0)
						example.setText(ndParse.item(0).getTextContent().trim());
					
					NodeList ndArg = emExample.getElementsByTagName("arg");
					for (int l=0; l<ndArg.getLength(); l++)
					{
						Element emArg = (Element)ndArg.item(l);
						PBRole role = new PBRole();
						role.setN(emArg.getAttribute("n"));
						role.setF(emArg.getAttribute("f"));
						role.setDescr(emArg.getTextContent().trim());
						example.addArg(role);
					}
					
					roleset.addExample(example);
				}
				
				predicate.addRoleset(roleset);
			}
					
			frameset.addPredicate(predicate);
		}
		catch (Exception e){System.err.println(e);}
		
		return frameset;
	}
	
/*	static public void main(String[] args)
	{
		PBFramesetReader reader = new PBFramesetReader();
		PBFrameset frameset = reader.getFrameset("take");
		for (int i=0; i<frameset.size(); i++)
			System.out.println(frameset.get(i)+"\n");
	}*/
}
