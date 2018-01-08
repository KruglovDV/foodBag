package com.rest.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class POJOList {
    @XmlElement public String date;
    @XmlElement public List<String> items;
    @XmlElement public String purchased;
}