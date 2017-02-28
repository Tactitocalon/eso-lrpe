package com.esolrpe.client.forms;

import com.esolrpe.client.Application;
import com.esolrpe.client.config.Config;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

public class ConfigureAddonLocationForm extends JFrame {
    public static void display() throws InterruptedException, InvocationTargetException {
        CountDownLatch waitLatch = new CountDownLatch(1);
        SwingUtilities.invokeAndWait(() -> {
            ConfigureAddonLocationForm form = new ConfigureAddonLocationForm(waitLatch);
            form.setVisible(true);
        });
        waitLatch.await();
    }


    private ConfigureAddonLocationForm(CountDownLatch waitLatch) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(Application.TITLE);
        setResizable(false);
        FormUtils.setIcon(this);

        setLayout(new MigLayout("", "[500px][]", "[][][]8px[]"));

        add(new JLabel("Location of ESO AddOns folder:"), "cell 0 0, spanx");
        add(new JLabel("eg. \"C:\\Users\\YourWindowsUsername\\Documents\\Elder Scrolls Online\\live\\AddOns\""),
                "cell 0 2, spanx");

        JTextField txtAddonFolderLocation = new JTextField();
        txtAddonFolderLocation.setText(computeDefaultAddonLocation().getAbsolutePath());
        add(txtAddonFolderLocation, "cell 0 1, grow");

        JButton btnSelectFileLocation = new JButton("Browse");
        add(btnSelectFileLocation, "cell 1 1");

        JButton btnSave = new JButton("Save");
        add(btnSave, "cell 0 3, spanx, align center");

        btnSave.addActionListener(e -> {
            Config.getInstance().setEsoAddonLocation(txtAddonFolderLocation.getText());
            Config.save();
            dispose();
            waitLatch.countDown();
        });

        pack();
        FormUtils.centerWindow(this);
        setVisible(true);
    }

    private File computeDefaultAddonLocation() {
        File myDocuments = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
        return new File(myDocuments, "Elder Scrolls Online/live/AddOns");
    }

}
