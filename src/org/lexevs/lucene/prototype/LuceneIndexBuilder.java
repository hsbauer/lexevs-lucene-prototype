package org.lexevs.lucene.prototype;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;


public class LuceneIndexBuilder {

	private int maxDocs = 100000;
	private int maxProps = 10;
	LuceneContentBuilder builder;
	
	
	public LuceneIndexBuilder() {
		init();
		builder = new LuceneContentBuilder();
	}
	public void init(){
		try {
			Path path = Paths.get("/Users/m029206/git/lexevs/zzsandbox/index");
			Directory dir = new MMapDirectory(path);
			IndexWriter writer = new IndexWriter(dir, null);
			List<Document> docs = new ArrayList<Document>();
//			for(int j = 0; j < maxDocs; j++){
//			DocObject doc =builder.generateParentDoc();
//            Document parent = builder.mapToDocument(doc);
//			for(int i = 0; i < maxProps ; i++){
//			Document child = builder.mapToDocument(builder.generateChildDoc(doc));
//			docs.add(child);
//			}
//			docs.add(parent);
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createCodingSchemeIndex(CodingScheme scheme, LuceneContentBuilder builder, IndexWriter writer) throws IOException{
		for(CodingScheme cs: CodingScheme.values()){
			int count = 0;
			boolean done = false;
			for(int i = 0; i < cs.numberOfEntities; i++){
				List<Document> list = createBlockJoin(cs, builder, count++, done);
				writer.addDocuments(list);
			}
		}
	}
	
	public List<Document> createBlockJoin(CodingScheme cs, LuceneContentBuilder builder, int count, boolean done){
		List<Document> list = new ArrayList<Document>();

		DocObject parent = builder.generateParentDoc(cs.getCodingSchemeName(),
				cs.getVersion(), cs.getURI());
		if (cs.codingSchemeName.equals(CodingScheme.THESSCHEME.codingSchemeName)) {
			//One per coding Scheme
			int numberOfProperties = 12;
				if(!done){
				DocObject child1 = builder.generateChildDocWithSalt(parent,SearchTerms.BLOOD.getTerm());
				Document doc1 = builder.mapToDocument(child1);
				list.add(doc1);
				count++;

				DocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CHAR.term);
				Document doc = builder.mapToDocument(child);
				count++;
				list.add(doc);
				done = true;
				}
			while (numberOfProperties < 0) {
				//Semi random application of some search terms.  Attempting to replicate
				//Number of values present in previous searches.
				if(count % 708 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 8 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 57 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 5953 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 13395 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 2363 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 76 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 3572 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 20 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else{
				DocObject child = builder.generateChildDoc(parent);
				Document doc = builder.mapToDocument(child);
				list.add(doc);
				count++;
				}
			}
			Document par = builder.mapToDocument(parent);
			list.add(par);
		}
		if (cs.codingSchemeName.equals(CodingScheme.SNOMEDSCHEME.codingSchemeName)) {
			//One per coding Scheme
			int numberOfProperties = 12;
				if(!done){
				DocObject child1 = builder.generateChildDocWithSalt(parent,SearchTerms.BLOOD.getTerm());
				Document doc1 = builder.mapToDocument(child1);
				list.add(doc1);
				count++;

				DocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CHAR.term);
				Document doc = builder.mapToDocument(child);
				count++;
				list.add(doc);
				done = true;
				}
			while (numberOfProperties < 0) {
				//Semi random application of some search terms.  Attempting to replicate
				//Number of values present in previous searches.
				if(count % 212 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 50081 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 5 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 85 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 50082 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 60098 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 91 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 60097 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGeneratorStartsWith(
							builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 465 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 11 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else{
				DocObject child = builder.generateChildDoc(parent);
				Document doc = builder.mapToDocument(child);
				list.add(doc);
				count++;
				}
			}
			Document par = builder.mapToDocument(parent);
			list.add(par);
		}
		if (cs.codingSchemeName.equals(CodingScheme.METASCHEME.codingSchemeName)) {
			
			//One per coding Scheme
			int numberOfProperties = 12;
				if(!done){
				DocObject child1 = builder.generateChildDocWithSalt(parent,SearchTerms.BLOOD.getTerm());
				Document doc1 = builder.mapToDocument(child1);
				list.add(doc1);
				count++;

				DocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CHAR.term);
				Document doc = builder.mapToDocument(child);
				count++;
				list.add(doc);
				done = true;
				}
			while (numberOfProperties < 0) {
				//Semi random application of some search terms.  Attempting to replicate
				//Number of values present in previous searches.
				if(count % 529 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 71124 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 4 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 112 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 29490 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 142248 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 725 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 96728 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGeneratorStartsWith(
							builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 505 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 37203 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 806074 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else if(count % 12 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGeneratorStartsWith(
									builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;
				}else{
				DocObject child = builder.generateChildDoc(parent);
				Document doc = builder.mapToDocument(child);
				list.add(doc);
				count++;
				}
			}
			Document par = builder.mapToDocument(parent);
			list.add(par);
		}
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
