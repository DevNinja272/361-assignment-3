/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jinwook
 */
public class BLPobject 
{
    String name;
    int value;
    
    public BLPobject()
    {
        value = 0;
    }
    
    public BLPobject(String name)
    {
        this();
        this.name = name.toLowerCase();
    }
    
    public BLPobject(String name, int value)
    {
        this(name);
        this.value = value;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public void setValue(int val)
    {
        value = val;
    }
    
    public boolean equals(BLPobject object)
    {
        return getName().equals(object.getName());
    }

    public boolean equals(String name)
    {
        return getName().equals(name);
    }

    public boolean equals(Object item)
    {
        if (item instanceof  BLPobject)
        {
            return this.equals((BLPobject)item);
        }
        else if (item instanceof String)
        {
            return this.equals((String)item);
        }
        else
        {
            return ((Object)this).equals(item);
        }
    }
}
