package data_management;

import com.data_management.DataStorage;
import com.data_management.MyWebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
public class MyWebSocketClientTest {

    private DataStorage mockDataStorage;
    private MyWebSocketClient client;
    private URI serverUri;

    //Basic setup
    @BeforeEach
    void setUp() throws URISyntaxException {
        serverUri = new URI("1.2.2.etc");
        client = new MyWebSocketClient(serverUri, mockDataStorage);
    }

    //making sure you can close succesfully
    @Test
    void onCloseTest() {
        client.onClose(100, "Closed", true);
        assertTrue(true);
    }

    @Test
    void onErrorTest() {
        Exception exception = new Exception("Exception");
        client.onError(exception);
        assertTrue(true);
    }

    @Test
    void connectTest() {
        try {
            client.connect();
            assertTrue(true);
        } catch (Exception e) {
            fail("Connection failed :(");
        }
    }

    @Test
    void disconnectTest() {
        try {
            client.disconnect();
            assertTrue(true);
        } catch (Exception e) {
            fail("Disconnection failed, you're locked in!");
        }
    }
}
