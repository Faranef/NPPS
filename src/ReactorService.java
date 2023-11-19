public class ReactorService
{
    private static ReactorService Instance = null;
    private RodService rodService;

    private ReactorService()
    {
        rodService = RodService.GetInstance();
    }

    public static ReactorService GetInstance()
    {
        if (Instance == null)
        {
            Instance = new ReactorService();
        }

        return Instance;
    }

    // do the calculations here

    public double CalculateTemp()
    {
        double amount = 0;
        double amountAbove = 0;
        double amountControlRod = 0;
        var fuelRods = rodService.GetRod(RodModelStyle.FuelRod);
        var controlRod = rodService.GetRod(RodModelStyle.ControlRod);
        //amountControlRod = controlRod.GetRodLevel();
        amountControlRod = controlRod.GetRodLevel() * 6;
        
        for (var fuelRod : fuelRods.GetRodModelList()) 
        {
            if (fuelRod.IsActive()) 
            {
                amountAbove += fuelRod.GetRodLevel();
            }
        }

        //amount = amountAbove * (amountControlRod /100);

        amount = amountAbove - amountControlRod;
            
        return amount;
    }

    public double CalculateWattage(double temp)
    {
        double amount = 0;

        if (temp <= 100) 
        {
            amount = 0;
        }
        else if (temp >= 101 && temp <= 299) 
        {
            amount = temp * 3.67;
        }
        else if (temp >= 300)
        {
            amount = 1100;
        } 

        return amount;
    }
}
