package org.example.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.util.List;

public class Answer
{
    private String status;
    @JacksonXmlProperty
    @JacksonXmlElementWrapper
    private List<Person> items;

    public Answer() {}

    public Answer(String status, List<Person> items)
    {
        this.status = status;
        this.items = items;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public List<Person> getItems()
    {
        return items;
    }

    public void setItems(List<Person> items)
    {
        this.items = items;
    }
}
