/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twinkle.framework.configure.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple plain text serializable encapsulation of a list of property sources. Basically a
 * DTO for {@link org.springframework.core.env.Environment}, but also applicable outside
 * the domain of a Spring application.
 *
 * @author Dave Syer
 * @author Spencer Gibb
 *
 */
public class ConfigurationEnvironment {

	private String name;

	private String[] profiles = new String[0];

	private String label;

	private List<ConfigurationPropertySource> configurationPropertySources = new ArrayList<>();

	private String version;

	private String state;

	public ConfigurationEnvironment(String name, String... profiles) {
		this(name, profiles, "master", null, null);
	}

	/**
	 * Copies all fields except configurationPropertySources.
	 * @param env Spring ConfigurationEnvironment
	 */
	public ConfigurationEnvironment(ConfigurationEnvironment env) {
		this(env.getName(), env.getProfiles(), env.getLabel(), env.getVersion(),
				env.getState());
	}

	@JsonCreator
	public ConfigurationEnvironment(@JsonProperty("name") String name,
									@JsonProperty("profiles") String[] profiles,
									@JsonProperty("label") String label, @JsonProperty("version") String version,
									@JsonProperty("state") String state) {
		super();
		this.name = name;
		this.profiles = profiles;
		this.label = label;
		this.version = version;
		this.state = state;
	}

	public void add(ConfigurationPropertySource configurationPropertySource) {
		this.configurationPropertySources.add(configurationPropertySource);
	}

	public void addAll(List<ConfigurationPropertySource> configurationPropertySources) {
		this.configurationPropertySources.addAll(configurationPropertySources);
	}

	public void addFirst(ConfigurationPropertySource configurationPropertySource) {
		this.configurationPropertySources.add(0, configurationPropertySource);
	}

	public List<ConfigurationPropertySource> getConfigurationPropertySources() {
		return this.configurationPropertySources;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String[] getProfiles() {
		return this.profiles;
	}

	public void setProfiles(String[] profiles) {
		this.profiles = profiles;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "ConfigurationEnvironment [name=" + this.name + ", profiles="
				+ Arrays.asList(this.profiles) + ", label=" + this.label
				+ ", configurationPropertySources=" + this.configurationPropertySources + ", version="
				+ this.version + ", state=" + this.state + "]";
	}

}
