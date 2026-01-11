
package app.store;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientConnectionStrategyConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import app.model.Student;

public class HazelcastStore {
    static HazelcastInstance hz;
    static IMap<String, Student> map;

    public static void init() {
        ClientConfig config = new ClientConfig();
        config.getNetworkConfig().addAddress("hazelcast:5701");
        config.getConnectionStrategyConfig()
            .getConnectionRetryConfig()
            .setClusterConnectTimeoutMillis(60000);
        
        // Retry ayarları
        config.getConnectionStrategyConfig()
            .setAsyncStart(false)
            .setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ON);
        
        hz = HazelcastClient.newHazelcastClient(config);
        map = hz.getMap("ogrenciler");
        
        // Eğer veri varsa tekrar ekleme
        if (!map.isEmpty()) {
            System.out.println("Hazelcast already has data, skipping...");
            return;
        }
        
        System.out.println("Inserting 10,000 records to Hazelcast...");
        java.util.Map<String, Student> batch = new java.util.HashMap<>();
        for (int i = 0; i < 10000; i++) {
            String id = "2025" + String.format("%06d", i);
            Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
            batch.put(id, s);
        }
        map.putAll(batch);
        System.out.println("Hazelcast init complete!");
    }

    public static Student get(String id) {
        return map.get(id);
    }
}
