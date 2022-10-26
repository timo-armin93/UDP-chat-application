import com.sun.jdi.request.ThreadDeathRequest;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

// class ClientHandler implementing the Runnable Interface
// for multithreading
public class ClientHandler implements Runnable {

    // creating the client instance so that
    // we can handle the Client components passed through 
    // the perameters
    final Client refgClient;

    // perameterized constructor
    public ClientHandler(Client c) {
        refgClient = c;
        refgClient.threadNo++;
    }

    // overriding and defining the run function of Runnable interface
    @Override
    public void run() {
        // starting a loop 
        while (true) {
            
            try {
                byte buf[] = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                // recieving the packets 
                refgClient.socket.receive(packet);

                // parsing the packet into the string
                String str2 = new String(packet.getData(), 0, packet.getLength());

                // checking if the user entered his name
                if (!str2.contains("Connected")) {
                    // displaying messages
                    refgClient.txtArea.append(str2 + "\n");
                } else if(str2!=null){
                    refgClient.txtArea.append(refgClient.name + "\n");
                }

                refgClient.txtField.setText("");
                packet = null;
            } catch (IOException ex) {
                Thread.interrupted();
                //Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
