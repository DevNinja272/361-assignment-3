/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jinwook
 */
public enum SecurityLevel
{
    HIGH, LOW;

    public boolean dominates(SecurityLevel sl)
    {
        return this == HIGH || sl == LOW;
    }
}
