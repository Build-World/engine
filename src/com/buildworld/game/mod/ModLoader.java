package com.buildworld.game.mod;

import com.buildworld.engine.graphics.Window;
import com.shawnclake.morgencore.core.component.filesystem.Files;
import com.shawnclake.morgencore.core.component.services.Services;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class ModLoader implements IMod {

    public final String modsPath;

    public ModLoader(String modsPath) {
        this.modsPath = modsPath;
    }

    public ModService modService()
    {
        return Services.getService(ModService.class);
    }


    @Override
    public String getKey() {
        return "modloader";
    }

    @Override
    public String getName() {
        return "Mod Loader";
    }

    @Override
    public String getDescription() {
        return "Loads more mods";
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public void onBoot(Window window) throws Exception {
        Files fileHelper = new Files();
        List<File> files = fileHelper.listFilesRecursively(modsPath);
        for(File file : files)
        {
            if(!fileHelper.getFileExtension(file, false).equalsIgnoreCase("jar"))
                continue;

            URL url = file.toURI().toURL();
            ClassLoader cl = new URLClassLoader(new URL[]{url});

            IMod mod = (IMod)cl.loadClass("Mod").getDeclaredConstructor().newInstance();
            modService().add(mod);
            System.out.println("Loaded mod: " + mod.getName() + " - " + mod.getVersion());
        }

        for(IMod mod : modService().getItems())
        {
            mod.onBoot(window);
        }
    }

    @Override
    public void onLoad() throws Exception {
        for(IMod mod : modService().getItems())
        {
            mod.onLoad();
        }
    }

    @Override
    public void onReady() throws Exception {
        for(IMod mod : modService().getItems())
        {
            mod.onReady();
        }
    }

    @Override
    public void onPlay() throws Exception {
        for(IMod mod : modService().getItems())
        {
            mod.onPlay();
        }
    }

    @Override
    public void onEnable() throws Exception {
        for(IMod mod : modService().getItems())
        {
            mod.onEnable();
        }
    }

    @Override
    public void onDisable() throws Exception {
        for(IMod mod : modService().getItems())
        {
            mod.onDisable();
        }
    }

    @Override
    public void onTick() throws Exception {
        for(IMod mod : modService().getItems())
        {
            mod.onTick();
        }
    }

    @Override
    public void onDraw() throws Exception {
        for(IMod mod : modService().getItems())
        {
            mod.onDraw();
        }
    }
}
