import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

import java.util.List;

public class ResearchFrame extends JInternalFrame
{
    private BudgetService budgetService;
    private ResearchService researchService;
    private List<TechnologyModel> techList;
    private JLabel labelNumber;
    private int sliderValue = 10;
    private int researchProgress = 0;
    private Timer timer;
    private boolean timerCreated = false;
    private JProgressBar researchPBar;
    private TechnologyModel currentResearch;

    public ResearchFrame()
    {
        try 
        {
            GetServices();
            researchService = ResearchService.GetInstance();
            CreateFrame();
            CreateComponents();

            if (!timerCreated) 
            {
                timer = new Timer(((100*1000 ) / sliderValue),e -> {
                    try
                    {
                        TimerAction(e);
                    } 
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                });
                timerCreated = true;
            }

        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    private void TimerAction(ActionEvent e)
    {
        researchProgress++;
        researchPBar.setValue(researchProgress);
        
        if (researchPBar.getValue() >= 100) 
        {
            researchPBar.setValue(0);
            researchProgress = 0;
            timer.stop();
            currentResearch.SetIsResearched(true);

            SetTechButtonAsResearched();
            UpdateMainFrame();

            JOptionPane.showMessageDialog(this, "Research finished!");
        }
    }

    private void UpdateMainFrame()
    {
        var container = this.getParent();
        var researchEffect = currentResearch.GetResearchEffect();

        if (!researchEffect.isEmpty()) 
        {
            for (var component : container.getComponents()) 
            {
                if (component instanceof JLabel && researchEffect.equals(component.getName()))
                {
                    var reactor = (JLabel)component;
                    
                    try
                    {
                        var upgrade = ImageIO.read(new File(currentResearch.GetImagePath()));
                        reactor.setIcon(new ImageIcon(upgrade));
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                
            }
        }
    }

    private void SetTechButtonAsResearched()
    {
        // very dirty hack
        var componentList =  ((JComponent)((JComponent)((JComponent) this.getComponent(0)).getComponent(1)).getComponent(0));

        for (Component component : componentList.getComponents()) 
        {
            if (component instanceof TechButton) 
            {
                var techButton = ((TechButton)component);    

                if (techButton.GetTechId() == currentResearch.GetTechId()) 
                {
                    //techButton.setBackground(new Color(0, 190, 0));
                    techButton.removeActionListener(null);
                    techButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    try
                    {
                        var techImg = ImageIO.read(new File(currentResearch.GetLogoPathFinished()));
                        techButton.setIcon(new ImageIcon(techImg));
                    } 
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }


    }

    private void CreateComponents() throws IOException
    {
        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(149,149,149));
        
        var labelRF = new JLabel("Research funding: ");
        labelRF.setFont(new Font(labelRF.getName(), Font.BOLD, 28));
        topBar.add(labelRF);
        var slider = new JSlider(JSlider.HORIZONTAL, 0, 100,sliderValue);
        slider.addChangeListener(e -> 
        {
            ResearchFundingSliderChange(e);
        });
        topBar.add(slider);

        labelNumber = new JLabel();
        labelNumber.setFont(new Font(labelNumber.getName(), Font.BOLD, 28));
        labelNumber.setText(Integer.toString(sliderValue)+" %");
        topBar.add(labelNumber);

        researchPBar = new JProgressBar(0, 100);
        researchPBar.setPreferredSize(new Dimension(300, 25));
        topBar.add(researchPBar);

        this.add(topBar, BorderLayout.PAGE_START);

        JPanel techTree = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                PaintLines(g);
            }
        };

        techTree.setBackground(new Color(168,168,168));
        
        techList = researchService.GetTechList();
        int offestFirst = 175;
        int offestSecond = 175;
        int iF = 0;
        int iS = 0;
        
        for (TechnologyModel technologyModel : techList) 
        {
            var techImg = ImageIO.read(new File(technologyModel.GetLogoPath()));
            var techButton = new TechButton();
            techButton.setIcon(new ImageIcon(techImg));
            var style = technologyModel.GetTechnologyStyle();
            switch (style) 
            {
                case StartTech:
                    techButton.setBounds((offestFirst * iF) + 50, 250, 100,100);
                    iF++;
                    iS++;
                    break;
                case SafetyTech:
                    techButton.setBounds((offestFirst * iF) + 50, 150, 100,100);
                    iF++;
                    break;
                case FinaceTech:
                    techButton.setBounds((offestSecond * iS) + 50, 350, 100,100);
                    iS++;
                    break;
                default:
                    break;
            }

            techButton.setVisible(true);
            techButton.setToolTipText(technologyModel.GetResearchDescription());
            techButton.setMargin(new Insets(0, 0, 0, 0));
            techButton.setBorder(null);
            techButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            techButton.addActionListener(e -> {StartResearch(technologyModel);});
            techButton.SetTechId(technologyModel.GetTechId());
            this.add(techButton);
        }

        this.add(techTree, BorderLayout.CENTER);
    }

    private void StartResearch(TechnologyModel selectedResearch)
    {
        var mainTech = techList.get(0);
        int result = JOptionPane.YES_OPTION;

        if (timer.isRunning() | researchPBar.getValue() > 0 ) 
        {
            result = JOptionPane.showConfirmDialog(this, 
                                          "You are already researching! \n Do you want to abort and start a new one?",
                                            "Warning!",
                                                  JOptionPane.YES_NO_OPTION);
        }

        if (result == JOptionPane.YES_OPTION)
        {
            // check if main tech is researched
            if (mainTech.IsResearched()) 
            {
                //check if can afford research
                if (selectedResearch.GetResearchCost() <= budgetService.GetLastBudgetFromList().GetBudget()) 
                {
                    //check if depending research has been researched
                    var depList = selectedResearch.GetDependingTechnology();

                    for (TechnologyModel technologyModel : depList) 
                    {
                        if(!technologyModel.IsResearched())
                        {
                            JOptionPane.showMessageDialog(this, "You need to research the previous tech to start this research!");
                            return;
                        }
                    }

                    currentResearch = selectedResearch;
                    budgetService.GetLastBudgetFromList().SetResearchCost(selectedResearch.GetResearchCost());
                    budgetService.GetLastBudgetFromList().Deduct(selectedResearch.GetResearchCost());
                    timer.start();
                    JOptionPane.showMessageDialog(this, "Research started!");
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "You need more money to start this research!");
                }
            }
            else
            {
                if ((mainTech.equals(selectedResearch))) 
                {
                    currentResearch = selectedResearch;
                    budgetService.GetLastBudgetFromList().SetResearchCost(selectedResearch.GetResearchCost());
                    budgetService.GetLastBudgetFromList().Deduct(selectedResearch.GetResearchCost());
                    timer.start();
                    JOptionPane.showMessageDialog(this, "Research started!");
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "You need to research 'Main Tech' first!");
                }
            }
        }
    }

    private void PaintLines(Graphics  g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3f));

