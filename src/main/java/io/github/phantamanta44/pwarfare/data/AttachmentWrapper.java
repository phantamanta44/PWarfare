package io.github.phantamanta44.pwarfare.data;

import net.dmulloy2.swornguns.types.Attachment;

public class AttachmentWrapper {

	public final Attachment att;
	public final int killsNeeded, slot;
	
	public AttachmentWrapper(Attachment attach, int kills, int slt) {
		att = attach;
		killsNeeded = kills;
		slot = slt;
	}
	
}
