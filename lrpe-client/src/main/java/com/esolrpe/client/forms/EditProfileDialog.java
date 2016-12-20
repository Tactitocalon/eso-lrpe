package com.esolrpe.client.forms;

import com.esolrpe.shared.profiles.ProfileData;
import net.miginfocom.swing.MigLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Frame;

public class EditProfileDialog extends JDialog {
    public EditProfileDialog(Frame owner, ProfileData profileData) {
        super(owner, owner.getTitle(), true);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new MigLayout("", "[][400px]", "[][][][fill][]"));
        setContentPane(contentPanel);

        contentPanel.add(new JLabel("ESO Character Name:"), "cell 0 0");
        JTextField txtCharacterName = new JTextField();
        contentPanel.add(txtCharacterName, "cell 1 0, grow");

        contentPanel.add(new JLabel("Display Name:"), "cell 0 1");
        JTextField txtDisplayName = new JTextField();
        contentPanel.add(txtDisplayName, "cell 1 1, grow");

        contentPanel.add(new JLabel("Description:"), "cell 0 2, spanx");
        JTextArea txtProfileData = new JTextArea();
        txtProfileData.setRows(30);
        txtProfileData.setLineWrap(true);
        txtProfileData.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtProfileData);
        contentPanel.add(scrollPane, "cell 0 3, grow, spanx");

        if (profileData != null) {
            txtCharacterName.setText(profileData.getCharacterName());
            txtCharacterName.setEditable(false);
            txtDisplayName.setText(profileData.getDisplayName());
            txtProfileData.setText(profileData.getProfileText());
        }

        pack();
    }
}
