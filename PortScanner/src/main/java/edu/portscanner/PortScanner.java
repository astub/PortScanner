package edu.portscanner;

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PortScanner extends Frame {
    
    //Globals
    static boolean InSufficientDataForScan = false;
    Socket SOCK;
    int PORT_START;
    int PORT_END;
    int PortStart;
    int PortEnd;
    String TARGET;
    Frame MainWindow = new Frame("Port Scanner");
    JLabel L_Title = new JLabel("Port Scanner",JLabel.CENTER);
    JLabel L_Target = new JLabel("Target:");
    JLabel L_Port_Start = new JLabel("Starting Port:");
    JLabel L_Port_End = new JLabel("Ending Port:");
    JLabel L_Status = new JLabel("Ready. Click SCAN.",JLabel.CENTER);
    JTextField TF_Target = new JTextField(15);
    JTextField TF_PORT_START = new JTextField(15);
    JTextField TF_PORT_END = new JTextField(15);
    JTextArea TA_Message = new JTextArea();
    JScrollPane SP_Message = new JScrollPane();
    JButton B_Scan = new JButton("SCAN");
    JButton B_Reset = new JButton("RESET");
    
    public static void main(String[] args) {
        new PortScanner();
    }
    
    public PortScanner() {
        BuildGUI();
    }

    private void BuildGUI() {
        MainWindow.setLayout(new GridLayout(5,1));
        MainWindow.setSize(180,320);
        MainWindow.setResizable(false);
        Panel UpperPanel = new Panel(new GridLayout(3,3));
        
        L_Title.setForeground(Color.red);
        L_Title.setFont(new Font("TimesRoman",Font.BOLD,20));
        MainWindow.add(L_Title);
        
        L_Target.setForeground(Color.black);
        UpperPanel.add(L_Target);
        
        TF_Target.setForeground(Color.black);
        TF_Target.setFocusable(true);
        TF_Target.requestFocus();
        UpperPanel.add(TF_Target);
        
        L_Port_Start.setForeground(Color.black);
        UpperPanel.add(L_Port_Start);
        
        TF_PORT_START.setForeground(Color.black);
        TF_PORT_START.setFocusable(true);
        UpperPanel.add(TF_PORT_START);
        
        L_Port_End.setForeground(Color.black);
        UpperPanel.add(L_Port_End);
        
        TF_PORT_END.setForeground(Color.black);
        TF_PORT_END.setFocusable(true);
        UpperPanel.add(TF_PORT_END);
        
        MainWindow.add(UpperPanel);
        
        Panel LowerPanel = new Panel();
        JLabel SPACER = new JLabel();
        LowerPanel.add(B_Scan);
        LowerPanel.add(SPACER);
        LowerPanel.add(B_Reset);
        MainWindow.add(LowerPanel);
        
        SP_Message.setViewportView(TA_Message);
        SP_Message.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SP_Message.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        MainWindow.add(SP_Message);
        
        L_Status.setForeground(Color.red);
        MainWindow.add(L_Status);
        
        AddEventHandelers();
        
        MainWindow.setVisible(true);
        MainWindow.repaint();
        
    }

    private void AddEventHandelers() {
        
        MainWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
        
        B_Scan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent r) {
                B_SCAN_ACTION();
            } 
        });
        
        B_Reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent t) {
                B_RESET_ACTION();
            }
        });
    }
    
    public void B_SCAN_ACTION() {
        
        if(TF_Target.getText().equals("")){
            TA_Message.setText("Enter a TARGET.");
            return;
        } else if (TF_PORT_START.getText().equals("")) {
            TA_Message.setText("Enter a START port.");
            return;
        } else if (TF_PORT_END.getText().equals("")) {
            TA_Message.setText("Enter a END port.");
            return;
        }
        
        InSufficientDataForScan = false;
        
        TA_Message.setText("");
        
        Thread X1 = new Thread() {
            public void run() {
                
                B_Scan.setEnabled(false);
                B_Reset.setText("Stop");
                
                TARGET = TF_Target.getText();
                PortStart = Integer.parseInt(TF_PORT_START.getText());
                PortEnd = Integer.parseInt(TF_PORT_END.getText());
                
                for(int x = PortStart; x <= PortEnd; x++) 
                {
                    L_Status.setText("Port " + x + " is being tested");
                    
                    if(InSufficientDataForScan)
                    break;
                    
                    try 
                    {
                        SOCK = new Socket();
                        SOCK.connect(new InetSocketAddress(TARGET, x), 1000); //ms
                        
                        TA_Message.append("Port " + x + " is open.\n");
                        SOCK.close();
                    } catch(Exception X) 
                    {
                        //TA_Message.append("Port " + x + " is closed.\n");
                        continue;
                    }
                }
                
                B_Scan.setEnabled(true);
                B_Reset.setText("RESET");
                L_Status.setText("Press Scan to Start.");
            }
        };
        
        X1.start();
    }
    
    public void B_RESET_ACTION() {
        InSufficientDataForScan = true;
        TF_Target.setText("");
        TF_PORT_START.setText("");
        TF_PORT_END.setText("");
        TA_Message.setText("");
        B_Reset.setText("STOP");
    }
    
}

