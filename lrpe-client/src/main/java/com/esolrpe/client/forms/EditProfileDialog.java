package com.esolrpe.client.forms;

import com.esolrpe.client.api.ProfileService;
import com.esolrpe.client.config.Config;
import com.esolrpe.client.forms.util.DocumentSizeFilter;
import com.esolrpe.client.forms.util.FormUtils;
import com.esolrpe.client.tray.AppTrayIcon;
import com.esolrpe.shared.profiles.ContextUpdateProfile;
import com.esolrpe.shared.profiles.ProfileAPI;
import com.esolrpe.shared.profiles.ProfileData;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import java.awt.Frame;

public class EditProfileDialog extends JDialog {
    private final JLabel lblRemainingChars;
    private final DefaultStyledDocument doc;

    public EditProfileDialog(Frame owner, ProfileData profileData) {
        super(owner, owner.getTitle(), true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        AppTrayIcon.getInstance().registerHideableComponent(this);
        FormUtils.setIcon(this);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new MigLayout("", "[][400px]", "[][][][fill][][]"));
        setContentPane(contentPanel);

        contentPanel.add(new JLabel("ESO Character Name:"), "cell 0 0");
        JTextField txtCharacterName = new JTextField();
        contentPanel.add(txtCharacterName, "cell 1 0, grow");
        DefaultStyledDocument docCharacterName = new DefaultStyledDocument();
        docCharacterName.setDocumentFilter(new DocumentSizeFilter(ProfileAPI.MAXIMUM_ACTUAL_NAME_SIZE));
        txtCharacterName.setDocument(docCharacterName);

        contentPanel.add(new JLabel("Display Name:"), "cell 0 1");
        JTextField txtDisplayName = new JTextField();
        contentPanel.add(txtDisplayName, "cell 1 1, grow");
        DefaultStyledDocument docDisplayName = new DefaultStyledDocument();
        docDisplayName.setDocumentFilter(new DocumentSizeFilter(ProfileAPI.MAXIMUM_DISPLAY_NAME_SIZE));
        txtDisplayName.setDocument(docDisplayName);

        contentPanel.add(new JLabel("Description:"), "cell 0 2, spanx");
        JTextArea txtProfileData = new JTextArea();
        txtProfileData.setRows(30);
        txtProfileData.setLineWrap(true);
        txtProfileData.setWrapStyleWord(true);

        lblRemainingChars = new JLabel();
        contentPanel.add(lblRemainingChars, "cell 0 4, spanx");

        doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new DocumentSizeFilter(ProfileAPI.MAXIMUM_PROFILE_TEXT_SIZE));
        doc.addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) { updateCount(); }
            @Override
            public void insertUpdate(DocumentEvent e) { updateCount(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateCount(); }
        });
        txtProfileData.setDocument(doc);
        updateCount();

        JScrollPane scrollPane = new JScrollPane(txtProfileData);
        contentPanel.add(scrollPane, "cell 0 3, grow, spanx");

        if (profileData != null) {
            txtCharacterName.setText(profileData.getCharacterName());
            txtCharacterName.setEditable(false);
            txtDisplayName.setText(profileData.getDisplayName());
            txtProfileData.setText(profileData.getProfileText());
        }

        JButton btnSave = new JButton("Save");
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> this.dispose());

        btnSave.addActionListener(e -> {
            ContextUpdateProfile newProfileData = new ContextUpdateProfile();
            String characterName;
            if (profileData != null) {
                characterName = profileData.getCharacterName();
            } else {
                characterName = txtCharacterName.getText().trim();
            }

            newProfileData.setDisplayName(txtDisplayName.getText());
            newProfileData.setProfileText(txtProfileData.getText());

            ProfileService profileService = new ProfileService();
            profileService.updateProfile(Config.getInstance().getMegaserver(),
                    characterName,
                    newProfileData);

            this.dispose();
        });

        contentPanel.add(btnSave, "cell 0 5, spanx, align center");
        contentPanel.add(btnClose, "cell 0 5, spanx, align center");

        pack();
    }

    private void updateCount() {
        lblRemainingChars.setText((ProfileAPI.MAXIMUM_PROFILE_TEXT_SIZE - doc.getLength()) + " characters remaining");
    }
}
