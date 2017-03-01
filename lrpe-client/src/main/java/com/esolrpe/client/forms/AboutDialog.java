package com.esolrpe.client.forms;

import com.esolrpe.client.tray.AppTrayIcon;
import com.esolrpe.shared.version.VersionDetails;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Frame;

public class AboutDialog extends JDialog {
    public AboutDialog(Frame owner) {
        super(owner, owner.getTitle(), true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        AppTrayIcon.getInstance().registerHideableComponent(this);
        FormUtils.setIcon(this);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new MigLayout("", "[]", "[][][]"));
        setContentPane(contentPanel);

        VersionDetails versionDetails = VersionDetails.createCurrentVersionDetails();

        JLabel label1 = new JLabel("eso-lrpe-updater - version " + versionDetails.getVersion());
        contentPanel.add(label1, "cell 0 0");

        JLabel labelHtml = new JLabel(
                "<html><div>eso-lrpe created by @Tactitocalon</div>" +
                        "<div>www.eso-lrpe.com</div>" +
                        "<div></div>" +
                        "<div>Icons made by " +
                        "<a href=\"http://www.flaticon.com/authors/madebyoliver\" " +
                        "title=\"Madebyoliver\">Madebyoliver</a> from " +
                        "<a href=\"http://www.flaticon.com\" " +
                        "title=\"Flaticon\">www.flaticon.com</a> is licensed by " +
                        "<a href=\"http://creativecommons.org/licenses/by/3.0/\" " +
                        "title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a>" +
                        "</div></html>");


        contentPanel.add(labelHtml, "cell 0 1");


        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> this.dispose());
        contentPanel.add(btnClose, "cell 0 2, align center");

        pack();
        FormUtils.centerWindow(this);
    }
}
