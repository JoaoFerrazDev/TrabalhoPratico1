package ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResourceMonitorGUI {
    private JFrame windowFrame;
    private JTextArea alertsArea;
    private AtomicBoolean isRunning;

    public ResourceMonitorGUI(AtomicBoolean isRunning) {
        windowFrame = new JFrame("Resource Monitor GUI");
        windowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        windowFrame.setResizable(false);
        alertsArea = new JTextArea();
        alertsArea.setBounds(0,0, 500,500);
        alertsArea.setMargin(new Insets(10, 10, 10, 10));
        alertsArea.setLineWrap(true);
        alertsArea.setWrapStyleWord(true);
        alertsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(alertsArea);
        windowFrame.add(scrollPane);
        windowFrame.setSize(500, 500);
        windowFrame.setBackground(Color.WHITE);
        windowFrame.setVisible(true);
        JButton stopButton = new JButton("Stop");
        this.isRunning = isRunning;
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isRunning.set(false);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(stopButton);
        windowFrame.add(buttonPanel, BorderLayout.NORTH);

    }

    public void addAlert(String alert) {
        if (!alert.endsWith("\n"))
            alert += "\n";
        alertsArea.append(alert);
    }

    public static ResourceMonitorGUI newInstance(AtomicBoolean isRunning) {
        return new ResourceMonitorGUI(isRunning);
    }
}
