
import java.awt.FlowLayout;
import java.awt.Font;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Server {

    JFrame frame;
    JTextArea txtArea;

    // default constructor of the class
    public Server() {
        initGUI();  // calling function to initialzing the Gui components
    }

    // function defination of the initGui function
    public void initGUI() {
        frame = new JFrame();
        frame.setLayout(new FlowLayout());

        txtArea = new JTextArea(20, 35);
        txtArea.setFont(new Font("verdana", Font.PLAIN, 15));
        txtArea.setEditable(false);

        frame.add(txtArea);

        frame.setSize(550, 500);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // main method of the class
    public static void main(String[] args) {
        // creating and initialzing the instances of the class
        Server server = new Server();
        String str2;
        try {
            int port = 12540;
            // initializing and setting the IP-Address of the InetAddress
            InetAddress group = InetAddress.getByName("234.235.236.237");
            
            // exception handling
            try ( MulticastSocket socket = new MulticastSocket(port)) {
                // joining the group 
                socket.joinGroup(group);
                server.txtArea.setText("Server is Running.....");
                while (true) {
                    byte buf[] = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);

                    // recieving the packets send through the sockets
                    socket.receive(packet);
                    
                    // parsing the packet into string 
                    str2 = new String(packet.getData(), 0, packet.getLength());

                    
                    // displaying in the text Area
                    //s.txtArea.append(str2 + "\t");
                    

                    // sending back to all connected group  
                    socket.send(packet);
                }

            }

        } catch (Exception e) {
            server.txtArea.setText("Server is Terminated.....");
            System.out.println(e.toString());
        }
    }

}
