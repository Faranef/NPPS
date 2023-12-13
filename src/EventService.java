public class EventService 
{
    private static EventService Instance = null;
    private BudgetService economyService;
    private RodService rodService;
    private ReactorService reactorService;

    private EventService() 
    {
        economyService = BudgetService.GetInstance();
        rodService = RodService.GetInstance();
        reactorService = ReactorService.GetInstance();
    }


    public static EventService GetInstance()
    {
        if(Instance == null)
        {
            Instance = new EventService();
        }

        return Instance;
    }   
}
