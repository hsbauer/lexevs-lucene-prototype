package org.lexevs.lucene.prototype;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;

public class MultiIndexBuilder {

	public ConcurrentHashMap<String, MapNameVersion> indexes;
	private int count = 0;
	LuceneContentBuilder builder;
	boolean thesExactMatchDone = false;
	boolean snomedExactMatchDone = false;
	boolean metaExactMatchDone = false;
	
	public MultiIndexBuilder() {
		builder = new LuceneContentBuilder();
		indexes = new ConcurrentHashMap<String, MapNameVersion>(CodingScheme.values().length);
	}
	
	public void initIndex(CodingScheme scheme){
		try {
			Path path = Paths.get("/Users/m029206/git/lexevs-lucene-prototype/" + scheme.codingSchemeName);
			Directory dir = new MMapDirectory(path);
			Analyzer analyzer=new StandardAnalyzer(new CharArraySet( 0, true));
			IndexWriterConfig iwc= new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(dir, iwc);
			for(int i = 0; i < scheme.numberOfEntities; i++){
				writer.addDocuments(buildScheme(scheme));
			}
			writer.commit();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createCodingSchemeIndex() throws IOException{
		long start = System.currentTimeMillis();
		for(CodingScheme cs: CodingScheme.values()){
			initIndex(cs);
		}
		System.out.println("Time loading: " + (System.currentTimeMillis() - start));
	}
	
    public List<Document> buildScheme(CodingScheme scheme) {

        switch (scheme) {
            case THESSCHEME: 
                return buildThesaurusAnalog();
            case SNOMEDSCHEME: 
                return buildSnomedSchemeAnalog();
            case METASCHEME:
            	return buildMetaSchemeAnalog();
            default: 
                return buildAnyOtherScheme(scheme);
        }
    }
	private List<Document> buildAnyOtherScheme(CodingScheme scheme) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Document> buildThesaurusAnalog(){
		List<Document> list = new ArrayList<Document>();
		//build a parent regardless
		ParentDocObject parent = builder.generateParentDoc(CodingScheme.THESSCHEME.getCodingSchemeName(),
				CodingScheme.THESSCHEME.getVersion(), CodingScheme.THESSCHEME.getURI(), "description" + CodingScheme.THESSCHEME.getCodingSchemeName());
			//One per coding Scheme
			int numberOfProperties = 12;
				if(!thesExactMatchDone){
				ChildDocObject child1 = builder.generateChildDocWithSalt(parent,SearchTerms.BLOOD.getTerm());
				Document doc1 = builder.mapToDocument(child1);
				list.add(doc1);
				count++;
				numberOfProperties--;
				
				ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CHAR.term);
				Document doc = builder.mapToDocument(child);
				count++;
				list.add(doc);
				numberOfProperties--;
				thesExactMatchDone = true;
				}
			while (numberOfProperties > 0) {
				//Semi random application of some search terms.  Attempting to replicate
				//Number of values present in previous searches.
				if(count % 709 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(count % 7 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(count % 59 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(count % 5953 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(count % 13399 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(count % 2371 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(count % 79 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(count % 3581 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(count % 23 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				
				} else {
					ChildDocObject child = builder.generateChildDoc(parent);
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
					numberOfProperties--;
				}
			}
			Document par = builder.mapToDocument(parent);
			list.add(par);
			return list;
	}
	
	public List<Document> buildSnomedSchemeAnalog(){
		
		List<Document> list = new ArrayList<Document>();
		//build a parent regardless
		ParentDocObject parent = builder.generateParentDoc(CodingScheme.SNOMEDSCHEME.getCodingSchemeName(),
				CodingScheme.SNOMEDSCHEME.getVersion(), CodingScheme.SNOMEDSCHEME.getURI(), "description" + CodingScheme.SNOMEDSCHEME.getCodingSchemeName());
		
		int numberOfProperties = 12;
		if(!snomedExactMatchDone){
		ChildDocObject child1 = builder.generateChildDocWithSalt(parent,SearchTerms.BLOOD.getTerm());
		Document doc1 = builder.mapToDocument(child1);
		list.add(doc1);
		count++;numberOfProperties--;

		ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CHAR.term);
		Document doc = builder.mapToDocument(child);
		count++;numberOfProperties--;
		list.add(doc);
		snomedExactMatchDone = true;
		}
	while (numberOfProperties > 0) {
		//Semi random application of some search terms.  Attempting to replicate
		//Number of values present in previous searches.
		if(count % 211 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent, 
					builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 50087 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent, 
					builder.randomTextGenerator(
					builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 5 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent, 
					builder.randomTextGenerator(
					builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 89 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent, 
					builder.randomTextGenerator(
					builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 50077 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 60101 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 89 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGeneratorStartsWith(
							builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 60091 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent, 
					builder.randomTextGeneratorStartsWith(
					builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 467 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGeneratorStartsWith(
							builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 17 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGeneratorStartsWith(
							builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else{
		ChildDocObject child = builder.generateChildDoc(parent);
		Document doc = builder.mapToDocument(child);
		list.add(doc);
		count++;numberOfProperties--;
		}
	}
	Document par = builder.mapToDocument(parent);
	list.add(par);
	return list;
	}
	
	public List<Document> buildMetaSchemeAnalog(){
		List<Document> list = new ArrayList<Document>();
		//build a parent regardless
		ParentDocObject parent = builder.generateParentDoc(CodingScheme.METASCHEME.getCodingSchemeName(),
				CodingScheme.METASCHEME.getVersion(), CodingScheme.METASCHEME.getURI(), "description" + CodingScheme.METASCHEME.getCodingSchemeName());
		int numberOfProperties = 12;
		if(!metaExactMatchDone){
		ChildDocObject child1 = builder.generateChildDocWithSalt(parent,SearchTerms.BLOOD.getTerm());
		Document doc1 = builder.mapToDocument(child1);
		list.add(doc1);
		count++;numberOfProperties--;

		ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CHAR.term);
		Document doc = builder.mapToDocument(child);
		count++;numberOfProperties--;
		list.add(doc);
		metaExactMatchDone = true;
		}
	while (numberOfProperties > 0) {
		//Semi random application of some search terms.  Attempting to replicate
		//Number of values present in previous searches.
		if(count % 523 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent, 
					builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 71129 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent, 
					builder.randomTextGenerator(
					builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 19 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent, 
					builder.randomTextGenerator(
					builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 113 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent, 
					builder.randomTextGenerator(
					builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 29501 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 142237 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 727 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGeneratorStartsWith(
							builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 96731 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent, 
					builder.randomTextGeneratorStartsWith(
					builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 509 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGeneratorStartsWith(
							builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 37201 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGeneratorStartsWith(
							builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 806059 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGeneratorStartsWith(
							builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else if(count % 11 == 0){
			ChildDocObject child = builder.generateChildDocWithSalt(parent,
					builder.randomTextGeneratorStartsWith(
							builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
			Document doc = builder.mapToDocument(child);
			list.add(doc);
			count++;numberOfProperties--;
		}else{
		ChildDocObject child = builder.generateChildDoc(parent);
		Document doc = builder.mapToDocument(child);
		list.add(doc);
		count++;numberOfProperties--;
		}
	}
	Document par = builder.mapToDocument(parent);
	list.add(par);
	return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
