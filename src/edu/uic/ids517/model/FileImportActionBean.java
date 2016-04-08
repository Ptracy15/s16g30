package edu.uic.ids517.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.fileupload.UploadedFile;

public class FileImportActionBean {
	private UploadedFile uploadedFile;
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getFileLabel() {
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}

	public boolean isDispExcel() {
		return dispExcel;
	}

	public void setDispExcel(boolean dispExcel) {
		this.dispExcel = dispExcel;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public List<TableMetaData> getTableMetaData() {
		return tableMetaData;
	}

	public void setTableMetaData(List<TableMetaData> tableMetaData) {
		this.tableMetaData = tableMetaData;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getUploadedFileContents() {
		return uploadedFileContents;
	}

	public void setUploadedFileContents(String uploadedFileContents) {
		this.uploadedFileContents = uploadedFileContents;
	}

	public InputStream getUploadedInputStream() {
		return uploadedInputStream;
	}

	public void setUploadedInputStream(InputStream uploadedInputStream) {
		this.uploadedInputStream = uploadedInputStream;
	}

	public List<String> getHeaderList() {
		return headerList;
	}

	public void setHeaderList(List<String> headerList) {
		this.headerList = headerList;
	}

	public List<String> getDataTypeList() {
		return dataTypeList;
	}

	public void setDataTypeList(List<String> dataTypeList) {
		this.dataTypeList = dataTypeList;
	}

	public OutputStream getOutput() {
		return output;
	}

	public void setOutput(OutputStream output) {
		this.output = output;
	}

	public File getTempFile() {
		return tempFile;
	}

	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	private String fileLabel="";
	public boolean dispExcel = false;
	public String outcome;
	public List<TableMetaData> tableMetaData;
	private String fileName;
	private long fileSize;
	private String fileContentType;
	private String uploadedFileContents;
	private InputStream uploadedInputStream;
	private List<String> headerList;
	private List<String> dataTypeList;
	private OutputStream output;
	private File tempFile = null;
	private String errorMessage;
	private String userMessage="";
	/**
	 * 
	 */
	public FileImportActionBean() {
		// TODO Auto-generated constructor stub
	}
	
	public String displayDataTypes() {
		//uploadedFileContents = null;
		try {
			fileName = uploadedFile.getName();	
			//System.out.println(fileName);
			fileSize = uploadedFile.getSize();
			
			//System.out.println(fileSize);
			fileContentType = uploadedFile.getContentType();
			
			//System.out.println("FIle COntent Type : " + fileContentType);
			uploadedFileContents = new String(uploadedFile.getBytes());
			uploadedInputStream = uploadedFile.getInputStream();
			}
		catch (IOException e) {
			e.printStackTrace();
		}
		tableMetaData = new ArrayList<TableMetaData>();
		tableMetaData = ReadData.readCsv(uploadedInputStream);
		setDispExcel(true);
		FacesMessage msg = new FacesMessage(fileName
				+ " is read. Please verify the datatypes");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		if(processFileUpload().matches("SUCCESS"))
		return "SUCCESS";
		else
			return "FAIL";
	}

	public String processFileUpload() {
		int rows = 0;
		DataTableBean table = new DataTableBean(fileLabel);
		userMessage = "";
		try {
			if (uploadedInputStream == null)
			System.out.println("Input stream null");
			else
			uploadedInputStream = uploadedFile.getInputStream();
			table.createTable(tableMetaData);
			rows = table.insertCsvData(uploadedInputStream, tableMetaData);
		} // end try
		catch (IOException e) {
			e.printStackTrace();
			userMessage = "File Upload failed. Kindly verify the data and upload the file again.";
			FacesMessage msg = new FacesMessage(
					"File Upload failed. Kindly verify the data and upload the file again.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "FAIL";
		}

		if (rows == 0) {
			userMessage = "File Upload failed. Kindly verify the data and upload the file again.";
			FacesMessage msg = new FacesMessage(
					"File Upload failed. Kindly verify the data and upload the file again.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "FAIL";
		} else {
			userMessage =  "File uploaded successfully for ticker symbol " +fileLabel+". Rows: "+rows;
			FacesMessage msg = new FacesMessage("Table " + fileLabel
					+ " is created with " + rows + " rows");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "SUCCESS";
		}

	}

}
