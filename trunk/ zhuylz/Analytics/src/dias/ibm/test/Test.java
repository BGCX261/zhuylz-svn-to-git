package dias.ibm.test;

import java.io.File;





import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.ScoreDoc;

import dias.ibm.util.CreateIndex;
import dias.ibm.util.IndexSearch;
import dias.ibm.util.LuceneUtils;
import junit.framework.TestCase;

public class Test extends TestCase {

	/**
	 * test if can test create index
	 */
	public void testCreIdx(){
	 
	   String dataDir = "C:\\study\\Lucene\\Data";
	   String idxDir = "C:\\study\\Lucene\\Index";
	   
	   LuceneUtils.delAll(idxDir);
	   
	   CreateIndex ci = new CreateIndex();
	   
	   ci.Indexer(new File(idxDir), new File(dataDir));
	   
		
	}
	
	
	public void testSearcher(){
		
		String idxDir = "C:\\study\\Lucene\\Index";
		
		IndexSearch is = new IndexSearch();
		
		String queryField = "contents";
		String queryStr = "document";
		
		List<Document> docs = LuceneUtils.getSearchRes(idxDir, "lucene");
		
		for(Document doc : docs){
			
			System.out.println(doc.getField("filepath") + " " + doc.getField("filename"));
			
		}
		
	}
	
	public void delIndex(){
		
		Term term = new Term("contents", "lucene");
		String idxDir = "C:\\study\\Lucene\\Index";
		
		IndexWriter writer;
		try {
			writer = LuceneUtils.getIndexWriter(idxDir);
			LuceneUtils.delDoc(writer, term);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
}
