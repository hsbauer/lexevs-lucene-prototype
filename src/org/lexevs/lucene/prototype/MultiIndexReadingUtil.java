package org.lexevs.lucene.prototype;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.search.join.BitDocIdSetCachingWrapperFilter;
import org.apache.lucene.search.join.BitDocIdSetFilter;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToChildBlockJoinQuery;
import org.apache.lucene.search.join.ToParentBlockJoinCollector;
import org.apache.lucene.search.join.ToParentBlockJoinIndexSearcher;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.store.MMapDirectory;

public class MultiIndexReadingUtil {
	ConcurrentHashMap<String, MapNameVersion> indexes = null;
	ToParentBlockJoinIndexSearcher searcher;
	StringBuilder output;
	int hitsPerPage = 50;
	
	public MultiIndexReadingUtil(ConcurrentHashMap<String, MapNameVersion> indexes) throws IOException {
		searcher = readIndexs(indexes);
		output = new StringBuilder();
	}

	public ToParentBlockJoinIndexSearcher readIndexs(ConcurrentHashMap<String, MapNameVersion> indexes) throws IOException{

		String baseURI = "/Users/m029206/git/lexevs-lucene-prototype/";
		
		IndexReader iR1 = DirectoryReader.open(new MMapDirectory(Paths.get(baseURI + "SNOMEDScheme")));
		IndexReader iR2 = DirectoryReader.open(new MMapDirectory(Paths.get(baseURI + "TheScheme")));
		IndexReader iR3 = DirectoryReader.open(new MMapDirectory(Paths.get(baseURI + "NCIMetaCodingScheme")));
		IndexReader iR4 = DirectoryReader.open(new MMapDirectory(Paths.get(baseURI + "YetAnotherScheme")));
		IndexReader iR5 = DirectoryReader.open(new MMapDirectory(Paths.get(baseURI + "AScheme")));
		IndexReader iR6 = DirectoryReader.open(new MMapDirectory(Paths.get(baseURI + "OkCodingScheme")));
		IndexReader iR7 = DirectoryReader.open(new MMapDirectory(Paths.get(baseURI + "AnotherScheme")));
		IndexReader iR8 = DirectoryReader.open(new MMapDirectory(Paths.get(baseURI + "ThesScheme")));
		IndexReader iR9 = DirectoryReader.open(new MMapDirectory(Paths.get(baseURI + "TestScheme")));

		MultiReader mr = new MultiReader(iR1, iR2, iR3, iR4, iR5, iR6, iR7, iR8, iR9);
	     return new ToParentBlockJoinIndexSearcher(mr);
	}
	
