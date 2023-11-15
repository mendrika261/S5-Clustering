package mg.clustering.model.core;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPTransfert extends Transfert {
  public FTPTransfert(String ipv4, String username, String password,
                      String port) {
    super(ipv4, username, password, port);
  }

  FTPClient getClient() {
    FTPClient ftpClient = new FTPClient();
    try {
      ftpClient.connect(getHost(), Integer.parseInt(getPort()));
      ftpClient.login(getUsername(), getPassword());
      ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    } catch (IOException e) {
      throw new RuntimeException("Error while connecting to FTP server " +
                                 getUsername() + "@" + getHost() + ":" +
                                 getPort());
    }
    return ftpClient;
  }

  @Override
  public void upload(String sourcePath, String destinationPath) {
    try (FileInputStream localFile = new FileInputStream(sourcePath)) {
      getClient().storeFile(destinationPath, localFile);
      getClient().logout();
      getClient().disconnect();
    } catch (IOException e) {
      throw new RuntimeException("Error while uploading file " + sourcePath +
                                 " to " + getHost() + ":" + getPort());
    }
  }

  @Override
  public void execute(String command) {
    throw new UnsupportedOperationException(
        "FTP does not support command execution");
  }
}
