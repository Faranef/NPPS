import java.awt.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class BudgetingFrame extends JInternalFrame
{
    private EconomyService economyService;
    private List<BudgetModel> budgetList = new ArrayList<BudgetModel>();

    public BudgetingFrame() 
    {
        GetServices();
        budgetList = economyService.GetBudgetList();
        CreateFrame();
        CreatePanels();

        //Create tabs with "Previous Month"  "Two Months Ago"
        //https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java
    }

    private void CreatePanels()
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        for (BudgetModel budgetModel : budgetList) 
        {
            JPanel bugetPanel = new JPanel();
            JPanel panelIncome = IncomePanel(budgetModel);
            JPanel panelLoss = LossPanel(budgetModel);
            bugetPanel.add(panelIncome, BorderLayout.NORTH);
            bugetPanel.add(new JSeparator(), BorderLayout.NORTH);
            bugetPanel.add(panelLoss, BorderLayout.NORTH);
            tabbedPane.addTab("Two Previous Months",bugetPanel);
        }
        
        this.add(tabbedPane);
    }

    private JPanel LossPanel(BudgetModel budgetModel)
    {
        JPanel panelLoss = new JPanel();
        panelLoss.setLayout(new GridLayout(0,3));
        panelLoss.add(new Label("Loss:"));
        panelLoss.add(new Label());
        panelLoss.add(new Label());
        panelLoss.add(new Label());
        panelLoss.add(new Label("Running Costs:"));
        panelLoss.add(new Label(Double.toString(budgetModel.GetTotalLoss())));
        panelLoss.add(new Label());
        panelLoss.add(new Label("Fuelrods:"));
        panelLoss.add(new Label(Double.toString(budgetModel.GetFuelRodPrice())));
        return panelLoss;
    }

    private JPanel IncomePanel(BudgetModel budgetModel)
    {
        JPanel panelIncome = new JPanel();
        panelIncome.setLayout(new GridLayout(0,3));
        panelIncome.add(new Label("Income:"));
        panelIncome.add(new Label());
        panelIncome.add(new Label());
        panelIncome.add(new Label());
        panelIncome.add(new Label("Electricity sold:"));
        panelIncome.add(new Label("123.45"));
        return panelIncome;
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




    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
}
