package org.example;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.currentThread;

public class GraphicInterface
{
    private JFrame frame;
    private JPanel panel;
    private JTextField noOfClients;
    private JTextField noOfQueues;
    private JTextField simulationInterval;
    private JTextField minArrivalTime;
    private JTextField maxArrivalTime;
    private JTextField minServiceTime;
    private JTextField maxServiceTime;
    private JTextField nameOfFile;
    private JButton startButton;
    private JButton stopButton;
    private JComboBox<Policy> policy;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private SimulationManager sm;
    public GraphicInterface()
    {
        frame = new JFrame("Queue Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 960);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setLayout(new GridLayout(11, 2));
        noOfClients = new JTextField();
        noOfQueues = new JTextField();
        simulationInterval = new JTextField();
        minArrivalTime = new JTextField();
        maxArrivalTime = new JTextField();
        minServiceTime = new JTextField();
        maxServiceTime = new JTextField();
        nameOfFile = new JTextField();
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        policy = new JComboBox<>(Policy.values());
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        panel.add(new JLabel("Name of file, to save the output"));
        panel.add(nameOfFile);
        panel.add(new JLabel("No of clients"));
        panel.add(noOfClients);
        panel.add(new JLabel("No of queues"));
        panel.add(noOfQueues);
        panel.add(new JLabel("Simulation interval"));
        panel.add(simulationInterval);
        panel.add(new JLabel("Min arrival time"));
        panel.add(minArrivalTime);
        panel.add(new JLabel("Max arrival time"));
        panel.add(maxArrivalTime);
        panel.add(new JLabel("Min service time"));
        panel.add(minServiceTime);
        panel.add(new JLabel("Max service time"));
        panel.add(maxServiceTime);
        panel.add(new JLabel("Policy"));
        panel.add(policy);
        panel.add(startButton);
        panel.add(stopButton);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
        AtomicReference<PrintWriter> writer = new AtomicReference<>();
        AtomicReference<Boolean> started = new AtomicReference<>(false);
        AtomicReference<Boolean> stopped = new AtomicReference<>(true);
        startButton.addActionListener(e -> {
            int noOfClientsValue;
            int noOfQueuesValue;
            int simulationIntervalValue;
            int minArrivalTimeValue;
            int maxArrivalTimeValue;
            int minServiceTimeValue;
            int maxServiceTimeValue;
            try {
                noOfClientsValue = Integer.parseInt(noOfClients.getText());
                noOfQueuesValue = Integer.parseInt(noOfQueues.getText());
                simulationIntervalValue = Integer.parseInt(simulationInterval.getText());
                minArrivalTimeValue = Integer.parseInt(minArrivalTime.getText());
                maxArrivalTimeValue = Integer.parseInt(maxArrivalTime.getText());
                minServiceTimeValue = Integer.parseInt(minServiceTime.getText());
                maxServiceTimeValue = Integer.parseInt(maxServiceTime.getText());
            } catch (NumberFormatException ex) {
                textArea.setText("Invalid input");
                return;
            }
            if(minArrivalTimeValue > maxArrivalTimeValue || minServiceTimeValue > maxServiceTimeValue) {
                textArea.setText("Invalid input");
                return;
            }
            if(minArrivalTimeValue < 0 || maxArrivalTimeValue < 0 || minServiceTimeValue < 0 || maxServiceTimeValue < 0) {
                textArea.setText("Invalid input");
                return;
            }
            if(noOfClientsValue < 0 || noOfQueuesValue < 0 || simulationIntervalValue < 0) {
                textArea.setText("Invalid input");
                return;
            }
            if(minArrivalTimeValue>simulationIntervalValue || maxArrivalTimeValue>simulationIntervalValue) {
                textArea.setText("Invalid input");
                return;
            }
            if(minServiceTimeValue>simulationIntervalValue || maxServiceTimeValue>simulationIntervalValue) {
                textArea.setText("Invalid input");
                return;
            }
            Policy policyValue = (Policy) policy.getSelectedItem();
            String fileName = nameOfFile.getText();
            try{
                writer.set(new PrintWriter(fileName + ".txt", "UTF-8"));

            }catch (FileNotFoundException | UnsupportedEncodingException ex){
                textArea.setText("Invalid file name");
            }
            if(stopped.get())
            {
                textArea.setText("");
                started.set(true);
                stopped.set(false);
                sm = new SimulationManager(noOfClientsValue, noOfQueuesValue, simulationIntervalValue, minArrivalTimeValue, maxArrivalTimeValue, minServiceTimeValue, maxServiceTimeValue, policyValue, writer.get(), textArea);
                Thread t = new Thread(sm);
                noOfClients.setEditable(false);
                noOfQueues.setEditable(false);
                simulationInterval.setEditable(false);
                minArrivalTime.setEditable(false);
                maxArrivalTime.setEditable(false);
                minServiceTime.setEditable(false);
                maxServiceTime.setEditable(false);
                nameOfFile.setEditable(false);
                t.start();
            }
        });
        stopButton.addActionListener(e -> {
            if(sm != null&&started.get())
            {
                started.set(false);
                stopped.set(true);
                sm.stopSimulation();
                noOfClients.setEditable(true);
                noOfQueues.setEditable(true);
                simulationInterval.setEditable(true);
                minArrivalTime.setEditable(true);
                maxArrivalTime.setEditable(true);
                minServiceTime.setEditable(true);
                maxServiceTime.setEditable(true);
                nameOfFile.setEditable(true);
            }
        });
    }
}
