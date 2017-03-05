/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author jinwook
 */
public class SecureObject
{
    ////////////////
    /* Properties */
    ////////////////

    private final String name;
    private       int    value;

    //////////////////
    /* Constructors */
    //////////////////

    public SecureObject(String name)
    {
        this.name = name;
    }

    //////////////////////////
    /* Accessors & Mutators */
    //////////////////////////

    final String getName()
    {
        return this.name;
    }

    final int getValue()
    {
        return this.value;
    }

    final void setValue(int value)
    {
        this.value = value;
    }

    ////////////////////
    /* Public Methods */
    ////////////////////

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof SecureObject))
        {
            return false;
        }

        SecureObject secureObject = (SecureObject) object;

        return getName() != null
               ? getName().equals(secureObject.getName())
               : secureObject.getName() == null;
    }

    @Override
    public int hashCode()
    {
        return getName() != null ? getName().hashCode() : 0;
    }
}