	public List<String> luceneToParentJoinQuery(SearchTerms term, CodingScheme scheme) throws IOException, ParseException{
		List<String> list = null;
		//TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		ToParentBlockJoinCollector collector = new ToParentBlockJoinCollector(Sort.RELEVANCE, 10, true, true);
//		  Query queryWarmup = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("propertyValue", term.getTerm(), Occur.MUST);
//		searcher.search(queryWarmup , collector);
		long start = System.currentTimeMillis();
//		BitDocIdSetFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
//                  new QueryWrapperFilter(new QueryParser("codingSchemeName", new StandardAnalyzer(new CharArraySet( 0, true))).parse(scheme.getCodingSchemeName())));
		BitDocIdSetFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
	              new QueryWrapperFilter(new QueryParser("parentDoc", new StandardAnalyzer(new CharArraySet( 0, true))).parse("yes")));
		  BooleanQuery booleanQuery = new BooleanQuery();
		  Query query = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("propertyValue", term.getTerm(), Occur.MUST);
		  ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
				    query, 
				    codingScheme,
				    ScoreMode.Avg);
		  Query query1 = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("codingSchemeName", scheme.getCodingSchemeName());
		  booleanQuery.add(query1, BooleanClause.Occur.MUST);
		  booleanQuery.add(termJoinQuery, BooleanClause.Occur.MUST);
		  searcher.search(booleanQuery, collector);

		  TopGroups<Integer> getTopGroupsResults = collector.getTopGroups(termJoinQuery, null, 0, 10, 0, true);
		  String ecode = searcher.doc(getTopGroupsResults.groups[0].groupValue).getField("entityCode").stringValue();
		  if(getTopGroupsResults != null){
		  for (GroupDocs<Integer> result : getTopGroupsResults.groups) {
			  final GroupDocs<Integer> group = result;
			  Document parent = searcher.doc(result.groupValue);
			  Document d =	searcher.doc(result.scoreDocs[0].doc);
					 System.out.println("Property Value: " + d.getField("propertyValue").stringValue());
					 System.out.println("Entity Code: " + d.getField("ecode").stringValue());
					 System.out.println("Parent Entity Code: " + parent.getField("entityCode").stringValue() );
					 System.out.println("Coding Scheme URI: " + parent.getField("codingSchemeId").stringValue());
					 System.out.println("Coding Scheme Name: " + parent.getField("codingSchemeName").stringValue());
					 System.out.println("Coding Scheme Version : " + parent.getField("codingSchemeVersion").stringValue() + "\n");
					
		  }
			long time = System.currentTimeMillis() - start;
		  
			System.out.println("\nFor codingScheme: " + scheme.codingSchemeName +  " term: " + term.getTerm());
			System.out.println("Search Time to Parent Join: " + time);
		  System.out.println("Found Parent Hits: " + getTopGroupsResults.totalGroupCount + " hits.");
		  list = new ArrayList<String>();
		  list.add(ecode);
		  output.append(scheme.getCodingSchemeName() + "," + term.getTerm() + "," + "ToParentJoinParsedQuery" + ","+ "time in milliseconds: " + time + "," + "results: " + getTopGroupsResults.groups.length + "\n" );
		  }else {
			  long time = System.currentTimeMillis() - start;
			  
				System.out.println("\nFor codingScheme: " + scheme.codingSchemeName +  " term: " + term.getTerm());
				System.out.println("Search Time to Parent Join: " + time);
			  System.out.println("Found Parent Hits: " + 0 + " hits.");
			  list = new ArrayList<String>();
			  list.add(ecode);
			  output.append(scheme.getCodingSchemeName() + "," + term.getTerm() + "," + "ToParentJoinParsedQuery" + ","+ "time in milliseconds: " + time + "," + "results: " + 0 + "\n" );
		  }
		  return list;
	}
	
	public List<String> luceneToParentJoinStartsWithQuery(SearchTerms term, CodingScheme scheme) throws IOException, ParseException{

		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		long start = System.currentTimeMillis();
//		  BitDocIdSetCachingWrapperFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
//                  new QueryWrapperFilter(new QueryParser("codingSchemeName", new StandardAnalyzer(new CharArraySet( 0, true))).parse(scheme.getCodingSchemeName())));
		  BitDocIdSetFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
		              new QueryWrapperFilter(new QueryParser("parentDoc", new StandardAnalyzer(new CharArraySet( 0, true))).parse("yes")));
		  BooleanQuery booleanQuery = new BooleanQuery();
//		  bquery.add(new TermQuery(new Term("propertyValue", term.getTerm() + "*")), BooleanClause.Occur.MUST);
		  Query query = new QueryParser(null, new KeywordAnalyzer()).createBooleanQuery("propertyValue", term.getTerm().toLowerCase(), Occur.MUST);
		  ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
				    query, 
				    codingScheme,
				    ScoreMode.Total);
		  Query query1 = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("codingSchemeName", scheme.getCodingSchemeName());
		  booleanQuery.add(query1, BooleanClause.Occur.MUST);
		  booleanQuery.add(termJoinQuery, BooleanClause.Occur.MUST);
		  searcher.search(booleanQuery, collector);
//		  searcher.search(termJoinQuery, collector);

		  ScoreDoc[] hits = collector.topDocs().scoreDocs;

			String ecode = null;
			if(hits.length > 0){
			ecode = searcher.doc(hits[0].doc).get("entityCode");
			}
			long time = System.currentTimeMillis() - start;
			System.out.println("\nSearch Time to Parent Join: " + time);
			System.out.println("For codingScheme: " + scheme.codingSchemeName +  " Starts with term: " + term.getTerm().toLowerCase());
			System.out.println("Found entitycode: " + ecode);
			System.out.println("Found Parent Hits: " + hits.length + " hits.");
		  List<String> list = new ArrayList<String>();
		  list.add(ecode);	
		  output.append(scheme.getCodingSchemeName() + "," + term.getTerm() + "," + "ToParentJoinStartsWithQuery" + ","+ "time in milliseconds: " + time + "," + "results: " + hits.length + "\n" );
		  return list;
	}
	

	public List<String> luceneToParentJoinExactMatchQuery(SearchTerms term, CodingScheme scheme) throws IOException, ParseException{

		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		long start = System.currentTimeMillis();
//		  BitDocIdSetCachingWrapperFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
//                  new QueryWrapperFilter(new QueryParser("codingSchemeName", new StandardAnalyzer(new CharArraySet( 0, true))).parse(scheme.getCodingSchemeName())));
		  BitDocIdSetFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
	              new QueryWrapperFilter(new QueryParser("parentDoc", new StandardAnalyzer(new CharArraySet( 0, true))).parse("yes")));
		  BooleanQuery booleanQuery = new BooleanQuery();
