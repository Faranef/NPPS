import java.awt.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class BudgetFrame extends JInternalFrame
{
    private BudgetService economyService;
    private List<BudgetModel> budgetList = new ArrayList<BudgetModel>();

    public BudgetFrame() 
    {
        GetServices();
        budgetList = economyService.GetBudgetList();
        CreateFrame();
        CreatePanels();
    }

    private void CreatePanels()
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        List<BudgetModel> list;

        if(budgetList.size() < 3)
        {
            list= budgetList;
        }
        else
        {
            list = budgetList.subList(budgetList.size()-3, budgetList.size());
        }

        for (BudgetModel budgetModel : list) 
        {
            JPanel bugetPanel = new JPanel();
            JPanel panelIncome = TotalPanel(budgetModel);
            bugetPanel.add(panelIncome, BorderLayout.NORTH);
            tabbedPane.addTab(budgetModel.toString(),bugetPanel);
        }
        
        this.add(tabbedPane);
    }

    private JPanel TotalPanel(BudgetModel budgetModel)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,3));
        panel.add(new Label("Income:"));
        panel.add(new Label("Electricity sold:"));
        panel.add(new Label(Double.toString(budgetModel.GetSoldElectricity())));
        panel.add(new Label());
        panel.add(new Label());
        panel.add(new Label());
        panel.add(new Label("Loss:"));
        panel.add(new Label("Running Costs:"));
        panel.add(new Label(Double.toString(budgetModel.GetTotalLoss())));
        panel.add(new Label());
        panel.add(new Label("Fuelrods:"));
        panel.add(new Label(Double.toString(budgetModel.GetFuelRodPrice())));
        panel.add(new Label());
        panel.add(new Label());
        panel.add(new Label());
        panel.add(new Label("Total:"));
        panel.add(new Label());
        panel.add(new Label(Double.toString(budgetModel.Budget)));

        return panel;
    }

    private void GetServices()
    {
        economyService = BudgetService.GetInstance();
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
