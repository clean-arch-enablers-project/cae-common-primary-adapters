package com.cae.command_controller.client_settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class ClientSettingsProvider {

    public static final ClientSettingsProvider SINGLETON = new ClientSettingsProvider();

    private String clientName = "$default";
    private String version = "0.0.0";

}
