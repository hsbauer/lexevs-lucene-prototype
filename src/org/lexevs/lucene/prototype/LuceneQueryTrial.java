package org.lexevs.lucene.prototype;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.search.join.FixedBitSetCachingWrapperFilter;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToChildBlockJoinQuery;
import org.apache.lucene.search.join.ToParentBlockJoinCollector;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;

public class LuceneQueryTrial {
	Directory index;
	public LuceneQueryTrial(Directory index) {
		this.index = index;
	}
	
	public void luceneQuery(SearchTerms term, CodingScheme scheme) throws IOException{
		int hitsPerPage = 10000;
		IndexReader reader = IndexReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
//		ToParentBlockJoinCollector collector = new ToParentBlockJoinCollector(   
//    Sort.RELEVANCE, // sort
//    10,             // numHits
//    true,           // trackScores
//    false           // trackMaxScore
//    );
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		
		  Filter codingScheme = new FixedBitSetCachingWrapperFilter(
                  new QueryWrapperFilter(
                    new TermQuery(
                      new Term("codingSchemeName", "thesscheme"))));
		  BooleanQuery termQuery = new BooleanQuery();
		  termQuery.add(new TermQuery(new Term("propertyValue", "blood")), Occur.MUST);
//		  ToChildBlockJoinQuery termJoinQuery = new ToChildBlockJoinQuery(termQuery, codingScheme, false);
		  ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
				    termQuery, 
				    codingScheme,
				    ScoreMode.None);
		  searcher.search(termJoinQuery, collector);
//		  Sort termSort = new Sort();

//		TopGroups hits = collector.getTopGroups(
//				    termJoinQuery,
//				    Sort.RELEVANCE,
//				    0,   // offset
//				    10,  // maxDocsPerGroup
//				    0,   // withinGroupOffset
//				    true // fillSortFields
//				  );
		
		
//		  System.out.println("Found " + hits.totalHitCount + " hits.");
//		  for(int i=0;i<hits.totalHitCount;++i) {
//		      GroupDocs[] docs = hits.groups;
//		      for(int j=0;j < docs.length; j++){
//		      //Document d = searcher.doc(docs[j].;
//		    	  System.out.println("total group hits: " + docs[j].totalHits);
//		      }
////		      System.out.println((i + 1) + ". " + d.get("entityCode") + "\t" + d.get("propertyValue"));
//		  	}
		  ScoreDoc[] hits = collector.topDocs().scoreDocs;
		  System.out.println("Found " + hits.length + " hits.");
		  for(int i=0;i<hits.length;++i) {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      System.out.println((i + 1) + ". " + d.get("entityCode") + "\t" + d.get("entityDescription"));
		  	}
	}
	
	public static void  main(String[] args){
		File path = new File("/Users/m029206/git/lexevs-lucene-prototype/index");
		Directory index = null;
		try {
			index = new MMapDirectory(path);
			LuceneQueryTrial trial = new LuceneQueryTrial(index);
			trial.luceneQuery(SearchTerms.BLOOD, CodingScheme.THESSCHEME);
//			trial.luceneQuery(SearchTerms.MUD, CodingScheme.THESSCHEME);
//			trial.luceneQuery(SearchTerms.CHAR, CodingScheme.THESSCHEME);
//			trial.luceneQuery(SearchTerms.ARTICLE, CodingScheme.THESSCHEME);
//			trial.luceneQuery(SearchTerms.LUNG_CANCER, CodingScheme.THESSCHEME);
//			trial.luceneQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.THESSCHEME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
