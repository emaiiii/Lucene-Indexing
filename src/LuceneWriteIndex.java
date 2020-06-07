import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.packed.PackedLongValues.Iterator;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.*;

public class LuceneWriteIndex {

	// directory to files
	private static final String INDEX_DIR = "C:\\lucene\\indexes";
	private static final String JSON_FILE_PATH = "\\convertedtweets";
	
	 
    public static void main(String[] args) throws Exception 
    {
    	JSONArray arrayObject = new JSONArray();
    	List<Document> docs = new ArrayList<>();
    	
    	// traverse through all 12 files from convertedtweets directory
    	for(int index = 1; index <= 12; index++) {
    		IndexWriter indexWriter = createWriter(index);
    		arrayObject = parseJSON(index);

    		docs = createDocument(arrayObject);
    		
    		indexWriter.deleteAll();
    		
    		indexWriter.addDocuments(docs);
    		indexWriter.commit();
    		indexWriter.close();
    	}
    	
    	
        /*IndexWriter writer = createWriter();
        List<Document> documents = new ArrayList<>();
         
        Document document1 = createDocument(1, "Lokesh", "Gupta", "howtodoinjava.com");
        documents.add(document1);
         
        Document document2 = createDocument(2, "Brian", "Schultz", "example.com");
        documents.add(document2);
         
        writer.deleteAll();
         
        writer.addDocuments(documents);
        writer.commit();
        writer.close();*/
    }
    
    
    // parse the JSON file and put the data into a JSONArray
    public static JSONArray parseJSON(int fileNum){

		// read the contents of the JSON file 
		// parse and then cast the object to JSONArray
    	// fileNum = the number for the JSON file
    	String filePath = JSON_FILE_PATH + "\\t-" + Integer.toString(fileNum) + ".json";
    	System.out.println("Creating index for " + filePath);
    	
		InputStream jsonFile = LuceneWriteIndex.class.getResourceAsStream(filePath);
		JSONArray jsonArray = new JSONArray();
		
		if(jsonFile == null) {
			System.out.println("Error: the file does not exist");
		}
		else {
			System.out.println("The file has successfully been found");
			
			Reader readerJSON = new InputStreamReader(jsonFile);
			
			Object fileObject = JSONValue.parse(readerJSON);
			jsonArray = (JSONArray)fileObject;
		}
		
		return jsonArray;
    }
    
    // configuration for index including target directory
    // create index writer
    private static IndexWriter createWriter(int fileNum) throws IOException 
    {
    	String filePath = INDEX_DIR + "\\t-" + Integer.toString(fileNum) + ".index";
    	
        FSDirectory dir = FSDirectory.open(Paths.get(filePath));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }
 
    // add JSON fields into documents
    // add documents to the index
    private static List<Document> createDocument(JSONArray jsonArray) 
    {
        List<Document> docs = new ArrayList<>();
        
        String username;
        String location;
        String text;
        String favoritesCount;
        
        for(JSONObject obj : (List<JSONObject>) jsonArray) {
        	Document doc = new Document();
        	
        	// tweetobj/content = all attributes of tweet
        	// userobj/content2 = all attributes of user
        	String content = obj.get("tweet").toString();
        	content = content.substring(1, content.length() - 1);
        	
        	//System.out.println(content);
        	
        	Object fileObject = JSONValue.parse(content);
        	JSONObject tweetobj = (JSONObject)fileObject;
        	//JSONArray array2 = new JSONArray();
        	//array2.add(fileObject);
        	//JSONObject item2 = (JSONObject)jsonArray.get(0);
        	//content2 = item.get("tweet").toString();

        	//System.out.println(newobj.get("text"));
        	//System.out.println(newobj.get("user"));
        	//title = obj.get("titles").toString();
        	
        	String content2 = tweetobj.get("user").toString();
        	//System.out.println(content2);
        	
        	Object fileObject2 = JSONValue.parse(content2);
        	JSONObject userobj = (JSONObject)fileObject2;
        	
        	text = tweetobj.get("text").toString();
        	username = userobj.get("screen_name").toString();
        	favoritesCount = userobj.get("favourites_count").toString();
        	if(userobj.get("location") == null) {
        		location = "null";
        	}
        	else {
        		location = userobj.get("location").toString();
        	}
        	
        	//title = obj.get("titles").toString();

        	System.out.println("username: " + username);
        	System.out.println("location: " + location);
        	System.out.println("text: " + text);
        	System.out.println("favorites count: " + favoritesCount);
        	//System.out.println("title: " + title);
        	System.out.println("");
        	
        	doc.add(new StringField("username", username, Field.Store.YES));
        	doc.add(new StringField("location", location, Field.Store.YES));
        	doc.add(new StringField("text", text, Field.Store.YES));
        	doc.add(new StringField("favoritesCount", favoritesCount, Field.Store.YES));
        	//doc.add(new StringField("title", title, Field.Store.YES));
        	
        	docs.add(doc);
        }
 
        return docs;
    }
 
  
    
    // write the document to the index and then close it
    /*public void finish() {
    	try {
    		indexWriter.commit();
    		indexWriter.close();
    	} catch(IOException ex) {
    		System.err.println("Error:" + ex.getMessage());
    	}
    }*/
}
