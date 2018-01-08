package com.rest.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Item {
    @XmlElement public String name;
    @XmlElement public String date;
    @XmlElement public String price;
}
