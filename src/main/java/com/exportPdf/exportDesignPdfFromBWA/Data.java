package com.exportPdf.exportDesignPdfFromBWA;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Data implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object overview;
	private List<Process> processes;

	public Object getOverview() {
		return overview;
	}

	public void setOverview(Object overview) {
		this.overview = overview;
	}

	public List<Process> getProcesses() {
		return processes;
	}

	public void setProcesses(List<Process> processes) {
		this.processes = processes;
	}
}
