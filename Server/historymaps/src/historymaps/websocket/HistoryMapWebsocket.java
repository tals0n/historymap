package historymaps.websocket;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import historymaps.api.HistoryMapApi;

@ServerEndpoint(value = "/", configurator=EndpointConfig.class)
public class HistoryMapWebsocket {
	
	private HistoryMapApi historyMapApi;
	private Integer requestCounter = 0;
	
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApiCommand {
		String descriptor();
	}
	
	@OnOpen
	public void onOpen(Session session) {
		try {
			historyMapApi = (HistoryMapApi)session.getUserProperties().get(EndpointConfig.HISTORY_MAP_API);
			System.out.println("[" + session.getId() + "] joined");
		} catch(RuntimeException ex1) {
			try {
				ex1.printStackTrace();
				Gson gson = new Gson();
	    		session.getBasicRemote().sendText(gson.toJson(new Error("3")));
			} catch (IOException ex2) {
				ex2.printStackTrace();
			}
		}
	}
	
	@OnClose
    public void onClose(Session session) {
		System.out.println("[" + session.getId() + "] left");
    }

	@OnMessage
    public void onMessage(Session session, String request) {
		requestCounter++;
    	Gson gson = new Gson();
    	try {
			String descriptor = null;
			System.out.println("inc: " + request);
			JsonReader jsonReader = new JsonReader(new StringReader(request));
			jsonReader.setLenient(true);
			jsonReader.beginObject();
			if(jsonReader.hasNext()) {
				descriptor = jsonReader.nextName();
			}
			if(descriptor != null) {
				Method[] methods = historyMapApi.getClass().getDeclaredMethods();
				for(Method method: methods) {
					ApiCommand apiCommand = method.getAnnotation(ApiCommand.class);
					if(apiCommand != null && apiCommand.descriptor().equals(descriptor) && method.getParameterCount() == 1) {
						if(jsonReader.hasNext()) {
							method.setAccessible(true);
							Object requestObject = gson.fromJson(jsonReader, method.getParameterTypes()[0]);
							try {
								Object responseObject = method.invoke(historyMapApi, requestObject);
								Response response = new Response(requestCounter, responseObject);
								String text = gson.toJson(response);
								System.out.println("out: " + text);
								session.getBasicRemote().sendText(text);
							} catch (InvocationTargetException ex) {
								String exMessage = ex.getCause().getMessage();
								Response response = new Response(requestCounter, exMessage);
								String text = gson.toJson(response);
								System.out.println("out: " + text);
								session.getBasicRemote().sendText(text);
							}
							jsonReader.close();
							return;
						}
					}
				}
			}
			jsonReader.close();
		} catch (SecurityException | IllegalAccessException | IOException ex1) {
			ex1.printStackTrace();
		} catch (JsonSyntaxException | IllegalArgumentException | JsonIOException ex1) {
			try {
				// json syntax error
				session.getBasicRemote().sendText(gson.toJson(new Response(requestCounter, "input error")));
				return;
			} catch (IOException ex2) {
				ex2.printStackTrace();
			}
			ex1.printStackTrace();
		}
    	try {
    		// falls es keine passende "ein parameter"-methode mit dem in json angegebenem descriptor gibt
    		session.getBasicRemote().sendText(gson.toJson(new Response(requestCounter, "code: 2")));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    }
}