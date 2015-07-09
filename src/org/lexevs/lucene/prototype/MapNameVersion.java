package org.lexevs.lucene.prototype;

public class MapNameVersion {
public MapNameVersion(String codingSchemeName, String version) {
	this.name = codingSchemeName;
	this.version = version;
	}

public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}

private String name;
private String version;


}
