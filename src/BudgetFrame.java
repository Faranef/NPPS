import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class BudgetFrame extends JInternalFrame
{
    private BudgetService budgetService;
    private List<BudgetModel> budgetList = new ArrayList<BudgetModel>();

    private JLabel budgetLabel;
    private JLabel soldElecLabel;
    private JLabel fuelRodPriceLabel;
    private JLabel maintenance;
    private int currentTabIndex;
    private DecimalFormat decimalFormat;

    public BudgetFrame() 
    {
        decimalFormat = new DecimalFormat("#.##");
        GetServices();
        budgetList = budgetService.GetBudgetList();
        CreateFrame();
        CreatePanels();
        UpdateLabels();
    }

    private void UpdateLabels()
    {
            Timer timer = new Timer(1000, e -> {
            try
            {
                TimerAction(e);
            } 
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });

        timer.start();
    }

    private void TimerAction(ActionEvent e)
    {
        //only update if current month is selected
        //var budgetModel = budgetList.get(currentTabIndex);
        var budgetModel = budgetService.GetLastBudgetFromList();
        soldElecLabel.setText(decimalFormat.format(budgetModel.GetSoldElectricity()));
        fuelRodPriceLabel.setText(decimalFormat.format(budgetModel.GetFuelRodTotalCost()));
        budgetLabel.setText(decimalFormat.format(budgetModel.GetBudget()));
        maintenance.setText(decimalFormat.format(budgetModel.GetMaintenance()));
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
        
        currentTabIndex = tabbedPane.getComponentCount() - 1;
        tabbedPane.setSelectedIndex(currentTabIndex);

        this.add(tabbedPane);
    }

    private JPanel TotalPanel(BudgetModel budgetModel)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,3));
        panel.add(new Label("Income:"));
        panel.add(new Label("Electricity sold:"));
        soldElecLabel = new JLabel(decimalFormat.format(budgetModel.GetSoldElectricity()));
        panel.add(soldElecLabel);

        panel.add(new Label());
        panel.add(new Label());
        panel.add(new Label());

        panel.add(new Label("Loss:"));
        panel.add(new Label("Running Costs:"));
        maintenance = new JLabel(decimalFormat.format(budgetModel.GetMaintenance()));
        panel.add(maintenance);

        panel.add(new Label());
        panel.add(new Label("Research Cost:"));
        fuelRodPriceLabel = new JLabel(decimalFormat.format(budgetModel.GetResearchCost()));
        panel.add(fuelRodPriceLabel);

        panel.add(new Label());
        panel.add(new Label("Fuelrods:"));
        fuelRodPriceLabel = new JLabel(decimalFormat.format(budgetModel.GetFuelRodTotalCost()));
        panel.add(fuelRodPriceLabel);
        panel.add(new Label());
        panel.add(new Label());
        panel.add(new Label());
        panel.add(new Label("Total:"));
        panel.add(new Label());
        budgetLabel = new JLabel(decimalFormat.format(budgetModel.GetBudget()));
        panel.add(budgetLabel);

        return panel;
    }

    private void GetServices()
    {
        budgetService = BudgetService.GetInstance();
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
