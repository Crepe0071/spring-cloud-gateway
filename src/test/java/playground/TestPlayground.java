package playground;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestPlayground {

    public static void main(String[] args) throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        String ipAddress = localHost.getHostAddress();

        System.out.println(ipAddress);
    }
}
