import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NPPS extends JFrame
{
    private JLayeredPane viewPanelLayerd;
    private RodModel selectedRod;
    private JComboBox fuelRodComboBox;
    private JComboBox rodComboBox;
    private JLabel fuelRodLabel;
    private JLabel rodLevelLabel;
    private JLabel dateLabel;
    private JLabel waterImg;
    private JLabel reactorImg;
    private JLabel propImg;
    private JSlider rodSlider;
    private int sliderValue;
    private RodService rodService;
    private ReactorService reactorService;
    private BudgetService budgetService;
    private EventService eventService;
    private LoadSaveService loadSaveService;
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
    private JInternalFrame budgetWindow;
    private JInternalFrame researchWindow;
    private double tempValue;
    private double maintenanceCost;
    private int wattValue;
    private int timerDelay = 1000;
    private List<RodModel> fuelRodList;
    private Timer spinnerTimer;
    private Date gameDate;
    private Date tempGameDate;
    private Color fuelRodColour = new Color(0, 255, 0);

    public NPPS(String name)
    {
        super(name);
        setResizable(false);
        setPreferredSize(new Dimension(1440, 900));
        budgetService = BudgetService.GetInstance();
        rodService = RodService.GetInstance();
        loadSaveService = LoadSaveService.GetInstance();
        CreateRods();
        reactorService = ReactorService.GetInstance();
        eventService = EventService.GetInstance();
        maintenanceCost = budgetService.GetMaintenanceCost();
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
        viewPanelLayerd = new JLayeredPane();
        viewPanelLayerd.setOpaque(true);
        viewPanelLayerd.setBackground(new Color(255, 255, 255));

        JPanel controlsPanel = new JPanel();
        controlsPanel.setBackground(new Color(224, 224, 224));
        controlsPanel.setBounds(0, 600, 1000, 25);
        controlsPanel.setLayout(new GridLayout(0,6));
        
        JPanel gaugePanel = new JPanel();
        gaugePanel.setBackground(new Color(151, 166, 176));
        gaugePanel.setBounds(1000, 0, 424, 900);

        rodComboBox = new JComboBox(new RodModel[]
        { rodService.GetRod(RodModelStyle.ControlRod), rodService.GetRod(RodModelStyle.FuelRod),
                // rodService.GetRod(RodModelStyle.Moderator)
        });
        
        rodComboBox.setSize(new Dimension(100,25));

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

        rodSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, sliderValue);
        rodSlider.addChangeListener(e -> {
            RodSliderChange(e);
        });

        fuelRodLabel = new JLabel("Selected Fuel Rod:");
        fuelRodLabel.setVisible(false);

        rodLevelLabel = new JLabel("Rod Level: " + sliderValue);

        //controlsPanel.add(new JSeparator(SwingConstants.VERTICAL));
        controlsPanel.add(new JLabel("Selected Rod:"));
        controlsPanel.add(rodComboBox);

        //controlsPanel.add(new JSeparator(SwingConstants.VERTICAL));
        controlsPanel.add(fuelRodLabel);
        controlsPanel.add(fuelRodComboBox);

        //controlsPanel.add(new JSeparator(SwingConstants.VERTICAL));
        controlsPanel.add(rodLevelLabel);
        controlsPanel.add(rodSlider);

        JLabel reactor = CreateImages();

        CreateDateLabel(gaugePanel);
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
            rotationAngle += Math.toRadians(36); // rotation speed
            propImg.setIcon(GetRotatedIcon(rotationAngle));
        });

        Timer dateTimer = new Timer(timerDelay * 4, e ->{
            RunDateTimer();
        });

        dateTimer.start();

        CreateScramButton();
        
        AddControlsToPanel(viewPanelLayerd, controlsPanel, gaugePanel, reactor);
        CreateMenuBar();
        contentPane.add(viewPanelLayerd);
    }

    private void CreateScramButton()
    {
        //SCRAM Button
        var scramButton = new JButton("SCRAM");
        scramButton.setSize(new Dimension(400 , 125));
        scramButton.setBackground(Color.red);
        scramButton.setForeground(Color.white);
        scramButton.setLocation(300,680 );
        scramButton.setFont(new Font(scramButton.getName(), Font.BOLD, 28));
        scramButton.setMargin(new Insets(0, 0, 0, 0));
        scramButton.setBorder(null);
        scramButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        scramButton.addActionListener(e-> 
        {
            rodService.Scram();
            UpdateRods();
         });

        viewPanelLayerd.add(scramButton);
    }

    private void UpdateRods()
    {
        sliderValue = 100;
        controlRods.setBounds(0, 132, 1000, sliderValue * 3);
        rodSlider.setValue(sliderValue);
        sliderValue = 0;
        fuelRod0.setBounds(406, 132, 20, sliderValue * 3);
        fuelRod1.setBounds(436, 132, 20, sliderValue * 3);
        fuelRod2.setBounds(466, 132, 20, sliderValue * 3);
        fuelRod3.setBounds(496, 132, 20, sliderValue * 3);
        fuelRod4.setBounds(526, 132, 20, sliderValue * 3);
        fuelRod5.setBounds(556, 132, 20, sliderValue * 3);
    }

    private void CreateDateLabel(JPanel gaugePanel)
    {
        var gaugeTempPanel = new JPanel();
        gaugeTempPanel.setLayout(new BoxLayout(gaugeTempPanel, BoxLayout.PAGE_AXIS));
        gaugeTempPanel.setBackground(new Color(151, 166, 176));
        gaugeTempPanel.setPreferredSize(new Dimension(400,50));

        dateLabel = new JLabel();
        var date = GetCurrentDate();
        String todaysdate = ConvertDate(date);
        dateLabel.setText(todaysdate);
        dateLabel.setFont(new Font(dateLabel.getName(), Font.PLAIN, 20));
        dateLabel.setForeground(Color.black);

        gaugeTempPanel.add(dateLabel);
        gaugeTempPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        gaugePanel.add(gaugeTempPanel);
    }

    private String ConvertDate(Date date)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        return dateFormat.format(date);
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

        menuItem = new JMenuItem("Research", KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(e -> { OpenResearchWindow(); });
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Help", KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(e -> { OpenHelpDisplay(); });
        menu.add(menuItem);

        setJMenuBar(menuBar);
    }

    private void OpenHelpDisplay()
    {

        var dialog = new JDialog(this, "Help", false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(250, 50);
        var text = new JTextArea();
        text.setFont(new Font(text.getName(), Font.PLAIN, 16));
        text.setEditable(false);
        text.setForeground(Color.BLACK);
        text.setText("\n"+
                     "     - Each fuel rod can be controlled separately.\n"+
                     "     - Selecte Fuel Rod in the first drop down to get the optoin to select fuel rods   \n"+
                     "     - The control rods are there to regulate the reaction.\n"+
                     "     \n"+
                     "     - All the rods are moved by the slider.\n"+
                     "     - There is a 'SCRAM' button for emergency shut downs\n"+
                     "     \n"+
                     "     - On the rihgt hand side you have an overview of the temperature.\n"+
                     "       as well the wattage generated and the lifespan of each fuel rod.\n"+
                     "     - Here as well you can replace the fuel rods if they run out.\n"+
                     "       simple right mouse click and replace."+
                     "     \n"+
                     "     \n"+
                     "     - There are two more views:\n"+
                     "          - Budget\n"+
                     "          - Research\n"+
                     "     \n"+
                     "     - Under budget you can see the monthly income and loss\n"+
                     "     - Under research you can research new technologies.\n"+
                     "     \n"+
                     "     - You can save your game. (save as a josn file)"+
                     "     \n\n\n"+
                     "     - What's still missing:\n"+
                     "          - In game event system\n"+
                     "          - Loading a saved games.\n"+
                     "          - Add more tech to research like different kind of fuel rods. (uranium, plutonium,...)    \n"+
                     "          - And a few bugs need to be fixed as well.\n");


        dialog.add(text, BorderLayout.CENTER);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

    }

    private void OpenResearchWindow()
    {
        if (researchWindow == null) 
        {
            researchWindow = new ResearchFrame();
            viewPanelLayerd.add(researchWindow);
        }

        researchWindow.setVisible(true);
    }

    private void OpenBudegtWindow()
    {
        budgetWindow = new BudgetFrame();
        viewPanelLayerd.add(budgetWindow);
        budgetWindow.setVisible(true);
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
        loadSaveService.LoadGame(file);
    }

    private void SaveGame()
    {
        // save value to a json file
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(".\\GameData"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) 
        {
            File selectedFile = fileChooser.getSelectedFile();
            var filePath = selectedFile.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".json")) 
            {
                selectedFile = new File(filePath + ".json");
            }

            loadSaveService.SaveGame(selectedFile, gameDate);
            // save to file
        }

    }

    private void NewGame()
    {
        //reset all values to 0
        tempValue = 0;
        wattValue = 0;
        rodService.SetAllToNew();
        //economyService.NewGameSetting();
        selectedRod = rodService.GetRod(RodModelStyle.ControlRod);
        dateLabel.setText(ConvertDate(GetCurrentDate()));
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

        tempLabel = new JLabel("Temperature: " + (int)tempValue + " °C");
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

    private Date GetCurrentDate()
    {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        gameDate = date;
        return date;
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

        var contextMenu = new ContextMenuListener(5);
        fuelRodEnergy5.addMouseListener(contextMenu);
        gaugeDecayPanel.add(fuelRodEnergy5);

        fuelRodBar5 = new JProgressBar(0, 100);
        fuelRodBar5.setPreferredSize(new Dimension(300, 25));
        fuelRodBar5.setForeground(fuelRodColour);
        fuelRodBar5.addMouseListener(contextMenu);
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

        var contextMenu = new ContextMenuListener(4);
        fuelRodEnergy4.addMouseListener(contextMenu);
        gaugeDecayPanel.add(fuelRodEnergy4);

        fuelRodBar4 = new JProgressBar(0, 100);
        fuelRodBar4.setPreferredSize(new Dimension(300, 25));
        fuelRodBar4.setForeground(fuelRodColour);
        fuelRodBar4.addMouseListener(contextMenu);
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

        var contextMenu = new ContextMenuListener(3);
        fuelRodEnergy3.addMouseListener(contextMenu);
        gaugeDecayPanel.add(fuelRodEnergy3);

        fuelRodBar3 = new JProgressBar(0, 100);
        fuelRodBar3.setPreferredSize(new Dimension(300, 25));
        fuelRodBar3.setForeground(fuelRodColour);
        fuelRodBar3.addMouseListener(contextMenu);
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

        var contextMenu = new ContextMenuListener(2);
        fuelRodEnergy2.addMouseListener(contextMenu);
        gaugeDecayPanel.add(fuelRodEnergy2);

        fuelRodBar2 = new JProgressBar(0, 100);
        fuelRodBar2.setPreferredSize(new Dimension(300, 25));
        fuelRodBar2.setForeground(fuelRodColour);
        fuelRodBar2.addMouseListener(contextMenu);
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

        var contextMenu = new ContextMenuListener(1);
        fuelRodEnergy1.addMouseListener(contextMenu);
        gaugeDecayPanel.add(fuelRodEnergy1);

        fuelRodBar1 = new JProgressBar(0, 100);
        fuelRodBar1.setPreferredSize(new Dimension(300, 25));
        fuelRodBar1.setForeground(fuelRodColour);
        fuelRodBar1.addMouseListener(contextMenu);
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

        var contextMenu = new ContextMenuListener(0);
        fuelRodEnergy0.addMouseListener(contextMenu);
        gaugeDecayPanel.add(fuelRodEnergy0);

        fuelRodBar0 = new JProgressBar(0, 100);
        fuelRodBar0.setPreferredSize(new Dimension(300, 25));
        fuelRodBar0.setForeground(fuelRodColour);
        fuelRodBar0.addMouseListener(contextMenu);
        gaugeDecayPanel.add(fuelRodBar0);
        gaugeDecayPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        gaugePanel.add(gaugeDecayPanel);
    }

    private JLabel CreateImages() throws IOException
    {
        var img = ImageIO.read(new File("./Images/case.png"));
        reactorImg = new JLabel(new ImageIcon(img));
        reactorImg.setBounds(0, 0, 1000, 600);
        reactorImg.setVisible(true);
        reactorImg.setName("Reactor");

        var imgCold = ImageIO.read(new File("./Images/water00.png"));
        waterImg = new JLabel(new ImageIcon(imgCold));
        waterImg.setBounds(0, 0, 1000, 600);
        waterImg.setVisible(true);

        var imgProp = ImageIO.read(new File("./Images/prop.png"));
        propImg = new JLabel(new ImageIcon(imgProp));
        propImg.setBounds(80, 440, 138, 131);
        propImg.setVisible(true);

        fuelRod0 = new JLabel();
        fuelRod0.setBackground(fuelRodColour);
        fuelRod0.setOpaque(true);
        fuelRod0.setBounds(406, 132, 20, 0);
        fuelRod0.setVisible(true);

        fuelRod1 = new JLabel();
        fuelRod1.setBackground(fuelRodColour);
        fuelRod1.setOpaque(true);
        fuelRod1.setBounds(436, 132, 20, 0);
        fuelRod1.setVisible(true);

        fuelRod2 = new JLabel();
        fuelRod2.setBackground(fuelRodColour);
        fuelRod2.setOpaque(true);
        fuelRod2.setBounds(466, 132, 20, 0);
        fuelRod2.setVisible(true);

        fuelRod3 = new JLabel();
        fuelRod3.setBackground(fuelRodColour);
        fuelRod3.setOpaque(true);
        fuelRod3.setBounds(496, 132, 20, 0);
        fuelRod3.setVisible(true);

        fuelRod4 = new JLabel();
        fuelRod4.setBackground(fuelRodColour);
        fuelRod4.setOpaque(true);
        fuelRod4.setBounds(526, 132, 20, 0);
        fuelRod4.setVisible(true);

        fuelRod5 = new JLabel();
        fuelRod5.setBackground(fuelRodColour);
        fuelRod5.setOpaque(true);
        fuelRod5.setBounds(556, 132, 20, 0);
        fuelRod5.setVisible(true);

        var controlrodsImg = ImageIO.read(new File("./Images/controlRods.png"));
        controlRods = new JLabel(new ImageIcon(controlrodsImg));
        controlRods.setBounds(0, 132, 1000, sliderValue * 3);
        controlRods.setVisible(true);

        return reactorImg;
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
        MaintenanceCost();
    }

    private void MaintenanceCost()
    {
        //var
        budgetService.GetLastBudgetFromList().Deduct(maintenanceCost);
        budgetService.GetLastBudgetFromList().SetMaintenance(maintenanceCost);
    }

    private void RunDateTimer()
    {
        String todaysdate = SetDate();
        dateLabel.setText(todaysdate);
    }

    private String SetDate()
    {
        var date = gameDate;
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance(); 
        c.setTime(date); 
        c.add(Calendar.DATE, 1);
        date = c.getTime();
        gameDate = date;

        MonthlyCount(c);

        return dateFormat.format(date);
    }

    private void MonthlyCount(Calendar c)
    {
        var month = c.get(Calendar.MONTH);
        var year = c.get(Calendar.YEAR);
        Calendar cTemp = Calendar.getInstance(); 
        int tempMonth = -1;

        if (tempGameDate != null) 
        {
            cTemp.setTime(tempGameDate); 
            tempMonth = cTemp.get(Calendar.MONTH);
        }
        
        if (month != tempMonth) 
        {
            tempGameDate = gameDate;
            var budget = budgetService.GetLastBudgetFromList().GetBudget();

            if ( tempMonth != -1) 
            {
               budgetService.CreateBudgetModel(budget,month,year);
               tempMonth = month;
            }
        }
    }

    private void CoolDown()
    {
        int areRodsIn = rodService.CountFuelRodsAbove(0);

        if (areRodsIn == 0 && tempValue != 0)
        {
            tempValue = tempValue - 0.125;
            tempLabel.setText("Temperature: " + (int)tempValue + " °C");
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

        tempLabel.setText("Temperature: " + (int)tempValue + " °C");
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
            dialog = new JDialog(this, "Warning!", false);
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
        selectedRod = (RodModel)((JComboBox) e.getSource()).getSelectedItem();

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
                    fuelRod0.setBounds(406, 132, 20, sliderValue * 3);
                }

                if (selectedRod.GetRodName().equals("2"))
                {
                    fuelRod1.setBounds(436, 132, 20, sliderValue * 3);
                }

                if (selectedRod.GetRodName().equals("3"))
                {
                    fuelRod2.setBounds(466, 132, 20, sliderValue * 3);
                }

                if (selectedRod.GetRodName().equals("4"))
                {
                    fuelRod3.setBounds(496, 132, 20, sliderValue * 3);
                }

                if (selectedRod.GetRodName().equals("5"))
                {
                    fuelRod4.setBounds(526, 132, 20, sliderValue * 3);
                }

                if (selectedRod.GetRodName().equals("6"))
                {
                    fuelRod5.setBounds(556, 132, 20, sliderValue * 3);
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
