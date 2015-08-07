/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.teste;

import gnu.io.CommPortIdentifier;
import java.util.Enumeration;

/**
 *
 * @author marcelo
 */
public class TestarRxTx {

    public static void main(String[] args) {
        System.out.println("deu certo");

        Enumeration port_list = CommPortIdentifier.getPortIdentifiers();
        //Enumeration generates a series of elements, one at a time.
        while (port_list.hasMoreElements()) //Tests if enumeration contains more elements
        {
            CommPortIdentifier port_id = (CommPortIdentifier) port_list.nextElement();
            if (port_id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.out.println("Serial port:" + port_id.getName());
            } else if (port_id.getPortType() == CommPortIdentifier.PORT_PARALLEL) {
                System.out.println("Parallel port:" + port_id.getName());
            } else {
                System.out.println("Other port:" + port_id.getName());
            }
        }

    }

}
