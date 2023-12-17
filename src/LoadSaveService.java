import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class LoadSaveService 
{
    private static LoadSaveService Instance;
    private RodService rodService;
    private BudgetService budgetService;
    private ReactorService reactorService;

    public static LoadSaveService GetInstance()
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

    public LoadSaveModel LoadGame()
    {
        LoadSaveModel loadModel = null;

        try 
        {
            ObjectMapper mapper = new ObjectMapper();
            LoadSaveModel loadModels = mapper.readValue(new File("F:\\UNI\\GUI\\Project\\NPPS\\GameData\\saveFile.json"), LoadSaveModel.class);

            System.out.println(loadModels.getBudget());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }


        return loadModel;
    }

    public void SaveGame(LoadSaveModel saveModel, String fileName)
    {
        try 
        {
            String pathName = "./GameDate/"+fileName+".json";
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(pathName), saveModel);
        } 
        catch (IOException e) 
        {
        }


    }

}

