import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
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
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.packed.PackedLongValues.Iterator;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.TopDocs;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.*;

public class LuceneWriteIndex {

	// directory to files
	// INDEX_DIR target directory for index files to be created
	// JSON_FILE_PATH directory for JSON files to be indexed
	private static final String INDEX_DIR = "C:\\lucene\\indexes";
	private static final String JSON_FILE_PATH = "\\convertedtweets";
	
	 
    public static void main(String[] args) throws Exception 
    {
    	// create the indexes
    	//createIndexes();
    	
    	// testing for searches
    	testSearches();
    }
    
    public static void testSearches() throws Exception{
    	IndexSearcher searcher = createSearcher(1);
    	
    	// search by username
    	TopDocs foundDocs = searchByUsername("hazeleyes094", searcher);
    	
    	System.out.println("Searching for username: hazeleyes094");
    	System.out.println("Total Results: " + foundDocs.totalHits);
    	
    	int index = 0;
    	PrintWriter out = null;
    	
    	for(ScoreDoc sd : foundDocs.scoreDocs) {
    		Document d = searcher.doc(sd.doc);
    		
    		String username = d.get("username");
    		String location = d.get("location").replace("77", " ");
    		String text = d.get("text").replace("77", " ");
    		String favoritesCount = d.get("favoritesCount");
    		
    		System.out.println(String.format(username));
    		System.out.println(String.format(location));
    		System.out.println(String.format(text));
    		System.out.println(String.format(favoritesCount));
    		System.out.println("");
    		
    		// prepare to put into json format
    		JSONObject json = new JSONObject();
    		json.put("username", username);
    		json.put("location", location);
    		json.put("text", text);
    		json.put("favoritesCount", favoritesCount);
    		
    		System.out.println(json.toJSONString());
    		System.out.println("");
    		
    		String jsonString = json.toString();
    		

    		try {
    			out = new PrintWriter(new FileWriter("C:\\lucene\\searchResults\\userSearchResults.json"));
    			out.write(jsonString);
    			
    		}catch (Exception ex) {
    			System.out.println("error: " + ex.toString());
    		}
    	
    	}
    	out.close();
    	
    	TopDocs foundDocs2 = searchByLocation("united77states", searcher);
    	
    	System.out.println("Searching for location: united states");
    	System.out.println("Total Results: " + foundDocs2.totalHits);
    	
    	String jsonString = "";
    	
    	for(ScoreDoc sd : foundDocs2.scoreDocs) {
    		Document d = searcher.doc(sd.doc);
    		System.out.println(String.format(d.get("username")));
    		
    		String username = d.get("username");
    		String location = d.get("location").replace("77", " ");
    		String text = d.get("text").replace("77", " ");
    		String favoritesCount = d.get("favoritesCount");
    		
    		System.out.println(String.format(location));
    		System.out.println(String.format(text));
    		System.out.println(String.format(d.get("favoritesCount")));
    		System.out.println("");
    		
    		// prepare to put into json format
    		JSONObject json = new JSONObject();
    		json.put("username", username);
    		json.put("location", location);
    		json.put("text", text);
    		json.put("favoritesCount", favoritesCount);
    		
    		System.out.println(json.toJSONString());
    		System.out.println("");
    			
    		if(index != 0) {
    			jsonString = jsonString + "," + json.toString();
    			index++;
    		}
    		else {
    			jsonString = json.toString();
    			index++;
    		}
    		

    		try {
    			out = new PrintWriter(new FileWriter("C:\\lucene\\searchResults\\locationSearchResults.json"));
    			out.write(jsonString);
    			
    		}catch (Exception ex) {
    			System.out.println("error: " + ex.toString());
    		}
    	}
    	out.close();
    	
    	index = 0;

    	TopDocs foundDocs3 = searchByText("we77shall!!", searcher);
    	
    	System.out.println("Searching for text: we shall!!");
    	System.out.println("Total Results: " + foundDocs3.totalHits);
    	
    	for(ScoreDoc sd : foundDocs3.scoreDocs) {
    		Document d = searcher.doc(sd.doc);
    		System.out.println(String.format(d.get("username")));
    		
    		String username = d.get("username");
    		String location = d.get("location").replace("77", " ");
    		String text = d.get("text").replace("77", " ");
    		String favoritesCount = d.get("favoritesCount");
    		
    		System.out.println(String.format(location));
    		System.out.println(String.format(text));
    		System.out.println(String.format(d.get("favoritesCount")));
    		System.out.println("");
    		
    		// prepare to put into json format
    		JSONObject json = new JSONObject();
    		json.put("username", username);
    		json.put("location", location);
    		json.put("text", text);
    		json.put("favoritesCount", favoritesCount);
    		
    		System.out.println(json.toJSONString());
    		System.out.println("");
    			
    		if(index != 0) {
    			jsonString = jsonString + "," + json.toString();
    			index++;
    		}
    		else {
    			jsonString = json.toString();
    			index++;
    		}
    		

    		try {
    			out = new PrintWriter(new FileWriter("C:\\lucene\\searchResults\\textSearchResults.json"));
    			out.write(jsonString);
    			
    		}catch (Exception ex) {
    			System.out.println("error: " + ex.toString());
    		}
    	}
    	out.close();
    	
    	index = 0;
    	
    	TopDocs foundDocs4 = searchByFavCount("891", searcher);
    	
    	System.out.println("Searching for favorites count: 891");
    	System.out.println("Total Results: " + foundDocs4.totalHits);
    	
    	for(ScoreDoc sd : foundDocs4.scoreDocs) {
    		Document d = searcher.doc(sd.doc);
    		System.out.println(String.format(d.get("username")));
    		
    		String username = d.get("username");
    		String location = d.get("location").replace("77", " ");
    		String text = d.get("text").replace("77", " ");
    		String favoritesCount = d.get("favoritesCount");
    		
    		System.out.println(String.format(location));
    		System.out.println(String.format(text));
    		System.out.println(String.format(d.get("favoritesCount")));
    		System.out.println("");
    		
    		// prepare to put into json format
    		JSONObject json = new JSONObject();
    		json.put("username", username);
    		json.put("location", location);
    		json.put("text", text);
    		json.put("favoritesCount", favoritesCount);
    		
    		System.out.println(json.toJSONString());
    		System.out.println("");
    			
    		if(index != 0) {
    			jsonString = jsonString + "," + json.toString();
    			index++;
    		}
    		else {
    			jsonString = json.toString();
    			index++;
    		}
    		

    		try {
    			out = new PrintWriter(new FileWriter("C:\\lucene\\searchResults\\favCountSearchResults.json"));
    			out.write(jsonString);
    			
    		}catch (Exception ex) {
    			System.out.println("error: " + ex.toString());
    		}
    	}
    	out.close();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // CREATE INDEX
    ///////////////////////////////////////////////////////////////////////////////////////////
    public static void createIndexes() throws Exception {

    	List<Document> docs = new ArrayList<>();
    	
    	// traverse through all 12 files from convertedtweets directory
    	for(int index = 1; index <= 12; index++) {
    		IndexWriter indexWriter = createWriter(index);
    		JSONArray arrayObject = parseJSON(index);

    		docs = createDocument(arrayObject);
    		
    		indexWriter.deleteAll();
    		
    		indexWriter.addDocuments(docs);
    		indexWriter.commit();
    		indexWriter.close();
    	}
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
        	
        	// replace spaces with 77
        	// get rid of special characters
        	username = username.toLowerCase();
        	location = location.toLowerCase();
        	location = location.replace(" ", "77");
        	text = text.toLowerCase();
        	text = text.replace(" ", "77");
        	text = text.replaceAll("[^a-zA-Z0-9]", "");

        	System.out.println("username: " + username);
        	System.out.println("location: " + location);
        	System.out.println("text: " + text);
        	System.out.println("favorites count: " + favoritesCount);
        	System.out.println("");
        	
        	
        	// add data into the documents
        	// add document into the list
        	doc.add(new StringField("username", username, Field.Store.YES));
        	doc.add(new StringField("location", location, Field.Store.YES));
        	doc.add(new StringField("text", text, Field.Store.YES));
        	doc.add(new StringField("favoritesCount", favoritesCount, Field.Store.YES));
        	
        	docs.add(doc);
        }
 
        return docs;
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////
    // SEARCH INDEX
    ///////////////////////////////////////////////////////////////////////////////////////////
    
    // filePath is the target index file to be searched
    // create index searcher to search lucene documents from indexes
    private static IndexSearcher createSearcher(int fileNum) throws IOException{
    	String filePath = INDEX_DIR + "\\t-" + Integer.toString(fileNum) + ".index";
    	
    	Directory dir = FSDirectory.open(Paths.get(filePath));
    	IndexReader reader = DirectoryReader.open(dir);
    	IndexSearcher searcher = new IndexSearcher(reader);
    	return searcher;
    }
    
    private static TopDocs searchByUsername(String username, IndexSearcher searcher) throws IOException{
    	QueryParser qp = new QueryParser("username", new StandardAnalyzer());
    	Query usernameQuery = null;
		try {
			usernameQuery = qp.parse(username);
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			System.out.println("Error: " + e.getMessage());			
		}
    	
		TopDocs hits = searcher.search(usernameQuery, 40);
		
		return hits;
    }
    
    private static TopDocs searchByLocation(String location, IndexSearcher searcher) throws IOException{
    	QueryParser qp = new QueryParser("location", new StandardAnalyzer());
    	Query locationQuery = null;
		try {
			locationQuery = qp.parse(location);
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			System.out.println("Error: " + e.getMessage());			
		}
    	
		TopDocs hits = searcher.search(locationQuery, 40);
		
		return hits;
    }
    
    private static TopDocs searchByText(String text, IndexSearcher searcher) throws IOException{
    	text = text.replaceAll("[^a-zA-Z0-9]", "");
    	QueryParser qp = new QueryParser("text", new StandardAnalyzer());
    	Query locationQuery = null;
		try {
			locationQuery = qp.parse(text);
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			System.out.println("Error: " + e.getMessage());			
		}
    	
		TopDocs hits = searcher.search(locationQuery, 10);
		
		return hits;
    }
    
    private static TopDocs searchByFavCount(String favoritesCount, IndexSearcher searcher) throws IOException{
    	QueryParser qp = new QueryParser("favoritesCount", new StandardAnalyzer());
    	Query favCountQuery = null;
		try {
			favCountQuery = qp.parse(favoritesCount);
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			System.out.println("Error: " + e.getMessage());			
		}
    	
		TopDocs hits = searcher.search(favCountQuery, 10);
		
		return hits;
    }
}
