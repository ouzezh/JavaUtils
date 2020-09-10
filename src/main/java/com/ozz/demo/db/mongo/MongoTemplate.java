package com.ozz.demo.db.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Setter;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

@Setter
public class MongoTemplate implements InitializingBean, DisposableBean {

  private MongoClient mongoClient;
  @Value("${mongo.nodes}")
  private String nodes;

  public static void main(String[] args) throws Exception {
    System.out.println("-start-");
    MongoTemplate mt = new MongoTemplate();
    mt.setNodes("localhost:27017,localhost2:27017");
    mt.afterPropertiesSet();

    System.out.println(mt.selectById("x"));

    mt.destroy();
    System.out.println("-end-");
  }

  @Override
  public void destroy() throws Exception {
    if (mongoClient != null) {
      mongoClient.close();
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    List<ServerAddress> seeds = Arrays
        .stream(this.nodes.replaceAll("\\s", "").split(","))
        .map(node -> new ServerAddress(node.split(":")[0], Integer.valueOf(node.split(":")[1])))
        .collect(Collectors.toList());
    mongoClient = new MongoClient(seeds);
  }

  public Document selectById(String id) {
    MongoDatabase db = mongoClient.getDatabase("databaseName");
    MongoCollection<Document> collection = db.getCollection("collectionName");

    FindIterable<Document> findIt = collection
        .find(Filters.and(Filters.gte("_id", new ObjectId(id)))).sort(Sorts.ascending("ts"));
    try (MongoCursor<Document> it = findIt.iterator();) {
      if (it.hasNext()) {
        return it.next();
      }
    }
    return null;
  }

  public void select() {
    MongoDatabase db = mongoClient.getDatabase("databaseName");
    MongoCollection<Document> collection = db.getCollection("collectionName");

    FindIterable<Document> findIt = collection
        .find(Filters.and(Arrays.asList(Filters.gte("ts", 1), Filters.lt("ts", 2))))
        .sort(Sorts.ascending("ts"));
    try (MongoCursor<Document> it = findIt.iterator();) {
      while (it.hasNext()) {
        Document doc = it.next();
        System.out.println(doc.getObjectId("_id").toHexString());
      }
    }
  }

  public void update() {
    MongoDatabase db = mongoClient.getDatabase("databaseName");
    MongoCollection<Document> collection = db.getCollection("collectionName");

    collection.updateOne(Filters.eq("_id", new ObjectId("id")),
        new BasicDBObject().append("$set", new BasicDBObject().append("time", 1)),
        new UpdateOptions().upsert(true));
  }

  public void jsonToDocument() {
    Document doc = Document.parse("{}");
    System.out.println(doc);
  }

  public void documentToJson() {
    Document doc = selectById("id");
    System.out.println(doc.toJson());
  }

}
