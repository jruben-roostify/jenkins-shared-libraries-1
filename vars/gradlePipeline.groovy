/*
Author - jruben@roostify.com
Date   - 08/23/2018
*/

def call(Map config) {
 buildFilePath = "./build.gradle"
 imageName = ""
 scanner = new sonarScanner()
 if (config.containsKey('buildFilePath')) {
  buildFilePath = config.get('buildFilePath')
 }
 if (config.containsKey('imageName')) {
   imageName = config.get('imageName')
  }
 pipeline {
  agent any
  stages {
   stage('Checkout') {
    steps {
     checkout scm
    }
   }
   stage('Clean') {
    steps {
     sh "./gradlew -b ${buildFilePath} clean"
    }
   }
   stage('Build') {
    steps {
     sh "./gradlew -b ${buildFilePath} build"
    }
   }

   stage('Test') {
    when {
     expression {
      return (config.containsKey('runTest') && config.get('runTest'))
     }
    }
    steps {
     sh "./gradlew -b ${buildFilePath} test"
    }
   }
   stage('Analysis') {
       steps {
        jacoco buildOverBuild: true, changeBuildStatus: true
       }
   }
   stage('Sonar Analysis') {
    when {
     expression {
      return (config.containsKey('sonarNeeded') && config.get('sonarNeeded'))
     }
    }
    steps {
         script {
          scanner.sonarRunner(buildFilePath)
         }
    }
   }
   stage('Dockerize') {
     when {
      expression {
       return (config.containsKey('dockerNeeded') && config.get('dockerNeeded'))
      }
     }
     steps {
       docker.build(imageName)
     }
    }
  }
 }
}
