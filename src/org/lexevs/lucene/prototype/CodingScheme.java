package org.lexevs.lucene.prototype;

public enum CodingScheme {
	
SCHEME1("AScheme", "1.0", "this.is.a.scheme", 3000 ), 
//THESSCHEME("ThesScheme", "2014_12_1", "some.scheme.this.is", 50);
THESSCHEME("ThesScheme", "2014_12_1", "some.scheme.this.is", 160747),
SNOMEDSCHEME("SNOMEDScheme", "v34.1", "nice.coding.scheme", 300488),
METASCHEME("NCIMetaCodingScheme", "2014", "big.coding.scheme", 2418224),
SCHEME6("YetAnotherScheme", "20.3", "yet.another.coding.sheme", 8000),
SCHEME7("TheScheme", "1.0", "this.is.the.scheme", 3000 ), 
SCHEME8("TestScheme", "2014_12_1", "test.scheme.this.is", 20000),
SCHEME9("OkCodingScheme", "v34.1", "ok.coding.scheme", 300000), 
SCHEME10("AnotherScheme", "20.3", "another.coding.sheme", 80000);

public String codingSchemeName;
public String version;

public String URI;
public int numberOfEntities;
	CodingScheme(String codingSchemeName, String version, String URI, int numberOfEntities){
	this.codingSchemeName = codingSchemeName;
	this.version = version;
	this.URI = URI;
	this.numberOfEntities = numberOfEntities;
}
	
	public String getCodingSchemeName() {
		return codingSchemeName;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getURI() {
		return URI;
	}
	
	public int getNumberOfEntities() {
		return numberOfEntities;
	}
	
}
