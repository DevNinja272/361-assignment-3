/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * @author jinwook
 */
class SecureSubject extends SecureObject implements Runnable
{
    ////////////////
    /* Properties */
    ////////////////

    private Consumer<IntSupplier> code;

    //////////////////
    /* Constructors */
    //////////////////

    SecureSubject(String name)
    {
        super(name);
        this.setCode(null);
    }

    SecureSubject(String name, Consumer<IntSupplier> code)
    {
        this(name);
        this.setCode(code);
    }

    //////////////////////////
    /* Accessors & Mutators */
    //////////////////////////

    Consumer<IntSupplier> getCode()
    {
        return this.code;
    }

    void setCode(Consumer<IntSupplier> code)
    {
        this.code = code;
    }

    //////////////
    /* Runnable */
    //////////////

    public void run()
    {
        if (getCode() != null)
        {
            getCode().accept(this::getValue);
        }
    }
}
