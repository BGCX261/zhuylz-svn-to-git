package dias.ibm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class CreateIndex {
	
	private Directory directory;
	private IndexWriterConfig config;
	
	private void init(File indexDir){
		
		try {
			directory = FSDirectory.open(indexDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		config = new IndexWriterConfig(Version.LUCENE_44, new StandardAnalyzer(Version.LUCENE_44));
		
		
		
	}
	
	/**
	 * 
	 * @param indexDir index directory
	 * @param dataDir  data directory
	 */
	public void Indexer(File indexDir, File dataDir){
		
		init(indexDir);
	    try {
			IndexWriter writer = new IndexWriter(directory, config);
			indexDir(writer, dataDir);	
			writer.forceMergeDeletes();
			writer.commit();
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void indexDir(IndexWriter writer, File dataDir){
		
		File[] files = dataDir.listFiles();
		
		for(int i = 0; i < files.length; i++){
			
			File f = (File)files[i];
			if(f.isDirectory()){
				indexDir(writer,f);
			}else{
				indexFile(writer, f);
			}
			
		}
		
	}
	
	
	private Document getDoc(File f){
		
		Document doc = new Document();
		try {
			FileInputStream fis = new FileInputStream(f);
			Reader reader = new InputStreamReader(fis);
			doc.add(new TextField("contents",reader));
			doc.add(new TextField("filepath", f.getPath(),Store.YES));
			doc.add(new TextField("filename", f.getName(), Store.YES));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	
	private void indexFile(IndexWriter writer, File f){
		
		if(f.isHidden() || !f.exists() || !f.canRead()){
			return;
		}
		
		Document doc = this.getDoc(f);
		try {
			writer.addDocument(doc);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
