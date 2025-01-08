[![Clojars Project](https://img.shields.io/clojars/v/net.clojars.macielti/aws-client-component.svg)](https://clojars.org/net.clojars.macielti/aws-client-component)
![Compatible with GraalVM](https://img.shields.io/badge/compatible_with-GraalVM-green)

# AWS Client Component

AWS Client Integrant component

## Warnings

You should declare both `com.cognitect.aws/api` and `com.cognitect.aws/endpoints` as direct dependencies for your
project:

```clojure 
  [com.cognitect.aws/api "0.8.692"]
  [com.cognitect.aws/endpoints "1.1.12.772"]
```

If your product also use `pedestal` you should use the `0.5.7` version.

## GraalVM Native Image Generation

You should use `"-H:ReflectionConfigurationFiles=reflect-config.json"` with the following as content to
`reflect-config.json` file:

```json 
[
  {
    "name": "java.nio.HeapByteBuffer[]",
    "unsafeAllocated": true
  }
]
```

You should also use `"-H:ConfigurationFileDirectories=graalvm-resources"` and inside the `graalvm-resources` folder you
should have a `resource-config.json` file with the following content:

```json
{
  "resources": {
    "includes": [
      {
        "pattern": "cognitect/aws/.+\\.edn"
      },
      {
        "pattern" : "cognitect_aws_http.edn"
      }
    ]
  }
}
```

## License

Copyright © 2024 Bruno do Nascimento Maciel

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
