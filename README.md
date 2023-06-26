Raspberry Pi Info :: API Service providing information about the Raspberry Pi boards
====================================================================================

Build state:
![GitHub Actions build state](https://github.com/pi4j/raspberry-pi-info-service/workflows/Continious%20Integration/badge.svg)

[![License](https://img.shields.io/github/license/pi4j/pi4j-v2)](http://www.apache.org/licenses/LICENSE-2.0)
[![Site](https://img.shields.io/badge/Website-pi4j.com-green)](https://pi4j.com)
[![Twitter Follow](https://img.shields.io/twitter/follow/pi4j?label=Pi4J&style=social)](https://twitter.com/pi4j)

---

## PROJECT INFORMATION

### General information

Project website: [pi4j.com](https://pi4j.com/).

This service provides the following end-points on https://api.pi4j.com:

* Vaadin UI
    * `/web`
    * Header view only: `/web/header?name=HEADER_40`
* OpenAPI/Swagger documentation
    * `/api/docs/pi4j`
    * `/swagger-ui/index.html`
* APIs
    * Service
        * `/api/service/actual`
        * `/api/service/board`
        * `/api/service/memory`
    * Raspberry Pi Info
        * `/api/raspberrypi/board`
        * `/api/raspberrypi/board/MODEL`
            * e.g. `/api/raspberrypi/board/MODEL_4_B`

### Build and distribute with GitHub Actions

#### Build to deploy to Raspberry Pi server

```bash
sdk use java 18.0.1-zulu
mvn clean package -Pproduction
```

#### Start as a service on Linux

See https://www.baeldung.com/linux/run-java-application-as-service

### Adding a feature or solving a problem

If you have and idea to extend and improve this project, please first create a ticket to discuss how
it fits in the project and how it can be implemented.

If you find a bug, create a ticket, so we are aware of it and others with the same problem can
contribute what they already investigated. And the quickest way to get a fix? Try to search for
the cause of the problem or even better provide a code fix!

### Join the team

You want to become a member of the Pi4J-team? Great idea! Send a short message to frank@pi4j.com
with your experience, ideas, and what you would like to contribute to the project.

## LICENSE

Pi4J Version 2.0 and later is licensed under the Apache License,
Version 2.0 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at:
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