        int x1 = 150;
        int y1 = 250;

        int x2 = x1 + 75;
        int y2 = 150;
        g2d.drawLine(x1, y1, x2, y2);

        x1 = x2 + 100;
        y1 = 150;

        x2 = x1 + 75;
        y2 = 150;
        g2d.drawLine(x1, y1, x2, y2);

        x1 = 150;
        y1 = 250;

        x2 = x1 + 75;
        y2 = 350;
        g2d.drawLine(x1, y1, x2, y2);
    }

    private void ResearchFundingSliderChange(ChangeEvent e)
    {
        sliderValue = ((JSlider) e.getSource()).getValue();
        labelNumber.setText( Integer.toString(sliderValue)+" %");

        if (sliderValue == 0) 
        {
            timer.stop();
        } 
        else
        {
            if(!timer.isRunning())
            {
                timer.start();
            }
            timer.setDelay((100*1000 )/sliderValue);
        }
    }

    private void GetServices()
    {
        budgetService = BudgetService.GetInstance();
    }

    private void CreateFrame()
    {
        setSize(new Dimension(900,600));
        setClosable(true);
        setDefaultCloseOperation();
        setLocation(1440/2 - this.getWidth()/2, 900/2 - this.getHeight()/2);
    }

    private void setDefaultCloseOperation()
    {
        setVisible(false);
    }
}
