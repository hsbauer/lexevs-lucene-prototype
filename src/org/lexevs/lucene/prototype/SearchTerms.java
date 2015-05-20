package org.lexevs.lucene.prototype;

public enum SearchTerms {
	
	BLOOD("Blood"), 
	MUD("mud"),
	ARTICLE("The"),
	CHAR("a"),
	PHRASE1("Lung Cancer"), 
	PHRASE2("liver carcinoma");

	String term;
	SearchTerms(String term){
		this.term = term;
	}
	
	public String getTerm(){
		return term;
	}
}
