package com.tf2center.discordbot.parser.discord.notifications.csv;

import java.io.File;
import java.io.FilenameFilter;

public class CSVFileNameFilter implements FilenameFilter {

    private String csvFile;

    public CSVFileNameFilter(String csvFile) {
        this.csvFile = csvFile;
    }

    @Override
    public boolean accept(File file, String s) {
        return s.equals(csvFile);
    }
}
