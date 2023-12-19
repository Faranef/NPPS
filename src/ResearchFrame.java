import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class ResearchFrame extends JInternalFrame
{
    private BudgetService budgetService;
    private BudgetModel currentBudget;
    private ResearchService researchService;
    private List<TechnologyModel> techList;
    private JLabel labelNumber;
    private int sliderValue = 10;

    public ResearchFrame()
    {
        try 
        {
            GetServices();
            currentBudget = budgetService.GetLastBudgetList();
            researchService = ResearchService.GetInstance();
            CreateFrame();
            CreateComponents();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
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
        labelNumber.setText(Integer.toString(sliderValue));
        topBar.add(labelNumber);
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
            var techImg = ImageIO.read(new File(technologyModel.GetImagePath()));
            var tech = new JLabel(new ImageIcon(techImg));
            var style = technologyModel.GetTechnologyStyle();
            switch (style) 
            {
                case StartTech:
                    tech.setBounds((offestFirst * iF) + 50, 250, 100,100);
                    iF++;
                    iS++;
                    break;
                case SaftyTech:
                    tech.setBounds((offestFirst * iF) + 50, 150, 100,100);
                    iF++;
                    break;
                case FinaceTech:
                    tech.setBounds((offestSecond * iS) + 50, 350, 100,100);
                    iS++;
                    break;
                default:
                    break;
            }

            tech.setVisible(true);
            tech.setToolTipText(technologyModel.GetResearchDescription());
            this.add(tech);
        }

        this.add(techTree, BorderLayout.CENTER);
    }

    private void PaintLines(Graphics  g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3f));

        int x1 = 150;
        int y1 = 250;

        for (int i = 1; i < techList.size(); i++) 
        {
            int x2 = x1 + (75 * i);
            int y2 = 150;

            g2d.drawLine(x1, y1, x2, y2);
            x1 = x2 + 75;
            y1 = 150;
        }


    }

    private void ResearchFundingSliderChange(ChangeEvent e)
    {
        sliderValue = ((JSlider) e.getSource()).getValue();
        labelNumber.setText( Integer.toString(sliderValue));
    }

    private void GetServices()
    {
        budgetService = BudgetService.GetInstance();
    }

    private void CreateFrame()
    {
        setSize(new Dimension(900,600));
        setPreferredSize(new Dimension(900,600));
        setMaximumSize(new Dimension(900,600));
        setMinimumSize(new Dimension(900,600));
        setMaximizable(false);
        setClosable(true);

        setLocation(1440/2 - this.getWidth()/2, 900/2 - this.getHeight()/2);
    }
}
