
package app.store;

import com.mongodb.client.*;
import org.bson.Document;
import app.model.Student;
import com.google.gson.Gson;

public class MongoStore {
    static MongoClient client;
    static MongoCollection<Document> collection;
    static Gson gson = new Gson();

    public static void init() {
        client = MongoClients.create("mongodb://mongodb:27017"); // Docker service name
        collection = client.getDatabase("nosqllab").getCollection("ogrenciler");
        
        // Eğer veri varsa tekrar ekleme
        if (collection.countDocuments() > 0) {
            System.out.println("MongoDB already has data, skipping...");
            return;
        }
        
        System.out.println("Inserting 10,000 records to MongoDB...");
        java.util.List<Document> documents = new java.util.ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            String id = "2025" + String.format("%06d", i);
            Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
            documents.add(Document.parse(gson.toJson(s)));
        }
        collection.insertMany(documents);
        System.out.println("MongoDB init complete!");
    }

    public static Student get(String id) {
        try {
            Document doc = collection.find(new Document("ogrenciNo", id)).first();
            return doc != null ? gson.fromJson(doc.toJson(), Student.class) : null;
        } catch (Exception e) {
            System.err.println("MongoDB get error: " + e.getMessage());
            return null;
        }
    }
}
