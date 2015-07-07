package org.lexevs.lucene.prototype;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.search.join.BitDocIdSetCachingWrapperFilter;
import org.apache.lucene.search.join.BitDocIdSetFilter;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinCollector;
import org.apache.lucene.search.join.ToParentBlockJoinIndexSearcher;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;

public class BlockJoinTestQuery {

	public BlockJoinTestQuery() {
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
	Path path = Paths.get("/Users/m029206/Desktop/index");
	Directory index;
	try {
		index = new MMapDirectory(path);

	IndexReader reader =  DirectoryReader.open(index);
	IndexSearcher searcher = new ToParentBlockJoinIndexSearcher(reader);
	ToParentBlockJoinCollector collector = new ToParentBlockJoinCollector(Sort.RELEVANCE, 20, true, true);
	BitDocIdSetFilter codingScheme = new BitDocIdSetCachingWrapperFilter(
              new QueryWrapperFilter(new QueryParser("parentDoc", new StandardAnalyzer(new CharArraySet( 0, true))).parse("yes")));
	  BooleanQuery booleanQuery = new BooleanQuery();
//	  booleanQuery.add(new TermQuery(new Term("propertyValue", "Blood")), BooleanClause.Occur.MUST);
//	  booleanQuery.add(new TermQuery(new Term("codingSchemeName", "TestScheme")), BooleanClause.Occur.MUST);
	  Query query = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("propertyValue", "Blood");
//	  Query query = parser.parse(booleanQuery.toString());
	  ToParentBlockJoinQuery termJoinQuery = new ToParentBlockJoinQuery(
			    query, 
			    codingScheme,
			    ScoreMode.Avg);
	  Query query1 = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true))).createBooleanQuery("codingSchemeName", "TestScheme");
	  booleanQuery.add(query1, BooleanClause.Occur.MUST);
	  booleanQuery.add(termJoinQuery, BooleanClause.Occur.MUST);
//	  BooleanQuery booleanQueryForParent = new BooleanQuery();
//	  booleanQuery.add(new TermQuery(new Term("propertyValue", "Blood")), BooleanClause.Occur.MUST);
//	  booleanQueryForParent.add(new TermQuery(new Term("codingSchemeName", "TestScheme")), BooleanClause.Occur.MUST);
	  
//	  QueryParser parser1 = new QueryParser(null, new StandardAnalyzer(new CharArraySet( 0, true)));


	  
//	  booleanQueryForParent.add(termJoinQuery, BooleanClause.Occur.MUST);
//	  Query query1 = parser1.parse(booleanQueryForParent.toString());
	  searcher.search(booleanQuery, collector);
	  TopGroups<Integer> getTopGroupsResults = collector.getTopGroups(termJoinQuery, null, 0, 10, 0, true);
	  String ecode = null;
	  String scheme = null;
	  for (GroupDocs<Integer> result : getTopGroupsResults.groups) {
		  Document parent = searcher.doc(result.groupValue);
		 ecode = parent.get("entityCode");
		 scheme = parent.get("codingSchemeName");
		 System.out.println("\nentityCode: " + ecode);
		 System.out.println("scheme name: " + scheme);
	  }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public static void main(String[] args) {
		new BlockJoinTestQuery().run();

	}

}
