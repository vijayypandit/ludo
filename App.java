package l1;
import java.io.FileReader;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.util.ArrayList;
 
import java.io.BufferedReader;
import java.io.File;
// parse csv file code
public class App {
	public static  void main(String args[]){
		App o = new App();

	
		 String file = "P://Downloads/Ludo.csv";
		List<Document> documents = new ArrayList<Document>();
		try {
					
			
			
			// connection mongo Service
			//MongoClient mongoClient = new MongoClient("localhost", 27017);
			MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

						 //Connect to the database 
			MongoDatabase mongoDatabase = mongoClient.getDatabase("users");
			System.out.println("*********************connect to database successfully*********************");
								// Create Collection
			boolean collectionExists = mongoClient.getDatabase("users").listCollectionNames().into(new ArrayList<String>()).contains("usersort");
			if(collectionExists == true) {
				mongoDatabase.getCollection("usersort").drop();
				 System.out.println ( "*********************Existing Collection Dropped *******************");
											}
				mongoDatabase.createCollection("usersort");
				 System.out.println ( "*********************Creating a collection of success*********************");
				 
				 	// select a collection
			MongoCollection<Document> collection = mongoDatabase.getCollection("usersort");
			
			 System.out.println ( " usersort collection from users database selected successfully");
				
						 
						 
				 
						 
						 FileReader filereader = new FileReader(file);
						  
					        // create csvReader object and skip first Line
					        CSVReader csvReader = new CSVReaderBuilder(filereader)
					                                  .withSkipLines(1)
					                                  .build();
					        List<String[]> allData = csvReader.readAll();
						 	
					        for (int rowIndex=0; rowIndex<allData.size(); rowIndex++) {
					        	String[] row =  allData.get(rowIndex);

					          
					        	 String uid = row[0]; // get the value in the csv assign keywords
					        	 Double tx_coins = Double.parseDouble(row[1]);
								Long total_coins_after = Long.parseLong(row[3]);
								String time_stamp = row[10];
							//	System.out.println();
								
									Document old = collection.find(Filters.eq("_id",  uid)).first();
									if (old!=null) {
										DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:m:ss zzz");
										ZonedDateTime	oldDate = ZonedDateTime.parse(old.get("time_stamp").toString(), inputFormatter);
										
										ZonedDateTime	newDate = ZonedDateTime.parse(time_stamp, inputFormatter);
										if (newDate.compareTo(oldDate) > 0) {
											Document document = new Document (); // create a document
											document.put( "_id",uid); // data into the database
											document.put("tx_coins", tx_coins);
											document.put("total_coins_after", total_coins_after);
											document.put("time_stamp", time_stamp);
									//		documents.add(document);
											//collection.insertOne(document);
											UpdateOptions updateOp =  new UpdateOptions();
											updateOp.upsert(true);
											
											collection.updateOne(Filters.eq("_id",  uid), new Document("$set", document), updateOp);
											
										}
										//System.out.println(old.get("time_stamp"));
									} else {
										Document document = new Document (); // create a document
										document.put( "_id",uid); // data into the database
										document.put("tx_coins", tx_coins);
									document.put("total_coins_after", total_coins_after);
										document.put("time_stamp", time_stamp);
										//documents.add(document);
										collection.insertOne(document);
									}
					
					        }
							 System.out.println("################ Inserts Succesfull  #################");	

					       
						 	int counter = 0;
							 FindIterable<Document> fit = collection.find().sort(new BasicDBObject("total_coins_after",-1));
							 MongoCursor<Document> cursor = fit.iterator();
							 Document d1 = new Document();
							 while(cursor.hasNext()) {
								 
					             // System.out.println(cursor.next());
					              		counter++;	
					              		String uid = cursor.next().getString("_id");
					              	//	System.out.println(id);	
					              		//System.out.println("counter : = " +counter);
					       				d1.put("rank", counter);
										documents.add(d1);

										UpdateOptions updateOp1 =  new UpdateOptions();
										updateOp1.upsert(true);
										
										collection.updateOne(Filters.eq("_id",  uid), new Document("$set", d1), updateOp1);
					              		
					              		
							 }
					        
							 	System.out.println("..................Updating Users with Rank done Successfully ............");

		}
		
		
		
		
								catch (Exception e){
			e.printStackTrace();
		}
	
	//Calling awt class for output screen
		text ob = new text();
	
	 }
	public int findRank(String usr) {
		String user = usr;
		
	
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
					 //Connect to the database 
		MongoDatabase mongoDatabase = mongoClient.getDatabase("users");
		System.out.println("*********************connect to database successfully*********************");
							// Create Collection
		boolean collectionExists = mongoClient.getDatabase("users").listCollectionNames().into(new ArrayList<String>()).contains("usersort");
		if(collectionExists == true) {
			
		MongoCollection<Document> collection = mongoDatabase.getCollection("usersort");
		 System.out.println ( " usersort collection from users database selected successfully");
			
		FindIterable<Document> ftr = 	collection.find(Filters.eq("_id",  user));

		 MongoCursor<Document> cursor = ftr.iterator();
	if(cursor.hasNext()) {
		 while(cursor.hasNext()) {
       		int rank  = cursor.next().getInteger("rank");
       		System.out.println("Rank : "+rank);
			 	return rank;
		 }
	}
	else {
		return 0;
	}
		
	
		}
		
		return 0; 
	}
	
	
	public String findRank() {
		
		String s="";
	
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
					 //Connect to the database 
		MongoDatabase mongoDatabase = mongoClient.getDatabase("users");
		System.out.println("*********************connect to database successfully*********************");
							// Create Collection
		boolean collectionExists = mongoClient.getDatabase("users").listCollectionNames().into(new ArrayList<String>()).contains("usersort");
		if(collectionExists == true) {
			
		MongoCollection<Document> collection = mongoDatabase.getCollection("usersort");
		 System.out.println ( " usersort collection from users database selected successfully");
			
		 FindIterable<Document> fit = collection.find().sort(new BasicDBObject("total_coins_after",-1)).limit(100);

		 MongoCursor<Document> cursor = fit.iterator();
		 while(cursor.hasNext()) {
			  Document d =cursor.next(); 
			 System.out.println(s);
		 }
	
	
		
	
		}
		
		return s; 
	}
	
	
}