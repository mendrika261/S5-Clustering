package mg.clustering.model.core;

public class Utils {
    public static boolean isValidIpV4(String ipv4) {
        return ipv4.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }

    public static boolean isValidPort(String port) {
        return port.matches("^[0-9]{1,5}$");
    }
}
