package dias.ibm.util;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexSearch {
	

	public void searcher(String indexDir,String queryField, String queryStr){
		
		try {
			Directory directory = FSDirectory.open(new File(indexDir));
			IndexReader reader = DirectoryReader.open(directory);
			
			IndexSearcher searcher = new IndexSearcher(reader);
			
			Query query = new TermQuery(new Term(queryField, queryStr));
			
			TopDocs topdoc = searcher.search(query, 10);
			
			ScoreDoc[] sds = topdoc.scoreDocs;
			
			for(ScoreDoc doc: sds){
				
				Document document = searcher.doc(doc.doc);
				System.out.println(document.getField("filepath"));
				System.out.println(document.getField("filename"));
				
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
