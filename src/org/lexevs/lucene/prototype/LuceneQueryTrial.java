package org.lexevs.lucene.prototype;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
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
import org.apache.lucene.util.BytesRef;

public class LuceneQueryTrial {
	Directory index;
	StringBuilder output;
	IndexReader reader;
	IndexSearcher searcher;
	int hitsPerPage = 20000;
	
	public LuceneQueryTrial(Directory index) throws IOException {
		this.index = index;
		output = new StringBuilder();
		reader =  DirectoryReader.open(index);;
		searcher = new IndexSearcher(reader);;
	}
	
	public List<String> luceneToParentJoinQuery(SearchTerms term, CodingScheme scheme) throws IOException, ParseException{

		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		long start = System.currentTimeMillis();
		  Filter codingScheme = new FixedBitSetCachingWrapperFilter(
                  new QueryWrapperFilter(new QueryParser("codingSchemeName", new StandardAnalyzer(new CharArraySet( 0, true))).parse(scheme.getCodingSchemeName())));

		  Query query = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("propertyValue", term.getTerm(), Occur.MUST);
		  ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
				    query, 
				    codingScheme,
				    ScoreMode.Total);
		  searcher.search(termJoinQuery, collector);

		  ScoreDoc[] hits = collector.topDocs().scoreDocs;
			long time = System.currentTimeMillis() - start;
			System.out.println("\nFor codingScheme: " + scheme.codingSchemeName +  " term: " + term.getTerm());
			System.out.println("Search Time to Parent Join: " + time);
		  System.out.println("Found Parent Hits: " + hits.length + " hits.");
		  List<String> list = new ArrayList<String>();
		  for(int i=0;i<hits.length;++i) {
			  Document d = null;
		      int docId = hits[i].doc;
		      if(docId > 0){
		      d = searcher.doc(docId);
		      list.add(d.get("entityCode"));
//		      System.out.println((i + 1) + ". " + d.get("entityCode") + "\t" + d.get("entityDescription"));
		      }
		  	}
		  
		  return list;
	}
	


	public void luceneToParentGroupingJoinQuery(SearchTerms term,
			CodingScheme scheme) throws IOException, ParseException {
		ToParentBlockJoinCollector collector = new ToParentBlockJoinCollector(
				Sort.INDEXORDER, // sort
				hitsPerPage, // numHits
				true, // trackScores
				true // trackMaxScore
		);
		long start = System.currentTimeMillis();
		Filter codingScheme = new FixedBitSetCachingWrapperFilter(
				new QueryWrapperFilter(new QueryParser("codingSchemeName",
						new StandardAnalyzer(new CharArraySet(0, true)))
						.parse(scheme.getCodingSchemeName())));

		Query query = new QueryParser(null, new StandardAnalyzer(
				new CharArraySet(0, true))).createBooleanQuery("propertyValue",
				term.getTerm(), Occur.MUST);
		ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
				query, codingScheme, ScoreMode.Total);
		searcher.search(termJoinQuery, collector);

		TopGroups<Integer> hits = collector.getTopGroupsWithAllChildDocs(termJoinQuery,
				Sort.RELEVANCE, 
				0, // offset
//				hitsPerPage, // maxDocsPerGroup
				0, // withinGroupOffset
				true // fillSortFields
				);
		long time = System.currentTimeMillis() - start;
		System.out.println("\nFor codingScheme: " + scheme.codingSchemeName +  " term: " + term.getTerm());
		System.out.println("Search Time To Parent Group Join: " + time);
		System.out.println("Found " + 
		hits.totalHitCount 
		+ " hits.");
		System.out.println("Found " + hits.totalGroupedHitCount + " hits.");
		System.out.println("Found " + hits.totalGroupCount.toString()
				+ " hits.");

		GroupDocs<Integer>[] docs = hits.groups;
		for (int j = 0; j < docs.length; j++) {

			ScoreDoc[] sd = docs[j].scoreDocs;
//			for (int hit = 0; hit < sd.length; hit++) {
//				System.out.println("docid: " + sd[hit].doc);
//				Document d = searcher.doc(sd[0].doc);
//				System.out.println((j + 1) + ". " + d.get("propertyName")
//						+ "\t" + d.get("propertyValue"));
//			}

		}

	}

	public void luceneToChildJoinQuery(String search,
			CodingScheme scheme) throws IOException, ParseException {
		long start = System.currentTimeMillis();
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		
		  Filter codingScheme = new FixedBitSetCachingWrapperFilter(
                  new QueryWrapperFilter(new QueryParser("codingSchemeName", new StandardAnalyzer(new CharArraySet( 0, true))).parse(scheme.getCodingSchemeName())));

		  Query query = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("entityCode", search, Occur.MUST);
		  ToChildBlockJoinQuery termJoinQuery = new ToChildBlockJoinQuery(
				    query, 
				    codingScheme,
				   false);
		  searcher.search(termJoinQuery, collector);
		  ScoreDoc[] hits = collector.topDocs().scoreDocs;
		long time = System.currentTimeMillis() - start;
		System.out.println("\nFor codingScheme: " + scheme.codingSchemeName +  " psuedo entity code: " + search);
		System.out.println("Search Time To Child Join: " + time);
		  System.out.println("Found " + hits.length + " hits.");
//		  for(int i=0;i<hits.length;++i) {
//		      int docId = hits[i].doc;
//		      Document d = searcher.doc(docId);
////		      System.out.println((i + 1) + ". " + d.get("propertyName") + "\t" + d.get("propertyValue"));
//		  	}
		
	}
	
	
	public List<String> luceneToAllParentJoinQuery(SearchTerms term) throws IOException, ParseException{

		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		long start = System.currentTimeMillis();
		  Filter codingScheme = new FixedBitSetCachingWrapperFilter(
                  new QueryWrapperFilter(new QueryParser("parentDoc", new StandardAnalyzer(new CharArraySet( 0, true))).parse("yes")));

		  Query query = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("propertyValue", term.getTerm(), Occur.MUST);
		  ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
				    query, 
				    codingScheme,
				    ScoreMode.Total);
		  searcher.search(termJoinQuery, collector);

		  ScoreDoc[] hits = collector.topDocs().scoreDocs;
			long time = System.currentTimeMillis() - start;
			System.out.println("\n" + " term: " + term.getTerm());
			System.out.println("Search Time to Parent Join: " + time);
		  System.out.println("Found Parent Hits: " + hits.length + " hits.");
		  List<String> list = new ArrayList<String>();
