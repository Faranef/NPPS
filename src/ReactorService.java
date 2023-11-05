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

    public double Calculate()
    {
        double amount = 0;
        double amountAbove = 0;
        double amountUnder = 0;
        var fuelRods = rodService.GetRod(RodModelStyle.FuelRod);
        var controlRod = rodService.GetRod(RodModelStyle.ControlRod);
        amountUnder = controlRod.GetRodLevel() * 6;
        
        for (var fuelRod : fuelRods.GetRodModelList()) 
        {
            // if (fuelRod.GetRodLevel() > controlRod.GetRodLevel()) 
            // {
            //     amountAbove += fuelRod.GetRodLevel() - controlRod.GetRodLevel();
            // }    
            amountAbove += fuelRod.GetRodLevel();
            
            // if (fuelRod.GetRodLevel() < controlRod.GetRodLevel()) 
            // {
            //     amountUnder +=  controlRod.GetRodLevel() - fuelRod.GetRodLevel();
            // }  
        }

        // if (amountAbove <= 0) 
        // {
        //     amountUnder = 0;
        // }

        amount = amountAbove - amountUnder;
            
        return amount;
    }

}
