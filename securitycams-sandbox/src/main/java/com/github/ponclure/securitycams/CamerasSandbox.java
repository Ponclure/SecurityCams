/*
 * This file is part of the SecurityCams (https://github.com/Ponclure/SecurityCams).
 *
 *  Copyright (c) 2020 Ponclure team and contributors
 *
 *  SecurityCams is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, version 3.
 *
 *  SecurityCams is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.ponclure.securitycams;

import com.github.ponclure.securitycams.commands.DestroyCameraCommand;
import com.github.ponclure.securitycams.commands.SetCameraCommand;
import com.github.ponclure.securitycams.commands.UseCameraCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CamerasSandbox extends JavaPlugin {

	private final FilteringCriteria criteria = new FilteringCriteria();
	private CameraManager cameraManager;

	@Override
	public void onEnable() {
		getLogger().info(ChatColor.GREEN + "Security Cams API is Loading Up");

		try {
			cameraManager = new CameraManager(this, new File(getDataFolder(), "cameras.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		new UseCameraCommand(this, getCommand("usecamera"));
		new SetCameraCommand(this, getCommand("setcamera"));
		new DestroyCameraCommand(this, getCommand("deletecamera"));
	}

	@Override
	public void onDisable() {
		getLogger().info(ChatColor.GREEN + "Security Cams API is Shutting Down");
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	public List<String> filterNames(final String base) {
		criteria.setBase(base);
		return cameraManager.getCameraNames().stream().filter(criteria).collect(Collectors.toList());
	}

	private static class FilteringCriteria implements Predicate<String> {

		private String base = "";
		public void setBase(final String base) {
			this.base = base.toLowerCase();
		}

		@Override
		public boolean test(final String other) {
			return other.toLowerCase().startsWith(base);
		}
	}
}
