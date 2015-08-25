package dias.ibm.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneUtils {
	
	private static Directory directory;
	private static IndexWriter writer;
	private static IndexWriterConfig config;
	
	/**
	 * 
	 * @param idxPath : the path for document index
	 * @return
	 * @throws IOException
	 */
	public static IndexWriter getIndexWriter(String idxPath) throws IOException{
		
	    directory = FSDirectory.open(new File(idxPath));
		
		config = new IndexWriterConfig(Version.LUCENE_44, new StandardAnalyzer(Version.LUCENE_44));
		
		writer = new IndexWriter(directory, config);
		
		return writer;
	}
	
	
	public static List<Document> getSearchRes(String idxDir, String queryStr){
		
		try {
			Directory dir = FSDirectory.open(new File(idxDir));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			Query query = new TermQuery(new Term("contents", queryStr));
			
			TopDocs topDoc = searcher.search(query, 10);
			
			ScoreDoc[] docs = topDoc.scoreDocs;
			
			List<Document> documents = new ArrayList<Document>();
			
			for(ScoreDoc doc : docs){
				
				Document document = searcher.doc(doc.doc);
				documents.add(document);
			}
			
			return documents;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	/**
	 * 
	 * @param term
	 */
	public static void delDoc(IndexWriter writer, Term term){
		
		try {
			writer.deleteDocuments(term);
			writer.commit();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void delAll(String idxPath){
		
		try {
			IndexWriter writer = getIndexWriter(idxPath);
			writer.deleteAll();
			writer.commit();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
