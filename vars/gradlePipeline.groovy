def call(Map config) {
 buildFilePath = "./build.gradle"
 if (config.containsKey('buildFilePath')) {
  buildFilePath = config.get('buildFilePath')
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
                if (env.GIT_BRANCH == 'develop') {
                    sh "./gradlew -b "+buildFilePath+" sonar -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_PASSWORD}"
                }else if (env.CHANGE_ID){
                    echo 'This is a PR build. Running sonnar preview analysis'
                    sh "./gradlew -b "+buildFilePath+" sonar -Dsonar.github.pullRequest=${env.CHANGE_ID} -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_PASSWORD} -Dsonar.analysis.mode=preview -Dsonar.github.oauth=68eb8d979fe364931397bf39faa671159e5926ec -Dsonar.github.repository=${env.CUSTOM_SONAR_REPO_NAME} -i"
                }else{
                    echo 'This is a branch build.'
                    sh "./gradlew -b "+buildFilePath+" sonar -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_PASSWORD} -Dsonar.github.repository="+getRepoName()+" -Dsonar.branch=${env.GIT_BRANCH} -i"
                }
         }
    }
   }
  }
 }
}

def getRepoName(){
    def repo = "${env.GIT_URL}"
    repo_val = repo.replaceAll('https://github.com/', '').replaceAll('.git', '')
    return repo_val;
}
