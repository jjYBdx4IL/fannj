[![Build Status](https://travis-ci.org/jjYBdx4IL/fannj.png?branch=master)](https://travis-ci.org/jjYBdx4IL/fannj)

# fannj

Java bindings to [FANN](http://leenissen.dk/fann), the Fast Artificial Neural Network C library.

## Overview

Use FannJ if you have an existing ANN from the FANN project that you would like to access from Java. There are several GUI tools that will help you create and train an ANN.

## Supported Operating Systems

Currently only x86-64 variants of Linux and Windows are supported out of the box. However, if you are willing to test the build, it should be easy to include any target for which there is cross compilation readily available on a regular Linux installation.

## Code Example

    Fann fann = new Fann( "/path/to/file" );
    float[] inputs = new float[]{ -1, 1 };
    float[] outputs = fann.run( inputs );
    fann.close();

For more example, have a look at the junit tests.
  
## Dependencies

[FANN](http://leenissen.dk/fann) - Does all the work. Binaries for Linux/Windows (x86-64) are included.

[JNA](https://github.com/java-native-access/jna) - Provides the native access to FANN.
   
## Maven Support

This project is now in the Maven Central Repository.

    <dependencies>
        <dependency>
            <groupId>com.github.jjYBdx4IL.nn.fannj</groupId>
            <artifactId>fannj</artifactId>
            <version>0.7.3</version>
        </dependency>
    </dependencies>

## Running

If you are using maven, including the main artifact listed above is enough. Otherwise, you'd have to manually include the dependencies listed in pom.xml.

## Development

The native libs for windows and linux can only be built on linux systems atm. You need the mingw compiler and the cmake tool for that. We use cmake toolchain config files to configure the compiler for each target platform.

### Manual Windows Testing

* copy the entire built project to a Windows system.
* mvn failsafe:integration-test@default-test
* mvn failsafe:verify@default-cli

Use specific execution ids (`@...`) or maven will execute prior steps and rebuild stuff.

### Automatic Windows Testing on Linux

If you have `/usr/bin/wineconsole` installed, an automatic integration test is done against your wine installation.

This is highly recommended, also because the wine integration test is more rigorous because it runs against
the final jar.
