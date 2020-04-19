
[![License](https://img.shields.io/badge/license-LGPL%202.1-blue.svg)](http://www.gnu.org/licenses/lgpl-2.1.html)
[![License](https://img.shields.io/badge/license-EPL%202.0-blue.svg)](http://www.eclipse.org/legal/epl-2.0)
[![Language](http://img.shields.io/badge/language-java-brightgreen.svg)](https://www.java.com/)

# JGraphT Native Library

This library provides a native C api to the JGraphT library. Since the JGraphT library is written in Java, we first 
use [GraalVM](https://www.graalvm.org/) in order to build a shared library and then build a second shared library
which also takes care of the initialization, i.e. attaching a thread, etc.

## Build

We use cmake to build the native library. 

```
mkdir build/
cd build/
cmake ..
make
make install
```

After a successful build, you should see the following files: 

```
jgrapht.h
jgrapht_nlib.h
jgrapht_nlib_dynamic.h
graal_isolate.h
graal_isolate_dynamic.h
```

and the shared libraries `jgrapht_nlib.so` and `jgrapht.so`.
```

## License

This library may be used under the terms of either the

 * GNU Lesser General Public License (LGPL) 2.1
   http://www.gnu.org/licenses/lgpl-2.1.html

or the

 * Eclipse Public License (EPL)
   http://www.eclipse.org/org/documents/epl-v20.php

As a recipient, you may choose which license to receive the code under.
A copy of the [EPL license](license-EPL.txt) and the [LPGL license](license-LGPL.txt) is included in this repository.

Please note that this library is distributed WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

Please refer to the license for details.

SPDX-License-Identifier: LGPL-2.1-or-later OR EPL-2.0


Enjoy!
