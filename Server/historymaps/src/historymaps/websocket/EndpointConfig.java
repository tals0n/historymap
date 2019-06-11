package historymaps.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import historymaps.api.HistoryMapApi;

public class EndpointConfig extends ServerEndpointConfig.Configurator {

	public static final String HISTORY_MAP_API = "HISTORY_MAP_API";
	public static final String HTTP_SESSION = "HTTP_SESSION";
	private HistoryMapApi historyMapApi;
	
	@Override
	public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
		HttpSession httpSession = (HttpSession)request.getHttpSession();
		historyMapApi = HistoryMapApi.getHistoryMapApi(httpSession.getServletContext());
		config.getUserProperties().put(HISTORY_MAP_API, historyMapApi);
		config.getUserProperties().put(HTTP_SESSION, httpSession);
	}

}
