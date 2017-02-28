package com.esolrpe.client.forms;

import com.esolrpe.client.tray.AppTrayIcon;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormUtils {
    public static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public static void setIcon(Window window) {
        try {
            BufferedImage imgIcon = ImageIO.read(AppTrayIcon.class.getResourceAsStream(
                    "/com/esolrpe/client/icons/icon_resume_color.png"));
            window.setIconImage(imgIcon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getNowAsString() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        return sdfDate.format(now);
    }

    public static String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
