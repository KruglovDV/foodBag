package com.rest.api.dal;

import java.util.List;

public interface IDataAccess {

    List<String> getLists();

    String getCurrentList();

    boolean setCurrentToInactive();

    boolean createCurrentList(List<String> items);

    boolean addItemToCurrent(String name);

    boolean moveToPurchased(String item);

    List<String> getItems();

    boolean createItem(String name);

    String getItem(String name);

    boolean addPriceToItem(String item, String date, String price);
}
