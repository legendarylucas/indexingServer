package Modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import Interfaces.ProcessRequest;

public class Server implements Runnable, ProcessRequest{
	public final static String TAG="SERVER>";
	private ServerSocket serverSocket = null;
	private int listeningPort=0;
	private Socket socket=null;
	private boolean terminate=false;
	
	public Server(int port){
		this.listeningPort=port;
		
	}
	
	public void stopServer(){
		terminate=true;
	}

	@Override
	public void run() {
		try {
            serverSocket = new ServerSocket(listeningPort);
        } catch (IOException e) {
            Utils.log(TAG, e.toString());
        }
		
		while (true) {
			//terminate server if thread is interrupted or forced to stop
			if(terminate||Thread.currentThread().isInterrupted()){
                try {
                	Utils.log(TAG, "Server is terminated");
                	if(socket!=null){
                		socket.close();
                	}
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
			try {
                BufferedReader in = null;
                socket = serverSocket.accept();
                socket.setReuseAddress(true);
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                
                PrintStream output = new PrintStream(socket.getOutputStream());

                String read = in.readLine();
                if( read != null) {
                	InetAddress remoteAddress=socket.getInetAddress();
                    String result=process(read);
                    output.println(result);
                    output.flush();
                    
                }else {
                    socket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
		}
		terminate=true;
	}

	@Override
	public String process(String read) {
		Utils.log(TAG, "data received " + read);
		return null;
	}

}