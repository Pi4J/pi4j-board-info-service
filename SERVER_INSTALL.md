ssh frank@46.167.244.5

mkdir /home/frank/pi4j-board-info-service

sudo apt update
sudo apt upgrade

sudo apt install zip
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.4.1-zulu
java -version
openjdk version "17.0.4.1" 2022-08-12 LTS
OpenJDK Runtime Environment Zulu17.36+17-CA (build 17.0.4.1+1-LTS)
OpenJDK 64-Bit Server VM Zulu17.36+17-CA (build 17.0.4.1+1-LTS, mixed mode)

# Firewall and management

sudo apt install cockpit

TODO
https://github-wiki-see.page/m/cockpit-project/cockpit/wiki/Cockpit-with-LetsEncrypt
https://cockpit-project.org/external/wiki/Proxying-Cockpit-over-Apache-with-LetsEncrypt

# ON PC

scp service/pi4j_board_info.service frank@46.167.244.5://home/frank/pi4j_board_info.service
scp service/start_pi4j_board_info_service.sh frank@46.167.244.5:
//home/frank/pi4j-board-info-service/start_pi4j_board_info_service.sh

# ON PI

sudo mv /home/frank/pi4j_board_info.service /etc/systemd/system/pi4j_board_info.service
sudo systemctl daemon-reload
sudo systemctl enable pi4j_board_info.service
sudo systemctl start pi4j_board_info.service
sudo systemctl status pi4j_board_info.service

# DNS routing, setup by Robert Savage:

* DNS of pi4j.com is managed via namecheap.com
* https://api.pi4j.com/web/ is working behind HTTPS proxy server via Traefik on 54.86.178.48
* api.pi4j.com points to 54.86.178.48
    * Using LetsEncrypt certificates, it proxies to -> http://api-backend.pi4j.com:8080
* api-backend.pi4j.com points to 93.99.105.220, the Raspberry Pi hosted at FinalTek.com
    * TTL of 5 minutes, so can be quickly updated

