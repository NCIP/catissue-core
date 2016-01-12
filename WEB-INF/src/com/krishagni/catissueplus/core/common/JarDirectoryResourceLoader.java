package com.krishagni.catissueplus.core.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.runtime.resource.loader.JarResourceLoader;


public class JarDirectoryResourceLoader extends JarResourceLoader {

	public void init(ExtendedProperties configuration) {
		Vector paths = configuration.getVector("path");
		if (paths == null || paths.size() == 0) {
			return;
		}

		List<String> jarFileNames = new ArrayList<String>();
		for (Object path : paths) {
			jarFileNames.addAll(getJarFileNames((String)path));
		}

		if (jarFileNames.isEmpty()) {
			return;
		}

		configuration.setProperty("path", String.join(",", jarFileNames));
		super.init(configuration);
	}

	private List<String> getJarFileNames(String dirPath) {
		List<String> jarFileNames = new ArrayList<String>();
		File jarDirFile = new File(dirPath);
		for (File file : jarDirFile.listFiles()) {
			if (file.isDirectory() || !file.getName().endsWith(".jar")) {
				continue;
			}

			JarFile jarFile = null;
			try {
				jarFile = new JarFile(file);
				Attributes attrs = jarFile.getManifest().getMainAttributes();
				String pluginName = attrs.getValue("os-plugin-name");
				if (StringUtils.isNotBlank(pluginName)) {
					jarFileNames.add("jar:file:" +  jarFile.getName());
				}
			} catch (Exception e) {
				rsvc.error("Error loading template resources from: " + e.getStackTrace());
			} finally {
				IOUtils.closeQuietly(jarFile);
			}
		}

		return jarFileNames;
	}
}
