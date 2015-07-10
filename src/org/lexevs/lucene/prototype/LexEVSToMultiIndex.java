package org.lexevs.lucene.prototype;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.locator.LexEvsServiceLocator;

public class LexEVSToMultiIndex {
	final int batchSize = 1000;
	LexBIGService lbs;
	DatabaseServiceManager dbsm;
	public LexEVSToMultiIndex() {
		lbs = LexBIGServiceImpl.defaultInstance();
	}
	
	public void indexCodingSchemesToSeparateIndexes() throws LBInvocationException{
		dbsm = LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
		EntityService entityService = dbsm.getEntityService();
		CodingSchemeRenderingList schemes = lbs.getSupportedCodingSchemes();
		for(CodingSchemeRendering rendering: schemes.getCodingSchemeRendering()){
			String uri = rendering.getCodingSchemeSummary().getCodingSchemeURI();
			String version = rendering.getCodingSchemeSummary().getRepresentsVersion();
				//TODO Create an index
				//TODO Create a writer for the index
			int position = 0;
			for(List<? extends Entity> entities = 
						entityService.getEntities(uri, version, position, batchSize);
					entities.size() > 0; 
					entities = entityService.getEntities(uri, version, position += batchSize, batchSize)) {

			this.indexEntities(entities);
		}
		}

		
	}

	private void indexEntities(List<? extends Entity> entities) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
