package io.github.phantamanta44.pwarfare.data;

import java.util.List;

import net.dmulloy2.swornguns.types.Gun;

import com.google.common.collect.Lists;

public class GunWrapper {
	
	public final Gun gun;
	public final int amt;
	public final List<AttachmentWrapper> attachments;
	public final int xpNeeded, slot;
	
	public GunWrapper(Gun g, int amount, int xp, int slt, AttachmentWrapper... attach) {
		gun = g;
		amt = amount;
		xpNeeded = xp;
		slot = slt;
		attachments = Lists.newArrayList(attach);
		for (AttachmentWrapper wrapper : attach)
			GameKit.attMap.put(Integer.valueOf(wrapper.hashCode()), wrapper);
	}
	
	public static class PlayerGun {
		
		GunWrapper gun;
		int xp;
		
	}
	
}
