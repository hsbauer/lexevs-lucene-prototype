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
			for(int i = 0; i < cs.numberOfEntities; i++){
				List<Document> list = createBlockJoin(cs, builder, count++);
				writer.addDocuments(list);
			}
		}
	}
	
	public List<Document> createBlockJoin(CodingScheme cs, LuceneContentBuilder builder, int count){
		List<Document> list = new ArrayList<Document>();

		DocObject parent = builder.generateParentDoc(cs.getCodingSchemeName(),
				cs.getVersion(), cs.getURI());
		if (cs.codingSchemeName.equals(CodingScheme.THESSCHEME.codingSchemeName)) {
			int numberOfProperties = 12;
			while (numberOfProperties < 0) {
				if(count % 544 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, "blood");
					Document doc = builder.mapToDocument(child);
					count++;
				}else if(count % 6 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, "a");
					Document doc = builder.mapToDocument(child);
				}else if(count % 57 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, "the");
					Document doc = builder.mapToDocument(child);
				}else if(count % 2232 == 0){
					DocObject child = builder.generateChildDocWithSalt(parent, "Lung Cancer");
					Document doc = builder.mapToDocument(child);
				}else if(count % 2232 == 0){
					
				}
				DocObject child = builder.generateChildDoc(parent);
				Document doc = builder.mapToDocument(child);
				list.add(doc);
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
