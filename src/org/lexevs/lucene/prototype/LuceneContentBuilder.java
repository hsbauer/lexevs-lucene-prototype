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
	}
	
	public String randomTextGenerator(){
		return new BigInteger(130, random).toString(32);
	}
	
	public String randomTextGenerator(int length){
		return new BigInteger(130, random).toString(length);
	}
	
	public String randomTextGenerator(int length, String salt){
		String randomText = randomTextGenerator(randomNumberGenerator()) + " "+
				randomTextGenerator(randomNumberGenerator()) + " "+ salt + " "+
				randomTextGenerator(randomNumberGenerator()) + " "+
				randomTextGenerator(randomNumberGenerator());
		return randomText;
	}
	
	public String randomTextGeneratorStartsWith(int length, String salt){
		String randomText =  salt + " "+ randomTextGenerator(randomNumberGenerator()) + " "+
				randomTextGenerator(randomNumberGenerator()) + " "+ 
				randomTextGenerator(randomNumberGenerator()) + " "+
				randomTextGenerator(randomNumberGenerator());
		return randomText;
	}
	
	public int randomNumberGenerator(){
		 Random randomGenerator = new Random();
		 return randomGenerator.nextInt(32);
	}
	
	public ChildDocObject generateChildDoc(ParentDocObject parent){
		ChildDocObject o = new ChildDocObject();
//		o.UID = randomTextGenerator();
		o.namespace = parent.entityCodeNamepace;
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
	
	public ParentDocObject generateParentDoc(String codingScheme, String version, String URI, String salt){
		ParentDocObject o = new ParentDocObject();
		o.codingSchemeId = URI;
		o.blockId = randomTextGenerator();
		o.codingSchemeName = codingScheme;
		o.codingSchemeVersion = version;
		o.entityCode = randomTextGenerator();
		o.entityCodeLC = randomTextGenerator();
		o.entityCodeNamepace = codingScheme;
		o.entityDescription = salt;
		o.entityType = randomTextGenerator();
		o.entityUID = randomTextGenerator();
		o.format = randomTextGenerator();
		o.isActive = randomTextGenerator();
		o.isAnonymous = randomTextGenerator();
		o.parentDoc = "yes";
		return o;
	}
	
	public ChildDocObject generateChildDocWithSalt(ParentDocObject parent, String salt){
		ChildDocObject o = new ChildDocObject();
//		o.UID = randomTextGenerator();
		o.blockId = parent.blockId;
		o.ecode = parent.entityCode;
		o.namespace = parent.entityCodeNamepace;
		o.dm_propertyValue = randomTextGenerator();
		o.isPreferred = randomTextGenerator();
		o.language = "en";
		o.literal_propertyValue = randomTextGenerator();
		o.literal_reverse_propertyValue = randomTextGenerator();
		o.propertyId = randomTextGenerator();
		o.propertyName = randomTextGenerator();
		o.propertyType = randomTextGenerator();
		o.propertyValue = salt;
		o.qualifiers = randomTextGenerator();
		o.representationalForm = randomTextGenerator();
		o.reverse_propertyValue = randomTextGenerator();
		o.sources = randomTextGenerator();
		o.stem_PropertyValue = randomTextGenerator();
		o.untokenizedLCPropertyValue = randomTextGenerator();
		return o;
	}
	
	
	
	public Document mapToDocument(DocObject doc){
		Class<? extends DocObject> clazz = doc.getClass();
		Document document = new Document();
		for(java.lang.reflect.Field field : clazz.getFields()){
		try {
			String fieldName = field.getName();
			String fieldValue = null;
			if(field.get(doc) != null){
				fieldValue = field.get(doc).toString();
				document.add(new org.apache.lucene.document.TextField(fieldName, fieldValue, Field.Store.YES));
			}
		
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
	
	public Document mapToDocumentExactMatch(DocObject doc){
		Class<? extends DocObject> clazz = doc.getClass();
		Document document = new Document();
		for(java.lang.reflect.Field field : clazz.getFields()){
		try {
			String fieldName = field.getName();
			String fieldValue = null;
			if(field.get(doc) != null){
				fieldValue = field.get(doc).toString();
				if(fieldName.equals("propertyValue")){
				document.add(new org.apache.lucene.document.StringField(fieldName, fieldValue, Field.Store.YES));
				}else{
					document.add(new org.apache.lucene.document.TextField(fieldName, fieldValue, Field.Store.YES));
				}
			}
		
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
		ParentDocObject parent = builder.generateParentDoc("CodingScheme","version", "uri", "blood");
		System.out.println("Parent: " + parent.toString());
		ChildDocObject child = builder.generateChildDoc(parent);
		System.out.println("Child: " + child.toString());
	}

	}

}
