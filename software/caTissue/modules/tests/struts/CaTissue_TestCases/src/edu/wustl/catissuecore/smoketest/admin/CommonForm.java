package edu.wustl.catissuecore.smoketest.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.fileupload.FileItem;
import org.apache.struts.upload.FormFile;

public class CommonForm implements FormFile
{

	/**
	 * @param args
	 */

	FileItem fileItem;
	String hudsonPath;

	public CommonForm(FileItem diskFileItem,String filePath)
	{
			this.fileItem = diskFileItem;
			this.hudsonPath = new String(filePath);
	}

	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
			return "SPPTest.xml";
	}

	@Override
	public InputStream getInputStream() throws FileNotFoundException,
			IOException {
		// TODO Auto-generated method stub

		FileInputStream inputStream = new FileInputStream(new File(hudsonPath).getCanonicalPath());
		return inputStream;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getFileData() throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFileSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setContentType(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFileName(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFileSize(int arg0) {
		// TODO Auto-generated method stub

	}
}
