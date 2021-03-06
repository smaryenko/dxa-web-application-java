package com.sdl.dxa.maven

interface Defaults {
    static final String DEFAULT_COMMAND = 'install'
    static final int NUMBER_THREADS = Runtime.getRuntime().availableProcessors()
    static final String MAVEN_PROPERTIES = "-T 1C -e"
}