//		  bquery.add(new TermQuery(new Term("propertyValue", term.getTerm())), BooleanClause.Occur.MUST);
		  Query query = new QueryParser(null, new KeywordAnalyzer()).createBooleanQuery("propertyValue", term.getTerm().toLowerCase(), Occur.MUST);
		  ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
				    query, 
				    codingScheme,
				    ScoreMode.Total);
		  Query query1 = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("codingSchemeName", scheme.getCodingSchemeName());
		  booleanQuery.add(query1, BooleanClause.Occur.MUST);
		  booleanQuery.add(termJoinQuery, BooleanClause.Occur.MUST);
		  searcher.search(booleanQuery, collector);
//		  searcher.search(termJoinQuery, collector);

		  ScoreDoc[] hits = collector.topDocs().scoreDocs;

			String ecode = null;
			if(hits.length > 0){
			ecode = searcher.doc(hits[0].doc).get("entityCode");
			}
			long time = System.currentTimeMillis() - start;
			System.out.println("\nSearch Time to Parent Join: " + time);
			System.out.println("For codingScheme: " + scheme.codingSchemeName +  " Exact Match term: " + term.getTerm().toLowerCase());
		  System.out.println("Found Parent Hits: " + hits.length + " hits.");
		  List<String> list = new ArrayList<String>();
		  list.add(ecode);		 
		  output.append(scheme.getCodingSchemeName() + "," + term.getTerm() + "," + "ToParentExactQuery" + ","+ "time in milliseconds: " + time + "," + "results: " + hits.length + "\n" );
		  return list;
	}
	


	public void luceneToParentGroupingJoinQuery(SearchTerms term,
			CodingScheme scheme) throws IOException, ParseException {
		ToParentBlockJoinCollector collector = new ToParentBlockJoinCollector(
				Sort.RELEVANCE, // sort
				hitsPerPage, // numHits
				true, // trackScores
				true // trackMaxScore
		);
		long start = System.currentTimeMillis();
//		BitDocIdSetCachingWrapperFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
//				new QueryWrapperFilter(new QueryParser("codingSchemeName",
//						new StandardAnalyzer(new CharArraySet(0, true)))
//						.parse(scheme.getCodingSchemeName())));
		BitDocIdSetFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
	              new QueryWrapperFilter(new QueryParser("parentDoc", new StandardAnalyzer(new CharArraySet( 0, true))).parse("yes")));
		  BooleanQuery booleanQuery = new BooleanQuery();
		Query query = new QueryParser(null, new StandardAnalyzer(
				new CharArraySet(0, true))).createBooleanQuery("propertyValue",
				term.getTerm(), Occur.MUST);
		ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
				query, codingScheme, ScoreMode.Max);
		  Query query1 = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("codingSchemeName", scheme.getCodingSchemeName());
		  booleanQuery.add(query1, BooleanClause.Occur.MUST);
		  booleanQuery.add(termJoinQuery, BooleanClause.Occur.MUST);
		  searcher.search(booleanQuery, collector);
//		searcher.search(termJoinQuery, collector);

		TopGroups<Integer> hits = collector.getTopGroupsWithAllChildDocs(termJoinQuery,
				Sort.RELEVANCE, 
				0, // offset
//				hitsPerPage, // maxDocsPerGroup
				0, // withinGroupOffset
				true // fillSortFields
				);
//		for(SortField sf : hits.groupSort){
//			System.out.println(sf.getField());
//		}
		GroupDocs<Integer>[] docs = null;
		if(hits != null){
		docs = hits.groups;
		}
		else{
			System.out.println("No values returned for: " + term.getTerm());
		}
