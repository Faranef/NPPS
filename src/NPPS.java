import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class NPPS extends JFrame
{
    private RodModel selectedRod;
    private JComboBox fuelRodComboBox;
    private JComboBox rodComboBox;
    private JLabel fuelRodLabel;
    private JLabel rodLevelLabel;
    private JLabel waterImg;
    private JSlider rodSlider;
    private int sliderValue;
    private RodService rodService;
    private ReactorService reactorService;
    private JLabel fuelRod0;
    private JLabel fuelRod1;
    private JLabel fuelRod2;
    private JLabel fuelRod3;
    private JLabel fuelRod4;
    private JLabel fuelRod5;
    private JLabel fuelRodEnergy0;
    private JLabel fuelRodEnergy1;
    private JLabel fuelRodEnergy2;
    private JLabel fuelRodEnergy3;
    private JLabel fuelRodEnergy4;
    private JLabel fuelRodEnergy5;
    private JLabel controlRods;
    private JLabel tempLabel;
    private JLabel elecLabel;
    private JProgressBar tempBar;
    private JProgressBar fuelRodBar0;
    private JProgressBar fuelRodBar1;
    private JProgressBar fuelRodBar2;
    private JProgressBar fuelRodBar3;
    private JProgressBar fuelRodBar4;
    private JProgressBar fuelRodBar5;
    private JDialog dialog;
    private double tempValue;
    private double wattValue;
    private int timerDelay = 1000;
    private List<RodModel> fuelRodList;

    public NPPS(String name)
    {
        super(name);
        setResizable(false);
        setPreferredSize(new Dimension(1440, 900));
        rodService = RodService.GetInstance();
        CreateRods();
        reactorService = ReactorService.GetInstance();
    }

    private void CreateRods()
    {
        RodModel rod = new RodModel(100, "Control Rod", RodModelStyle.ControlRod);
        rodService.AddRodToList(rod);

        rod = new RodModel(0, "Fuel Rod", RodModelStyle.FuelRod, CreateFuelRods());
        rodService.AddRodToList(rod);

        rod = new RodModel(0, "Moderator", RodModelStyle.Moderator);
        rodService.AddRodToList(rod);
    }

    private List<RodModel> CreateFuelRods()
    {
        List<RodModel> list = new ArrayList<RodModel>();

        for (int i = 1; i < 7; i++)
        {
            RodModel rod = new RodModel(0, Integer.toString(i), RodModelStyle.FuelRod, 100000);
            list.add(rod);
        }

        return list;
    }

    public static void main(String[] args) throws Exception
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    CreateGUI();
                } 
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    protected static void CreateGUI() throws IOException
    {
        NPPS frame = new NPPS("Nuclear Power Plant Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.AddComponentsToPane(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }

    private void AddComponentsToPane(Container contentPane) throws IOException
    {
        JLayeredPane viewPanelLayerd = new JLayeredPane();
        viewPanelLayerd.setOpaque(true);
        viewPanelLayerd.setBackground(new Color(96, 96, 96));

        JPanel controlsPanel = new JPanel();
        controlsPanel.setBackground(new Color(224, 224, 224));
        controlsPanel.setBounds(0, 600, 1000, 300);

        JPanel gaugePanel = new JPanel();
        gaugePanel.setBackground(new Color(151, 166, 176));
        gaugePanel.setBounds(1000, 0, 440, 900);
        gaugePanel.setLayout(new BoxLayout(gaugePanel, BoxLayout.Y_AXIS));

        rodComboBox = new JComboBox(new RodModel[]
        {  
            rodService.GetRod(RodModelStyle.ControlRod), 
            rodService.GetRod(RodModelStyle.FuelRod),
            rodService.GetRod(RodModelStyle.Moderator) 
        });

        selectedRod = rodService.GetRod(RodModelStyle.ControlRod);
        rodComboBox.setSelectedItem(selectedRod.GetRodName());
        rodComboBox.addActionListener(e -> {
            RodComboBoxAction(e);
        });

        sliderValue = rodService.GetRod(RodModelStyle.ControlRod).GetRodLevel();

        fuelRodComboBox = new JComboBox(rodService.GetRod(RodModelStyle.FuelRod).GetRodModelList().toArray());
        fuelRodComboBox.setVisible(false);

        fuelRodComboBox.addActionListener(e -> {
            FuelRodComboBoxChange(e);
        });

        rodSlider = new JSlider(JSlider.VERTICAL, 0, 100, sliderValue);
        rodSlider.addChangeListener(e -> {
            RodSliderChange(e);
        });

        fuelRodLabel = new JLabel("Selected Fuel Rod:");
        fuelRodLabel.setVisible(false);

        rodLevelLabel = new JLabel("Rod Level: " + sliderValue);

        controlsPanel.add(new JLabel("Selected Rod:"));
        controlsPanel.add(rodComboBox);

        controlsPanel.add(fuelRodLabel);
        controlsPanel.add(fuelRodComboBox);

        controlsPanel.add(rodLevelLabel);
        controlsPanel.add(rodSlider);

        var img = ImageIO.read(new File("./Images/case.png"));
        JLabel reactor = new JLabel(new ImageIcon(img));
        reactor.setBounds(0, 0, 1000, 600);
        reactor.setVisible(true);

        var imgCold = ImageIO.read(new File("./Images/water00.png"));
        waterImg = new JLabel(new ImageIcon(imgCold));
        waterImg.setBounds(0, 0, 1000, 600);
        waterImg.setVisible(true);

        var fuelRodImg0 = ImageIO.read(new File("./Images/fuelRod0.png"));
        fuelRod0 = new JLabel(new ImageIcon(fuelRodImg0));
        fuelRod0.setBounds(0, 132, 1000, 0);
        fuelRod0.setVisible(true);

        fuelRod1 = new JLabel(new ImageIcon(fuelRodImg0));
        fuelRod1.setBounds(0, 132, 1060, 0);
        fuelRod1.setVisible(true);

        fuelRod2 = new JLabel(new ImageIcon(fuelRodImg0));
        fuelRod2.setBounds(0, 132, 1120, 0);
        fuelRod2.setVisible(true);

        fuelRod3 = new JLabel(new ImageIcon(fuelRodImg0));
        fuelRod3.setBounds(0, 132, 1180, 0);
        fuelRod3.setVisible(true);

        fuelRod4 = new JLabel(new ImageIcon(fuelRodImg0));
        fuelRod4.setBounds(0, 132, 1240, 0);
        fuelRod4.setVisible(true);

        fuelRod5 = new JLabel(new ImageIcon(fuelRodImg0));
        fuelRod5.setBounds(0, 132, 1300, 0);
        fuelRod5.setVisible(true);

        var controlrodsImg = ImageIO.read(new File("./Images/controlRods.png"));
        controlRods = new JLabel(new ImageIcon(controlrodsImg));
        controlRods.setBounds(0, 132, 1000, sliderValue*3);
        controlRods.setVisible(true);

        tempLabel = new JLabel("Temperature: " + tempValue + " °C");
        tempLabel.setFont(new Font(tempLabel.getName(), Font.PLAIN, 24));
        gaugePanel.add(tempLabel, BorderLayout.CENTER);

        tempBar = new JProgressBar (0,100);
        tempBar.setPreferredSize( new Dimension (100, 25));
        gaugePanel.add(tempBar);

        elecLabel = new JLabel("Wattage: " + wattValue + " W");
        elecLabel.setFont(new Font(elecLabel.getName(), Font.PLAIN, 24));
        gaugePanel.add(elecLabel, BorderLayout.CENTER);

        // fuel rod label show decay

        var fuelRod = rodService.GetRod(RodModelStyle.FuelRod);
        fuelRodList = fuelRod.GetRodModelList();

        fuelRodEnergy0 = new JLabel("Fuelrod " +fuelRodList.get(0).GetRodName() + " fuel "+ fuelRodList.get(0).GetLifeSpan()/1000 +" %");
        fuelRodEnergy0.setFont(new Font(elecLabel.getName(), Font.PLAIN, 24));
        gaugePanel.add(fuelRodEnergy0, BorderLayout.CENTER);

        fuelRodBar0 = new JProgressBar (0,100);
        fuelRodBar0.setPreferredSize( new Dimension (100, 25));
        fuelRodBar0.setForeground(new Color(0,255,255));
        gaugePanel.add(fuelRodBar0);

        fuelRodEnergy1 = new JLabel("Fuelrod " +fuelRodList.get(1).GetRodName() + " fuel "+ fuelRodList.get(1).GetLifeSpan()/1000 +" %");
        fuelRodEnergy1.setFont(new Font(elecLabel.getName(), Font.PLAIN, 24));
        gaugePanel.add(fuelRodEnergy1, BorderLayout.CENTER);

        fuelRodBar1 = new JProgressBar (0,100);
        fuelRodBar1.setPreferredSize( new Dimension (100, 25));
        fuelRodBar1.setForeground(new Color(0,255,255));
        gaugePanel.add(fuelRodBar1);

        fuelRodEnergy2 = new JLabel("Fuelrod " +fuelRodList.get(2).GetRodName() + " fuel "+ fuelRodList.get(2).GetLifeSpan()/1000 +" %");
        fuelRodEnergy2.setFont(new Font(elecLabel.getName(), Font.PLAIN, 24));
        gaugePanel.add(fuelRodEnergy2, BorderLayout.CENTER);

        fuelRodBar2 = new JProgressBar (0,100);
        fuelRodBar2.setPreferredSize( new Dimension (100, 25));
        fuelRodBar2.setForeground(new Color(0,255,255));
        gaugePanel.add(fuelRodBar2);

        fuelRodEnergy3 = new JLabel("Fuelrod " +fuelRodList.get(3).GetRodName() + " fuel "+ fuelRodList.get(3).GetLifeSpan()/1000 +" %");
        fuelRodEnergy3.setFont(new Font(elecLabel.getName(), Font.PLAIN, 24));
        gaugePanel.add(fuelRodEnergy3, BorderLayout.CENTER);

        fuelRodBar3 = new JProgressBar (0,100);
        fuelRodBar3.setPreferredSize( new Dimension (100, 25));
        fuelRodBar3.setForeground(new Color(0,255,255));
        gaugePanel.add(fuelRodBar3);

        fuelRodEnergy4 = new JLabel("Fuelrod " +fuelRodList.get(4).GetRodName() + " fuel "+ fuelRodList.get(4).GetLifeSpan()/1000 +" %");
        fuelRodEnergy4.setFont(new Font(elecLabel.getName(), Font.PLAIN, 24));
        gaugePanel.add(fuelRodEnergy4, BorderLayout.CENTER);

        fuelRodBar4 = new JProgressBar (0,100);
        fuelRodBar4.setPreferredSize( new Dimension (100, 25));
        fuelRodBar4.setForeground(new Color(0,255,255));
        gaugePanel.add(fuelRodBar4);

        fuelRodEnergy5 = new JLabel("Fuelrod " + fuelRodList.get(5).GetRodName() + " fuel "+ fuelRodList.get(5).GetLifeSpan()/1000 +" %");
        fuelRodEnergy5.setFont(new Font(elecLabel.getName(), Font.PLAIN, 24));
        gaugePanel.add(fuelRodEnergy5, BorderLayout.CENTER);

        fuelRodBar5 = new JProgressBar (0,100);
        fuelRodBar5.setPreferredSize( new Dimension (100, 25));
        fuelRodBar5.setForeground(new Color(0,255,255));
        gaugePanel.add(fuelRodBar5);

        Timer timer = new Timer(timerDelay, e -> 
        {
            try
                {
                    TimerAction(e);
                } 
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
        });

        timer.start();

        viewPanelLayerd.add(reactor, BorderLayout.CENTER, 1);
        viewPanelLayerd.add(waterImg, BorderLayout.CENTER, 2);
        viewPanelLayerd.add(fuelRod0, BorderLayout.CENTER, 0);
        viewPanelLayerd.add(fuelRod1, BorderLayout.CENTER, 0);
        viewPanelLayerd.add(fuelRod2, BorderLayout.CENTER, 0);
        viewPanelLayerd.add(fuelRod3, BorderLayout.CENTER, 0);
        viewPanelLayerd.add(fuelRod4, BorderLayout.CENTER,0);
        viewPanelLayerd.add(fuelRod5, BorderLayout.CENTER,0);
        viewPanelLayerd.add(controlRods, BorderLayout.CENTER,0);
        viewPanelLayerd.add(controlsPanel, 98);
        viewPanelLayerd.add(gaugePanel, 99);

        contentPane.add(viewPanelLayerd);
    }

    private void TimerAction(ActionEvent e) throws IOException
    {
        SetTempLabel();
        SetWaterTempImg();
        SetLabelColour();
        SetTempBar();
        SetFuelRodBar();
    }
    
    private void SetFuelRodBar()
    {
        rodService.FuelRodDecay();
        var fuel0 = ((int)fuelRodList.get(0).GetLifeSpan())/1000;
        fuelRodEnergy0.setText("Fuelrod " + fuelRodList.get(0).GetRodName() + " fuel "+ fuelRodList.get(0).GetLifeSpan()/1000 +" %");
        fuelRodBar0.setValue(fuel0);

        var fuel1 = ((int)fuelRodList.get(1).GetLifeSpan())/1000;
        fuelRodEnergy1.setText("Fuelrod " + fuelRodList.get(1).GetRodName() + " fuel "+ fuelRodList.get(1).GetLifeSpan()/1000 +" %");
        fuelRodBar1.setValue(fuel1);

        var fuel2 = ((int)fuelRodList.get(2).GetLifeSpan())/1000;
        fuelRodEnergy2.setText("Fuelrod " + fuelRodList.get(2).GetRodName() + " fuel "+ fuelRodList.get(2).GetLifeSpan()/1000 +" %");
        fuelRodBar2.setValue(fuel2);

        var fuel3 = ((int)fuelRodList.get(3).GetLifeSpan())/1000;
        fuelRodEnergy3.setText("Fuelrod " + fuelRodList.get(3).GetRodName() + " fuel "+ fuelRodList.get(3).GetLifeSpan()/1000 +" %");
        fuelRodBar3.setValue(fuel3);

        var fuel4 = ((int)fuelRodList.get(4).GetLifeSpan())/1000;
        fuelRodEnergy4.setText("Fuelrod " + fuelRodList.get(4).GetRodName() + " fuel "+ fuelRodList.get(4).GetLifeSpan()/1000 +" %");
        fuelRodBar4.setValue(fuel4);

        var fuel5 = ((int)fuelRodList.get(5).GetLifeSpan())/1000;
        fuelRodEnergy5.setText("Fuelrod " + fuelRodList.get(5).GetRodName() + " fuel "+ fuelRodList.get(5).GetLifeSpan()/1000 +" %");
        fuelRodBar5.setValue(fuel5);
    }

    private void SetTempLabel()
    {
        double add = 0;
        add = reactorService.Calculate();

        if (tempValue <= 0 && add <= 0) 
        {
            tempValue = 0;
        }
        else
        {
            if (tempValue + add <= 0) 
            {
                tempValue = 0;
            }
            else
            {
                tempValue += add;
            }
        }

        tempLabel.setText("Temperature: " + tempValue + " °C");
    }

    private void SetTempBar()
    {
        tempBar.setValue(((int)tempValue)/10);
    }

    private void SetLabelColour()
    {
        if(tempValue >= 750)
        {
            tempBar.setForeground(Color.orange);
        }
        
        if(tempValue >= 900)
        {
            tempBar.setForeground(Color.red);
        }
        
        if(tempValue >= 1000)
        {
            tempLabel.setForeground(Color.red);
        }
        else
        {
            tempLabel.setForeground(Color.black);
        }

        if (tempValue >= 1500) 
        {
            ShowDialog();
        }
        
        if(tempValue <=749)
        {
            tempBar.setForeground(Color.black);
        }
    }

    private void ShowDialog()
    {
        if (dialog == null || !dialog.isVisible()) 
        {
            dialog = new JDialog(this, "Warning!", false); // Set non-modal to interact with main window
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setSize(250, 50);
            JLabel label = new JLabel("Warning Temperatur is getting too high!");
            label.setForeground(Color.red);
            label.setFont(new Font(label.getName(), Font.PLAIN, 24));
            dialog.add(label,BorderLayout.CENTER);
    
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }
    }

    private void SetWaterTempImg() throws IOException
    {
        BufferedImage imgWater = null;

        if(tempValue <=99)
        {
            imgWater = ImageIO.read(new File("./Images/water00.png"));
        }

        if(tempValue >=100 && tempValue <=449)
        {
            imgWater = ImageIO.read(new File("./Images/water01.png"));
        }

        if(tempValue >=450 && tempValue <=849)
        {
            imgWater = ImageIO.read(new File("./Images/water02.png"));
        }

        if(tempValue >=850)
        {
            imgWater = ImageIO.read(new File("./Images/water03.png"));
        }

        waterImg.setIcon(new ImageIcon(imgWater));
    }

    private void FuelRodComboBoxChange(ActionEvent e)
    {
        selectedRod = (RodModel) ((JComboBox) e.getSource()).getSelectedItem();

        sliderValue = selectedRod.GetRodLevel();
        rodSlider.setValue(sliderValue);
    }

    private void RodSliderChange(ChangeEvent e)
    {
        sliderValue = ((JSlider) e.getSource()).getValue();
        rodLevelLabel.setText("Rod Level: " + sliderValue + " %");

        selectedRod.SetRodLevel(sliderValue);
        rodSlider.setValue(sliderValue);

        switch (selectedRod.GetRodStyle())
        {
            case ControlRod:
                controlRods.setBounds(0, 132, 1000, sliderValue*3);
                break;
            case FuelRod:
                if(selectedRod.GetRodName().equals("1"))
                {
                    fuelRod0.setBounds(0, 132, 1000, sliderValue*3);
                }

                if(selectedRod.GetRodName().equals("2"))
                {
                    fuelRod1.setBounds(0, 132, 1060, sliderValue*3);
                }

                if(selectedRod.GetRodName().equals("3"))
                {
                    fuelRod2.setBounds(0, 132, 1120, sliderValue*3);
                }

                if(selectedRod.GetRodName().equals("4"))
                {
                    fuelRod3.setBounds(0, 132, 1180, sliderValue*3);
                }

                if(selectedRod.GetRodName().equals("5"))
                {
                    fuelRod4.setBounds(0, 132, 1240, sliderValue*3);
                }

                if(selectedRod.GetRodName().equals("6"))
                {
                    fuelRod5.setBounds(0, 132, 1300, sliderValue*3);
                }
                break;
            case Moderator:

                break;
        }
    }

    private void RodComboBoxAction(ActionEvent e)
    {
        selectedRod = (RodModel) ((JComboBox) e.getSource()).getSelectedItem();
        
        switch (selectedRod.GetRodStyle())
        {
            case ControlRod:
            fuelRodComboBox.setVisible(false);
            fuelRodLabel.setVisible(false);
            break;
            case FuelRod:
            selectedRod = selectedRod.GetRodModelList().get(0);
            fuelRodComboBox.setVisible(true);
            fuelRodLabel.setVisible(true);
            break;
            case Moderator:
            fuelRodComboBox.setVisible(false);
            fuelRodLabel.setVisible(false);
            break;
        }
        
        sliderValue = selectedRod.GetRodLevel();
        rodSlider.setValue(sliderValue);
    }
}
