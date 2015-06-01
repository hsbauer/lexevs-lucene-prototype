package org.lexevs.lucene.prototype;

public class ChildDocObject extends DocObject {


		public String degreeOfFidelity;
		public String dm_propertyValue;
		public String ecode;
		public String isPreferred;
		public String language;
		public String literal_propertyValue;
		public String literal_reverse_propertyValue;
		public String matchifNoContext;
		public String namespace;
		public String propertyId;
		public String propertyName;
		public String propertyType;
		public String propertyValue;
		public String qualifiers;
		public String representationalForm;
		public String reverse_propertyValue;
		public String sources;
		public String stem_PropertyValue;
		public String untokenizedLCPropertyValue;
				
		public String toString(){
			return  
					//"UID: " + UID  + "\n" +
//			"codingSchemeName: " + codingSchemeName  + "\n" +
//			"codingSchemeVersion: " + codingSchemeVersion  + "\n" +
//			"parentDoc: " +	parentDoc + "\n" +
//			"codingSchemeId: " + codingSchemeId + "\n" +
//			"conceptStatus: " + conceptStatus +  "\n" +
//			"csUriVersionKey: " + csUriVersionKey + "\n" +
//			"degreeOfFidelity: " + degreeOfFidelity + "\n" +
//			"dm_propertyValue: " + dm_propertyValue + "\n" +
//			"entityCode: " + entityCode + "\n" +
//			"entityCodeLC: " + entityCodeLC + "\n" +
//			"entityCodeNamepace: " +  entityCodeNamepace + "\n" +
//			"entityDescription: " +  entityDescription  + "\n" +
//			"entityType: " + entityType + "\n" +
//			"entityUID: " + entityUID + "\n" +
//			"format: " + format + "\n" + 
//			"isActive: " + isActive + "\n" +
//			"isAnonymous: " + isAnonymous + "\n" +
			"isPreferred: " + isPreferred + "\n" +
			"language: " + language + "\n" +
			"literal_propertyValue: " + literal_propertyValue + "\n" +
			"literal_reverse_propertyValue: " + literal_reverse_propertyValue + "\n" +
			"matchifNoContext: " + matchifNoContext + "\n" +
			"propertyId: " + propertyId + "\n" +
			"propertyName: " + propertyName + "\n" +
			"propertyType: " + propertyType + "\n" +
			"propertyValue: " + propertyValue + "\n" +
			"qualifiers: " + qualifiers + "\n" +
			"representationalForm: " + representationalForm + "\n" +
			"reverse_propertyValue: " + reverse_propertyValue + "\n" +
			"sources: " + sources + "\n" +
			"stem_PropertyValue: " + stem_PropertyValue + "\n" +
			"untokenizedLCPropertyValue: " + untokenizedLCPropertyValue + "\n";
	
		}
}
