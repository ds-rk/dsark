package com.dsark.utility;

import com.dsark.mongo.MongoDbUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.logging.MyLogger;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpConfiguration {
    public static void main(String[] args) {
        IpConfigMain();
    }

    public static void IpConfigMain(){
        try{
            ipConfig();
            boolean isMongoConfigure = Utility.getWorkbenchProperty("MONGO_CONFIGURED", false);
            setMongoConfig(!isMongoConfigure);
            MongoDbUtility.runMongo(isMongoConfigure);
        }catch (Exception e){
            MyLogger.error(e);
        }
    }


    private static void ipConfig() throws UnknownHostException {
        String hostIp = InetAddress.getLocalHost().getHostAddress();
        Utility.setWorkbenchProperty("SERVER_IP", hostIp);

        // Todo fetch everything form config file...
    }

    public static void setMongoConfig(boolean isFirst) throws IOException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        // Parse the YAML file
        ObjectNode root = (ObjectNode) mapper.readTree(new File("mongodb/bin/mongod.cfg"));


        // Update the value
        String ip = Utility.getWorkbenchProperty("SERVER_IP", "localhost");
        String port = Utility.getWorkbenchProperty("MONGO_PORT", "6000");
        String bindIp = "localhost, " + ip;
        root.putPOJO("net", new Document("bindIp", bindIp).append("port", port));
        if (!isFirst) {
            root.putPOJO("security", new Document("authorization", "enabled"));
        }else {
            root.remove("security");
            //root.putPOJO("#security", new Document("#authorization", "enabled"));
        }

        // Write changes to the YAML file
        mapper.writer().writeValue(new File("mongodb/bin/mongod.cfg"), root);
        MyLogger.info("Data successfully write in mongo.cfg file");
    }

}
