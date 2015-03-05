/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enphasis.serverhttp.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Milton
 */
public class Service extends Thread{
    
    private Socket socket;
    private BufferedReader buf;
    private boolean stop = false;
    
    public Service(Socket socket){
        System.out.println("[Service] Servicio iniciado");
        this.socket = socket; 
    }
    @Override
    public void run(){
        
        try {
            System.out.println("[Service] Procesando...");
            buf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataInputStream in = new DataInputStream(socket.getInputStream());
            OutputStream outStream = socket.getOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(outStream);
            
            String request = buf.readLine().trim();
            System.out.println(request);
            StringTokenizer req = new StringTokenizer(request);
            String header = req.nextToken();
            if(header.equals("GET")){
                String fileName = req.nextToken().replaceAll("/","");
                FileInputStream file = null;
                //System.out.println(fileName);
                boolean existFile = true;
                
                try{
                    file = new FileInputStream("H:/"+fileName);
                    System.out.println("[Service] Pagina encontrada.");
                }catch(Exception ex){
                    existFile = false;
                    System.out.println("[Service] El archivo no existe.");
                }
                
                String ServerLine="Simple HTTP Server";
                String StatusLine=null;
                String ContentTypeLine=null;
                String ContentLengthLine=null;
                String ContentBody=null;
                
                if(existFile)
                {
                    StatusLine="HTTP/1.0 200 OK";
                    ContentTypeLine="Content-type: text/html";
                    ContentLengthLine="Content-Length: "+ (new Integer(file.available()).toString());                
                }
                else
                {
                    StatusLine = "HTTP/1.0 200 OK";
                    ContentTypeLine="Content-type: text/html";
                    ContentBody = "<HTML>" + 
                                    "<HEAD><TITLE>404 Not Found</TITLE></HEAD>" +
                                "<BODY>404 Not Found" +                                               
                                    "</BODY></HTML>" ;
                    ContentLengthLine=(new Integer(ContentBody.length()).toString());
                }
                
                System.out.println(StatusLine);
                System.out.println( ServerLine);
                System.out.println(ContentTypeLine);
                System.out.println( ContentLengthLine);
                
                if(existFile)    
                {

                    byte[] buffer = new byte[1024];
                    int bytes = 0 ;
                    while ((bytes = file.read(buffer)) != -1 ) 
                        {
                        out.write(buffer, 0, bytes);
                        for(int iCount=0;iCount<bytes;iCount++)
                        {
                            int temp=buffer[iCount];
                            System.out.print((char)temp);
                        }
                    }    
                
                    file.close();
                }
                else
                {
                    System.out.println(ContentBody.getBytes());
                    System.out.println(ContentBody);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
