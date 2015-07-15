package org.lexevs.lucene.prototype;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.Entity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.locator.LexEvsServiceLocator;

public class LexEVSToMultiIndex {
	final int batchSize = 1000;
	LexBIGService lbs;
	DatabaseServiceManager dbsm;
	LuceneContentBuilder builder;
	public LexEVSToMultiIndex() {
		lbs = LexBIGServiceImpl.defaultInstance();
		builder = new LuceneContentBuilder();
	}
	
	public void indexCodingSchemesToSeparateIndexes() throws LBInvocationException, IOException{
		dbsm = LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
		EntityService entityService = dbsm.getEntityService();
		CodingSchemeRenderingList schemes = lbs.getSupportedCodingSchemes();
		for(CodingSchemeRendering rendering: schemes.getCodingSchemeRendering()){
			String uri = rendering.getCodingSchemeSummary().getCodingSchemeURI();
			String version = rendering.getCodingSchemeSummary().getRepresentsVersion();
			String name = rendering.getCodingSchemeSummary().getFormalName();
			Path path = Paths.get("/Users/m029206/Desktop/lexIndex/" 
					+ rendering.getCodingSchemeSummary().getFormalName() + "_" + version );
				Directory dir = new MMapDirectory(path);
				Analyzer analyzer=new StandardAnalyzer(new CharArraySet( 0, true));
				IndexWriterConfig iwc= new IndexWriterConfig(analyzer);
				IndexWriter writer = new IndexWriter(dir, iwc);
			int position = 0;
			for(List<? extends Entity> entities = 
						entityService.getEntities(uri, version, position, batchSize);
					entities.size() > 0; 
					entities = entityService.getEntities(uri, version, position += batchSize, batchSize)) {

				for(Entity e : entities){
					List<Document> docs = this.indexEntity(e, name, version, uri );
					writer.addDocuments(docs);
				}
			}
			writer.commit();
			writer.close();
		}

		
	}

	private List<Document> indexEntity(Entity e, String codingScheme, String version, String URI) {
		List<Document> docs = new ArrayList<Document>();
		Document parentDoc = new Document();
		parentDoc.add( new StringField("codingSchemeId", URI, Store.YES));
		parentDoc.add( new StringField("codingSchemeName", codingScheme, Store.YES));
		parentDoc.add( new StringField("codingSchemeVersion", version, Store.YES));
		parentDoc.add( new StringField("entityCode", e.getEntityCode(), Store.YES));
		parentDoc.add( new StringField("entityCodeLC", e.getEntityCode().toLowerCase(), Store.NO));
		parentDoc.add(new TextField("entityCodeNamespace", e.getEntityCodeNamespace(), Store.NO));
		parentDoc.add(new TextField("entityDescription", e.getEntityDescription().getContent(), Store.NO));
		parentDoc.add( new StringField("entityType", e.getEntityType()[0], Store.YES));
		parentDoc.add( new StringField("entityUID", e.getEntityCode() + ":" + URI + ":" + version, Store.YES));
		parentDoc.add( new StringField("format", "text", Store.NO));
		parentDoc.add( new TextField("isActive", e.getIsActive().toString(), Store.YES));
		parentDoc.add( new TextField("isAnonymous", e.getIsAnonymous().toString(), Store.YES));
		for(Property p: e.getAllProperties()){
			docs.add(indexProperty(p, e.getEntityCode(), e.getEntityCodeNamespace()));
		}
		docs.add(parentDoc);
		return docs;
	}



	private Document indexProperty(Property p, String code, String namespace ) {
		Document child = new Document();
		child.add( new TextField("degreeOfFidelity", p instanceof Presentation? ((Presentation)p).getDegreeOfFidelity():"", Store.NO));
		child.add( new TextField("dm_propertyValue", builder.randomTextGenerator(), Store.YES));
		child.add( new TextField("ecode", code, Store.NO));
		child.add( new StringField("isPreferred", p instanceof Presentation? ((Presentation)p).getIsPreferred().toString():"", Store.YES));
		child.add( new TextField("language", p.getLanguage(), Store.YES));
		child.add( new StringField("literal_propertyValue", p.getValue().getContent(), Store.YES));
		child.add( new StringField("literal_reverse_propertyValue", new StringBuilder(p.getValue().getContent()).reverse().toString(), Store.YES));
		child.add( new TextField("matchifNoContext",p instanceof Presentation? ((Presentation)p).getMatchIfNoContext().toString():"" , Store.NO));
		child.add( new TextField("namespace", namespace, Store.NO));
		child.add( new TextField("propertyId", p.getPropertyId(), Store.NO));
		child.add( new StringField("propertyName", p.getPropertyName(), Store.YES));
		child.add( new StringField("propertyType", p.getPropertyType(), Store.NO));
		child.add( new TextField("propertyValue", p.getValue().getContent(), Store.YES));
		child.add( new TextField("qualifiers", p.getPropertyQualifierAsReference().toString(), Store.NO));
		child.add( new TextField("representationalForm", p instanceof Presentation? ((Presentation)p).getRepresentationalForm():"", Store.NO));
		child.add( new TextField("reverse_propertyValue", new StringBuilder(p.getValue().getContent()).reverse().toString(), Store.YES));
		child.add( new TextField("sources", p.getSourceAsReference().toString(), Store.NO));
		child.add( new TextField("stem_PropertyValue", "", Store.NO));
		child.add( new StringField("untokenizedLCPropertyValue", p.getValue().getContent().toLowerCase(), Store.YES));
		return child;
	}

	public static void main(String[] args) {
		try {
			new LexEVSToMultiIndex().indexCodingSchemesToSeparateIndexes();
		} catch (LBInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
