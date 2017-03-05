/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jinwook
 */
public class BadInstruction {
    private String errorMessage;

    public BadInstruction(String message)
    {
        errorMessage = message;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public String toString()
    {
        return "Bad Instruction";
    }
}
