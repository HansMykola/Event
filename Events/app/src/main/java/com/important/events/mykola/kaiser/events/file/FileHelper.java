package com.important.events.mykola.kaiser.events.file;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

import com.important.events.mykola.kaiser.events.MyApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileHelper
{
    public final static String ERROR = "no";
    private final static String USERFILE = "SaveUser.bin";
    private final static String NAMEDIRECTORY = "EventImages";
    private ContextWrapper mContextWrapper;

    public FileHelper()
    {
        this.mContextWrapper = new ContextWrapper(MyApp.get().getApplicationContext());
    }

    public boolean isUserBin()
    {
        if ((new File(USERFILE).exists()) || (!readUserBin().equals(ERROR)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void writeUserBin(String id)
    {
        try
        {
            FileOutputStream fileOutput = MyApp.get().openFileOutput(USERFILE, Context.MODE_PRIVATE);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutput);
            outputStream.writeObject(id);
            outputStream.close();
            fileOutput.close();
        }
        catch (Exception e)
        {

        }
    }

    public String readUserBin()
    {
        try
        {
            FileInputStream fileInput = MyApp.get().openFileInput(USERFILE);
            ObjectInputStream inputStream = new ObjectInputStream(fileInput);
            String id = String.valueOf(inputStream.readObject());
            MyApp.get().getUser().setId(id);
            return id;
        }
        catch (Exception e)
        {

        }
        return "no";
    }

    public void saveImage(Bitmap bitmap, String name)
    {
        try
        {
            FileOutputStream fileOutput = new FileOutputStream(
                                              new File(mContextWrapper
                                                   .getDir(NAMEDIRECTORY, Context.MODE_PRIVATE),
                                                      changeName(name)));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutput);
            fileOutput.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String changeName(String name)
    {
        return name.replace(" ", "_")
                .replace(":", "_")
                .replace(";", "_")
                .replace("`", "_")
                .replace("'", "_")
                .replace(",", "_")
                .replace("-", "_") + ".png";
    }

    public File readImage(String name)
    {
        return new File(mContextWrapper.getDir(NAMEDIRECTORY, Context.MODE_PRIVATE), changeName(name));
    }

    public void deleteImage(String name)
    {
        File directory = mContextWrapper.getDir(NAMEDIRECTORY, Context.MODE_PRIVATE);
        File myImageFile = new File(directory, changeName(name));
        myImageFile.delete();
    }
}
