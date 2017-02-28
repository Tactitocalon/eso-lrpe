package com.esolrpe.client.forms;

import com.esolrpe.client.Application;
import com.esolrpe.client.api.ProfileService;
import com.esolrpe.client.config.Config;
import com.esolrpe.client.tray.AppTrayIcon;
import com.esolrpe.shared.profiles.ContextDeleteProfile;
import com.esolrpe.shared.profiles.ProfileData;
import com.esolrpe.shared.profiles.ProfileDatabase;
import com.esolrpe.shared.profiles.ProfileDatabaseUpdate;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class MainForm extends JFrame {
    private final JList<String> lstProfiles;

    public static void display() throws InterruptedException, InvocationTargetException {
        CountDownLatch waitLatch = new CountDownLatch(1);
        SwingUtilities.invokeAndWait(() -> {
            MainForm form = new MainForm(waitLatch);
            form.setVisible(true);
        });
        waitLatch.await();
    }

    private List<ProfileData> profiles;

    private MainForm(CountDownLatch waitLatch) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(Application.TITLE);
        setResizable(false);
        FormUtils.setIcon(this);
        AppTrayIcon.getInstance().registerHideableComponent(this);

        setLayout(new MigLayout("", "[350px][]", "[][][][][][]"));

        lstProfiles = new JList<>();
        lstProfiles.setOpaque(false);
        lstProfiles.setLayoutOrientation(JList.VERTICAL);
        lstProfiles.setVisibleRowCount(12);
        lstProfiles.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        JScrollPane pnlProfiles = new JScrollPane(lstProfiles);
        pnlProfiles.setBorder(new TitledBorder("Your Profiles"));

        add(pnlProfiles, "cell 0 0, spany 4, grow");

        JButton btnNewProfile = new JButton("New Profile");
        add(btnNewProfile, "cell 1 0, growx");
        JButton btnEditProfile = new JButton("Edit Profile");
        add(btnEditProfile, "cell 1 1, growx");
        JButton btnDeleteProfile = new JButton("Delete Profile");
        add(btnDeleteProfile, "cell 1 2, growx");

        JTextArea txtUpdateLog = new JTextArea();
        txtUpdateLog.setLineWrap(true);
        txtUpdateLog.setRows(8);
        txtUpdateLog.setFont(new Font("monospaced", Font.PLAIN, 12));
        txtUpdateLog.setEditable(false);

        JScrollPane pnlUpdateLog = new JScrollPane(txtUpdateLog);
        add(pnlUpdateLog, "cell 0 5, growx, spanx 2");


        JButton btnDownloadProfiles = new JButton("Update Profile Database");
        add(btnDownloadProfiles, "cell 0 4, growx, spanx 2");

        btnNewProfile.addActionListener(a -> {
            EditProfileDialog dialog = new EditProfileDialog(MainForm.this, null);
            dialog.setVisible(true);

            refreshProfiles();
        });

        btnEditProfile.addActionListener(a -> {
            int selectedIndex = lstProfiles.getSelectedIndex();
            if (selectedIndex == -1) {
                 return;
            }

            EditProfileDialog dialog = new EditProfileDialog(MainForm.this, profiles.get(selectedIndex));
            dialog.setVisible(true);

            refreshProfiles();
        });

        btnDownloadProfiles.addActionListener(e -> {
            txtUpdateLog.append(FormUtils.getNowAsString() + " - Beginning profile database update.\n");

            ProfileDatabaseUpdate profileDatabaseUpdate;
            try {
                profileDatabaseUpdate = new ProfileService().syncProfiles(Config.getInstance().getMegaserver(), 0L);
            } catch (Exception exception) {
                txtUpdateLog.append(FormUtils.getNowAsString() + " - Error occurred profile database update.\n");
                txtUpdateLog.append(FormUtils.getStackTrace(exception) + "\n");
                throw exception;
            }

            // TODO: need to read an existing profile database or w/e
            ProfileDatabase profileDatabase = new ProfileDatabase();
            profileDatabase.setApplicationVersion(profileDatabaseUpdate.getApplicationVersion());
            profileDatabase.setDatabaseVersion(profileDatabaseUpdate.getDatabaseVersion());
            profileDatabase.setTotalProfileCount(profileDatabaseUpdate.getTotalProfileCount());
            profileDatabase.setProfiles(profileDatabaseUpdate.getProfiles());

            File database = new File(Config.getInstance().getEsoAddonLocation(), "LitheRPEssentials/data/LitheDatabase.lua");
            database.delete();

            try {
                profileDatabase.exportToLuaDatabase(database);
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }

            int newProfiles = profileDatabaseUpdate.getProfiles().size();
            int totalProfiles = profileDatabase.getProfiles().size();
            txtUpdateLog.append(FormUtils.getNowAsString() + " - Database update complete.\nReceived " + newProfiles
                    + " new profile" + (newProfiles == 1 ? "" : "s")
                    + ", database now contains " + totalProfiles + " profiles.\n");

            if (this.isVisible()) {
                JOptionPane.showMessageDialog(null,
                        "Download and installation of profiles complete!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        lstProfiles.addListSelectionListener(e -> {
            boolean profileSelected = lstProfiles.getSelectedIndex() != -1;
            btnEditProfile.setEnabled(profileSelected);
            btnDeleteProfile.setEnabled(profileSelected);
        });

        btnDeleteProfile.addActionListener(e -> {
            int selectedIndex = lstProfiles.getSelectedIndex();
            if (selectedIndex == -1) {
                return;
            }

            ProfileData profile = profiles.get(selectedIndex);

            int result = JOptionPane.showConfirmDialog(MainForm.this,
                    "Are you sure you want to delete the profile for \"" +
                            profile.getCharacterName() +
                            "\"?",
                            "Confirm Delete Profile",
                            JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                ContextDeleteProfile deleteProfile = new ContextDeleteProfile();
                deleteProfile.setCharacterName(profile.getCharacterName());
                new ProfileService().deleteProfile(Config.getInstance().getMegaserver(),
                        deleteProfile);

                refreshProfiles();
            }
        });

        refreshProfiles();
        lstProfiles.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {

                    int index = lstProfiles.locationToIndex(e.getPoint());
                    EditProfileDialog dialog = new EditProfileDialog(MainForm.this, profiles.get(index));
                    dialog.setVisible(true);

                    refreshProfiles();
                }

                super.mouseClicked(e);
            }
        });

        pack();
        FormUtils.centerWindow(this);
        setVisible(true);
    }

    private void refreshProfiles() {
        profiles = new ProfileService().getProfilesForAccount(Config.getInstance().getMegaserver());

        // TODO: reselect the profile (by NAME, not by index) that we had selected before a refresh

        String[] characterNames = profiles.stream()
                .map(ProfileData::getCharacterName)
                .collect(Collectors.toList())
                .toArray(new String[profiles.size()]);
        lstProfiles.setListData(characterNames);
        if (characterNames.length > 0) {
            lstProfiles.setSelectedIndex(0);
        }
    }
}
