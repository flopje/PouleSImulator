#!groovy
pipeline {
  agent {
    docker { image 'thedrhax/android-sdk' } 
  }
  stages {
    stage('Checkout') {
      checkout([
        $class           : 'GitSCM',
        branches         : scm.branches,
        extensions       : scm.extensions +  [[$class: 'CheckoutOption', timeout: 20]], // Add higher timeout (in minutes), instead of default 10.
        userRemoteConfigs: scm.userRemoteConfigs
      ])
    }
    stage('Build') {
      steps {
        gradle assembleRelease
      }
    }
  }
}
