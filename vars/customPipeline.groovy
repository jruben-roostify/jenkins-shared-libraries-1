def call(int buildNumber, Map config) {
  if (buildNumber % 2 == 0) {
    pipeline {
      agent any
      stages {
        stage('Even Stage') {
          steps {
            echo "The build number is even"
          }
        }
      }
    }
  } else {
    pipeline {
      agent any
      stages {
        stage('Odd Stage') {
          steps {
            echo "The build number is odd"
          }
        }
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
}
