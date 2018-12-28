package ivrbase;


import java.io.*;
import java.util.*;


public class Parameters
{
    public boolean IsLoaded = false;

    private Properties properties = null;

    public Parameters()
    {
        properties = new Properties();
    }

    public boolean ReadParametersFile (String ParametersFile)
    {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(ParametersFile);

            properties.load(fis);

            fis.close();

            IsLoaded = true;
        }

        catch (IOException e) {
            try
            {
                if( fis != null) fis.close();
            }
            catch (Exception e2) {
            }

            return false;
        }

        return true;
    }

    public boolean WriteParametersFile (String ParametersFile)
    {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(ParametersFile);

            properties.store(fos, "");

            fos.close();
        }

        catch (IOException e) {
            try
            {
                if( fos != null) fos.close();
            }
            catch (Exception e2) {
            }

            return false;
        }

        return true;
    }

    public String GetValue (String ParameterKey)
    {
        return properties.getProperty(ParameterKey, "");
    }

    public String GetValue (String ParameterKey, String DefaultValue)
    {
        return properties.getProperty(ParameterKey, DefaultValue);
    }

    public void SetValue (String ParameterKey, String ParameterValue)
    {
        properties.setProperty(ParameterKey, ParameterValue);

        return;
    }

    public void Clear ()
    {
        properties.clear();

        return;
    }
}