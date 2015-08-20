package com.krishagni.catissueplus.core.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.VfsResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class WildcardReloadableResourceBundle extends ReloadableResourceBundleMessageSource {
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	@Override
	public void setBasenames(String... inputList) {
		if (inputList == null) {
			return;
		}

		List<String> basenames = new ArrayList<String>();
		for (String name : inputList) {
			if (StringUtils.isBlank(name)) {
				continue;
			}

			try {
				name = name.trim();
				Resource[] resources = resourcePatternResolver.getResources(name);
				
				for (Resource resource : resources) {
					String uri = resource.getURI().toString();
					String basename = null;
					if (resource instanceof FileSystemResource || resource instanceof VfsResource) {
						basename = "classpath:"	+ StringUtils.substringBetween(uri, "/classes/", ".properties");
					} else if (resource instanceof ClassPathResource || resource instanceof UrlResource) {
						basename = StringUtils.substringBefore(uri, ".properties");
					} 

					if (basename != null) {
						String fullName = processBasename(basename);
						basenames.add(fullName);
					}
				}
			} catch (IOException e) {
				logger.debug("No message source files found for basename " + name + ".");
			}
		}

		super.setBasenames(basenames.toArray(new String[0]));
	}

	private String processBasename(String basename) {
		String prefix = StringUtils.substringBeforeLast(basename, "/");
		String name = StringUtils.substringAfterLast(basename, "/");
		do {
			name = StringUtils.substringBeforeLast(name, "_");
		} while (name.contains("_"));

		return prefix + "/" + name;
	}
}