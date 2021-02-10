package Plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import Plugin.MessageManager.MessageType;

public class ReloadCMD implements CommandExecutor {

	public ReloadCMD(Main main) {
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("str.reload")) {
			MessageManager.getManager().msg(sender, MessageType.BAD, "� ��� ��� ���� ��� ������������ �������");
			return true;
		}
		Bukkit.broadcastMessage("�r�a[�4�n����������a]: �b������������ �������");
		Bukkit.broadcastMessage("�r�a[�4�n����������a]: �b����� ����� �������� ������ ������� /login ������");
		Bukkit.reload();
		return true;
	}

}
