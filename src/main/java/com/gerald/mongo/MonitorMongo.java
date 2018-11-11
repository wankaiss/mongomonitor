package com.gerald.mongo;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MonitorMongo {

  private String composeMongoConnectString() {
    MongoConfig mongoConfig = MongoUtils.getMongoConfig();
    return String.format("mongodb://%s:%s@%s/", mongoConfig.getUsername(), mongoConfig.getPassword(),
        mongoConfig.getHost());
  }

  public void createMongoClientAndProcessData() {

    try (MongoClient mongoClient = MongoClients.create(composeMongoConnectString())) {
      MongoCollection<Document> collection = mongoClient.getDatabase("timemachine").getCollection("trades");
      Document doc1 = new Document("type", "trade")
          .append("typeValue", "0")
          .append("typeValid", "0");
      Document doc2 = new Document("type", "position")
          .append("typeValue", "1")
          .append("typeValid", "1");
      Document doc3 = new Document("type", "gsm")
          .append("typeValue", "2")
          .append("typeValid", "2");

      List<Document> docs = new ArrayList<>();
      docs.add(doc1);
      docs.add(doc2);
      docs.add(doc3);

      collection.insertMany(docs);
      System.out.println("insertSuccess");
    }
  }

  public void watchCollection() {

    Block<ChangeStreamDocument<Document>> printBlock = new Block<ChangeStreamDocument<Document>>() {

      @Override
      public void apply(ChangeStreamDocument<Document> changeStreamDocument) {
        System.out.println(changeStreamDocument);
      }
    };

    try (MongoClient mongoClient = MongoClients.create(composeMongoConnectString())) {
      MongoCollection<Document> collection = mongoClient.getDatabase("timemachine").getCollection("trades");
      System.out.println("start to monitor trades collection");
      collection.watch().forEach(printBlock);
    }
  }

  public static void main(String[] args) {
    MonitorMongo monitorMongo = new MonitorMongo();
    monitorMongo.watchCollection();
  }
}
