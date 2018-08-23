def call(int buildNumber, Map config) {
    pipeline {
      agent any
       stages {
        stage ('Checkout') {
          steps {
            checkout scm
          }
        }
        stage ('Clean') {
          steps {
            sh "./gradlew clean"
          }
        }
        stage ('Build') {
          steps {
            sh "./gradlew build"
          }
        }
        
          stage ('Test') {
          when {
            expression {
              return ( config.containsKey('runTest') && config.get('runTest') )
            }
          }
          steps {
            sh "./gradlew test"
          }
        }
      }
    }
}
