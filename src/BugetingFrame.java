import java.awt.*;
import javax.swing.*;

public class BugetingFrame extends JInternalFrame
{
    private EconomyService economyService;
    private JLabel Balance;
    private JLabel TotalProfit;
    private JLabel TotalLoss;

    public BugetingFrame() 
    {
        CreateFrame();
        GetServices();
        CreateLabels();

        //Create tabs with "Previous Month"  "Two Months Ago"
        //https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java
    }

    private void CreateLabels()
    {
        JPanel panelIncome = new JPanel();
        panelIncome.setLayout(new GridLayout(0,2));
        panelIncome.add(new Label("Income:"));
        panelIncome.add(new Label(""));
        panelIncome.add(new Label("Electricity sold:"));
        panelIncome.add(new Label("123.45"));

        JPanel panelLoss = new JPanel();
        panelLoss.setLayout(new GridLayout(0,2));
        panelLoss.add(new Label("Loss:"));
        panelLoss.add(new Label());
        panelLoss.add(new Label("Running Costs:"));
        panelLoss.add(new Label("123.45"), BorderLayout.EAST);
        panelLoss.add(new Label("Fuelrods:"));
        panelLoss.add(new Label("50000"));
        
        //panel.add(new Label(TotalProfit.toString()));





        this.add(panelIncome, BorderLayout.NORTH);
        this.add(new JSeparator(), BorderLayout.CENTER);
        this.add(panelLoss, BorderLayout.PAGE_END);
    }

    private void GetServices()
    {
        economyService = EconomyService.GetInstance();
    }

    private void CreateFrame()
    {
        setSize(new Dimension(450,600));
        setPreferredSize(new Dimension(450, 600));
        setMaximumSize(new Dimension(450, 600));
        setMinimumSize(new Dimension(450, 600));
        setMaximizable(false);
        setClosable(true);

        setLocation(1440/2 - this.getWidth()/2, 900/2 - this.getHeight()/2);
    }
}
