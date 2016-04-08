package edu.uic.ids517.model;


public class TableColumnBean {

	static String newline = "\r\n";

	private long length = 0;
	private String name = null;
	private double numericPrecision = 0;
	private final String SPACE = "     ";
	private int type = 0;

	public String getColumnDdlForInsert() {

		return SPACE + getName() + "," + newline;
	}

	public String getColumnLoaderLine() {

		return SPACE + getName() + "," + newline;
	}

	public long getLength() {
		return length;
	}

	public String getName() {
		// replace any characters that are not permissible
		if (name != null)
			name = name.replaceAll(" ", "_").replaceAll("-", "_")
					.replaceAll("\\.", "_");
		return name;
	}

	public double getNumericPrecision() {
		return numericPrecision;
	}

	public int getType() {
		return type;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumericPrecision(double numericPrecision) {
		this.numericPrecision = numericPrecision;
	}

	public void setType(int type) {
		this.type = type;
	}
}
