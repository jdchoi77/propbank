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
package jubilee.awt;

import java.awt.*;

/**
 * 'JDCFileDialog' creates a file-dialog that can act as either 'LOAD' or 'SAVE'.
 * <pre>
 * 	static public void main(String[] args)
	{
		Frame f = new Frame();
		JDCFileDialog fd = new JDCFileDialog(f);
		
		System.out.println(fd.loadFile("Open file"));
		System.out.println(fd.loadDir("Open directory"));
		System.out.println(fd.save());
	}
 * </pre>
 * <b>Last update:</b> 09/11/2007
 * @author Jinho D. Choi
 */
public class JDCFileDialog
{
	private Frame parent;
	
	/**
	 * Creates a file-dialog called by 'parent'.
	 * @param parent frame that calls this file-dialog
	 */
	public JDCFileDialog(Frame parent)
	{
		this.parent = parent;
	}
	
	/**
	 * Opens 'load' dialog, and returns the path of the selected file.
	 * @param title title of this dialog
	 * @return (isSelected) ? path of the selected file : null
	 */
	public String loadFile(String title)
	{
		System.setProperty("apple.awt.fileDialogForDirectories", "false");
		return showDialog(title, FileDialog.LOAD);
	}
	
	/**
	 * Opens 'load' dialog, and returns the path of the selected directory.
	 * @param title title of this dialog
	 * @return (isSelected) ? path of the selected file : null
	 */
	public String loadDir(String title)
	{
		System.setProperty("apple.awt.fileDialogForDirectories", "true");
		return showDialog(title, FileDialog.LOAD);
	}
	
	/**
	 * Opens 'save' dialog, and returns the path of the selected file.
	 * @return (isSelected) ? path of the selected file : null.
	 */
	public String save()
	{
		return showDialog("Save File", FileDialog.SAVE);
	}
	
	/**
	 * Prompts a dialog and returns the selected path.
	 * @param title title of this dialog
	 * @param mode FileDialog.LOAD | FileDialog.SAVE
	 * @return (isSelected) ? selected path : null
	 */
	private String showDialog(String title, int mode)
	{
		FileDialog fd = new FileDialog(parent, title, mode);
		fd.setVisible(true);
		
		return (fd.getFile() == null) ? null : fd.getDirectory()+fd.getFile();
	}
}
