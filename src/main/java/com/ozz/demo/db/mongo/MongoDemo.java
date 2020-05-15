package com.ozz.demo.db.mongo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
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
import com.ozz.demo.cache.redis.sentinel.RedisSentinelUtil;

public class MongoDemo {
  private static MongoClient mongoClient;

  static {
    Properties props = new Properties();
    try (InputStream in = RedisSentinelUtil.class.getResourceAsStream(
        "com/ozz/demo/cache/redis/sentinel/redis-sentinel.properties");) {
      props.load(in);
    } catch (RuntimeException e) {
      props = null;
      throw e;
    } catch (Exception e) {
      props = null;
      throw new RuntimeException(e);
    }

    List<ServerAddress> seeds = new ArrayList<>();
    for (int i = 1;; i++) {
      String host = props.getProperty("mongo.host." + i);
      String port = props.getProperty("mongo.port." + i);
      if (StringUtils.isNotEmpty(host) && StringUtils.isNotEmpty(port)) {
        seeds.add(new ServerAddress(host, Integer.valueOf(port)));
      } else {
        break;
      }
    }
    mongoClient = new MongoClient(seeds);
  }

  public Document selectById(String id) {
    MongoDatabase db = mongoClient.getDatabase("databaseName");
    MongoCollection<Document> collection = db.getCollection("collectionName");

    FindIterable<Document> findIt = collection.find(Filters.and(Filters.gte("_id", new ObjectId(id)))).sort(Sorts.ascending("ts"));
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

    FindIterable<Document> findIt = collection.find(Filters.and(Arrays.asList(Filters.gte("ts", 1), Filters.lt("ts", 2)))).sort(Sorts.ascending("ts"));
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
