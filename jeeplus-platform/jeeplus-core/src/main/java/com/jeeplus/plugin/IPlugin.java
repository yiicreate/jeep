package com.jeeplus.plugin;


import org.springframework.boot.ApplicationRunner;

public interface IPlugin extends ApplicationRunner {


	void init();
	String getName();

	String getVersion();

	String getIcon();

	String getSite();

	String getDescription();

	void setName(String name);

	void setVersion(String version);

	void setIcon(String icon);

	void setSite(String site);

	void setDescription(String description);
}
