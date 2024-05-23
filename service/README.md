# Running the service

The service runs on a Raspberry Pi provided by Finaltek.com.

## Prepare Raspberry Pi

* Ubuntu or Raspberry Pi OS (without desktop)
* Create SSH login
* Make sure all updates are installed

```
sudo apt update
sudo apt upgrade
```

## Install Java

Java 17 or newer

```
sudo apt install zip
curl -s “https://get.sdkman.io” | bash
source “$HOME/.sdkman/bin/sdkman-init.sh”
sdk install java 22.0.1-zulu
java -version

openjdk version "22.0.1" 2024-04-16
OpenJDK Runtime Environment Zulu22.30+13-CA (build 22.0.1+8)
OpenJDK 64-Bit Server VM Zulu22.30+13-CA (build 22.0.1+8, mixed mode, sharing)
```

## Upload the JAR and start script

```
mkdir /home/ft/pi4j-board-info-service
```

SCP the JAR file and script `start_pi4j_board_info_service.sh` to:

```
/home/ft/pi4j-board-info-service/pi4j-board-info-service.jar
/home/ft/pi4j-board-info-service/start_pi4j_board_info_service.sh
```

## Install the service script

SCP the script `/home/ft/pi4j_board_info.service` and configure as service:

```
sudo mv /home/ft/pi4j_board_info.service /etc/systemd/system/pi4j_board_info.service
sudo systemctl daemon-reload
sudo systemctl enable pi4j_board_info.service
sudo systemctl start pi4j_board_info.service
sudo systemctl status pi4j_board_info.service
```
