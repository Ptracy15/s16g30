package edu.uic.ids517.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.fileupload.UploadedFile;

@ManagedBean(name = "fileUploadActionBean")
@SessionScoped
public class FileUploadActionBean {

	@ManagedProperty("#{uploadedFile}")
	private UploadedFile uploadedFile;
	@ManagedProperty("#{fileLabel}")
	private String fileLabel;

	public boolean dispExcel = false;
	public String outcome;
	public List<TableMetaData> tableMetaData;
	private List<String> dataTypesDisplayList;

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

	public List<String> getDataTypesDisplayList() {
		return dataTypesDisplayList;
	}

	public void setDataTypesDisplayList(List<String> dataTypesDisplayList) {
		this.dataTypesDisplayList = dataTypesDisplayList;
	}

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

	FacesContext context = FacesContext.getCurrentInstance();
	Map<String, Object> m = context.getExternalContext().getSessionMap();

	String fileName;
	long fileSize;
	String fileContentType;
	String uploadedFileContents;

	InputStream uploadedInputStream;
	List<String> headerList;
	List<String> dataTypeList;
	OutputStream output;
	File tempFile = null;

	public String displayDataTypes() {
		uploadedFileContents = null;
		try {
			if (uploadedFile != null) {
				fileName = uploadedFile.getName();
				fileSize = uploadedFile.getSize();
				fileContentType = uploadedFile.getContentType();
				System.out.println("FIle COntent Type : " + fileContentType);
				uploadedFileContents = new String(uploadedFile.getBytes());
				uploadedInputStream = uploadedFile.getInputStream();

				// uploadedFile.
			} else {
				// FacesMessage msg = new
				// FacesMessage("Choose a file to import!");
				/*
				 * Iterator<FacesMessage> it =
				 * FacesContext.getCurrentInstance().getMessages();
				 * 
				 * while(it.hasNext()) { it.next(); it.remove(); }
				 */
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Choose a file to import!"));
			}
		} // end try
		catch (IOException e) {
			e.printStackTrace();
		}

		tableMetaData = new ArrayList<TableMetaData>();
		tableMetaData = ReadData.readCsv(uploadedInputStream);

		dataTypesDisplayList = new ArrayList<String>();
		// dataTypesDisplayList.add("VARCHAR");
		// dataTypesDisplayList.add("INTEGER");
		dataTypesDisplayList.add("DOUBLE");
		// dataTypesDisplayList.add("BOOLEAN");
		// dataTypesDisplayList.add("CHARACTER");
		// dataTypesDisplayList.add("FLOAT");
		// // newdone
		// Collections.shuffle(dataTypesDisplayList);
		setDispExcel(true);

		FacesMessage msg = new FacesMessage(fileName
				+ " is read. Please verify the datatypes");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "DISPLAYEXCEL";

	}

	public String processFileUpload() {
		int rows = 0;
		DataTableBean table = new DataTableBean(fileLabel);
		try {
			uploadedInputStream = uploadedFile.getInputStream();
			table.createTable(tableMetaData);
			rows = table.insertCsvData(uploadedInputStream, tableMetaData);
		} // end try
		catch (IOException e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage(
					"File Upload failed. Kindly verify the data and upload the file again.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "FAIL";
		}

		if (rows == 0) {
			FacesMessage msg = new FacesMessage(
					"File Upload failed. Kindly verify the data and upload the file again.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "FAIL";
		} else {
			FacesMessage msg = new FacesMessage(rows
					+ "Have been  succesfully  uploaded ");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "SUCCESS";
		}

	}

}