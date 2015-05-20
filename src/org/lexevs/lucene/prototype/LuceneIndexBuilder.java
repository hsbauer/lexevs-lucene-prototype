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
	public void createCodingSchemeIndex(CodingScheme scheme, LuceneContentBuilder builder, IndexWriter writer){
		for(CodingScheme cs: CodingScheme.values()){
			for(int i = 0; i < cs.numberOfEntities; i++){
				DocObject parent = builder.generateParentDoc(cs.getCodingSchemeName(), cs.getVersion(),cs.getURI());
				DocObject child = builder.generateChildDoc(parent);
			}
		}
		
	}
	public List<Document> createBlockJoin(){
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
