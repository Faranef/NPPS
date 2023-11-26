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
    }

    private void CreateLabels()
    {

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
