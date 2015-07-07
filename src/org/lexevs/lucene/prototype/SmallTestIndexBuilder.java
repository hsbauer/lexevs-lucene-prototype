package org.lexevs.lucene.prototype;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;

public class SmallTestIndexBuilder {
	public enum Code{
		C1234,C23432,C4234,C2308, C8958;
	}
	public SmallTestIndexBuilder() {
		// TODO Auto-generated constructor stub
	}

	public void init(){
		try {
			LuceneContentBuilder builder = new LuceneContentBuilder();
			Path path = Paths.get("/Users/m029206/Desktop/index");
			Directory dir = new MMapDirectory(path);
			Analyzer analyzer=new StandardAnalyzer(new CharArraySet( 0, true));
			IndexWriterConfig iwc= new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(dir, iwc);
			createCodingSchemeIndex(builder, writer );
			writer.commit();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void createCodingSchemeIndex(LuceneContentBuilder builder,
			IndexWriter writer) throws IOException {
			for(Code c :Code.values()){
			List<Document> list = createBlockJoin(c.name());
			writer.addDocuments(list);
			list = createBlockJoin2(c.name());
			writer.addDocuments(list);
			}
	}

	private List<Document> createBlockJoin(String code) {
		List<Document> list = new ArrayList<Document>();
	
		Document doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "Blood", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "Mud", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "Suds", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "coagulant", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "hepa", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "hematoma", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "normal", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "abnormal", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "notfound", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "red blood cells", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "Blood", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "Blood", Field.Store.YES));
		list.add(doc);
		
		Document par = new Document();
		par.add(new org.apache.lucene.document.TextField("codingSchemeName", "TestScheme", Field.Store.YES));
		par.add(new org.apache.lucene.document.TextField("parentDoc", "yes", Field.Store.YES));
		par.add(new org.apache.lucene.document.TextField("entityCode", code, Field.Store.YES));
		list.add(par);
		return list;
	}

	private List<Document> createBlockJoin2(String code) {
		List<Document> list = new ArrayList<Document>();
	
		Document doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "Blood", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "Mud", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "Suds", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "coagulant", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "hepa", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "hematoma", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "normal", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "abnormal", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "notfound", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "red blood cells", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "Blood", Field.Store.YES));
		list.add(doc);
		doc = new Document();
		doc.add(new org.apache.lucene.document.TextField("propertyValue", "Blood", Field.Store.YES));
		list.add(doc);
		
		Document par = new Document();
		par.add(new org.apache.lucene.document.TextField("codingSchemeName", "TestSchemeToo", Field.Store.YES));
		par.add(new org.apache.lucene.document.TextField("parentDoc", "yes", Field.Store.YES));
		par.add(new org.apache.lucene.document.TextField("entityCode", code, Field.Store.YES));
		list.add(par);
		return list;
	}
	public static void main(String[] args) {
		new SmallTestIndexBuilder().init();

	}

}
