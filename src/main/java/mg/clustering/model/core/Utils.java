package mg.clustering.model.core;

public class Utils {
    public static boolean isValidIpV4(String ipv4) {
        return ipv4.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }

    public static boolean isValidPort(String port) {
        return port.matches("^[0-9]{1,5}$");
    }

    public static boolean ping(String ipv4) {
        String[] command = {"ping", "-c", "1", ipv4};
        try {
            Process process = Runtime.getRuntime().exec(command);
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPortOpen(String ipv4, String port) {
        String[] command = {"nc", "-z", ipv4, port};
        try {
            Process process = Runtime.getRuntime().exec(command);
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
