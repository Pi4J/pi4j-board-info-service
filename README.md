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

This service provides the following end-points:

* OpenAPI/Swagger documentation
    * http://server:port/v3/api-docs
    * http://server:port/swagger-ui/
* APIs
    * Service
        * http://localhost:8080/api/service/java
        * http://localhost:8080/api/service/os

### Build and distribute with JReleaser

#### Build to deploy to Raspberry Pi server

```bash
sdk use java 18.0.1-zulu
mvn package
```

#### Initial setup

One-time process to prepare a new project for JReleaser

```
% sdk install jreleaser
% jreleaser init --format yml
```

### Adding a feature or solving a problem

If you have and idea to extend and improve this library, please first create a ticket to discuss how
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

