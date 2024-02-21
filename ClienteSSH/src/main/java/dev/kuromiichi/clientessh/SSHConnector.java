package dev.kuromiichi.clientessh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;

public class SSHConnector {

    public static void main(String[] args) {
        SSHConnector sshConnector = new SSHConnector();

        try {
            sshConnector.connect("localhost", 2222, "emma", "sge", "ls -la /home/emma");
        } catch (JSchException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void connect(String host, int port, String username, String password, String command)
        throws JSchException, InterruptedException {
        Session session;
        ChannelExec channel;

        session = new JSch().getSession(username, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        channel.setOutputStream(responseStream);
        channel.connect();

        while (channel.isConnected()) {
            Thread.sleep(100);
        }

        String response = responseStream.toString();
        System.out.println(response);

        channel.disconnect();
        session.disconnect();
    }
}
