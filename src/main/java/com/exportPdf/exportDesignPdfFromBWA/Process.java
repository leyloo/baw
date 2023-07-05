package com.exportPdf.exportDesignPdfFromBWA;

public class Process {
	private String piid;
	private String name;
	private String bpdName;
	private String snapshotID;
	private String dueDate;

	public String getPiid() {
		return piid;
	}

	public void setPiid(String piid) {
		this.piid = piid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBpdName() {
		return bpdName;
	}

	public void setBpdName(String bpdName) {
		this.bpdName = bpdName;
	}

	public String getSnapshotID() {
		return snapshotID;
	}

	public void setSnapshotID(String snapshotID) {
		this.snapshotID = snapshotID;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getExecutionState() {
		return executionState;
	}

	public void setExecutionState(String executionState) {
		this.executionState = executionState;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(String lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	private String executionState;
	private String creationDate;
	private String lastModificationTime;
}
