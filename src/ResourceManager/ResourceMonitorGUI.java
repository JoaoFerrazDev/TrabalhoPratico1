package ResourceManager;

import javax.swing.*;
import java.awt.*;

public class ResourceMonitorGUI
{
    private JFrame windowFrame;
    private JTextArea alertsArea;

    public ResourceMonitorGUI()
    {
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
    }

    public void addAlert(String alert)
    {
        if (!alert.endsWith("\n"))
            alert += "\n";
        alertsArea.append(alert);
    }

    public static ResourceMonitorGUI newInstance()
    {
        return new ResourceMonitorGUI();
    }

}