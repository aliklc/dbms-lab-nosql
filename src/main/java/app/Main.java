
package app;

import static spark.Spark.*;
import com.google.gson.Gson;
import app.store.*;
import app.model.Student;

public class Main {
    public static void main(String[] args) {
        port(8080);
        Gson gson = new Gson();

        RedisStore.init();
        HazelcastStore.init();
        MongoStore.init();

        get("/nosql-lab-rd/:param", (req, res) -> {
            try {
                String param = req.params(":param");
                if (!param.startsWith("student_no=")) {
                    res.status(400);
                    return "{\"error\":\"Invalid parameter format. Expected student_no=...\"}";
                }
                String id = param.split("=")[1];

                res.type("application/json");
                Student student = RedisStore.get(id);
                if (student == null) {
                    res.status(404);
                    return "{\"error\":\"Student not found\"}";
                }
                res.status(200);
                return gson.toJson(student);
            } catch (Exception e) {
                res.status(500);
                res.type("application/json");
                return "{\"error\":\"Internal server error: " + e.getMessage() + "\"}";
            }
        });

        get("/nosql-lab-hz/:param", (req, res) -> {
            try {
                String param = req.params(":param");
                if (!param.startsWith("student_no=")) {
                    res.status(400);
                    return "{\"error\":\"Invalid parameter format. Expected student_no=...\"}";
                }
                String id = param.split("=")[1];

                res.type("application/json");
                Student student = HazelcastStore.get(id);
                if (student == null) {
                    res.status(404);
                    return "{\"error\":\"Student not found\"}";
                }
                res.status(200);
                return gson.toJson(student);
            } catch (Exception e) {
                res.status(500);
                res.type("application/json");
                return "{\"error\":\"Internal server error: " + e.getMessage() + "\"}";
            }
        });

        get("/nosql-lab-mon/:param", (req, res) -> {
            try {
                String param = req.params(":param");
                if (!param.startsWith("student_no=")) {
                    res.status(400);
                    return "{\"error\":\"Invalid parameter format. Expected student_no=...\"}";
                }
                String id = param.split("=")[1];

                res.type("application/json");
                Student student = MongoStore.get(id);
                if (student == null) {
                    res.status(404);
                    return "{\"error\":\"Student not found\"}";
                }
                res.status(200);
                return gson.toJson(student);
            } catch (Exception e) {
                res.status(500);
                res.type("application/json");
                return "{\"error\":\"Internal server error: " + e.getMessage() + "\"}";
            }
        });
    }
}
