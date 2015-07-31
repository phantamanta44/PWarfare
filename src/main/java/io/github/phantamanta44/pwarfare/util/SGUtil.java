package io.github.phantamanta44.pwarfare.util;

import io.github.phantamanta44.pwarfare.PWarfare;
import net.dmulloy2.swornguns.types.Attachment;
import net.dmulloy2.swornguns.types.Gun;

public class SGUtil {

	public static Gun getGun(String name) {
		return PWarfare.INSTANCE.sguns.getLoadedGuns().get(name);
	}
	
	public static Attachment getAtt(String name) {
		return PWarfare.INSTANCE.sguns.getLoadedAttachments().get(name);
	}
	
}
