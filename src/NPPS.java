import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NPPS extends JFrame
{
    private RodModel selectedRod;
    private JComboBox fuelRodComboBox;
    private JComboBox rodComboBox;
    private JLabel fuelRodLabel;
    private JLabel rodLevelLabel;
    private JLabel waterImg;
    private JLabel propImg;
    private JSlider rodSlider;
    private int sliderValue;
    private RodService rodService;
    private ReactorService reactorService;
    private EconomyService economyService;
    private EventService eventService;
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
    private Timer spinnerTimer;
    private Color fuelRodColour = new Color(0, 255, 0);

    public NPPS(String name)
    {
        super(name);
        setResizable(false);
        setPreferredSize(new Dimension(1440, 900));
        economyService = EconomyService.GetInstance();
        rodService = RodService.GetInstance();
        CreateRods();
        reactorService = ReactorService.GetInstance();
        eventService = EventService.GetInstance();
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
            RodModel rod = new RodModel(0, Integer.toString(i), RodModelStyle.FuelRod, 100000, true);
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
                } catch (IOException e)
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
        viewPanelLayerd.setBackground(new Color(255, 255, 255));

        JPanel controlsPanel = new JPanel();
        controlsPanel.setBackground(new Color(224, 224, 224));
        controlsPanel.setBounds(0, 600, 1000, 300);

        JPanel gaugePanel = new JPanel();
        gaugePanel.setBackground(new Color(151, 166, 176));
        gaugePanel.setBounds(1000, 0, 424, 900);

        rodComboBox = new JComboBox(new RodModel[]
        { rodService.GetRod(RodModelStyle.ControlRod), rodService.GetRod(RodModelStyle.FuelRod),
                // rodService.GetRod(RodModelStyle.Moderator)
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

        JLabel reactor = CreateImages();

        CreateLabels(gaugePanel);
        CreateFuelRodDecayDisplay(gaugePanel);

        Timer timer = new Timer(timerDelay, e -> {
            try
            {
                TimerAction(e);
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        });

        timer.start();

        spinnerTimer = new Timer(100, e -> {
            double rotationAngle = 0;
            rotationAngle += Math.toRadians(36); // Adjust the rotation speed
            propImg.setIcon(GetRotatedIcon(rotationAngle));
        });

        AddControlsToPanel(viewPanelLayerd, controlsPanel, gaugePanel, reactor);
        CreateMenuBar();
        contentPane.add(viewPanelLayerd);
    }

    private void CreateMenuBar() throws IOException
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItem;

        menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        menuBar.add(menu);
        
        menuItem = new JMenuItem("New Game", KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(e -> { NewGame(); });
        menu.add(menuItem);
        menu.addSeparator();
        
        var saveImg = ImageIO.read(new File("./Images/save.gif"));
        menuItem = new JMenuItem("Save Game", new ImageIcon(saveImg));
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(e -> { SaveGame(); });
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Load Game");
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(e -> { LoadGame(); });
        menu.add(menuItem);
        menu.addSeparator();

        menuItem = new JMenuItem("Exit");
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(e -> { System.exit(0); });
        menu.add(menuItem);

        menu = new JMenu("Info");
        menu.setMnemonic(KeyEvent.VK_I);
        menuBar.add(menu);
        
        menuItem = new JMenuItem("Budget", KeyEvent.VK_B);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(e -> { OpenBudegtWindow(); });
        menu.add(menuItem);

        setJMenuBar(menuBar);
    }

    private void OpenBudegtWindow()
    {
        // show buegetting form
    }

    private void LoadGame()
    {
        // load values from a json file
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(".\\GameData"));

        var filter = new FileNameExtensionFilter("JSON", "json");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.showOpenDialog(this);
        
        File file = fileChooser.getSelectedFile();
    }

    private void SaveGame()
    {
        // save value to a json file
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(".\\GameData"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) 
        {
            File file = fileChooser.getSelectedFile();
            var filter = new FileNameExtensionFilter("JSON", "json");
            fileChooser.addChoosableFileFilter(filter);
            // save to file
        }

    }

    private void NewGame()
    {
        //reset all values to 0
        tempValue = 0;
        wattValue =0;
        rodService.SetAllToNew();
    }

    private void AddControlsToPanel(JLayeredPane viewPanelLayerd, JPanel controlsPanel, JPanel gaugePanel, JLabel reactor)
    {
        viewPanelLayerd.add(reactor, BorderLayout.CENTER, 1);
        viewPanelLayerd.add(waterImg, BorderLayout.CENTER, 2);
        viewPanelLayerd.add(propImg, BorderLayout.CENTER, 1);
        viewPanelLayerd.add(fuelRod0, BorderLayout.CENTER, 0);
        viewPanelLayerd.add(fuelRod1, BorderLayout.CENTER, 0);
        viewPanelLayerd.add(fuelRod2, BorderLayout.CENTER, 0);
        viewPanelLayerd.add(fuelRod3, BorderLayout.CENTER, 0);
        viewPanelLayerd.add(fuelRod4, BorderLayout.CENTER, 0);
        viewPanelLayerd.add(fuelRod5, BorderLayout.CENTER, 0);
        viewPanelLayerd.add(controlRods, BorderLayout.CENTER, 0);
        viewPanelLayerd.add(controlsPanel, 98);
        viewPanelLayerd.add(gaugePanel, 99);
    }

    private void CreateLabels(JPanel gaugePanel)
    {
        var gaugeTempPanel = new JPanel();
        gaugeTempPanel.setLayout(new BoxLayout(gaugeTempPanel, BoxLayout.PAGE_AXIS));
        gaugeTempPanel.setBackground(new Color(151, 166, 176));

        tempLabel = new JLabel("Temperature: " + tempValue + " °C");
        tempLabel.setFont(new Font(tempLabel.getName(), Font.PLAIN, 18));
        tempLabel.setForeground(Color.black);
        gaugeTempPanel.add(tempLabel);

        tempBar = new JProgressBar(0, 100);
        tempBar.setPreferredSize(new Dimension(300, 25));
        // tempBar.setStringPainted(true);
        gaugeTempPanel.add(tempBar);

        gaugeTempPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        elecLabel = new JLabel("Wattage: " + wattValue + " W");
        elecLabel.setFont(new Font(elecLabel.getName(), Font.PLAIN, 18));
        elecLabel.setForeground(Color.black);
        elecLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        gaugeTempPanel.add(elecLabel);

        gaugeTempPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        gaugePanel.add(gaugeTempPanel);
    }

    private void CreateFuelRodDecayDisplay(JPanel gaugePanel)
    {
        var fuelRod = rodService.GetRod(RodModelStyle.FuelRod);
        fuelRodList = fuelRod.GetRodModelList();

        FuelRod0(gaugePanel, fuelRodList);

        FuelRod1(gaugePanel, fuelRodList);

        FuelRod2(gaugePanel, fuelRodList);

        FuelRod3(gaugePanel, fuelRodList);

        FuelRod4(gaugePanel, fuelRodList);

        FuelRod5(gaugePanel, fuelRodList);
    }

    private void FuelRod5(JPanel gaugePanel, List<RodModel> fuelRodList)
    {
        var gaugeDecayPanel = new JPanel();
        gaugeDecayPanel.setLayout(new BoxLayout(gaugeDecayPanel, BoxLayout.PAGE_AXIS));
        gaugeDecayPanel.setBackground(new Color(151, 166, 176));

        fuelRodEnergy5 = new JLabel("Fuelrod " + fuelRodList.get(5).GetRodName() + " fuel " + fuelRodList.get(5).GetLifeSpan() / 1000 + " %");
        fuelRodEnergy5.setFont(new Font(fuelRodEnergy5.getName(), Font.PLAIN, 18));
        fuelRodEnergy5.setForeground(Color.black);
        fuelRodEnergy5.setToolTipText("Fuelrod 6 info");
        gaugeDecayPanel.add(fuelRodEnergy5);

        fuelRodBar5 = new JProgressBar(0, 100);
        fuelRodBar5.setPreferredSize(new Dimension(300, 25));
        fuelRodBar5.setForeground(fuelRodColour);
        gaugeDecayPanel.add(fuelRodBar5);

        gaugeDecayPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        gaugePanel.add(gaugeDecayPanel);
    }

    private void FuelRod4(JPanel gaugePanel, List<RodModel> fuelRodList)
    {
        var gaugeDecayPanel = new JPanel();
        gaugeDecayPanel.setLayout(new BoxLayout(gaugeDecayPanel, BoxLayout.PAGE_AXIS));
        gaugeDecayPanel.setBackground(new Color(151, 166, 176));

        fuelRodEnergy4 = new JLabel("Fuelrod " + fuelRodList.get(4).GetRodName() + " fuel " + fuelRodList.get(4).GetLifeSpan() / 1000 + " %");
        fuelRodEnergy4.setFont(new Font(fuelRodEnergy4.getName(), Font.PLAIN, 18));
        fuelRodEnergy4.setToolTipText("Fuelrod 5 info");
        fuelRodEnergy4.setForeground(Color.black);
        gaugeDecayPanel.add(fuelRodEnergy4);

        fuelRodBar4 = new JProgressBar(0, 100);
        fuelRodBar4.setPreferredSize(new Dimension(300, 25));
        fuelRodBar4.setForeground(fuelRodColour);
        gaugeDecayPanel.add(fuelRodBar4);
        gaugeDecayPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        gaugePanel.add(gaugeDecayPanel);
    }

    private void FuelRod3(JPanel gaugePanel, List<RodModel> fuelRodList)
    {
        var gaugeDecayPanel = new JPanel();
        gaugeDecayPanel.setLayout(new BoxLayout(gaugeDecayPanel, BoxLayout.PAGE_AXIS));
        gaugeDecayPanel.setBackground(new Color(151, 166, 176));

        fuelRodEnergy3 = new JLabel("Fuelrod " + fuelRodList.get(3).GetRodName() + " fuel " + fuelRodList.get(3).GetLifeSpan() / 1000 + " %");
        fuelRodEnergy3.setFont(new Font(fuelRodEnergy3.getName(), Font.PLAIN, 18));
        fuelRodEnergy3.setToolTipText("Fuelrod 4 info");
        fuelRodEnergy3.setForeground(Color.black);
        gaugeDecayPanel.add(fuelRodEnergy3);

        fuelRodBar3 = new JProgressBar(0, 100);
        fuelRodBar3.setPreferredSize(new Dimension(300, 25));
        fuelRodBar3.setForeground(fuelRodColour);
        gaugeDecayPanel.add(fuelRodBar3);
        gaugeDecayPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        gaugePanel.add(gaugeDecayPanel);
    }

    private void FuelRod2(JPanel gaugePanel, List<RodModel> fuelRodList)
    {
        var gaugeDecayPanel = new JPanel();
        gaugeDecayPanel.setLayout(new BoxLayout(gaugeDecayPanel, BoxLayout.PAGE_AXIS));
        gaugeDecayPanel.setBackground(new Color(151, 166, 176));

        fuelRodEnergy2 = new JLabel("Fuelrod " + fuelRodList.get(2).GetRodName() + " fuel " + fuelRodList.get(2).GetLifeSpan() / 1000 + " %");
        fuelRodEnergy2.setFont(new Font(fuelRodEnergy2.getName(), Font.PLAIN, 18));
        fuelRodEnergy2.setToolTipText("Fuelrod 3 info");
        fuelRodEnergy2.setForeground(Color.black);
        gaugeDecayPanel.add(fuelRodEnergy2);

        fuelRodBar2 = new JProgressBar(0, 100);
        fuelRodBar2.setPreferredSize(new Dimension(300, 25));
        fuelRodBar2.setForeground(fuelRodColour);
        gaugeDecayPanel.add(fuelRodBar2);
        gaugeDecayPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        gaugePanel.add(gaugeDecayPanel);
    }

    private void FuelRod1(JPanel gaugePanel, List<RodModel> fuelRodList)
    {
        var gaugeDecayPanel = new JPanel();
        gaugeDecayPanel.setLayout(new BoxLayout(gaugeDecayPanel, BoxLayout.PAGE_AXIS));
        gaugeDecayPanel.setBackground(new Color(151, 166, 176));

        fuelRodEnergy1 = new JLabel("Fuelrod " + fuelRodList.get(1).GetRodName() + " fuel " + fuelRodList.get(1).GetLifeSpan() / 1000 + " %");
        fuelRodEnergy1.setFont(new Font(fuelRodEnergy1.getName(), Font.PLAIN, 18));
        fuelRodEnergy1.setToolTipText("Fuelrod 2 info");
        fuelRodEnergy1.setForeground(Color.black);
        gaugeDecayPanel.add(fuelRodEnergy1);

        fuelRodBar1 = new JProgressBar(0, 100);
        fuelRodBar1.setPreferredSize(new Dimension(300, 25));
        fuelRodBar1.setForeground(fuelRodColour);
        gaugeDecayPanel.add(fuelRodBar1);
        gaugeDecayPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        gaugePanel.add(gaugeDecayPanel);
    }

    private void FuelRod0(JPanel gaugePanel, List<RodModel> fuelRodList)
    {
        var gaugeDecayPanel = new JPanel();
        gaugeDecayPanel.setLayout(new BoxLayout(gaugeDecayPanel, BoxLayout.PAGE_AXIS));
        gaugeDecayPanel.setBackground(new Color(151, 166, 176));

        fuelRodEnergy0 = new JLabel("Fuelrod " + fuelRodList.get(0).GetRodName() + " fuel " + (int) (fuelRodList.get(0).GetLifeSpan() / 1000) + " %");
        fuelRodEnergy0.setFont(new Font(fuelRodEnergy0.getName(), Font.PLAIN, 18));
        fuelRodEnergy0.setToolTipText("Fuelrod 1 info");
        fuelRodEnergy0.setForeground(Color.black);
        gaugeDecayPanel.add(fuelRodEnergy0);

        fuelRodBar0 = new JProgressBar(0, 100);
        fuelRodBar0.setPreferredSize(new Dimension(300, 25));
        fuelRodBar0.setForeground(fuelRodColour);
        gaugeDecayPanel.add(fuelRodBar0);
        gaugeDecayPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        gaugePanel.add(gaugeDecayPanel);
    }

    private JLabel CreateImages() throws IOException
    {
        var img = ImageIO.read(new File("./Images/case.png"));
        JLabel reactor = new JLabel(new ImageIcon(img));
        reactor.setBounds(0, 0, 1000, 600);
        reactor.setVisible(true);

        var imgCold = ImageIO.read(new File("./Images/water00.png"));
        waterImg = new JLabel(new ImageIcon(imgCold));
        waterImg.setBounds(0, 0, 1000, 600);
        waterImg.setVisible(true);

        var imgProp = ImageIO.read(new File("./Images/prop.png"));
        propImg = new JLabel(new ImageIcon(imgProp));
        propImg.setBounds(80, 440, 138, 131);
        propImg.setVisible(true);

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
        controlRods.setBounds(0, 132, 1000, sliderValue * 3);
        controlRods.setVisible(true);

        return reactor;
    }

    private void TimerAction(ActionEvent e) throws IOException
    {
        SetTempLabel();
        SetWattageLabel();
        SetWaterTempImg();
        SetLabelColour();
        SetTempBar();
        SetFuelRodBar();
        StartStopProp();
        CoolDown();
    }

    private void CoolDown()
    {
        int areRodsIn = rodService.CountFuelRodsAbove(0);

        if (areRodsIn == 0 && tempValue != 0)
        {
            tempValue = tempValue - 0.125;
            tempLabel.setText("Temperature: " + tempValue + " °C");
        }
    }

    private void StartStopProp()
    {
        if (tempValue > 0)
        {
            if (spinnerTimer.isRunning() == false)
            {
                spinnerTimer.start();
            }
        } else
        {
            spinnerTimer.stop();
        }
    }

    private void SetWattageLabel()
    {
        wattValue = reactorService.CalculateWattage(tempValue);
        elecLabel.setText("Wattage: " + wattValue + " MW");
    }

    private void SetFuelRodBar()
    {
        rodService.FuelRodDecay();

        if (fuelRodList.get(0).IsActive())
        {
            var fuel0 = ((int) fuelRodList.get(0).GetLifeSpan()) / 1000;
            fuelRodEnergy0.setText("Fuelrod " + fuelRodList.get(0).GetRodName() + " fuel "
                    + (int) (fuelRodList.get(0).GetLifeSpan() / 1000) + " %");
            fuelRodBar0.setValue(fuel0);
        } else
        {
            fuelRodEnergy0.setEnabled(false);
        }

        if (fuelRodList.get(1).IsActive())
        {
            var fuel1 = ((int) fuelRodList.get(1).GetLifeSpan()) / 1000;
            fuelRodEnergy1.setText("Fuelrod " + fuelRodList.get(1).GetRodName() + " fuel "
                    + (int) (fuelRodList.get(1).GetLifeSpan() / 1000) + " %");
            fuelRodBar1.setValue(fuel1);
        } else
        {
            fuelRodEnergy1.setEnabled(false);
        }

        if (fuelRodList.get(2).IsActive())
        {
            var fuel2 = ((int) fuelRodList.get(2).GetLifeSpan()) / 1000;
            fuelRodEnergy2.setText("Fuelrod " + fuelRodList.get(2).GetRodName() + " fuel "
                    + (int) (fuelRodList.get(2).GetLifeSpan() / 1000) + " %");
            fuelRodBar2.setValue(fuel2);
        } else
        {
            fuelRodEnergy2.setEnabled(false);
        }

        if (fuelRodList.get(3).IsActive())
        {
            var fuel3 = ((int) fuelRodList.get(3).GetLifeSpan()) / 1000;
            fuelRodEnergy3.setText("Fuelrod " + fuelRodList.get(3).GetRodName() + " fuel "
                    + (int) (fuelRodList.get(3).GetLifeSpan() / 1000) + " %");
            fuelRodBar3.setValue(fuel3);
        } else
        {
            fuelRodEnergy3.setEnabled(false);
        }

        if (fuelRodList.get(4).IsActive())
        {
            var fuel4 = ((int) fuelRodList.get(4).GetLifeSpan()) / 1000;
            fuelRodEnergy4.setText("Fuelrod " + fuelRodList.get(4).GetRodName() + " fuel "
                    + (int) (fuelRodList.get(4).GetLifeSpan() / 1000) + " %");
            fuelRodBar4.setValue(fuel4);
        } else
        {
            fuelRodEnergy4.setEnabled(false);
        }

        if (fuelRodList.get(5).IsActive())
        {
            var fuel5 = ((int) fuelRodList.get(5).GetLifeSpan()) / 1000;
            fuelRodEnergy5.setText("Fuelrod " + fuelRodList.get(5).GetRodName() + " fuel "
                    + (int) (fuelRodList.get(5).GetLifeSpan() / 1000) + " %");
            fuelRodBar5.setValue(fuel5);
        } else
        {
            fuelRodEnergy5.setEnabled(false);
        }
    }

    private void SetTempLabel()
    {
        double add = 0;
        add = reactorService.CalculateTemp();

        if (tempValue <= 0 && add <= 0)
        {
            tempValue = 0;
        } else
        {
            if (tempValue + add <= 0)
            {
                tempValue = 0;
            } else
            {
                tempValue += add;
            }
        }

        tempLabel.setText("Temperature: " + tempValue + " °C");
    }

    private void SetTempBar()
    {
        tempBar.setValue(((int) tempValue) / 10);
    }

    private void SetLabelColour()
    {
        if (tempValue >= 100)
        {
            tempBar.setForeground(Color.orange);
        }

        if (tempValue >= 300)
        {
            tempBar.setForeground(Color.red);
        }

        if (tempValue >= 300)
        {
            tempLabel.setForeground(Color.red);
        } else
        {
            tempLabel.setForeground(Color.black);
        }

        if (tempValue >= 600)
        {
            ShowDialog();
        }

        if (tempValue <= 100)
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
            JLabel label = new JLabel("Temperature is getting too high!");
            label.setForeground(Color.red);
            label.setFont(new Font(label.getName(), Font.PLAIN, 24));
            dialog.add(label, BorderLayout.CENTER);

            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }
    }

    private void SetWaterTempImg() throws IOException
    {
        BufferedImage imgWater = null;

        if (tempValue <= 99.9999)
        {
            imgWater = ImageIO.read(new File("./Images/water00.png"));
        }

        if (tempValue >= 100.0000 && tempValue <= 349.9999)
        {
            imgWater = ImageIO.read(new File("./Images/water01.png"));
        }

        if (tempValue >= 350.0000 && tempValue <= 549.9999)
        {
            imgWater = ImageIO.read(new File("./Images/water02.png"));
        }

        if (tempValue >= 550.0000)
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
            controlRods.setBounds(0, 132, 1000, sliderValue * 3);
            break;
        case FuelRod:
            if (selectedRod.GetRodName().equals("1"))
            {
                fuelRod0.setBounds(0, 132, 1000, sliderValue * 3);
            }

            if (selectedRod.GetRodName().equals("2"))
            {
                fuelRod1.setBounds(0, 132, 1060, sliderValue * 3);
            }

            if (selectedRod.GetRodName().equals("3"))
            {
                fuelRod2.setBounds(0, 132, 1120, sliderValue * 3);
            }

            if (selectedRod.GetRodName().equals("4"))
            {
                fuelRod3.setBounds(0, 132, 1180, sliderValue * 3);
            }

            if (selectedRod.GetRodName().equals("5"))
            {
                fuelRod4.setBounds(0, 132, 1240, sliderValue * 3);
            }

            if (selectedRod.GetRodName().equals("6"))
            {
                fuelRod5.setBounds(0, 132, 1300, sliderValue * 3);
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

    private Icon GetRotatedIcon(double angle)
    {
        // ImageIcon originalIcon = new ImageIcon("Images/prop.png");
        int width = propImg.getWidth();
        int height = propImg.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Rotate the image
        g2d.rotate(angle, width / 2, height / 2);
        propImg.paint(g2d);
        g2d.dispose();

        return new ImageIcon(bufferedImage);
    }

}
