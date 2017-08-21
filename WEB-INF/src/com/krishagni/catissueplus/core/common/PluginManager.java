package com.krishagni.catissueplus.core.common;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PluginManager {
	private static final PluginManager instance = new PluginManager();
	
	private Set<String> pluginNames = new LinkedHashSet<String>();

	public static PluginManager getInstance() {
		return instance;
	}
	
	public List<String> getPluginNames() {
		return new ArrayList<String>(pluginNames);
	}
	
	public boolean addPluginName(String name) {
		return pluginNames.add(name);
	}
}
