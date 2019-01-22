package vazkii.unmending;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KillingMendingAndOtherTales {

	// stolen from King Lemming thanks mate
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void killMending(PlayerPickupXpEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		EntityXPOrb orb = event.getOrb();

		player.xpCooldown = 2;
		player.onItemPickup(orb, 1);
		if (orb.xpValue > 0)
			player.addExperience(orb.xpValue);

		orb.setDead();
		event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		ItemStack out = event.getOutput();

		if(out.isEmpty() && (left.isEmpty() || right.isEmpty()))
			return;
		
		boolean isMended = false;

		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(left);
		if(enchants.containsKey(Enchantments.MENDING)) {
			enchants.remove(Enchantments.MENDING);
			if(out.isEmpty()) {
				out = left.copy();
				if(out.isItemStackDamageable())
					out.setItemDamage(0);
				EnchantmentHelper.setEnchantments(enchants, out);
			}
			
			isMended = true;
		}
		
		if(right.getItem() == Items.ENCHANTED_BOOK) {
	        NBTTagList enchList = ItemEnchantedBook.getEnchantments(right);
	        for(int i = 0; i < enchList.tagCount(); i++) {
	        	NBTTagCompound cmp = (NBTTagCompound) enchList.get(i);
	        	Enchantment ench = Enchantment.REGISTRY.getObjectById(cmp.getShort("id"));
	        	
	        	if(ench == Enchantments.MENDING) {
	        		isMended = true;
	        		break;
	        	}
	        }
		}

		if(isMended) {
			if(out.isEmpty())
				out = left.copy();

			if(!out.hasTagCompound())
				out.setTagCompound(new NBTTagCompound());
			
			out.getTagCompound().setInteger("RepairCost", 0);
			event.setOutput(out);
		}
		
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onTooltip(ItemTooltipEvent event) {
		int repairCost = event.getItemStack().getRepairCost();
		if(repairCost > 0)
			event.getToolTip().add(TextFormatting.YELLOW + I18n.translateToLocal("unmending.repaired"));
	}

}
