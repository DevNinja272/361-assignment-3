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
public class InstructionObject {
    private String command;
    private String subj;
    private String obj;
    private String val;

    private InstructionObject(String command, String subj, String obj)
    {
        this.command = command;
        this.subj = subj;
        this.obj = obj;
    }

    private InstructionObject(String command, String subj, String obj, String val)
    {
        this(command, subj, obj);
        this.val = val;
    }
    
    public InstructionObject(String line)
    {
        if(line != null)
        {
            String[] words = line.trim().replaceAll("\\p{javaSpaceChar}{2,}", " ").split(" ");
            int counter = 0;
            if(words.length > counter)
            {
                this.command = words[counter++];
                if(words.length > counter)
                {
                    this.subj = words[counter++];
                    if(words.length > counter)
                    {
                        this.obj = words[counter++];
                        if(words.length > counter)
                        {
                            this.val = words[counter];
                        }
                    }
                }
            }
        }
    }

    public String getCommand() { return command; }

    public String getSubj()
    {
        return subj;
    }

    public String getObj()
    {
        return obj;
    }

    public String getVal()
    {
        return val;
    }
}
