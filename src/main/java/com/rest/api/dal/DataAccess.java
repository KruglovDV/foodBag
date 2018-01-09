package com.rest.api.dal;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataAccess implements IDataAccess {

    public static IDataAccess dao;

    private static final String DB_NAME = "feedbag";

    private static final String  LIST_COLLECTION_NAME = "Lists";

    private static final String ITEMS_COLLECTION_NAME = "Items";

    private static final String IS_CURRENT_FIELD = "isCurrent";

    private static final String PURCHASED_FIELD = "purchased";

    private static final String DATE_FIELD = "date";

    private static final String ITEMS_FIELD = "items";

    private static final String EMPTY = "";

    private static final String ITEM_NAME_FIELD = "name";

    private static final MongoClient mongoClient;

    private static final MongoDatabase db;

    private static final MongoCollection<Document> LISTS_COLLECTION;

    private static final MongoCollection<Document> ITEMS_COLLECTION;

    private static final Document QUERY_FOR_CURRENT = new Document(IS_CURRENT_FIELD, true);

    static {
        mongoClient = new MongoClient();
        db = mongoClient.getDatabase(DB_NAME);
        LISTS_COLLECTION = db.getCollection(LIST_COLLECTION_NAME);
        ITEMS_COLLECTION = db.getCollection(ITEMS_COLLECTION_NAME);
        dao = new DataAccess();
    }

    private DataAccess() {}

    public List<String> getLists() {
        final List<String> target = new ArrayList<>();
        LISTS_COLLECTION.find()
                .map(JSON::serialize)
                .forEach((Block<? super String>) target::add);
        return target;
    }

    public String getCurrentList() {
        Document target = LISTS_COLLECTION.find(QUERY_FOR_CURRENT).first();
        return target != null ? JSON.serialize(target) : EMPTY;
    }

    public boolean setCurrentToInactive() {
        try {
            LISTS_COLLECTION.updateOne(QUERY_FOR_CURRENT,
                            new Document("$set", new Document(IS_CURRENT_FIELD, false)));
        } catch (final MongoException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean createCurrentList(final List<String> items) {
        Document newCurrentList = new Document(DATE_FIELD, new Date().toString())
                .append(IS_CURRENT_FIELD, true)
                .append(ITEMS_FIELD, items)
                .append(PURCHASED_FIELD, new ArrayList<String >());
        try {
            LISTS_COLLECTION.insertOne(newCurrentList);
        } catch (final MongoException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addItemToCurrent(final String item) {
        try {
            LISTS_COLLECTION.updateOne(QUERY_FOR_CURRENT,
                    new Document("$push", new Document(ITEMS_FIELD, item)));
        } catch (final MongoException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean moveToPurchased(final String item) {
        try {
            LISTS_COLLECTION.updateOne(QUERY_FOR_CURRENT,
                    new Document("$push", new Document(PURCHASED_FIELD, item)));
        } catch (final MongoException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public List<String> getItems() {
        final List<String> target = new ArrayList<>();
        ITEMS_COLLECTION.find()
                .map(JSON::serialize)
                .forEach((Block<? super String>)target::add);
        return target;
    }

    public String getItem(final String name) {
        final Document target = ITEMS_COLLECTION.find(new Document(ITEM_NAME_FIELD, name)).first();
        return target != null ? JSON.serialize(target) : EMPTY;
    }

    public boolean createItem(final String name) {

        if (!getItem(name).isEmpty()) {
            return false;
        }

        final Document newItem = new Document(ITEM_NAME_FIELD, name).append("prices", new ArrayList<String>());

        try {
            ITEMS_COLLECTION.insertOne(newItem);
        } catch (final MongoException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public boolean addPriceToItem(final String item, final String date, final String price) {
        try {
            ITEMS_COLLECTION.updateOne(new Document(ITEM_NAME_FIELD, item),
                    new Document("$push",
                            new Document("prices",
                                    new Document("date", date).append("price", price))));
        } catch (final MongoException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
