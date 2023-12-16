package com.tf2center.discordbot.dto.json.tf2cpreview;

public class TF2CMainPage {
    private TF2CLobbyPreview[] previews;

    public TF2CLobbyPreview[] getPreviews() {
        return previews;
    }

    public void setPreviews(TF2CLobbyPreview[] previews) {
        this.previews = previews;
    }
}
