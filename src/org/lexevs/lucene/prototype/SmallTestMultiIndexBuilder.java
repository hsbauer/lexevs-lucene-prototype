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

public class SmallTestMultiIndexBuilder {
	public String[] code1 = {
		"C1234","C23432","C4234","C2308", "C8958"
	};
	
	public String[] code2 = {
		"C1235","C23436","C4214","C2368", "C8768"
	};
	public SmallTestMultiIndexBuilder() {
		// TODO Auto-generated constructor stub
	}

	public void init(){
		try {
			LuceneContentBuilder builder = new LuceneContentBuilder();
			Path path1 = Paths.get("/Users/m029206/Desktop/index1");
			Path path2 = Paths.get("/Users/m029206/Desktop/index2");
			Directory dir1 = new MMapDirectory(path1);
			Directory dir2 = new MMapDirectory(path2);
			Analyzer analyzer=new StandardAnalyzer(new CharArraySet( 0, true));
			IndexWriterConfig iwc1 = new IndexWriterConfig(analyzer);
			IndexWriterConfig iwc2 = new IndexWriterConfig(analyzer);
			IndexWriter writer1 = new IndexWriter(dir1, iwc1);
			IndexWriter writer2 = new IndexWriter(dir2, iwc2);
			createCodingSchemeIndex(builder, writer1, code1);
			createCodingSchemeIndex(builder, writer2, code2);
			writer1.commit();
			writer1.close();
			writer2.commit();
			writer2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void createCodingSchemeIndex(LuceneContentBuilder builder,
			IndexWriter writer, String[] codes) throws IOException {
			for(String c :codes){
			List<Document> list = createBlockJoin(c);
			writer.addDocuments(list);
			list = createBlockJoin2(c);
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
		new SmallTestMultiIndexBuilder().init();

	}

}
