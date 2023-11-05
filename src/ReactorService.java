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
        int amount = 0;
        var fuelRodCount = rodService.CountFuelRodsAbove(30);
        
        if (fuelRodCount > 0) 
        {
            switch (fuelRodCount) 
            {
                case 1:
                    amount = 1;
                    break;
                case 2:
                    amount = 2;
                    break;
                case 3:
                    amount = 3;
                    break;
                case 4:
                    amount = 4;
                    break;
                case 5:
                    amount = 5;
                    break;
                case 6:
                    amount = 6;
                    break;
            }
        }
        else
        {

        }
        return amount;
    }

}