//		  for(int i=0;i<hits.length;++i) {
//			  Document d = null;
//		      int docId = hits[i].doc;
//		      if(docId > 0){
//		      d = searcher.doc(docId);
//		      list.add(d.get("entityCode"));
////		      System.out.println((i + 1) + ". " + d.get("entityCode") + "\t" + d.get("entityDescription"));
//		      }
//		  	}
		  
		  return list;
	}

	public static void  main(String[] args){
		File path = new File("/Users/m029206/git/lexevs-lucene-prototype/index");
		Directory index = null;
		try {
			index = new MMapDirectory(path);
			LuceneQueryTrial trial = new LuceneQueryTrial(index);
			
			List<String> list =  trial.luceneToParentJoinQuery(SearchTerms.BLOOD, CodingScheme.THESSCHEME);
			trial.luceneToChildJoinQuery(list.get(0), CodingScheme.THESSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.BLOOD, CodingScheme.THESSCHEME);
			List<String> mudlist = trial.luceneToParentJoinQuery(SearchTerms.MUD, CodingScheme.THESSCHEME);
			trial.luceneToChildJoinQuery(mudlist.get(0), CodingScheme.THESSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.MUD, CodingScheme.THESSCHEME);
			List<String> charlist = trial.luceneToParentJoinQuery(SearchTerms.CHAR, CodingScheme.THESSCHEME);
			trial.luceneToChildJoinQuery(charlist.get(0), CodingScheme.THESSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.CHAR, CodingScheme.THESSCHEME);
			List<String> Articlelist =  trial.luceneToParentJoinQuery(SearchTerms.ARTICLE, CodingScheme.THESSCHEME);
			trial.luceneToChildJoinQuery(Articlelist.get(0), CodingScheme.THESSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.ARTICLE, CodingScheme.THESSCHEME);
			List<String> lunglist =  trial.luceneToParentJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.THESSCHEME);
			trial.luceneToChildJoinQuery(lunglist.get(0), CodingScheme.THESSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.THESSCHEME);
			List<String> liverlist =  trial.luceneToParentJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.THESSCHEME);
			trial.luceneToChildJoinQuery(liverlist.get(0), CodingScheme.THESSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.THESSCHEME);
			
			List<String> snolist =  trial.luceneToParentJoinQuery(SearchTerms.BLOOD, CodingScheme.SNOMEDSCHEME);
			trial.luceneToChildJoinQuery(snolist.get(0), CodingScheme.SNOMEDSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.BLOOD, CodingScheme.SNOMEDSCHEME);
			List<String> snomudlist = trial.luceneToParentJoinQuery(SearchTerms.MUD, CodingScheme.SNOMEDSCHEME);
			trial.luceneToChildJoinQuery(snomudlist.get(0), CodingScheme.SNOMEDSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.MUD, CodingScheme.SNOMEDSCHEME);
			List<String> snocharlist = trial.luceneToParentJoinQuery(SearchTerms.CHAR, CodingScheme.SNOMEDSCHEME);
			trial.luceneToChildJoinQuery(snocharlist.get(0), CodingScheme.SNOMEDSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.CHAR, CodingScheme.SNOMEDSCHEME);
			List<String> snoArticlelist =  trial.luceneToParentJoinQuery(SearchTerms.ARTICLE, CodingScheme.SNOMEDSCHEME);
			trial.luceneToChildJoinQuery(snoArticlelist.get(0), CodingScheme.SNOMEDSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.ARTICLE, CodingScheme.SNOMEDSCHEME);
			List<String> snolunglist =  trial.luceneToParentJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.SNOMEDSCHEME);
			trial.luceneToChildJoinQuery(snolunglist.get(0), CodingScheme.SNOMEDSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.SNOMEDSCHEME);
			List<String> snoliverlist =  trial.luceneToParentJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.SNOMEDSCHEME);
			trial.luceneToChildJoinQuery(snoliverlist.get(0), CodingScheme.SNOMEDSCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.SNOMEDSCHEME);
			
			List<String> metalist =  trial.luceneToParentJoinQuery(SearchTerms.BLOOD, CodingScheme.METASCHEME);
			trial.luceneToChildJoinQuery(metalist.get(0), CodingScheme.METASCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.BLOOD, CodingScheme.METASCHEME);
			List<String> metamudlist = trial.luceneToParentJoinQuery(SearchTerms.MUD, CodingScheme.METASCHEME);
			trial.luceneToChildJoinQuery(metamudlist.get(0), CodingScheme.METASCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.MUD, CodingScheme.METASCHEME);
			List<String> metacharlist = trial.luceneToParentJoinQuery(SearchTerms.CHAR, CodingScheme.METASCHEME);
			trial.luceneToChildJoinQuery(metacharlist.get(0), CodingScheme.METASCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.CHAR, CodingScheme.METASCHEME);
			List<String> metaArticlelist =  trial.luceneToParentJoinQuery(SearchTerms.ARTICLE, CodingScheme.METASCHEME);
			trial.luceneToChildJoinQuery(metaArticlelist.get(0), CodingScheme.METASCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.ARTICLE, CodingScheme.METASCHEME);
			List<String> metalunglist =  trial.luceneToParentJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.METASCHEME);
			trial.luceneToChildJoinQuery(metalunglist.get(0), CodingScheme.METASCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.METASCHEME);
			List<String> metaliverlist =  trial.luceneToParentJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.METASCHEME);
			trial.luceneToChildJoinQuery(metaliverlist.get(0), CodingScheme.METASCHEME);
			trial.luceneToParentGroupingJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.METASCHEME);
			
			trial.luceneToAllParentJoinQuery(SearchTerms.BLOOD);
			trial.luceneToAllParentJoinQuery(SearchTerms.ARTICLE);
			trial.luceneToAllParentJoinQuery(SearchTerms.CHAR);
			trial.luceneToAllParentJoinQuery(SearchTerms.CODE1);
			trial.luceneToAllParentJoinQuery(SearchTerms.CODE2);
			trial.luceneToAllParentJoinQuery(SearchTerms.CODE3);
			trial.luceneToAllParentJoinQuery(SearchTerms.MUD);
			trial.luceneToAllParentJoinQuery(SearchTerms.LIVER_CARCINOMA);
			trial.luceneToAllParentJoinQuery(SearchTerms.LUNG_CANCER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
