/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author jinwook
 */
public class ObjectManager {
    private ArrayList<BLPobject> objects;

    public ObjectManager()
    {
        objects = new ArrayList<BLPobject>();
    }
    
    private boolean exists(String name)
    {
        return objects.contains(new BLPobject(name));
    }
    
    public BLPobject get(String name)
    {
        for(BLPobject obj: objects)
            if(obj.name.equals(name.toLowerCase()))
                return obj;
        return null;
    }
    
    public int read(String name)
    {
        if(exists(name))
            return get(name).value;
        return 0;
    }
    
    public boolean write(String name, int value)
    {
        if(exists(name))
        {
            get(name).setValue(value);
            return true;
        }
        return false;
    }
    
    public BLPobject createObject(String name)
    {
        if(exists(name))
            return null;
        BLPobject obj = new BLPobject(name);
        return objects.add(obj) ? obj : null;
    }
    
    public BLPobject createObject(String name, int value)
    {
        BLPobject obj = createObject(name);
        if(obj != null)
            write(name, value);
        return obj;
    }
}
