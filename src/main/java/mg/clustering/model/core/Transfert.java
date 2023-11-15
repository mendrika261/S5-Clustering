package mg.clustering.model.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Transfert {
  String host;
  String username;
  String password;
  String port;

  public Transfert() {}

  public Transfert(String host, String username, String password, String port) {
    setHost(host);
    setUsername(username);
    setPassword(password);
    setPort(port);
  }

  public abstract void upload(String sourcePath, String destinationPath);
  public abstract void execute(String command);
}
