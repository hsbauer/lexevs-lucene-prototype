package org.lexevs.lucene.prototype;

public enum SearchTerms {
	
	BLOOD("Blood"), 
	MUD("mud"),
	ARTICLE("The"),
	CHAR("a"),
	LUNG_CANCER("Lung Cancer"), 
	LIVER_CARCINOMA("liver carcinoma"),
	CODE1("C1243"),
	CODE2("10024003"),
	CODE3("8.61");

	String term;
	SearchTerms(String term){
		this.term = term;
	}
	
	public String getTerm(){
		return term;
	}
}
