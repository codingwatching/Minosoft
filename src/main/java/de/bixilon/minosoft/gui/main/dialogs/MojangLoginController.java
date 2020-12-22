/*
 * Minosoft
 * Copyright (C) 2020 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

package de.bixilon.minosoft.gui.main.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.bixilon.minosoft.Minosoft;
import de.bixilon.minosoft.data.locale.LocaleManager;
import de.bixilon.minosoft.data.locale.Strings;
import de.bixilon.minosoft.gui.main.AccountListCell;
import de.bixilon.minosoft.logging.Log;
import de.bixilon.minosoft.util.mojang.api.MojangAccount;
import de.bixilon.minosoft.util.mojang.api.MojangAccountAuthenticationAttempt;
import de.bixilon.minosoft.util.mojang.api.MojangAuthentication;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MojangLoginController implements Initializable {
    public HBox hBox;
    public Label header;
    public Label emailLabel;
    public JFXTextField email;
    public Label passwordLabel;
    public JFXPasswordField password;
    public JFXButton loginButton;
    public Label errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // translate

        this.header.setText(LocaleManager.translate(Strings.LOGIN_DIALOG_HEADER));
        this.emailLabel.setText(LocaleManager.translate(Strings.EMAIL));
        this.passwordLabel.setText(LocaleManager.translate(Strings.PASSWORD));
        this.loginButton.setText(LocaleManager.translate(Strings.BUTTON_LOGIN));


        this.email.textProperty().addListener(this::checkData);
        this.password.textProperty().addListener(this::checkData);


        this.hBox.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (this.loginButton.isDisable()) {
                    return;
                }
                this.loginButton.fire();
                return;
            }
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });
    }

    public void login(ActionEvent event) {
        event.consume();
        this.email.setDisable(true);
        this.password.setDisable(true);
        this.loginButton.setDisable(true);
        this.errorMessage.setVisible(false);


        new Thread(() -> { // ToDo: recycle thread
            MojangAccountAuthenticationAttempt attempt = MojangAuthentication.login(this.email.getText(), this.password.getText());
            if (attempt.succeeded()) {
                // login okay
                final MojangAccount account = attempt.getAccount();
                if (Minosoft.getConfig().getAccountList().containsValue(account)) {
                    // account already present
                    // replace access token
                    MojangAccount existingAccount = Minosoft.getConfig().getAccountList().get(account.getUserId());
                    existingAccount.setAccessToken(attempt.getAccount().getAccessToken());
                    existingAccount.saveToConfig();
                    Platform.runLater(() -> {
                        this.errorMessage.setText(LocaleManager.translate(Strings.LOGIN_ACCOUNT_ALREADY_PRESENT));
                        this.errorMessage.setVisible(true);
                        this.email.setDisable(false);
                        this.password.setDisable(false);
                        this.loginButton.setDisable(true);
                    });
                    return;
                }
                Minosoft.getConfig().putMojangAccount(account);
                account.saveToConfig();
                Log.info(String.format("Added and saved account (playerName=%s, email=%s, uuid=%s)", account.getPlayerName(), account.getMojangUserName(), account.getUUID()));
                Platform.runLater(() -> {
                    AccountListCell.MOJANG_ACCOUNT_LIST_VIEW.getItems().add(account);
                    close();
                });
                if (Minosoft.getSelectedAccount() == null) {
                    // select account
                    Minosoft.selectAccount(account);
                }
                return;
            }
            Platform.runLater(() -> {
                this.errorMessage.setText(attempt.getError());
                this.errorMessage.setVisible(true);
                this.email.setDisable(false);
                this.password.setDisable(false);
                this.loginButton.setDisable(true);
            });
        }, "AccountLoginThread").start();

    }

    private void checkData(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        if (newValue.isEmpty()) {
            this.loginButton.setDisable(true);
            return;
        }
        this.loginButton.setDisable(this.email.getText().isBlank() || this.password.getText().isBlank());
    }

    public void close() {
        getStage().close();
    }

    public Stage getStage() {
        return ((Stage) this.hBox.getScene().getWindow());
    }
}
