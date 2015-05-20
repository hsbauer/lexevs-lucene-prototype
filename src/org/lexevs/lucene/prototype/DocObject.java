package org.lexevs.lucene.prototype;

public class DocObject {

		String UID;
		String codingSchemeName;
		String codingSchemeVersion;
		String parentDoc;
		String codingSchemeId;
		String conceptStatus;
		String csUriVersionKey;
		String degreeOfFidelity;
		String dm_propertyValue;
		String entityCode;
		String entityCodeLC;
		String entityCodeNamepace;
		String entityDescription;
		String entityType;
		String entityUID;
		String format;
		String isActive;
		String isAnonymous;
		String isPreferred;
		String language;
		String literal_propertyValue;
		String literal_reverse_propertyValue;
		String matchifNoContext;
		String propertyId;
		String propertyName;
		String propertyType;
		String propertyValue;
		String qualifiers;
		String representationalForm;
		String reverse_propertyValue;
		String sources;
		String stem_PropertyValue;
		String untokenizedLCPropertyValue;
				
		public String toString(){
			return  "UID: " + UID  + "\n" +
			"codingSchemeName: " + codingSchemeName  + "\n" +
			"codingSchemeVersion: " + codingSchemeVersion  + "\n" +
			"parentDoc: " +	parentDoc + "\n" +
			"codingSchemeId: " + codingSchemeId + "\n" +
			"conceptStatus: " + conceptStatus +  "\n" +
			"csUriVersionKey: " + csUriVersionKey + "\n" +
			"degreeOfFidelity: " + degreeOfFidelity + "\n" +
			"dm_propertyValue: " + dm_propertyValue + "\n" +
			"entityCode: " + entityCode + "\n" +
			"entityCodeLC: " + entityCodeLC + "\n" +
			"entityCodeNamepace: " +  entityCodeNamepace + "\n" +
			"entityDescription: " +  entityDescription  + "\n" +
			"entityType: " + entityType + "\n" +
			"entityUID: " + entityUID + "\n" +
			"format: " + format + "\n" + 
			"isActive: " + isActive + "\n" +
			"isAnonymous: " + isAnonymous + "\n" +
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
