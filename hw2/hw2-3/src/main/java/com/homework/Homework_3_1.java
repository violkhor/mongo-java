/**
 * Created by SC on 2014-11-01.
 */
package com.homework;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

import java.net.UnknownHostException;

public class Homework_3_1 {
    public static void main(String argv[]) throws UnknownHostException {
        MongoClient client = new MongoClient("192.168.49.128", 27017);
        DB db = client.getDB("school");
        DBCollection collection = db.getCollection("students");
        QueryBuilder queryBuilder = QueryBuilder.start("_id").is(137).and("scores.type").is("homework");
        DBObject field = new BasicDBObject("scores", 1);
        DBCursor cursor = collection.find(new BasicDBObject(), field);
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            BasicDBList scores = (BasicDBList) doc.get("scores");
            double min = 1000;
            for (Object item : scores) {
                DBObject score = (DBObject) item;
                if (score.get("type").equals("homework")) {
                    Double d = new Double(score.get("score").toString());
                    if (d.compareTo(min) < 0) {
                        min = d;
                    }

                }
            }
            // remove the field from the document
            DBObject parent = new BasicDBObject("_id", doc.get("_id"));
            DBObject objToRemove = new BasicDBObject();
            objToRemove.put("type", "homework");
            objToRemove.put("score", min);
            collection.update(parent, new BasicDBObject("$pull", new BasicDBObject("scores", objToRemove)));


        }
        client.close();

    }
}

