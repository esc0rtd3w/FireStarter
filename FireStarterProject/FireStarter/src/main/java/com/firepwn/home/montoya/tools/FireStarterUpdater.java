package com.firepwn.home.montoya.tools;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Scanner;

/**
 * Update class for FireStarter
 */
public class FireStarterUpdater extends Updater
{
    /** Update URL where updated versions are found */
    private String mUpdateUrl = "https://github.com/esc0rtd3w/com.firepwn.home.montoya/releases";

    @Override
    public String getAppName()
    {
        return "FirePwnHome";
    }

    @Override
    public String getPackageName(Context context)
    {
        return context.getApplicationInfo().packageName;
    }

    @Override
    public Boolean isVersionNewer(String oldVersion, String newVersion)
    {
        // Use standard check
        return isVersionNewerStandardCheck(oldVersion, newVersion);
    }

    @Override
    protected void updateLatestVersionAndApkDownloadUrl() throws Exception
    {
        // Build the url
        URL url = new URL(mUpdateUrl);

        // Read from the URL
        Scanner scan = new Scanner(url.openStream());
        StringBuilder str = new StringBuilder();
        while (scan.hasNext())
        {
            str.append(scan.nextLine());
        }
        scan.close();

        // build a JSON object
        JSONArray obj = new JSONArray(str.toString());
        if(obj.length() <= 0)
        {
            throw new JSONException("No content found");
        }

        JSONObject latestRelease = obj.getJSONObject(0);
        String tagName = latestRelease.getString("tag_name");
        if(tagName == null || tagName.equals(""))
        {
            throw new JSONException("Latest release tag name is empty");
        }
        mLatestVersion = tagName;

        // Search for apk-download url
        JSONArray assets = latestRelease.getJSONArray("assets");
        for(Integer i = 0; i < assets.length(); i++)
        {
            JSONObject currentAsset = assets.getJSONObject(i);
            String downloadUrl = currentAsset.getString("browser_download_url");
            if(downloadUrl.startsWith("https://github.com/esc0rtd3w/com.firepwn.home.montoya/releases") && downloadUrl.endsWith(".apk"))
            {
                mApkDownloadUrl = downloadUrl;
                break;
            }
        }
        if(mApkDownloadUrl == null)
        {
            throw new JSONException("No .apk download URL found");
        }
    }
}
