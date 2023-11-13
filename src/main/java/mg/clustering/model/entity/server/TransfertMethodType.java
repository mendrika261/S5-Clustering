package mg.clustering.model.entity.server;

import lombok.Getter;

@Getter
public enum TransfertMethodType {
    FTP("FTP"),
    SSH("SSH/SFTP"),
    RSYNC("RSYNC");

    final String name;

    TransfertMethodType(String name) {
        this.name = name;
    }
}
