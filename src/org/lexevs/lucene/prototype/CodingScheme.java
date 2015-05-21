package org.lexevs.lucene.prototype;

public enum CodingScheme {
	
SCHEME1("AScheme", "1.0", "this.is.a.scheme", 3000 ), 
THESSCHEME("ThesScheme", "2014_12_1", "some.scheme.this.is", 160747),
SCHEME3("NiceCodingScheme", "v34.1", "nice.coding.scheme", 300488),
METASCHEME("NCIMetaCodingScheme", "2014", "big.coding.scheme", 2418224),
SCHEME5("BiggestCodingScheme", "2015_12_56", "biggest.one.yet", 3600000), 
SCHEME6("YetAnotherScheme", "20.3", "yet.another.coding.sheme", 8000),
SCHEME7("TheScheme", "1.0", "this.is.the.scheme", 3000 ), 
SCHEME8("TestScheme", "2014_12_1", "test.scheme.this.is", 200000),
SCHEME9("OkCodingScheme", "v34.1", "ok.coding.scheme", 300000),
SCHEME10("LargeCodingScheme", "v12.05d", "large.coding.scheme", 1000000),
SCHEME11("LargerCodingScheme", "2015_12_56", "larger.coding.scheme", 2000000), 
SCHEME12("AnotherScheme", "20.3", "another.coding.sheme", 80000);

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
