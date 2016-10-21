import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.SystemColor;

public class ScannerFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	//private DefaultListModel modelo;
	static JTextArea textArea = new JTextArea();
	private static JTextField textField_2;
	static JLabel lblIpAEscanear = new JLabel("IP a escanear:");
	static JButton button = new JButton("Escanear puertos");


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScannerFrame frame = new ScannerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ScannerFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 481, 354);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		//modelo = new DefaultListModel();
		
		//modelo.addElement("IP");
		
		ButtonGroup group = new ButtonGroup();
		
		JRadioButton rdbtnTodaLaRed = new JRadioButton("Toda la red");		
		group.add(rdbtnTodaLaRed);
		
		rdbtnTodaLaRed.setBackground(new Color(255, 255, 255));
		rdbtnTodaLaRed.setBounds(26, 49, 109, 23);
		contentPane.add(rdbtnTodaLaRed);
		
		JRadioButton rdbtnSegmentoDeRed = new JRadioButton("Segmento de red");
		group.add(rdbtnSegmentoDeRed);
		
		rdbtnSegmentoDeRed.setBackground(new Color(255, 255, 255));
		rdbtnSegmentoDeRed.setBounds(145, 49, 161, 23);
		contentPane.add(rdbtnSegmentoDeRed);
		
		textField = new JTextField();
		textField.setEnabled(false);
		
		textField.setBounds(154, 79, 135, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblA = new JLabel("a");
		lblA.setBounds(299, 82, 21, 14);
		contentPane.add(lblA);
		
		textField_1 = new JTextField();
		textField_1.setEnabled(false);
		textField_1.setBounds(320, 79, 135, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblLanScanner = new JLabel("LAN SCANNER");
		lblLanScanner.setForeground(new Color(25, 25, 112));
		lblLanScanner.setBounds(154, 11, 152, 31);
		contentPane.add(lblLanScanner);
		
		JButton btnEscanear = new JButton("Escanear");
		
		btnEscanear.setBounds(346, 110, 89, 23);
		contentPane.add(btnEscanear);
		
		
		textArea.setBackground(SystemColor.inactiveCaptionBorder);
		textArea.setBounds(9, 197, 445, 107);
		contentPane.add(textArea);
		
		textField_2 = new JTextField();
		textField_2.setVisible(false);
		textField_2.setColumns(10);
		textField_2.setBounds(20, 166, 269, 20);
		contentPane.add(textField_2);
		
		
		button.setVisible(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String ip = textField_2.getText();
				escanearPuertos(ip);				
			}
		});
		button.setBounds(299, 163, 156, 23);
		contentPane.add(button);
		
		lblIpAEscanear.setVisible(false);
		lblIpAEscanear.setBounds(26, 141, 97, 14);
		contentPane.add(lblIpAEscanear);
		
		//EVENTOS 
		
		rdbtnTodaLaRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Toda la red");
				textField.setEnabled(false);
				textField_1.setEnabled(false);
				
			}
		});
		
		rdbtnSegmentoDeRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Rango");
				
				if(rdbtnSegmentoDeRed.isSelected()){
					textField.setEnabled(true);
					textField_1.setEnabled(true);
				}else{
					textField.setEnabled(false);
					textField_1.setEnabled(false);
				}
			}
		});
		
		btnEscanear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//textArea.setText("");				
				if(rdbtnTodaLaRed.isSelected()){
					textArea.append(System.getProperty("line.separator"));
					textArea.append("Escaneando...\n"+ '\n');
					System.out.println("Escaneando...");
					escanearTodo();
				}else{
					if(rdbtnSegmentoDeRed.isSelected()){
						textArea.append("Escaneando..."+ '\n');
						System.out.println("Escaneando...");
						escanearPorRango(Integer.parseInt(textField.getText()),Integer.parseInt(textField_1.getText()));
					}else{
						textArea.append("Seleccione una opción...");
					}
				}
			}
		});
	}
	
	//ESCANEAR TODAS LAS IP EN LA RED
	public static void escanearTodo()
    {
        int Host = 1, activos = 0;
        Boolean activa = false;
        textArea.append("Host activos:"+ '\n');
        while(Host < 256)
            {
                try
                    {
                		//System.out.println("Buscando otra ip..");
                        InetAddress in;
                        in = InetAddress.getByName("192.168.1."+Host);
                        if(in.isReachable(3000))
                            {
                                System.out.println("-IP: 192.168.1."+Host+" Hostname: "+in.getHostName());
                                textArea.append("-IP: 192.168.1."+Host+" Hostname: "+in.getHostName()+ '\n');
                                activos++;         
                                activa = true;
                            }
                        if(activa == true){
                        	//escanearPuertos("192.168.1."+Host);         
                        }
                    }
                        catch(UnknownHostException UHE)
                            {
                                System.out.println(UHE.toString());
                            }
                        catch(IOException IO)
                            {
                                System.out.println(IO.toString());
                            }
                        Host++;
            }
                 System.out.println("Número de Host activos: " +activos);
                 lblIpAEscanear.setVisible(true);
                 textField_2.setVisible(true);
                 button.setVisible(true);
    }
	
	//ESCANEAR POR RANGO
	
	public static void escanearPorRango(int inicio, int fin)
    {
            int Host = inicio, activos = 0;
            Boolean activa = false;
            
            textArea.append("Host activos:"+ '\n');

            while(Host <= fin)
                {
                    try
                        {
                            InetAddress in;
                            in = InetAddress.getByName("192.168.1."+Host);
                            if(in.isReachable(3000))
                                {
                                    System.out.println("-IP: 192.168.1."+Host+" Hostname: "+in.getHostName()+ '\n');
                                    textArea.append("-IP: 192.168.1."+Host+" Hostname: "+in.getHostName()+ '\n');

                                    activos++;
                                    activa = true;
                                }
                            if(activa == true){
                            	//escanearPuertos("192.168.1."+Host);         
                            }
                        }
                            catch(UnknownHostException UHE)
                                {
                                    System.out.println(UHE.toString());
                                }
                            catch(IOException IO)
                                {
                                    System.out.println(IO.toString());
                                }
                            Host++;
                }
                     System.out.println("Número de Hosts en la red: " +activos);
                     lblIpAEscanear.setVisible(true);
                     textField_2.setVisible(true);
                     button.setVisible(true);
        }
	
	
	//ESCANEAR PUERTOS DE UNA IP
	public static void escanearPuertos(String ip)
    {
		textArea.append("Puertos de: " + ip +'\n');
		System.out.println("Escanenado puertos de: " + ip);
		String direccion = ip;
		Boolean b_encontro = false;
		
		for(int puerto = 1;puerto<=65500;puerto++){
		      try{    	  
				 Socket socket= new Socket (direccion,puerto);
				 System.out.println("- Puerto "+puerto+" Abierto");
				 textArea.append("- Puerto "+puerto+" Abierto"+'\n');
				 socket.close();
				 b_encontro = true;
		      } catch (IOException e)
		      {
		      }      
		}
		System.out.println("fin de puertos");
		if (b_encontro == false){
			System.out.println("No se encontraron puertos abiertos en esta IP");			
		}
    }
}
