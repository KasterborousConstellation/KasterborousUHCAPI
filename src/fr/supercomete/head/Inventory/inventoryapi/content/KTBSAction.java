package fr.supercomete.head.Inventory.inventoryapi.content;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
public final class KTBSAction {
    private final ClickType type;
    private final InventoryAction action;
    public KTBSAction(final ClickType type,final InventoryAction action){
        this.type=type;
        this.action=action;

    }

    public boolean ShiftedClicked(){
        return type.isShiftClick();
    }
    public boolean IsRightClick(){
        return type.isRightClick();
    }
    public boolean IsLeftClick(){
        return type.isLeftClick();
    }
    public boolean IsCreativeClick(){
        return type.isCreativeAction();
    }
    public boolean IsKeyBoardClick(){
        return type.isKeyboardClick();
    }
    public InventoryAction getAction(){
        return action;
    }
}
