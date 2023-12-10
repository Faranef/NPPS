import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContextMenuListener extends MouseAdapter
{
    private Integer fueRlrodId = null;

    public ContextMenuListener()
    {
    
    }

    public ContextMenuListener(int furlrodId) 
    {
        this.fueRlrodId = furlrodId;
    }


    public void mousePressed(MouseEvent e) 
    {
        doPop(e);
    }

    public void mouseReleased(MouseEvent e) 
    {
        doPop(e);
    }

    private void doPop(MouseEvent e) 
    {
        if (e.isPopupTrigger()) 
        {
            ContextMenu menu;

            if(fueRlrodId != null)
            {
                menu = new ContextMenu(fueRlrodId);
            }
            else
            {
                menu = new ContextMenu();
            }

            menu.show(e.getComponent(),e.getX(), e.getY());
        }
    }

}
