/*
Author - jruben@roostify.com
Date   - 08/23/2018
*/
def sonarRunner(String buildPath){
  if (env.GIT_BRANCH == 'develop') {
    sh "./gradlew -b "+buildPath+" sonar -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_PASSWORD}"
  }else if (env.CHANGE_ID){
    echo 'This is a PR build. Running sonnar preview analysis'
    sh "./gradlew -b "+buildPath+" sonar -Dsonar.github.pullRequest=${env.CHANGE_ID} -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_PASSWORD} -Dsonar.analysis.mode=preview -Dsonar.github.oauth=68eb8d979fe364931397bf39faa671159e5926ec -Dsonar.github.repository=${env.CUSTOM_SONAR_REPO_NAME} -i"
  }else{
    echo 'This is a branch build.'
    sh "./gradlew -b "+buildPath+" sonar -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_PASSWORD} -Dsonar.github.repository="+getRepoName()+" -Dsonar.branch=${env.GIT_BRANCH} -i"
  }
}

def getRepoName(){
    def repo = "${env.GIT_URL}"
    repo_val = repo.replaceAll('https://github.com/', '').replaceAll('.git', '')
    return repo_val;
}
