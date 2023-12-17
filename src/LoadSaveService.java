public class LoadSaveService 
{
    private LoadSaveService Instance;
    private RodService rodService;
    private BudgetService budgetService;
    private ReactorService reactorService;

    public LoadSaveService GetInstance()
    {
        if (Instance == null)
        {
            Instance = new LoadSaveService();
        }

        return Instance;
    }

    private LoadSaveService() 
    {
        rodService = RodService.GetInstance();
        budgetService = BudgetService.GetInstance();   
        reactorService = ReactorService.GetInstance();
    }




}
