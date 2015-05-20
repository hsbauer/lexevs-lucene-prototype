package org.lexevs.lucene.prototype;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

public class LuceneContentBuilder {

	  private SecureRandom random = new SecureRandom();
	  
	public LuceneContentBuilder() {
		// TODO Auto-g	enerated constructor stub
	}


	
	public String randomTextGenerator(){
		return new BigInteger(130, random).toString(32);
	}
	
	public String randomTextGenerator(int length){
		return new BigInteger(130, random).toString(length);
	}
	
	public String randomTextGenerator(int length, String salt){
		String randomText = new BigInteger(130, random).toString(randomNumberGenerator()) + salt + 
				new BigInteger(130, random).toString(randomNumberGenerator()) + 
				new BigInteger(130, random).toString(randomNumberGenerator()) + 
				new BigInteger(130, random).toString(randomNumberGenerator()) + salt;
		return randomText;
	}
	
	public int randomNumberGenerator(){
		 Random randomGenerator = new Random();
		 return randomGenerator.nextInt(32);
	}
	
	public DocObject generateChildDoc(DocObject parent){
		DocObject o = new DocObject();
		o.UID = randomTextGenerator();
		o.entityCode = parent.entityCode;
		o.entityCodeNamepace = parent.entityCodeNamepace;
		o.dm_propertyValue = randomTextGenerator();
		o.isPreferred = randomTextGenerator();
		o.language = "en";
		o.literal_propertyValue = randomTextGenerator();
		o.literal_reverse_propertyValue = randomTextGenerator();
		o.propertyId = randomTextGenerator();
		o.propertyName = randomTextGenerator();
		o.propertyType = randomTextGenerator();
		o.propertyValue = randomTextGenerator();
		o.qualifiers = randomTextGenerator();
		o.representationalForm = randomTextGenerator();
		o.reverse_propertyValue = randomTextGenerator();
		o.sources = randomTextGenerator();
		o.stem_PropertyValue = randomTextGenerator();
		o.untokenizedLCPropertyValue = randomTextGenerator();
		return o;
	}
	
	public DocObject generateParentDoc(String codingScheme, String version, String URI){
		DocObject o = new DocObject();
		o.codingSchemeId = URI;
		o.UID = randomTextGenerator();
		o.codingSchemeName = codingScheme;
		o.codingSchemeVersion = version;
		o.entityCode = randomTextGenerator();
		o.entityCodeLC = randomTextGenerator();
		o.entityCodeNamepace = codingScheme;
		o.entityDescription = randomTextGenerator();
		o.entityType = randomTextGenerator();
		o.entityUID = randomTextGenerator();
		o.format = randomTextGenerator();
		o.isActive = randomTextGenerator();
		o.isAnonymous = randomTextGenerator();
		return o;
	}
	
	
	public Document mapToDocument(DocObject doc){
		Class<? extends DocObject> clazz = doc.getClass();
		Document document = new Document();
		for(java.lang.reflect.Field field : clazz.getFields()){
		try {
			String fieldName = field.getName();
			byte[] fieldValue = null;
			if(field.get(doc) != null){
				fieldValue = field.get(doc).toString().getBytes();
			}
			document.add(new org.apache.lucene.document.Field(fieldName, fieldValue, TextField.TYPE_NOT_STORED));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return document;
	}
	
	
	public static void main(String[] args) {
		
	LuceneContentBuilder builder = new LuceneContentBuilder();
	for(int i = 10; i > 0; i--){
		DocObject parent = builder.generateParentDoc("CodingScheme","version", "uri");
		System.out.println("Parent: " + parent.toString());
		DocObject child = builder.generateChildDoc(parent);
		System.out.println("Child: " + child.toString());
	}

	}

}
