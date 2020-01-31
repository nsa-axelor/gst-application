package com.axelor.apps.gst.global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class Constants {
	
	public File getConfigFile() throws IOException {
		File configFile = File.createTempFile("csv-config", ".xml");
		InputStream inputStream = this.getClass().getResourceAsStream("/data-init/input-config.xml");
		FileOutputStream fileOutputStream = new FileOutputStream(configFile);
		IOUtils.copy(inputStream, fileOutputStream);
		return configFile;
	}

	public String getDataCsvFile() {
		URL url = this.getClass().getResource("/data-init/input/");
		return url.getPath();
	}
}
