package com.esolrpe.client.forms;

import com.esolrpe.client.Application;
import com.esolrpe.client.api.AccountService;
import com.esolrpe.client.config.Config;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

public class ConfigureCredentialsForm extends JFrame {
    public static void display() throws InterruptedException, InvocationTargetException {
        CountDownLatch waitLatch = new CountDownLatch(1);
        SwingUtilities.invokeAndWait(() -> {
            ConfigureCredentialsForm form = new ConfigureCredentialsForm(waitLatch);
            form.setVisible(true);
        });
        waitLatch.await();
    }

    private ConfigureCredentialsForm(CountDownLatch waitLatch) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(Application.TITLE);
        setResizable(false);

        setLayout(new MigLayout("", "[][250px]", "[]8px[][][]8px[]"));

        add(new JLabel("eso-lrpe.com account login"), "cell 0 0, spanx, align center");
        add(new JLabel("Username:"), "cell 0 1");
        add(new JLabel("Password:"), "cell 0 2");

        add(new JLabel("Warning: do NOT use your Elder Scrolls Online password!"), "cell 0 3, spanx");

        JTextField txtUsername = new JTextField();
        txtUsername.setText(Config.getInstance().getLrpeUsername());
        add(txtUsername, "cell 1 1, growx");

        JTextField txtPassword = new JTextField();
        txtPassword.setText(Config.getInstance().getLrpePassword());
        add(txtPassword, "cell 1 2, growx");

        JButton btnLogin = new JButton("Login");
        add(btnLogin, "cell 0 4, spanx, align center");

        JButton btnRegister = new JButton("Register");
        add(btnRegister, "cell 0 4, spanx, align center");

        btnLogin.addActionListener(e -> {
            Config.getInstance().setLrpeUsername(txtUsername.getText());
            Config.getInstance().setLrpePassword(txtPassword.getText());
            Config.save();

            AccountService.getInstance().authenticate();

            dispose();
            waitLatch.countDown();
        });

        btnRegister.addActionListener(e -> {
            Config.getInstance().setLrpeUsername(txtUsername.getText());
            Config.getInstance().setLrpePassword(txtPassword.getText());
            Config.save();

            AccountService.getInstance().register(txtUsername.getText(), txtPassword.getText());
            AccountService.getInstance().authenticate();

            dispose();
            waitLatch.countDown();
        });

        pack();
        FormUtils.centerWindow(this);
        setVisible(true);
    }

    /**
     * TODO: some client-side password rules
     * Ideally we want to physically prevent people from using their ESO password.
     *
     * ESO password rules:
     * Your password must contain at least 8 characters (200 max).
     * Your password must contain at least one letter (A-Z) and one number (0-9).
     * Your password can only contain letters (A-Z), numbers (0-9), and punctuation.
     * Your password cannot be similar to your UserID.
     * Passwords must match.
     *
     * Lets ban numbers!
     */
}
