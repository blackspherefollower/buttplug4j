# Buttplug - Java WebSocket Client Implementation

[![Build Status](https://github.com/blackspherefollower/buttplug4j/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/blackspherefollower/buttplug4j/actions/workflows/gradle.yml) [![Patreon donate button](https://img.shields.io/badge/patreon-donate-yellow.svg)](https://www.patreon.com/blackspherefollower)

Buttplug is a framework for hooking up hardware to interfaces, where
hardware usually means sex toys, but could honestly be just about
anything. It's basically a userland HID manager for things that may
not specifically be HID.

In more concrete terms, think of Buttplug as something like
[osculator](http://www.osculator.net/) or [VRPN](http://vrpn.org), but
for sex toys. Instead of wiimotes and control surfaces, we interface
with vibrators, electrostim equipment, fucking machines, and other
hardware that can communicate with computers.

The core of buttplug works as a router. It is an application that
connects to driver libraries, to register and communicate with
different hardware. Clients can then connect over different means
(network, websockets, etc...), to interact with the hardware.

## Other Buttplug Implementations

Buttplug implementations are available in multiple languages (c#, rust,
javascript, etc)/frameworks/platforms. For a full
list,
[see the README in the main buttplug repo](http://github.com/buttplugio/buttplug).

## Platform Support

Buttplug Java should run on any 1.8 or later JRE (including Android).
In depth testing is still pending, so your mileage may vary.

## Development

Snapshot libraries from the buttplug4j repo are available via Maven from the following
repository: https://github.com/blackspherefollower/buttplug4j/packages

```xml
<repositories>
  <repository>
    <id>blackspherefollower-buttplug4j</id>
    <url>https://maven.pkg.github.com/lackspherefollower/buttplug4j</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

## Support The Project

If you find this project helpful, you can
[support my projects via Patreon](http://patreon.com/blackspherefollower)!
Every donation helps us afford more hardware to reverse, document, and
write code for!

## License

Buttplug4J is BSD licensed.

    Copyright (c) 2016-2017, BlackSphereFollower
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
