import java.util.*;

public class RodService
{
    private static RodService Instance = null;
    private List<RodModel> rodList = new ArrayList<RodModel>();
    private EconomyService economyService;
    
    
    private RodService() 
    {
        economyService = EconomyService.GetInstance();
    }

    public static RodService GetInstance()
    {
        if(Instance == null)
        {
            Instance = new RodService();
        }

        return Instance;
    }

    public void AddRodToList(RodModel rod)
    {
        rodList.add(rod);
    }

    public List<RodModel> GetRodList()
    {
        return rodList;
    }
    
    public RodModel GetRod(RodModelStyle rodModelStyle)
    {
        RodModel rodModelReturn = null;
        // no LINQ in java...
        for (RodModel rodModel : rodList) 
        {
            if(rodModel.GetRodStyle() == rodModelStyle)   
            {
                rodModelReturn = rodModel;
            } 
        }
        return rodModelReturn;
    }

    public int CountFuelRodsAbove(int above)
    {
        // closes thing to LINQ
        //Optional<SomeObject> result = someObjects.stream().filter(obj -> some_condition_met).findFirst();
        int count = 0;
        Optional<RodModel> fuelRod = rodList.stream().filter(x -> x.GetRodStyle() == RodModelStyle.FuelRod).findFirst();

        for (RodModel rodModel : fuelRod.get().GetRodModelList()) 
        {
            if(rodModel.GetRodLevel() > above)
            {
                count++;
            }
        }

        return count;
    }

    public void FuelRodDecay()
    {
        Optional<RodModel> fuelRod = rodList.stream().filter(x -> x.GetRodStyle() == RodModelStyle.FuelRod).findFirst();

        for (RodModel rodModel : fuelRod.get().GetRodModelList()) 
        {
            var rodLevel = rodModel.GetRodLevel();
            var decay = rodLevel * 1.0025;
            rodModel.SetLifeSpan(rodModel.GetLifeSpan() - decay);

            if(rodModel.GetLifeSpan() <= 0)
            {
                rodModel.SetActivity(false);
            }
        }
    }

    public void SetAllToNew()
    {
        Optional<RodModel> fuelRod = rodList.stream().filter(x -> x.GetRodStyle() == RodModelStyle.FuelRod).findFirst();

        for (RodModel rodModel : fuelRod.get().GetRodModelList())
        {
            rodModel.SetRodLevel(0);
            rodModel.SetLifeSpan(100000);   
            economyService.DeductFromBalance(50000);    // set variable rod price - event driven
        }

        Optional<RodModel> controlRod = rodList.stream().filter(x -> x.GetRodStyle() == RodModelStyle.ControlRod).findFirst();
        controlRod.get().SetRodLevel(100);
    }
}
