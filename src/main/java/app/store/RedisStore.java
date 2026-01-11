
package app.store;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import app.model.Student;
import com.google.gson.Gson;

public class RedisStore {
    static Jedis jedis;
    static Gson gson = new Gson();

    public static void init() {
        jedis = new Jedis("redis", 6379); // Docker service name
        
        // Eğer veri varsa tekrar ekleme
        if (jedis.exists("2025000000")) {
            System.out.println("Redis already has data, skipping...");
            return;
        }
        
        System.out.println("Inserting 10,000 records to Redis...");
        Pipeline pipeline = jedis.pipelined();
        for (int i = 0; i < 10000; i++) {
            String id = "2025" + String.format("%06d", i);
            Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
            pipeline.set(id, gson.toJson(s));
        }
        pipeline.sync();
        System.out.println("Redis init complete!");
    }

    public static Student get(String id) {
        String json = jedis.get(id);
        return gson.fromJson(json, Student.class);
    }
}
