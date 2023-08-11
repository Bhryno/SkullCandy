package com.github.conditioner.skullcandy;

import com.github.conditioner.skullcandy.util.ArgumentParser;
import com.github.conditioner.skullcandy.util.ArrayListUtil;
import com.github.conditioner.skullcandy.util.AttachUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        String arg1 = ArgumentParser.requireType(args[0], String.class);
        String arg2 = ArgumentParser.requireType(args[1], String.class);
        String arg3 = ArgumentParser.requireType(args[2], String.class);

        String[] launchArgs = new String[args.length - 3];

        System.arraycopy(args, 3, launchArgs, 0, launchArgs.length);

        File in = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        String out = arg1 + " " + arg3 + " " + ArrayListUtil.separateList(getLoadedMods(new ArrayList<>(Arrays.asList(launchArgs))), " ");

        AttachUtil.attach(arg2, in, out);
    }

    private static ArrayList<File> getLoadedMods(ArrayList<String> args) {
        ArrayList<File> mods = new ArrayList<>();
        ListIterator<String> it = args.listIterator();
        
        while (it.hasNext()) {
            File in = ArgumentParser.requireType(it.next(), File.class);

            if (in.isDirectory()) {
                if (in.listFiles() == null) {
                    continue;
                }

                File[] skullCandyFiles = Objects.requireNonNull(in.listFiles());

                for (int length = skullCandyFiles.length, i = 0; i < length; i++) {
                    it.add(skullCandyFiles[i].getAbsolutePath());
                    it.previous();
                }
            } else {
                mods.add(in);
                it.remove();
            }
        }
        return mods;
    }
}
