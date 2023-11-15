package mg.clustering.model.core;

import com.jcraft.jsch.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSHTransfert extends Transfert {
    public SSHTransfert(String ipv4, String username, String password, String port) {
        super(ipv4, username, password, port);
    }

    Session getSession() {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(getUsername(), getHost(), 22);
            session.setPassword(getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException("Error while connecting to SSH server " + getUsername() + "@" + getHost() + ":" + getPort());
        }
        return session;
    }

    @Override
    public void upload(String sourcePath, String destinationPath) {
        try {
            System.out.println("1dfghj");
            Session session = getSession();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            System.out.println("2dfghj");
            channelSftp.put(sourcePath, destinationPath);
            System.out.println("4dfghj");

            channelSftp.disconnect();
            session.disconnect();

            session.disconnect();
            System.out.println("3dfghj");
        } catch (JSchException | SftpException e) {
            throw new RuntimeException("Error while transferring file via SSH to " + getHost() + " : " + e.getMessage());
        }
    }

    @Override
    public void execute(String command) {
        try {
            Session session = getSession();

            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.connect();

            channelExec.disconnect();
            session.disconnect();

            session.disconnect();
        } catch (JSchException e) {
            throw new RuntimeException("Error while executing command via SSH to " + getHost() + " : " + e.getMessage());
        }
    }
}
