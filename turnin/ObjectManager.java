/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 * @author jinwook
 */
class ObjectManager
{
    ////////////////
    /* Properties */
    ////////////////

    protected ArrayList<SecureObject> objects;

    /////////////////
    /* Initializer */
    /////////////////

    {
        this.setObjects(new ArrayList<>());
    }

    //////////////////////////
    /* Accessors & Mutators */
    //////////////////////////

    private ArrayList<SecureObject> getObjects()
    {
        return this.objects;
    }

    private void setObjects(ArrayList<SecureObject> objects)
    {
        this.objects = objects;
    }

    ////////////////////
    /* Helper Methods */
    ////////////////////

    protected SecureObject getNewInstance(String name)
    {
        return new SecureObject(name);
    }

    protected SecureObject getByName(String name)
    {
        for (SecureObject candidate : getObjects())
        {
            if (candidate != null && candidate.getName().equalsIgnoreCase(name))
            {
                return candidate;
            }
        }

        return null;
    }

    protected boolean existsWithName(String objectName)
    {
        return getByName(objectName) != null;
    }

    private void addNewByName(String objectName)
    {
        if (objectName != null && !existsWithName(objectName))
        {
            SecureObject object = getNewInstance(objectName);
            if (!getObjects().contains(object))
            {
                getObjects().add(object);
            }
        }
    }

    private void removeByName(String objectName)
    {
        getObjects().remove(getByName(objectName));
    }

    /////////////////////
    /* Package Methods */
    /////////////////////

    boolean exists(String name)
    {
        return existsWithName(name);
    }

    void add(String name)
    {
        addNewByName(name);
    }

    void remove(String name)
    {
        removeByName(name);
    }

    void write(String name, int value)
    {
        SecureObject blp_object = getByName(name);
        if (blp_object != null)
        {
            blp_object.setValue(value);
        }
    }

    int read(String name)
    {
        SecureObject object = getByName(name);
        return object == null ? 0 : object.getValue();
    }
}