//		Document d = null;
		if(docs != null){
			for(GroupDocs gd: docs){
			 Document d =	searcher.doc(gd.scoreDocs[0].doc);
			 System.out.println("Property Value: " + d.getField("propertyValue").stringValue());
			 System.out.println("Entity Code: " + d.getField("ecode").stringValue());
			 Document parent = searcher.doc((Integer)gd.groupValue);
			 System.out.println("Parent Entity Code: " + parent.getField("entityCode").stringValue() );
			 System.out.println("Coding Scheme URI: " + parent.getField("codingSchemeId").stringValue());
			 System.out.println("Coding Scheme Name: " + parent.getField("codingSchemeName").stringValue());
			 System.out.println("Coding Scheme Version : " + parent.getField("codingSchemeVersion").stringValue() + "\n");
			}
//		 d = searcher.doc(docs[0].scoreDocs[0].doc);
		}else{
			System.out.println("No docs found for term: " + term.getTerm());
			return;
		}
		long time = System.currentTimeMillis() - start;
		System.out.println("\nFor codingScheme: " + scheme.codingSchemeName +  " term: " + term.getTerm());
		System.out.println("Search Time To Parent Group Join: " + time);
		System.out.println("Found " + 
		hits.totalHitCount 
		+ " hits.");
		System.out.println("Found " + hits.totalGroupedHitCount + " hits.");
		System.out.println("Found " + hits.totalGroupCount.toString()
				+ " hits.\n");
		 output.append(scheme.getCodingSchemeName() + "," + term.getTerm() + "," + "ToParentGroupJoinParsedQuery" + ","+ "time in milliseconds: " + time + "," + "results: " + hits.totalGroupCount + "\n" );
	}

	public void luceneToChildJoinQuery(String search,
			CodingScheme scheme) throws IOException, ParseException {
		long start = System.currentTimeMillis();
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		
		  BitDocIdSetCachingWrapperFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
                  new QueryWrapperFilter(new QueryParser("parentDoc", new StandardAnalyzer(new CharArraySet( 0, true))).parse("yes")));
		  Query query = null;
		  if(search != null){
		  query = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("entityCode", search, Occur.MUST);
		  }else{
			  System.out.println("No values available for child search");
			  return;
		  }
		  ToChildBlockJoinQuery termJoinQuery = new ToChildBlockJoinQuery(
				    query, 
				    codingScheme);
		  searcher.search(termJoinQuery, collector);
		  ScoreDoc[] hits = collector.topDocs().scoreDocs;
		  for(ScoreDoc sd : hits){
			  Document d = searcher.doc(sd.doc);
			  System.out.println("child entity code: " + d.get("ecode"));
			  System.out.println("child propertyValue: " + d.get("propertyValue"));
		  }
		  if(hits != null && hits.length > 0){searcher.doc(hits[0].doc);}
		long time = System.currentTimeMillis() - start;
		System.out.println("\nFor codingScheme: " + scheme.codingSchemeName +  " psuedo entity code: " + search);
		System.out.println("Search Time To Child Join: " + time);
		  System.out.println("Found " + hits.length + " hits.");
		  output.append(scheme.getCodingSchemeName() + "," + "entityCode: "  + search + "," + "ToChildJoinParsedQuery" + ","+ "time in milliseconds: " + time + "," + "results: " + hits.length + "\n" );
	}
	
	
	public List<String> luceneToAllParentJoinQuery(SearchTerms term) throws IOException, ParseException{

		TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
		long start = System.currentTimeMillis();
		  BitDocIdSetCachingWrapperFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
                  new QueryWrapperFilter(new QueryParser("parentDoc", new StandardAnalyzer(new CharArraySet( 0, true))).parse("yes")));

		  Query query = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("propertyValue", term.getTerm(), Occur.MUST);
		  ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
				    query, 
				    codingScheme,
				    ScoreMode.Total);
		  searcher.search(termJoinQuery, collector);

		  ScoreDoc[] hits = collector.topDocs().scoreDocs;
		  Document d = null;
		  if(hits != null && hits.length > 0)
		   {d = searcher.doc(hits[0].doc);}
			long time = System.currentTimeMillis() - start;
			System.out.println("\n" + " term: " + term.getTerm());
			System.out.println("Search Time to Parent Join: " + time);
		  System.out.println("Found Parent Hits: " + hits.length + " hits.");
		  List<String> list = new ArrayList<String>();
		  list.add(d.get("entityCode"));
		  output.append("All Schemes" + "," + term.getTerm() + "," + "ToParentJoinParsedQuery" + ","+ "time in milliseconds: " + time + "," + "results: " + hits.length + "\n" );
		  return list;
	}
	
	public List<String> luceneToAllParentExactMatchQuery(SearchTerms term) throws IOException, ParseException{

		TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
		long start = System.currentTimeMillis();
		  BitDocIdSetCachingWrapperFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
                  new QueryWrapperFilter(new QueryParser("parentDoc", new StandardAnalyzer(new CharArraySet( 0, true))).parse("yes")));

		  Query query = new QueryParser(null, new KeywordAnalyzer()).createBooleanQuery("propertyValue", term.getTerm().toLowerCase(), Occur.MUST);
		  ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
				    query, 
				    codingScheme,
				    ScoreMode.Total);
		  searcher.search(termJoinQuery, collector);

		  ScoreDoc[] hits = collector.topDocs().scoreDocs;
		  Document d = null;
		  if(hits != null && hits.length > 0)
		   {d = searcher.doc(hits[0].doc);}
			long time = System.currentTimeMillis() - start;
			System.out.println("\n" + " term: " + term.getTerm());
			System.out.println("Search Time to Parent Join: " + time);
		  System.out.println("Found Parent Hits: " + hits.length + " hits.");
		  List<String> list = new ArrayList<String>();
		  output.append("All Schemes" + "," + term.getTerm() + "," + "ToParentJoinExactMatchQuery" + ","+ "time in milliseconds: " + time + "," + "results: " + hits.length + "\n" );
		  return list;
	}
	
	public void stringBuilderToFile() throws IOException{
		File file = new File("output.csv");
		 BufferedWriter writer = null;
		try {
		   writer = new BufferedWriter(new FileWriter(file));
		    writer.append(output);
		} finally {
		    if (writer != null) writer.close();
		}
	}
	
	public static void main(String[] args) {
		try {
			
			MultiIndexReadingUtil trial = new MultiIndexReadingUtil(null);
			
			List<String> list =  trial.luceneToParentJoinQuery(SearchTerms.BLOOD, CodingScheme.THESSCHEME);
			trial.luceneToChildJoinQuery(list.get(0), CodingScheme.THESSCHEME);
			List<String> list1 =  trial.luceneToParentJoinQuery(SearchTerms.BLOOD, CodingScheme.THESSCHEME);
			trial.luceneToChildJoinQuery(list1.get(0), CodingScheme.THESSCHEME);
			
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.BLOOD, CodingScheme.THESSCHEME);
//			List<String> mudlist = trial.luceneToParentJoinQuery(SearchTerms.MUD, CodingScheme.THESSCHEME);
//			trial.luceneToChildJoinQuery(mudlist.get(0), CodingScheme.THESSCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.MUD, CodingScheme.THESSCHEME);
//			List<String> charlist = trial.luceneToParentJoinQuery(SearchTerms.CHAR, CodingScheme.THESSCHEME);
//			trial.luceneToChildJoinQuery(charlist.get(0), CodingScheme.THESSCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.CHAR, CodingScheme.THESSCHEME);
//			List<String> Articlelist =  trial.luceneToParentJoinQuery(SearchTerms.ARTICLE, CodingScheme.THESSCHEME);
//			trial.luceneToChildJoinQuery(Articlelist.get(0), CodingScheme.THESSCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.ARTICLE, CodingScheme.THESSCHEME);
//			List<String> lunglist =  trial.luceneToParentJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.THESSCHEME);
//			trial.luceneToChildJoinQuery(lunglist.get(0), CodingScheme.THESSCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.THESSCHEME);
//			List<String> liverlist =  trial.luceneToParentJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.THESSCHEME);
//			trial.luceneToChildJoinQuery(liverlist.get(0), CodingScheme.THESSCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.THESSCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.BLOOD, CodingScheme.THESSCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.ARTICLE, CodingScheme.THESSCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.LUNG_CANCER, CodingScheme.THESSCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.CHAR, CodingScheme.THESSCHEME);
//			trial.luceneToParentJoinExactMatchQuery(SearchTerms.BLOOD, CodingScheme.THESSCHEME);
//			trial.luceneToParentJoinExactMatchQuery(SearchTerms.CHAR, CodingScheme.THESSCHEME);
//			System.out.println(trial.output.toString());
//			
//			List<String> snolist =  trial.luceneToParentJoinQuery(SearchTerms.BLOOD, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToChildJoinQuery(snolist.get(0), CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.BLOOD, CodingScheme.SNOMEDSCHEME);
//			List<String> snomudlist = trial.luceneToParentJoinQuery(SearchTerms.MUD, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToChildJoinQuery(snomudlist.get(0), CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.MUD, CodingScheme.SNOMEDSCHEME);
//			List<String> snocharlist = trial.luceneToParentJoinQuery(SearchTerms.CHAR, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToChildJoinQuery(snocharlist.get(0), CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.CHAR, CodingScheme.SNOMEDSCHEME);
//			List<String> snoArticlelist =  trial.luceneToParentJoinQuery(SearchTerms.ARTICLE, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToChildJoinQuery(snoArticlelist.get(0), CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.ARTICLE, CodingScheme.SNOMEDSCHEME);
//			List<String> snolunglist =  trial.luceneToParentJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToChildJoinQuery(snolunglist.get(0), CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.SNOMEDSCHEME);
//			List<String> snoliverlist =  trial.luceneToParentJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToChildJoinQuery(snoliverlist.get(0), CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.BLOOD, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.ARTICLE, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.LUNG_CANCER, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.CHAR, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentJoinExactMatchQuery(SearchTerms.BLOOD, CodingScheme.SNOMEDSCHEME);
//			trial.luceneToParentJoinExactMatchQuery(SearchTerms.CHAR, CodingScheme.SNOMEDSCHEME);
//			
//			List<String> metalist =  trial.luceneToParentJoinQuery(SearchTerms.BLOOD, CodingScheme.METASCHEME);
//			trial.luceneToChildJoinQuery(metalist.get(0), CodingScheme.METASCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.BLOOD, CodingScheme.METASCHEME);
//			List<String> metamudlist = trial.luceneToParentJoinQuery(SearchTerms.MUD, CodingScheme.METASCHEME);
//			trial.luceneToChildJoinQuery(metamudlist.get(0), CodingScheme.METASCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.MUD, CodingScheme.METASCHEME);
//			List<String> metacharlist = trial.luceneToParentJoinQuery(SearchTerms.CHAR, CodingScheme.METASCHEME);
//			trial.luceneToChildJoinQuery(metacharlist.get(0), CodingScheme.METASCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.CHAR, CodingScheme.METASCHEME);
//			List<String> metaArticlelist =  trial.luceneToParentJoinQuery(SearchTerms.ARTICLE, CodingScheme.METASCHEME);
//			trial.luceneToChildJoinQuery(metaArticlelist.get(0), CodingScheme.METASCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.ARTICLE, CodingScheme.METASCHEME);
//			List<String> metalunglist =  trial.luceneToParentJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.METASCHEME);
//			trial.luceneToChildJoinQuery(metalunglist.get(0), CodingScheme.METASCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.LUNG_CANCER, CodingScheme.METASCHEME);
//			List<String> metaliverlist =  trial.luceneToParentJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.METASCHEME);
//			trial.luceneToChildJoinQuery(metaliverlist.get(0), CodingScheme.METASCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.METASCHEME);
//			trial.luceneToParentGroupingJoinQuery(SearchTerms.LIVER_CARCINOMA, CodingScheme.METASCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.BLOOD, CodingScheme.METASCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.ARTICLE, CodingScheme.METASCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.LUNG_CANCER, CodingScheme.METASCHEME);
//			trial.luceneToParentJoinStartsWithQuery(SearchTerms.CHAR, CodingScheme.METASCHEME);
//			trial.luceneToParentJoinExactMatchQuery(SearchTerms.BLOOD, CodingScheme.METASCHEME);
//			trial.luceneToParentJoinExactMatchQuery(SearchTerms.CHAR, CodingScheme.METASCHEME);
//			
//			
//			trial.luceneToAllParentJoinQuery(SearchTerms.BLOOD);
//			trial.luceneToAllParentJoinQuery(SearchTerms.ARTICLE);
//			trial.luceneToAllParentJoinQuery(SearchTerms.CHAR);
//			trial.luceneToAllParentExactMatchQuery(SearchTerms.CODE1);
//			trial.luceneToAllParentExactMatchQuery(SearchTerms.CODE2);
//			trial.luceneToAllParentExactMatchQuery(SearchTerms.CODE3);
//			trial.luceneToAllParentJoinQuery(SearchTerms.MUD);
//			trial.luceneToAllParentJoinQuery(SearchTerms.LIVER_CARCINOMA);
//			trial.luceneToAllParentJoinQuery(SearchTerms.LUNG_CANCER);
			
//			System.out.println(trial.output.toString());
			trial.stringBuilderToFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}