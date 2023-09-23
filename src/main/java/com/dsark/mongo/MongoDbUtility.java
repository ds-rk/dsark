package com.dsark.mongo;

import com.dsark.utility.IpConfiguration;
import com.dsark.utility.Utility;
import com.logging.MyLogger;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.File;
import java.io.IOException;

public class MongoDbUtility {

    private static final String authDb = Utility.getWorkbenchProperty("AUTH_DB", "myDB");
    private static final String mongoStr = "mongodb://" + getMongoUserPass() + getIpAndPort() + "/?authSource=" + getDb() + "&authMechanism=SCRAM-SHA-256";

    private static final String mongoDatabase = getDb();


    public static void main(String[] args) throws IOException {
        //startMongo();
        //stopMongo();
        //firstTimeSetup();
        //test();
    }

    public static void runMongo(boolean isMongoConfigure) throws IOException {
        startMongo();
        if (!isMongoConfigure)
            firstTimeSetup();
    }

    private static String getMongoUserPass() {
        String user = Utility.getWorkbenchProperty("MONGO_USER", "sourav");
        return user + ":" + "sourav@";
    }

    private static String getIpAndPort() {
        try {
            String ip = Utility.getWorkbenchProperty("SERVER_IP", "127.0.0.1");
            String port = Utility.getWorkbenchProperty("MONGO_PORT", "6000");
            return ip + ":" + port;
        } catch (Exception e) {
            MyLogger.error(e);
            return null;
        }
    }

    private static String getDb() {
        return Utility.isStringNonEmpty(authDb) ? authDb : "myDB";
    }

    public static MongoClient getConnection() {
        return MongoClients.create(mongoStr);
    }

    private static MongoCollection<Document> getMongoCollection(String collectionName, MongoClient client) {
        MongoDatabase database = client.getDatabase(mongoDatabase);
        return database.getCollection(collectionName);
    }

    static void test() {
        try (MongoClient client = getConnection()) {
            MongoIterable<String> databaseNames = client.listDatabaseNames();
            for (String s : databaseNames) {
                System.out.println(s);
            }
        }
    }

    private static void startMongo() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "startmongo.bat");
        File dir = new File("bin");
        pb.directory(dir);
        Process p = pb.start();
        MyLogger.info("Mongo running process id: " + p);
        MyLogger.info("Mongo db started successfully ....!");
    }

    private static void stopMongo() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "stopmongo.bat");
        File dir = new File("bin");
        pb.directory(dir);
        Process p = pb.start();
        MyLogger.info("Process id: " + p);
        MyLogger.info("Mongo db stop successfully.. ");
    }

    private static void firstTimeSetup() {
        //  mongodb://mongodb0.example.com:27017
        String mongoUrl = "mongodb://" + getIpAndPort();
        System.out.println("Mongo url: " + mongoUrl);

        try (MongoClient client = MongoClients.create(mongoUrl)) {
            String authDb = Utility.getWorkbenchProperty("AUTH_DB", "myDB");
            MongoDatabase db = client.getDatabase(authDb);
            Document test = new Document("test", "Test Data").append("createdBy", "developer");
            db.getCollection("test").insertOne(test);
            MongoIterable<String> databaseNames = client.listDatabaseNames();
            for (String db1 : databaseNames) {
                if (db1.equals(authDb)) {
                    MyLogger.info(authDb + " created successfully...");
                    break;
                }
            }
            Document commandArguments = new Document();
            commandArguments.put("createUser", "sourav");
            commandArguments.put("pwd", "sourav");
            Document[] roles = {new Document("role", "dbAdmin").append("db", authDb), new Document("role", "dbOwner").append("db", authDb)};
            commandArguments.put("roles", roles);
            String[] mechanisms = {"SCRAM-SHA-256"};
            commandArguments.put("mechanisms", mechanisms);
            BasicDBObject command = new BasicDBObject(commandArguments);
            Document document = db.runCommand(command);
            MyLogger.info("User create response: " + document);
            MyLogger.info("Mongodb configured successfully....");
            stopMongo();
            IpConfiguration.setMongoConfig(false);
            Thread.sleep(5000);
            startMongo();
        } catch (Exception e) {
            MyLogger.error(e);
            MyLogger.error("Mongodb not configure successfully !!");
        }

        Utility.setWorkbenchProperty("MONGO_CONFIGURED", "true");
    }

}
