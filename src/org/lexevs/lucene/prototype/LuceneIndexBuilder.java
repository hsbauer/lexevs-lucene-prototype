package org.lexevs.lucene.prototype;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;


public class LuceneIndexBuilder {

//	private int maxDocs = 100000;
	private int count = 0;
	LuceneContentBuilder builder;
	boolean thesExactMatchDone = false;
	boolean snomedExactMatchDone = false;
	boolean metaExactMatchDone = false;
	
	
	public LuceneIndexBuilder() {
		builder = new LuceneContentBuilder();
	}
	
	public void init(){
		try {
			File path = new File("/Users/m029206/git/lexevs-lucene-prototype/index");
			Directory dir = new MMapDirectory(path);
			Analyzer analyzer=new StandardAnalyzer(new CharArraySet( 0, true));
			IndexWriterConfig iwc= new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);
			IndexWriter writer = new IndexWriter(dir, iwc);
			createCodingSchemeIndex(builder, writer );
			writer.commit();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createCodingSchemeIndex(LuceneContentBuilder builder, IndexWriter writer) throws IOException{
		long start = System.currentTimeMillis();
		for(CodingScheme cs: CodingScheme.values()){
			for(int i = 0; i < cs.numberOfEntities; i++){
				List<Document> list = createBlockJoin(cs, builder);
				writer.addDocuments(list);
			}
		}
		System.out.println("Time loading: " + (System.currentTimeMillis() - start));
	}
	
	
	public List<Document> createBlockJoin(CodingScheme cs, LuceneContentBuilder builder){
		List<Document> list = new ArrayList<Document>();
		//need a static
		int staticCount = count;
		ParentDocObject parent = builder.generateParentDoc(cs.getCodingSchemeName(),
				cs.getVersion(), cs.getURI());
		if (cs.codingSchemeName.equals(CodingScheme.THESSCHEME.codingSchemeName)) {
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
				if(staticCount % 11 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(staticCount % 3 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(staticCount % 5 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
							builder.randomTextGenerator(
							builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(staticCount % 13 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
					Document doc = builder.mapToDocument(child);
					list.add(doc);
					count++;numberOfProperties--;
				}else if(staticCount % 23 == 0){
					ChildDocObject child = builder.generateChildDocWithSalt(parent,
							builder.randomTextGenerator(
									builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
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
		}
			Document par = builder.mapToDocument(parent);
			list.add(par);
			return list;
			
		}	
//	public List<Document> createBlockJoin(CodingScheme cs, LuceneContentBuilder builder){
//		List<Document> list = new ArrayList<Document>();
//		//need a static
//		int staticCount = count;
//		ChildDocObject parent = builder.generateParentDoc(cs.getCodingSchemeName(),
//				cs.getVersion(), cs.getURI());
//		if (cs.codingSchemeName.equals(CodingScheme.THESSCHEME.codingSchemeName)) {
//			//One per coding Scheme
//			int numberOfProperties = 12;
//				if(!thesExactMatchDone){
//				ChildDocObject child1 = builder.generateChildDocWithSalt(parent,SearchTerms.BLOOD.getTerm());
//				Document doc1 = builder.mapToDocument(child1);
//				list.add(doc1);
//				count++;
//				numberOfProperties--;
//				
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CHAR.term);
//				Document doc = builder.mapToDocument(child);
//				count++;
//				list.add(doc);
//				numberOfProperties--;
//				thesExactMatchDone = true;
//				}
//			while (numberOfProperties > 0) {
//				//Semi random application of some search terms.  Attempting to replicate
//				//Number of values present in previous searches.
//				if(staticCount % 709 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGenerator(
//									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(staticCount % 7 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGenerator(
//							builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(staticCount % 59 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGenerator(
//							builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(staticCount % 5953 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGenerator(
//									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(staticCount % 13399 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGenerator(
//									builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(staticCount % 2371 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(staticCount % 79 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(staticCount % 3581 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(staticCount % 23 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				
//				} else {
//					ChildDocObject child = builder.generateChildDoc(parent);
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;
//					numberOfProperties--;
//				}
//			}
//			Document par = builder.mapToDocument(parent);
//			list.add(par);
//			return list;
//		}
//		if (cs.codingSchemeName.equals(CodingScheme.SNOMEDSCHEME.codingSchemeName)) {
//			//One per coding Scheme
//			int numberOfProperties = 12;
//				if(!snomedExactMatchDone){
//				ChildDocObject child1 = builder.generateChildDocWithSalt(parent,SearchTerms.BLOOD.getTerm());
//				Document doc1 = builder.mapToDocument(child1);
//				list.add(doc1);
//				count++;numberOfProperties--;
//
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CHAR.term);
//				Document doc = builder.mapToDocument(child);
//				count++;numberOfProperties--;
//				list.add(doc);
//				snomedExactMatchDone = true;
//				}
//			while (numberOfProperties > 0) {
//				//Semi random application of some search terms.  Attempting to replicate
//				//Number of values present in previous searches.
//				if(count % 212 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGenerator(
//									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 50081 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGenerator(
//							builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 5 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGenerator(
//							builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 85 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGenerator(
//							builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 50082 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGenerator(
//									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 60098 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGenerator(
//									builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 91 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 60097 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGeneratorStartsWith(
//							builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 465 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 11 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else{
//				ChildDocObject child = builder.generateChildDoc(parent);
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//				}
//			}
//			Document par = builder.mapToDocument(parent);
//			list.add(par);
//			return list;
//		}
//		if (cs.codingSchemeName.equals(CodingScheme.METASCHEME.codingSchemeName)) {
//			
//			//One per coding Scheme
//			int numberOfProperties = 12;
//				if(!metaExactMatchDone){
//				ChildDocObject child1 = builder.generateChildDocWithSalt(parent,SearchTerms.BLOOD.getTerm());
//				Document doc1 = builder.mapToDocument(child1);
//				list.add(doc1);
//				count++;numberOfProperties--;
//
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CHAR.term);
//				Document doc = builder.mapToDocument(child);
//				count++;numberOfProperties--;
//				list.add(doc);
//				metaExactMatchDone = true;
//				}
//			while (numberOfProperties > 0) {
//				//Semi random application of some search terms.  Attempting to replicate
//				//Number of values present in previous searches.
//				if(count % 529 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGenerator(
//									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 71124 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGenerator(
//							builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 4 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGenerator(
//							builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 112 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGenerator(
//							builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 29490 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGenerator(
//									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 142248 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGenerator(
//									builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 725 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 96728 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//							builder.randomTextGeneratorStartsWith(
//							builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 505 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 37203 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 806074 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else if(count % 12 == 0){
//					ChildDocObject child = builder.generateChildDocWithSalt(parent,
//							builder.randomTextGeneratorStartsWith(
//									builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
//					Document doc = builder.mapToDocument(child);
//					list.add(doc);
//					count++;numberOfProperties--;
//				}else{
//				ChildDocObject child = builder.generateChildDoc(parent);
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//				}
//			}
//			Document par = builder.mapToDocument(parent);
//			list.add(par);
//			return list;
//		}
		//All other coding schemes.
		//int numberOfProperties = 12;




			
//		while (numberOfProperties > 0) {
//			//Semi random application of some search terms.  Attempting to replicate
//			//Number of values present in previous searches.
//			if(count % 200003 == 0){
//				ChildDocObject child1 = builder.generateChildDocWithSalt(parent,SearchTerms.BLOOD.getTerm());
//				Document doc1 = builder.mapToDocument(child1);
//				list.add(doc1);
//				count++;numberOfProperties--;
//			}
//			if(count % 270000 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.MUD.getTerm());
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 360000 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.LUNG_CANCER.getTerm());
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 1800000 == 0){
//				ChildDocObject child1 = builder.generateChildDocWithSalt(parent,SearchTerms.CODE1.getTerm());
//				Document doc1 = builder.mapToDocument(child1);
//				list.add(doc1);
//				count++;numberOfProperties--;
//			}
//			if(count % 600000 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CODE2.getTerm());
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 360000 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CODE3.getTerm());
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 540000 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.LIVER_CARCINOMA.getTerm());
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 108000 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,SearchTerms.CHAR.term);
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 196 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//						builder.randomTextGenerator(
//								builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 6585 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//						builder.randomTextGenerator(
//						builder.randomNumberGenerator(),SearchTerms.MUD.getTerm()));
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 5 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//						builder.randomTextGenerator(
//						builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 106 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent, 
//						builder.randomTextGenerator(
//						builder.randomNumberGenerator(),SearchTerms.ARTICLE.getTerm()));
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 5468 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,
//						builder.randomTextGenerator(
//								builder.randomNumberGenerator(),SearchTerms.LUNG_CANCER.getTerm()));
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 1459 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,
//						builder.randomTextGenerator(
//								builder.randomNumberGenerator(),SearchTerms.LIVER_CARCINOMA.getTerm()));
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 725 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,
//						builder.randomTextGeneratorStartsWith(
//								builder.randomNumberGenerator(),SearchTerms.BLOOD.getTerm()));
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}if(count % 12 == 0){
//				ChildDocObject child = builder.generateChildDocWithSalt(parent,
//						builder.randomTextGeneratorStartsWith(
//								builder.randomNumberGenerator(),SearchTerms.CHAR.getTerm()));
//				Document doc = builder.mapToDocument(child);
//				list.add(doc);
//				count++;numberOfProperties--;
//			}
//			if(numberOfProperties <= 0){
//			return list;}
//			else{
//			ChildDocObject child = builder.generateChildDoc(parent);
//			Document doc = builder.mapToDocument(child);
//			list.add(doc);
//			count++;numberOfProperties--;
//			}
//		}
//		Document par = builder.mapToDocument(parent);
//		list.add(par);
//		return list;
//	}

	public static void main(String[] args) {
	LuceneIndexBuilder builder = new LuceneIndexBuilder();
	builder.init();

	}

}
