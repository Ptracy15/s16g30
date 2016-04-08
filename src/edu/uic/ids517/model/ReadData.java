package edu.uic.ids517.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class ReadData {

	public static List<TableMetaData> getMetaDataTable(List<String> headerList,
			List<String> dataTypeList, List<String> databaseColumnTypeList) {
		List<TableMetaData> metaDataTable = new ArrayList<TableMetaData>();

		if (headerList.size() == dataTypeList.size()) {

			for (int i = 0; i < headerList.size(); i++) {
				TableMetaData metaDataRow = new TableMetaData();
				metaDataRow.setColumnName(headerList.get(i));

				// metaDataRow.setColumnType(dataTypeList.get(i));
				// newdone
				metaDataRow.setColumnType(databaseColumnTypeList.get(i));
				metaDataRow
						.setDatabaseColumnType(databaseColumnTypeList.get(i));
				metaDataTable.add(metaDataRow);
			}

		} else {
			// display error message
		}
		return metaDataTable;

	}

	public static List<TableMetaData> readCsv(InputStream is) {

		InputStreamReader r = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(r);
		List<String> headerList = new ArrayList<String>();
		List<String> dataTypeList = new ArrayList<String>();
		List<String> databaseColumnTypeList = new ArrayList<String>();

		int row = 0;
		String line = "";
		try {
			boolean isHeader = true;
			int doublecheck = 0;
			outerloop: while ((line = reader.readLine()) != null) {

				StringTokenizer tokenizer = new StringTokenizer(line, ",");
				if (isHeader) {
					while (tokenizer.hasMoreTokens()) {
						headerList.add(tokenizer.nextToken());
					}
					isHeader = false;
				} else {
					databaseColumnTypeList.clear();
					dataTypeList.clear();
					while (tokenizer.hasMoreTokens()) {

						Object obj = interpret(tokenizer.nextToken());
						dataTypeList.add(obj.getClass().getSimpleName());
						System.out.println(obj.getClass().getSimpleName());
						if (obj.getClass().getSimpleName()
								.equalsIgnoreCase("String"))
							databaseColumnTypeList.add("VARCHAR(255)");
						else if (obj.getClass().getSimpleName()
								.equalsIgnoreCase("Integer"))
							databaseColumnTypeList.add("INTEGER");
						else if (obj.getClass().getSimpleName()
								.equalsIgnoreCase("Boolean"))
							databaseColumnTypeList.add("BOOLEAN");
						else if (obj.getClass().getSimpleName()
								.equalsIgnoreCase("Float"))
							databaseColumnTypeList.add("FLOAT");
						else if (obj.getClass().getSimpleName()
								.equalsIgnoreCase("Double")) {
							databaseColumnTypeList.add("DOUBLE");
							doublecheck = 1;
						}
					}
					if (doublecheck == 1) {
						break outerloop;
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		List<TableMetaData> metaDataTable = getMetaDataTable(headerList,
				dataTypeList, databaseColumnTypeList);
		return metaDataTable;
	}

	public static Object interpret(String s) {
		Scanner sc = new Scanner(s);
		return sc.hasNextInt() ? sc.nextInt() : sc.hasNextLong() ? sc
				.nextLong() : sc.hasNextBoolean() ? sc.nextBoolean() : sc
				.hasNextDouble() ? sc.nextDouble() : sc.hasNext() ? sc.next()
				: s;
	}

	public String writeXMLStringTo(String string, String fileName) {

		// String xmlString =
		// "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"></soap:Envelope>";
		String status = "FAIL";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();

			// Use String reader
			Document document = builder.parse(new InputSource(new StringReader(
					string)));

			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();
			Source src = new DOMSource(document);
			StreamResult result = new StreamResult(System.out);
			aTransformer.transform(src, result);
			JFileChooser fileChooser = new JFileChooser();

			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				Result dest = new StreamResult(file);
				aTransformer.transform(src, dest);
			}
			status = "SUCCESS";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

	public String writeCSVStringTo(String string, String fileName) {

		String status = "FAIL";
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();

			ec.responseReset(); // Some JSF component library or some Filter
								// might have set some headers in the buffer
								// beforehand. We want to get rid of them, else
								// it may collide.
			ec.setResponseContentType("csv"); // Check
												// http://www.iana.org/assignments/media-types
												// for all types. Use if
												// necessary
												// ExternalContext#getMimeType()
												// for auto-detection based on
												// filename.
			ec.setResponseContentLength(string.length()); // Set it with the
															// file size. This
															// header is
															// optional. It will
															// work if it's
															// omitted, but the
															// download progress
															// will be unknown.
			ec.setResponseHeader("Content-Disposition",
					"attachment; filename=\"" + fileName + "\""); // The Save As
																	// popup
																	// magic is
																	// done
																	// here. You
																	// can give
																	// it any
																	// file name
																	// you want,
																	// this only
																	// won't
																	// work in
																	// MSIE, it
																	// will use
																	// current
																	// request
																	// URL as
																	// file name
																	// instead.

			OutputStream output = ec.getResponseOutputStream();
			byte[] out = string.getBytes();
			output.write(out);
			output.flush();
			output.close();
			fc.responseComplete();
			/*
			 * StringWriter writer = new StringWriter(); PrintWriter pw = new
			 * PrintWriter(writer);
			 * 
			 * // Fill the stack trace into the write fillStackTrace(ex, pw);
			 * 
			 * return writer.toString();
			 */
			/*
			 * JFileChooser fileChooser = new JFileChooser();
			 * 
			 * if (fileChooser.showSaveDialog(this) ==
			 * JFileChooser.APPROVE_OPTION) { File file =
			 * fileChooser.getSelectedFile(); FileWriter fw = new
			 * FileWriter(file); fw.write(string); fw.flush(); fw.close();
			 * 
			 * }
			 */
			status = "SUCCESS";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

}
