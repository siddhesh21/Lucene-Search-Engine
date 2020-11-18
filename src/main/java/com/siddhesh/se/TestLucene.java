package com.siddhesh.se;

import com.siddhesh.se.CreateIndex;
import com.siddhesh.se.Searcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.search.similarities.Similarity;

import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;

public class TestLucene {
	//Storing of these files are under "Lucene-Search-Engine/"
	static String file_dir = "cran/cran.all.1400";
	static String query_dir = "cran/cran.qry";
	static String index_dir = "index";
	
	public static HashMap<Integer, HashMap<String, String>> file_reader(String dir) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(dir));
		String line=null;
		String previous_line = null;
        int count=0;
        HashMap<Integer, HashMap<String, String>> readable_docs = new HashMap<Integer, HashMap<String, String>>();
        while((line = br.readLine())!=null) {  
			String append_line;
			if(line.startsWith(".I")) {
				count++;
				previous_line=".I";
				readable_docs.put(count, new HashMap<String, String>());			
			}
			else if(line.startsWith(".A")) 
			{
				previous_line = ".A";
			}
			
			else if(line.startsWith(".T")) 
				{
						
				previous_line = ".T";
				
				}
			else if(line.startsWith(".B")) 
				{
				previous_line = ".B";
				
				}
			else if(line.startsWith(".W")) 
				{
					previous_line= ".W";
					}
			else {
				if(previous_line == ".A") {
					if(readable_docs.get(count).get("Author")!=null) {
						append_line = readable_docs.get(count).get("Author");
						readable_docs.get(count).put("Author", append_line+" "+line);}
						else{
							readable_docs.get(count).put("Author", line);}
							}
				
			else if(previous_line == ".T") {
					if(readable_docs.get(count).get("Title")!=null) {
						append_line = readable_docs.get(count).get("Title");
						readable_docs.get(count).put("Title", append_line+" "+line);
				}
					else {
					readable_docs.get(count).put("Title", line);
					}}
			else if (previous_line == ".B") {
					if(readable_docs.get(count).get("Bibliography")!=null) {
							append_line = readable_docs.get(count).get("Bibliography");
							readable_docs.get(count).put("Bibliography", append_line+" "+line);
						}
						else
						{
							readable_docs.get(count).put("Bibliography", line);
					}
					}
			else if (previous_line == ".W") {
				if(readable_docs.get(count).get("Text")!=null) {
					append_line = readable_docs.get(count).get("Text");
					readable_docs.get(count).put("Text", append_line+" "+line);
				}
				else
				{
					readable_docs.get(count).put("Text", line);
			}
				
			}		
				}
			};
			
		br.close();
		return readable_docs;
        }	
	public static HashMap<Integer, String> process_queries(String dir) throws IOException {
		// read in the query file 
		BufferedReader br = new BufferedReader(new FileReader(dir));
		String line=null;
		String previous_line = null;
        int count=0;
        HashMap<Integer, String> processed_query = new HashMap<Integer, String>();
		while((line = br.readLine())!=null) {  
			String append_line=null;  // or read from file
			if(line.startsWith(".I")) {
				count++;	
				previous_line = ".I";
			}
			else if(line.startsWith(".W")) 
			{
					previous_line= ".W";
				}
			else {
				if(previous_line == ".W") {
					if(processed_query.get(count)!=null) {
						append_line = processed_query.get(count);
						processed_query.put(count, append_line+" "+line);
					}
					else
						processed_query.put(count, line);
				}
			}
			}
		
		br.close();
		System.out.println("System has done parsing the file.");
		return processed_query;
	}
	public static Analyzer get_analzer(int num) {
		Analyzer analyzer = null;
		if (num == 1 ) {
			analyzer = new EnglishAnalyzer(EnglishAnalyzer.getDefaultStopSet());
		}
		else if(num == 2 ) {
			analyzer = new StandardAnalyzer();
		}
		else {
			analyzer = new SimpleAnalyzer();
		}
		
		return analyzer;
	}
	
	public static Similarity get_similarity (int num) {
		Similarity s = null;
		if( num ==1) {
			s = new BM25Similarity();
		}
		else{
			s = new ClassicSimilarity();
		}
		return s;
	}
	public static void main(String[] args) throws Exception {
		HashMap<Integer, HashMap<String, String>> processed_docs = file_reader(file_dir);
		HashMap<Integer, String> processed_qrys = process_queries(query_dir);
		

		Scanner sc= new Scanner(System.in);    //System.in is a standard input stream  
		System.out.print("\nExpecting choice of similarity measure as argument\n\n"
                + "Please run again with one of the following arguments:\n");
		System.out.print("1. English Analyzer\n2. Standard Analyzer\n3. Simple Analyzer\n");
		int analyzer_num= sc.nextInt(); 
		System.out.print("Choose the Scoring: \n");
		System.out.print("1. BM25\n2. Vector Space Model\n");
		int similarity_num= sc.nextInt(); 
		sc.close();
		
		Analyzer analyzer = get_analzer(analyzer_num);
		Similarity similarity = get_similarity(similarity_num);
		CreateIndex.get_index(processed_docs,index_dir,analyzer,similarity);
		Searcher.search(processed_qrys,index_dir,analyzer,similarity);
	}
}
