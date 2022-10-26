import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {

    JFrame frame; // Jframe to display a frame
    JTextArea txtArea;  // text Area to diplays messages
    JTextField txtField;    // text field to get the message
    JButton btnExit;        // button to exit the group

    // Internet Address for the group
    InetAddress group;
    // variable/instance of  Multicast socket  
    MulticastSocket socket;
    int port;       // port number
    String name;    // name of the user 
    static int threadNo;
    String Ip;
    boolean isGrouped;

    // defualt constructor for the Client Class
    public Client() {
        initGUI();  // function calling to initialize GUI components
        isGrouped = false;
        port = 1;
    }

    // function defination of inintGUI function
    void initGUI() {
        frame = new JFrame();     // initialzing the frame
        frame.setLayout(new FlowLayout());  // setting the layout of the frame

        txtArea = new JTextArea(20, 35);
        txtArea.setFont(new Font("verdana", Font.PLAIN, 15));   // setting font of the text Area
        txtArea.setText("Enter Your Name : ");
        txtArea.setEditable(false);         // disabling the text Area to be editable so that no user can edit the data 
        txtField = new JTextField(20);
        txtField.setFont(new Font("Verdana", Font.PLAIN, 20));

        // key listener to checking the which key is pressed 
        txtField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // do nothing
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {     // checking if enter is pressed
                    try {
                        String s = txtField.getText();
                        txtField.setText("");
                        if (name == null) {     // checking if user has not entered his name
                            if (s == null) {
                                return;
                            }
                            name = s;
                            frame.setTitle(name);
                            txtArea.append(name + "\n"+"Enter The IP address : ");
                            //sendData(name + " is Connected\n");       // sendData funciton to send data through udp sockets 
                            return;
                        } else if (Ip == null) {
                            Ip = s;
                            txtArea.append(Ip + "\n"+"Enter the Port Number : ");
                            return;
                        } else if (port == 1) {
                            port = Integer.parseInt(s);
                            txtArea.append(port + "\n");
                            setIP();
                            return;
                        } else {
                            if (isGrouped) {
                                if (!s.equals("")) {
                                    sendData(name + ": " + s + "\n");        // sendData funciton to send data through udp sockets
                                }
                            } else {
                                JOptionPane.showMessageDialog(frame, "Connetion Not made Yet", "Alert", JOptionPane.WARNING_MESSAGE);
                            }

                        }

                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // do nothing
            }
        });

        btnExit = new JButton("Exit");    // initializing the exit button 
        btnExit.setPreferredSize(new Dimension(70, 30));
        // adding action listener to check if button is pressed
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(socket!=null){
                    sendData(name + " has been DisConnected\n");
                    // closing the socket connection
                    socket.close();
                    }
                    frame.setVisible(false);
                    frame.dispose();
                    System.exit(0);

                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        JScrollPane txtScroll = new JScrollPane(txtArea);
        // adding components to the frame
        frame.add(txtScroll);
        frame.add(txtField);
        frame.add(btnExit);

        // setting the size of frame
        frame.setSize(550, 500);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public boolean setIP() {
        // exception handling
        try {
            if (Ip.equals("234.235.236.237") && port == 12540) {
                // initializing instances 
                group = InetAddress.getByName(Ip);
                socket = new MulticastSocket(port);
                socket.joinGroup(group);
                isGrouped = true;
                txtArea.append("Congrats Connection Has been Made !!!\n");

                // creating and initialzing the instance of ClientHandler class
                ClientHandler clientHandler = new ClientHandler(this);

                /*
            Multi Threading 
                 */
                new Thread(clientHandler).start();

                return true;
            } else {
                txtArea.setText("Try Again with Correct IP and Port#:\nEnter Your Name : " + name + "\n");
                Ip = null;
                port = 1;
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Something Went Wrong, Check your IP or Port# !", "Alert", JOptionPane.WARNING_MESSAGE);
            txtArea.setText("Enter Your Name : " + name + "\n");
            Ip = null;
            port = 1;
            System.out.println(e.toString());
            return false;
        }

    }

    // function defination of the sendData function
    public void sendData(String msg) throws IOException {
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), group, port);
        socket.send(packet);    // sending the data packets
    }

    // main methode of the class
    public static void main(String[] args) {
        // creating and initialzing the instance of Client class
        Client client = new Client();

    }

}
