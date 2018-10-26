package com.orsy.tdd.puissance4.client;



import java.io.IOException;
import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ClientEndpoint
public class WebSocketClient {
   private final String uri;
   private Session session;
   private ClientPuissance4 clientWindow;

   public WebSocketClient( String hostAdressPort, ClientPuissance4 cw){
      clientWindow=cw;
      uri = "ws://"+hostAdressPort+"/puissance4";
      try{
         WebSocketContainer container=ContainerProvider.
            getWebSocketContainer();
         container.connectToServer(this, new URI(uri));

      } catch(Exception ex){
  		ex.printStackTrace();
      }
   }

   @OnOpen
   public void onOpen(Session session){
      this.session=session;
   }

   @OnMessage
   public void onMessage(String message, Session session) throws JsonParseException, JsonMappingException, IOException{
	   clientWindow.handleServerMessage( message );
   }

   
   public void disconnectServer()  {
	   try {
		session.close();
		session = null;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   
   public void sendMessage(String message){
      try {
        session.getBasicRemote().sendText(message);
      } catch (IOException ex) {
  		ex.printStackTrace();
      }
   }
}
