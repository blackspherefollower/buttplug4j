# Buttplug for Java - A Buttplug Client Implementation for Native Java

[![Build Status](https://github.com/blackspherefollower/buttplug4j/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/blackspherefollower/buttplug4j/actions/workflows/gradle.yml) [![Patreon donate button](https://img.shields.io/badge/patreon-donate-yellow.svg)](https://www.patreon.com/blackspherefollower)

Learn more about buttplug.io at https://buttplug.io


## Platform Support

Buttplug Java should run on any 1.8 or later JRE (including Android).
In depth testing is still pending, so your mileage may vary.

## Usage

The core buttplug4j library does not contain a transport implementation,
as the connector layer is intended to be extensible. In reality, you'll probably just
want the Jetty websocket client connector (this depends on the core client
library, so there's no need to add a separate dependency); see the 2
dependency import examples below.

For Gradle:
```groovy
dependencies {
    implementation 'io.github.blackspherefollower:buttplug4j.connectors.jetty.websocket.client:3.1.180'
}
```

For Maven:
```xml
<dependencies>
    <dependency>
        <groupId>io.github.blackspherefollower</groupId>
        <artifactId>buttplug4j.connectors.jetty.websocket.client</artifactId>
        <version>3.1.180</version>
    </dependency>
</dependencies>
```

For an all-in-one example project, please see https://github.com/blackspherefollower/buttplug4j-example

In general, you'll want to create a `ButtplugClientWSClient` object in a scope where it will not get garbage collected 
before you're done, add callbacks for error and event handling, then connect it to the address Intiface is listening
on. Then you can scan for devices, iterate over them and send commands.


### Snapshots

Snapshot libraries from the buttplug4j repo are available via Maven from the Central Portal Snapshots
repository: https://central.sonatype.com/repository/maven-snapshots

Releases will be available from Maven Central.

For Gradle:
```groovy
repositories {
    maven {
        name = 'Central Portal Snapshots'
        url = 'https://central.sonatype.com/repository/maven-snapshots/'
    }
}
dependencies {
    implementation 'io.github.blackspherefollower:buttplug4j.connectors.jetty.websocket.client:3.1.+'
}
```

For Maven:
```xml
<repositories>
    <repository>
        <name>Central Portal Snapshots</name>
        <id>central-portal-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>io.github.blackspherefollower</groupId>
        <artifactId>buttplug4j.connectors.jetty.websocket.client</artifactId>
        <version>[3.1-SNAPSHOT,)</version>
    </dependency>
</dependencies>
```

## Support The Project

If you find this project helpful, you can
[support my projects via Patreon](http://patreon.com/blackspherefollower)!
Every donation helps us afford more hardware to reverse, document, and
write code for!

## License

Buttplug for Java is BSD licensed.

    Copyright (c) 2016-2025, BlackSphereFollower
    All rights reserved.
    
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:
    
    * Redistributions of source code must retain the above copyright notice, this
      list of conditions and the following disclaimer.
    
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.
    
    * Neither the name of buttplug nor the names of its
      contributors may be used to endorse or promote products derived from
      this software without specific prior written permission.
    
    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
    FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
    DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
    CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
    OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
    OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
