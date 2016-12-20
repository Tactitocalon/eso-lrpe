package com.esolrpe.client.forms;

import com.esolrpe.client.Application;
import com.esolrpe.client.api.ProfileService;
import com.esolrpe.shared.auth.AuthenticationDetails;
import com.esolrpe.shared.profiles.ProfileData;
import net.miginfocom.swing.MigLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
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

        setLayout(new MigLayout("", "[300px][]", "[][][][]32px[]"));

        lstProfiles = new JList<>();
        lstProfiles.setOpaque(false);
        lstProfiles.setLayoutOrientation(JList.VERTICAL);
        lstProfiles.setVisibleRowCount(12);
        lstProfiles.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        JScrollPane pnlProfiles = new JScrollPane(lstProfiles);
        pnlProfiles.setBorder(new TitledBorder("Profiles"));

        add(pnlProfiles, "cell 0 0, spany 3, grow");

        refreshProfiles();

        pack();
        FormUtils.centerWindow(this);
        setVisible(true);
    }

    private void refreshProfiles() {
        AuthenticationDetails authDetails = new AuthenticationDetails();
        authDetails.setUsername("TestUser");

        profiles = new ProfileService().getProfilesForAccount("NA", authDetails);

        String[] characterNames = profiles.stream()
                .map(ProfileData::getCharacterName)
                .collect(Collectors.toList())
                .toArray(new String[profiles.size()]);
        lstProfiles.setListData(characterNames);
        if (characterNames.length > 0) {
            lstProfiles.setSelectedIndex(0);
        }

        EditProfileDialog dialog = new EditProfileDialog(this, profiles.get(0));
        dialog.setVisible(true);
    }

    private File computeDefaultAddonLocation() {
        File myDocuments = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
        return new File(myDocuments, "Elder Scrolls Online/live/AddOns");
    }

}
