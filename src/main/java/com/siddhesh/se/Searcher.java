package com.siddhesh.se;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileWriter;   
import java.io.IOException;  
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
	public static File file_maker() {
		try {
			  // Storing indexed query result into "Lucene-Search-Engine/src/results" path.
		      File myObj = new File("src/results.txt");
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		      return myObj;
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return null;
	}
	public static void search(HashMap<Integer, String> queries, String index_dir, Analyzer analyzer, Similarity similarity) throws Exception {
		System.out.println(queries.size());
		// Index reader reads from the index at 'directory' and Searcher takes the index reader
	    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(index_dir))));
	    searcher.setSimilarity(similarity);
	    
	    //Analyzer analyzer = new EnglishAnalyzer(EnglishAnalyzer.getDefaultStopSet());
	    //Analyzer std_analyzer = new StandardAnalyzer();
	    //Analyzer simple_analyzer = new SimpleAnalyzer();
	   
	    //Similarity s = new BM25Similarity();
	    //Similarity s = new ClassicSimilarity();
	    
	    HashMap<String, Float> score_booster = new HashMap<String, Float>();
	    score_booster.put("Title", 0.65f);
	    score_booster.put("Author", 0.04f);
	    score_booster.put("Bibliography", 0.02f);
	    score_booster.put("Text", 0.35f);
	    MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
			new String[] {"Title", "Author", "Bibliography", "Text"},
				analyzer, score_booster);
	    File newFile = file_maker();
	    FileWriter myWriter = new FileWriter(newFile);
	    for (Map.Entry<Integer, String> q : queries.entrySet()) {
	    	
	    	String qry = q.getValue();
	    	//System.out.println();
			Query query = queryParser.parse(QueryParser.escape(qry)); // parsing because we need int from string
	          
	        TopDocs topDocs = searcher.search(query, 1000);
	        ScoreDoc[] hits = topDocs.scoreDocs;
	        
	        for(int j = 0; j < hits.length; j++) // hits.length seems to always report a value = 10
	        {	        	
	        	Document docc = searcher.doc(hits[j].doc);
	        	int s = Integer.parseInt(docc.get("Id"));   
	        	//System.out.println(s);
	        	// This string needs to be written to a trec_eval_results file ^^^
	        	System.out.println((q.getKey()) +" 0 " + s + " " + (j+1) + " " + hits[j].score);
				myWriter.write((q.getKey()) +" 0 " + s + " " + (j+1) + " " + hits[j].score +" STANDARD\n");
			}
	      }
	    System.out.println("Results File successfully created.");
	    myWriter.close();
	  }
}
