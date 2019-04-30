package vazkii.unmending;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class KillingMendingAndOtherTales {

	// stolen from King Lemming thanks mate
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void killMending(PlayerPickupXpEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		EntityXPOrb orb = event.getOrb();

		player.xpCooldown = 2;
		player.onItemPickup(orb, 1);
		if (orb.xpValue > 0) {
			player.giveExperiencePoints(orb.xpValue);
		}

		orb.remove();
		event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		ItemStack out = event.getOutput();

		if (out.isEmpty() && (left.isEmpty() || right.isEmpty())) {
			return;
		}

		boolean isMended = false;

		Map<Enchantment, Integer> enchLeft = EnchantmentHelper.getEnchantments(left);
		Map<Enchantment, Integer> enchRight = EnchantmentHelper.getEnchantments(right);

		if (enchLeft.containsKey(Enchantments.MENDING) || enchRight.containsKey(Enchantments.MENDING)) {
			if (left.getItem() == right.getItem()) {
				isMended = true;
			}

			if (right.getItem() == Items.ENCHANTED_BOOK) {
				isMended = true;
			}
		}

		if (isMended) {
			if (out.isEmpty()) {
				out = left.copy();
			}

			if (!out.hasTag()) {
				out.setTag(new NBTTagCompound());
			}

			Map<Enchantment, Integer> enchOutput = EnchantmentHelper.getEnchantments(out);
			enchOutput.putAll(enchRight);
			enchOutput.remove(Enchantments.MENDING);
			EnchantmentHelper.setEnchantments(enchOutput, out);

			out.setRepairCost(0);
			if(out.isDamageable()) {
				out.setDamage(0);
			}

			event.setOutput(out);
			if (event.getCost() == 0) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onTooltip(ItemTooltipEvent event) {
		TextComponentTranslation itemgotmodified = new TextComponentTranslation("unmending.repaired");
		itemgotmodified.getStyle().setColor(TextFormatting.YELLOW);
		int repairCost = event.getItemStack().getRepairCost();
		if (repairCost > 0) {
			event.getToolTip().add(itemgotmodified);
		}
	}
}
