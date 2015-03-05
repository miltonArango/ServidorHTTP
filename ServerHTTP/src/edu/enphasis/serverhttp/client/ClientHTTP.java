/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enphasis.serverhttp.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author LOG
 */
public class ClientHTTP {
    
    private Socket client;
    private DataOutputStream out;
    
    public static void main(String args[]){
        
        ClientHTTP app = new ClientHTTP();
        if(app.connect()){
            app.sendMessage(); 
        }
        
    }
    
    public boolean connect(){
        try{
            System.out.println("[Client] Conectando al servidor...");
            client = new Socket("10.0.0.100", 80);
            System.out.println("[Client] Conectado al servidor en "+client.getPort());
            out = new DataOutputStream(client.getOutputStream());
            return true;
        }catch(UnknownHostException e){
            return false;
            
        }catch(IOException e){
            return false;
            
        }
    }
    
    public void sendMessage(){  
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        
        try{
            String msg = "GET /index.html HTTP/1.1";
            out.writeUTF(msg+"\r\n");
            System.out.println("[Client] Mensaje enviado: " + msg);
        }catch(IOException e){

        }
        
    }
    
}
