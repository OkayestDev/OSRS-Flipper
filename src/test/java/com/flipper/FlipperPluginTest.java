package com.flipper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FlipperPluginTest
{
	/**
	 * Loads plugin with mock data from mocks/*.json
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception, IOException
	{
		// Path currentRelativePath = Paths.get("");
		// Path destBuys = Paths.get(RuneLite.RUNELITE_DIR + "\\flipper\\flipper-buys.json");
		// Path buysPath = Paths.get(
		// 	currentRelativePath.toAbsolutePath().toString()
		// 	+ "\\src\\test\\java\\com\\flipper\\mocks\\flipper-buys.json"
		// );
		// Path destSells = Paths.get(RuneLite.RUNELITE_DIR + "\\flipper\\flipper-sells.json");
		// Path sellsPath = Paths.get(
		// 	currentRelativePath.toAbsolutePath().toString()
		// 	+ "\\src\\test\\java\\com\\flipper\\mocks\\flipper-sells.json"
		// );
		// Path destFlips = Paths.get(RuneLite.RUNELITE_DIR + "\\flipper\\flipper-flips.json");
		// Path flipsPath = Paths.get(
		// 	currentRelativePath.toAbsolutePath().toString()
		// 	+ "\\src\\test\\java\\com\\flipper\\mocks\\flipper-flips.json"
		// );

		// Files.copy(buysPath, destBuys, StandardCopyOption.REPLACE_EXISTING);
		// Files.copy(sellsPath, destSells, StandardCopyOption.REPLACE_EXISTING);
		// Files.copy(flipsPath, destFlips, StandardCopyOption.REPLACE_EXISTING);

		ExternalPluginManager.loadBuiltin(FlipperPlugin.class);
		RuneLite.main(args);
	}
}