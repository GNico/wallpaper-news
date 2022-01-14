package com.gnico.utils;

import java.util.HashMap;
import java.util.Map;

import com.gnico.main.Main;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;

public class WindowsUtils {

    public static void setImageAsWallpaper(String fileName) {       
        String jarPath = Main.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();
        
        String dir = jarPath.substring(1, jarPath.lastIndexOf("/") + 1);
        String filePath = dir + fileName;
                
       // filePath = "F:/eclipse-workspace/headline-wallpaper/generatedWallpaper.bmp";
               
        SPI.INSTANCE.SystemParametersInfo(
            new UINT_PTR(SPI.SPI_SETDESKWALLPAPER), 
            new UINT_PTR(0), 
            filePath, 
            new UINT_PTR(SPI.SPIF_UPDATEINIFILE | SPI.SPIF_SENDWININICHANGE)
        );
    }    
    
    public interface SPI extends StdCallLibrary {
        long SPI_SETDESKWALLPAPER = 20;
        long SPIF_UPDATEINIFILE = 0x01;
        long SPIF_SENDWININICHANGE = 0x02;
    
        @SuppressWarnings("serial")
        Map<String, Object> amap = new HashMap<String, Object>() {
            {
                put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
                put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
             } 
             
        };
        @SuppressWarnings("deprecation")
        SPI INSTANCE = (SPI) Native.loadLibrary("user32", SPI.class, amap);
    
        boolean SystemParametersInfo(
            UINT_PTR uiAction,
            UINT_PTR uiParam,
            String pvParam,
            UINT_PTR fWinIni
        );
    }
}
