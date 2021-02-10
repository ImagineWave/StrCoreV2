package Plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Plugin.MessageManager.MessageType;

public class MuteSetter implements CommandExecutor{
	private Main plugin;

	public MuteSetter(Main plugin) {
		this.plugin = plugin;	
		}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		 if (!(sender instanceof Player)) {
	            sender.sendMessage("Only for player usage!");
	            return true;
	        }
		 Player p = (Player) sender;
		 if (!sender.hasPermission("str.tempmute")) {
			 MessageManager.getManager().msg(p, MessageType.BAD, "� ��� ��� ���� ��� ������������� ������� tempmute");
			 return true;
		 }
		 if(args.length == 0) {
				MessageManager.getManager().msg(p, MessageType.INFO, "�������������: /tempmute <player> <time (������)> <�������>");
				return true;
		 }
		 if(args.length == 1) {
			 Player t = (Bukkit.getPlayerExact(args[0]));
			 if(canRewriteMute(t, p)) {
				 unmutePlayer(t,p);
				 return true;
			 }
			 MessageManager.getManager().msg(p, MessageType.BAD, "�� �� ������ ����� ���, �������� ������ �������");
			 return true;
		 }
		 if(args.length >= 2) {
			 Player t = (Bukkit.getPlayerExact(args[0]));
			 String message = "";
				for (int i = 2; i != args.length; i++) {
					message += args[i] +" ";
				}
			 try {
			 Long time = Long.parseLong(args[1]);
			 if( (time> 3600) && (!p.hasPermission("str.tempmute.ulimited"))){
				 MessageManager.getManager().msg(p, MessageType.BAD, "�� �� ������ ������ ����� ��� �� ���");
				 return true;
			 }
			 if(!canRewriteMute(t, p)) {
				 MessageManager.getManager().msg(p, MessageType.BAD, "�� �� ������ �������� ���, �������� ������ �������");
				 return true;
			 }
			 Long qtime = System.currentTimeMillis() + time*1000;
			 String reason = message;
			 StrPlayerToMute(t,qtime,reason,p);
			 }
			 catch (NumberFormatException e) {
				 MessageManager.getManager().msg(p, MessageType.BAD, "������� ����� � ��������");
				 return true;
			 }
		 }
		return false;
	}
	
	public Boolean canRewriteMute (Player p, Player muter) {
		StrPlayer spl = new StrPlayer(p,plugin);
		if(spl.getMutedBy().equals(muter.getName())) {
			return true;
		}
		if(spl.getMuteTime()<System.currentTimeMillis()) {
			return true;
		}
		if(muter.hasPermission("str.admin")) {
			return true;
		}
		return false;
	}
	
	public void StrPlayerToMute(Player p, Long time, String reason, Player m) {
		StrPlayer spl = new StrPlayer(p,plugin);
		spl.setMuted(true);
		spl.setMuteReason(reason);
		spl.setMuteTime(time);
		spl.setMutedBy(m.getName());
		spl.setPlayerCfg(spl);
		String msgduration = formatDuration((time-System.currentTimeMillis())/1000);
		Bukkit.broadcastMessage("�7[�c���������7]: �6"+ m.getName()+" �b������� ������ �6"+p.getName()+"�b �� �6" + msgduration+ "�b �� ������� �6" + reason);
		MessageManager.getManager().msg(p, MessageType.BAD, "��� ������� �6"+m.getName()+"�c �� �6" + msgduration+ "�c �� ������� �6" + reason);
	}
	public void unmutePlayer(Player p, Player m) {
		StrPlayer spl = new StrPlayer(p,plugin);
		spl.setMuted(true);
		spl.setMuteReason("unmuted");
		spl.setMuteTime(0);
		spl.setMutedBy(m.getName());
		spl.setPlayerCfg(spl);
		Bukkit.broadcastMessage("�7[�c���������7]: �6"+ m.getName()+" �a���� ��� � ������ �6"+p.getName());
	}
	
	public String formatDuration(Long time) {
		long hours = time/3600;
		long minutes = time%3600/60;
		long seconds = time%3600%60;
		String sthours = Long.toString(hours);
		String stminutes = Long.toString(minutes);
		String stseconds = Long.toString(seconds);
		String msgduration = "";
		if(hours != 0) msgduration += sthours + " ���(��) ";
		if(minutes != 0) msgduration += stminutes + " �����(�) ";
		if(seconds != 0) msgduration += stseconds + " ������(�) ";
		return msgduration;
	}
}
